/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import java.io.FileNotFoundException;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.terminals.Jbaxi;
import pos_client.terminals.TerminalHandler;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JasperPrint;
import pos_client.common.DefinedVariables;
import pos_client.common.File;
import pos_client.communication.ControlUnit;
import pos_client.db.dao.BoutiqueDAO;
import pos_client.db.dao.VatDAO;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.db.dao.SalesDAO;
import pos_client.db.dao.SettingsDAO;
import pos_client.db.dao.UserDAO;
import pos_client.db.dao.VatDAO.VatType;
import pos_client.fxml.payment.PaymentController.PaymentType;
import pos_client.reporting.ReceiptReportBuilder;
import pos_client.reporting.ReceiptRow;
import pos_client.services.ReceiptService;
import pos_client.services.ReceiptServiceImpl;
import pos_client.windows.Log;

public class Receipt
{

    private UserModel user = null;

    private ReceiptType type = ReceiptType.ONGOING;

    private int copyPrinted;

    private int id;
    private int nr;
    private String tableNr = "-";

    private float cash = 0;
    private float cashBack = 0;

    private float card = 0;
    private float cardBack = 0;

    private float credit = 0;
    private float cashResult = 0;
    private float cardResult = 0;
    private int noOfServices = 0;

    private float[] vatAmount = new float[4];

    private String modifyDate;
    private String modifyTime;

    private String createdDate;
    private String createdTime;

    VatDAO vatDAO = new VatDAO();
    ReceiptDAO receiptDAO = new ReceiptDAO();

    public Receipt()
    {
        init();
    }

    private void init()
    {
        for (VatDAO.VatType vat : VatDAO.VatType.values())
        {
            vatAmount[vat.ordinal()] = 0;
        }

        createdTime = General.getInstance().time();
        createdDate = General.getInstance().date();

        modifyTime = createdTime;
        modifyDate = createdDate;
    }

    public Receipt(UserModel user, boolean store)
    {
        init();
        this.user = user;

        if (store)
        {
            nr = receiptDAO.getNewReceiptNr();
            store();
            id = getReceiptId(nr);
        }
    }

    public void SetReceipt(int id,
            int nr,
            int userId,
            String CreateDate,
            String CreateTime,
            String ModifyDate,
            String ModifyTime,
            int Type,
            float Cashback,
            float Cash,
            float Card,
            float Credit,
            float CashResult,
            float CardResult,
            int noOfArticles,
            int noOfServices,
            float totalAmount,
            float vat1Amount,
            float vat2Amount,
            float vat3Amount,
            float vat4Amount,
            String tableNr,
            int copyPrinted
    )
    {
        this.id = id;
        this.nr = nr;
        this.createdTime = CreateTime;
        this.createdDate = CreateDate;
        this.modifyTime = ModifyTime;
        this.modifyDate = ModifyDate;
        this.type = ReceiptType.values()[Type];
        this.cashBack = Cashback;
        this.cash = Cash;
        this.card = Card;
        this.credit = Credit;
        this.cashResult = CashResult;
        this.cardResult = CardResult;
        this.noOfServices = noOfServices;
        this.tableNr = tableNr;
        this.copyPrinted = copyPrinted;

        UserDAO userDAO = new UserDAO();
        this.user = userDAO.getUser(userId);
    }

    public UserModel getUser()
    {
        return this.user;
    }

    public void setUser(UserModel user)
    {
        this.user = user;
    }

    public ObservableList<SaleModel> getAllSales()
    {
        SalesDAO salesDAO = new SalesDAO();
        return salesDAO.getAllSales(this);
    }

    public ArrayList<SaleModel> getAllSales(VatType vatType)
    {
        ArrayList<SaleModel> salesSpecificVat = new ArrayList<>();
        for (SaleModel sale : getAllSales())
        {
            if (sale.getArticle().getVatType() == vatType)
            {
                salesSpecificVat.add(sale);
            }
        }

        return salesSpecificVat;
    }

    public ObservableList<ArticleModel> getAllArticles()
    {
        ObservableList<ArticleModel> articles = FXCollections.observableArrayList();
        for (SaleModel sale : getAllSales())
        {
            articles.add(sale.getArticle());
        }

        return articles;
    }

    public boolean removeSale(SaleModel sale)
    {
        SalesDAO salesDAO = new SalesDAO();
        return salesDAO.changeSaleType(SaleModel.SaleType.REMOVED, this, sale.getSaleId());
    }

    public boolean hasSales()
    {
        return (getAllSales().size() > 0);
    }

    public int countSales()
    {
        return (getAllSales().size());
    }

    public int countServices()
    {
        return 0; //TODO
    }

    public String updatePayment()
    {
        String discount = "RABATT: " + General.decimalFormat.format(getSumTotalDiscount());
        String tax = "MOMS: " + General.decimalFormat.format(getSumTotalVat());
        String payment = "BETALNING\n" + getSumTotalIncmStr();

        return (discount + "\n" + tax + "\n" + "\n" + "\n" + "\n" + payment);
    }

    public float getPaymentRemain()
    {
        return getSumTotalIncm() - getPaymentPayed();
    }

    public String getPaymentRemainStr()
    {
        return General.decimalFormat.format(getSumTotalIncm() - getPaymentPayed());
    }

    public float getPaymentPayed()
    {
        return (cash + card);
    }

    public String getPaymentPayedStr()
    {
        return General.decimalFormat.format(cash + card);
    }

    public float getSumSalesIncm(VatType vatType)
    {
        float total = 0;
        for (SaleModel sale : getAllSales(vatType))
        {
            total += sale.getPriceTotalIncm(true);
        }

        total = General.getInstance().decimal(total, 2, true);

        Core.getInstance().getLog().log("Total summa (incm) på kvittot för momsgrupp(" + vatDAO.getVat(vatType) + "): " + total, Log.LogLevel.DESCRIPTIVE);

        return total;
    }

    public float getSumSalesExcm(VatType vatType)
    {
        float total = 0;
        for (SaleModel sale : getAllSales(vatType))
        {
            total += sale.getPriceTotalExcm(true);
        }

        total = General.getInstance().decimal(total, 2, true);

        Core.getInstance().getLog().log("Total summa (excm) på kvittot för momsgrupp(" + vatDAO.getVat(vatType) + "): " + total, Log.LogLevel.DESCRIPTIVE);

        return total;
    }

    public float getSumSalesVat(VatType vatType)
    {
        float total = 0;
        for (SaleModel sale : getAllSales(vatType))
        {
            total += sale.getPriceTotalExcm(true);
        }

        total = General.getInstance().decimal(total, 2, true);

        Core.getInstance().getLog().log("Total summa (excm) på kvittot för momsgrupp(" + vatDAO.getVat(vatType) + "): " + total, Log.LogLevel.DESCRIPTIVE);

        return total;
    }

    public float getSumTotalIncm()
    {
        float total = 0;
        for (SaleModel sale : getAllSales())
        {
            total += sale.getPriceTotalIncm(true);
        }

        total = General.getInstance().decimal(total, 2, true);

        Core.getInstance().getLog().log("Total summa (incm) på kvittot: " + total, Log.LogLevel.DESCRIPTIVE);

        return total;
    }

    public String getSumTotalIncmStr()
    {
        return General.decimalFormat.format(getSumTotalIncm());
    }

    public float getSumTotalExcm()
    {
        float total = 0;
        for (SaleModel sale : getAllSales())
        {
            total += sale.getPriceTotalExcm(true);
        }

        total = General.getInstance().decimal(total, 2, true);

        Core.getInstance().getLog().log("Total summa (excm) på kvittot: " + total, Log.LogLevel.DESCRIPTIVE);

        return total;
    }

    public String getSumTotalExcmStr()
    {
        return General.decimalFormat.format(getSumTotalExcm());
    }

    public float getSumTotalDiscount()
    {
        float total = 0;
        for (SaleModel sale : getAllSales())
        {
            total += (sale.getPriceTotalIncm(false) - sale.getPriceTotalIncm(true));
        }

        Core.getInstance().getLog().log("Total rabatten på kvittot: " + total, Log.LogLevel.DESCRIPTIVE);

        return General.getInstance().decimal(total, 2, true);
    }

    public String getSumTotalDiscountStr()
    {
        return General.decimalFormat.format(getSumTotalDiscount());
    }

    public float getSumTotalVat()
    {
        float total = 0;
        for (SaleModel sale : getAllSales())
        {
            total += (sale.getPriceTotalIncm(true) - sale.getPriceTotalExcm(true));
        }

        Core.getInstance().getLog().log("Total moms summa på kvittot: " + total, Log.LogLevel.DESCRIPTIVE);

        return General.getInstance().decimal(total, 2, true);
    }

    public String getSumTotalVatStr()
    {
        return General.decimalFormat.format(getSumTotalVat());
    }

    public float getSumTotalVat(VatType vatType)
    {
        float amount = 0;
        for (SaleModel sale : getAllSales(vatType))
        {
            amount += (sale.getPriceTotalIncm(true) - sale.getPriceTotalExcm(true));
        }

        Core.getInstance().getLog().log("Total moms summa på kvittot för momsgrupp(" + vatType.ordinal() + "): " + amount, Log.LogLevel.DESCRIPTIVE);

        return General.getInstance().decimal(amount, 2, true);
    }

    public float getVat(VatType vatType)
    {
        return this.vatAmount[vatType.ordinal()];
    }

    public void cancel()
    {
        Core.getInstance().getLog().log("Avbryter kvittot.", Log.LogLevel.NORMAL);

        modifyTime = General.getInstance().time();
        modifyDate = General.getInstance().date();

        changeReceiptTypeId(ReceiptType.CANCELED);
    }

    public void normal()
    {
        Core.getInstance().getLog().log("Registrerar avslutat normalköp", Log.LogLevel.NORMAL);

        modifyTime = General.getInstance().time();
        modifyDate = General.getInstance().date();

        changeReceiptTypeId(ReceiptType.NORMAL);
    }

    public void refund()
    {
        Core.getInstance().getLog().log("Registrerar avslutad återbetalning", Log.LogLevel.NORMAL);

        modifyTime = General.getInstance().time();
        modifyDate = General.getInstance().date();

        changeReceiptTypeId(ReceiptType.REFUND);
    }

    public void park()
    {
        Core.getInstance().getLog().log("Parkerar kvittot", Log.LogLevel.NORMAL);

        modifyTime = General.getInstance().time();
        modifyDate = General.getInstance().date();

        changeReceiptTypeId(ReceiptType.PARKED);
    }

    public boolean finish()
    {
        return receiptDAO.finish(this);
    }

    public String getIdInfo()
    {
        return "Bord: " + tableNr + General.getInstance().newLine + user.getName();
    }

    public String getModifyInfo()
    {
        return "Start: " + modifyTime + General.getInstance().newLine + "Date: " + modifyDate;
    }

    public String getReceiptInfo()
    {
        return "Artiklar: " + General.getInstance().int2Str(countSales()) + General.getInstance().newLine + General.decimalFormat.format(getSumTotalIncm());
    }

    public int getReceiptId()
    {
        return id;
    }

    public int getNr()
    {
        return nr;
    }

    public String getModifyTime()
    {
        return modifyTime;
    }

    public String getModifyDate()
    {
        return modifyDate;
    }

    public String getCreatedTime()
    {
        return createdTime;
    }

    public String getCreatedDate()
    {
        return createdDate;
    }

    public void setType(ReceiptType type)
    {
        this.type = type;
    }

    public ReceiptType getType()
    {
        return this.type;
    }

    public void setCash(double cash)
    {
        this.cash += cash;
    }

    public float getCash()
    {
        return this.cash;
    }

    public void setCard(double card)
    {
        this.card += card;
    }

    public float getCard()
    {
        return this.card;
    }

    public void setCredit(float credit)
    {
        this.credit = credit;
    }

    public float getCredit()
    {
        return this.credit;
    }

    public void setCardBack(float cardBack)
    {
        this.cardBack = cardBack;
    }

    public float getCardBack()
    {
        return this.cardBack;
    }

    public void setCardResult(float cardResult)
    {
        this.cardResult = cardResult;
    }

    public float getCardResult()
    {
        return this.cardResult;
    }

    public void setCashBack(float cashBack)
    {
        this.cashBack = cashBack;
    }

    public float getCashBack()
    {
        return this.cashBack;
    }

    public void setCashResult(float cashResult)
    {
        this.cashResult = cashResult;
    }

    public float getCashResult()
    {
        return this.cashResult;
    }

    public void setTableNumber(String tableNr)
    {
        this.tableNr = tableNr;
    }

    public String getTableNumber()
    {
        return this.tableNr;
    }

    private void store()
    {
        receiptDAO.store(this);
    }

    public int getReceiptId(int nr)
    {
        return receiptDAO.getReceiptId(nr); // TODO Idt ligge rhär på kvittot redan?
    }

    public int getReceiptNewNr()
    {
        return receiptDAO.getNewReceiptNr();
    }

    public void changeReceiptTypeId(ReceiptType newType)
    {
        this.type = newType;
        receiptDAO.changeReceiptTypeId(this);
    }

    public void resetArticles(Receipt receiptOld)
    {
        if (receiptOld != null)
        {
            SalesDAO salesDAO = new SalesDAO();
            ObservableList<SaleModel> allSales = salesDAO.getAllSales(this);
            for (SaleModel sales : allSales)
            {
                salesDAO.changeReceiptId(receiptOld.getReceiptId(), sales.getSaleId());
            }
        }
    }

    public PaymentType getPaymentType()
    {
        PaymentType paymentType = PaymentType.NONE;

        if (getCard() > 0)
        {
            paymentType = PaymentType.CARD;
        }

        if (getCash() > 0)
        {
            paymentType = PaymentType.CASH;
        }

        if (getCash() > 0 && getCard() > 0)
        {
            paymentType = PaymentType.CASH_CARD;
        }

        return paymentType;
    }

    public enum ReceiptOutputType
    {
        SHOP,
        CUSTOMER,
        DEFAULT
    };

    public void createReceipt(ReceiptType receiptType, PaymentType paymentType)
    {
        LocalTime before = LocalTime.now();

        ReceiptReportBuilder report = new ReceiptReportBuilder();

        Date createDate = new Date();

        // Header
        report.setOrgNo(getOrgNo());
        report.setPhoneNumber(getPhoneNr());
        report.setAdress(getCompanyAdress1());
        report.setCompanyName(getCompanyName());

        // PosInfo
        report.setReceiptDate(createDate);
        report.setUserName(user.getName());
        report.setCounterId(getCashRegisterNr());
        report.setTableNr(tableNr);

        int isRefund = 1;
        if (receiptType == ReceiptType.REFUND) // if refund on receipt
        {
            isRefund *= -1;
        }

        float totalSum = getSumTotalIncm();

        report.setCounterId(getCashRegisterNr());
        report.setNoOfArticles(countSales());
        report.setNoOfServices(countServices());
        report.setTotalAmount(isRefund * totalSum);

        for (VatType vatType : VatType.values())
        {
            float sumTotalVat = getSumTotalVat(vatType);
            float sumTotal = getSumSalesIncm(vatType);
            report.setVatAmount(vatType, sumTotalVat);
            report.setVatExcluded(vatType, sumTotal - sumTotalVat);
            report.setVatIncluded(vatType, sumTotal);
        }

        List rows = new ArrayList<>();
        for (SaleModel sale : getAllSales())
        {
            ArticleModel article = sale.getArticle();
            ReceiptRow receiptRow = new ReceiptRow(article.getName(), isRefund < 0 ? "-" + sale.getPriceIncmStr() : sale.getPriceIncmStr(), sale.getSpecialStr(), sale.getIngredienceExtraStr(), sale.getIngredienceExcludeStr());
            rows.add(receiptRow);
        }
        report.setSalesRows(rows);

        List transRows = new ArrayList<>();
        transRows.add(new ReceiptRow("Kontant", General.decimalFormat.format(cash), "", "", ""));
        transRows.add(new ReceiptRow("Kort", General.decimalFormat.format(card), "", "", ""));
        transRows.add(new ReceiptRow("Växel", General.decimalFormat.format(getPaymentPayed() - totalSum), "", "", ""));
        report.setTransactionRows(transRows);

        ControlUnit controlUnit = Core.getInstance().getComHandler().getControlUnit();
        report.setControlUnitNr(controlUnit.getControlUnitNr(controlUnit.controlUnitData(receiptType, this)));

        ReceiptService receiptService = new ReceiptServiceImpl();
        String path = "C:\\Temp\\receipt_main";
        switch (receiptType)
        {
            case ADMIN:
                path = "C:\\Temp\\receipt_admin";
                buildReceipt(report, receiptType, paymentType, path);
                break;

            case NORMAL:
                path = "C:\\Temp\\receipt_customer";

                nr = receiptService.updateReceiptWithNextReceiptNo(this);
                report.setReceiptNo(nr);

                buildReceipt(report, receiptType, paymentType, path);

                TerminalHandler terminalHandler = Core.getInstance().getTerminalHandler();
                if (terminalHandler != null && terminalHandler.getActiveTerminal() != null && terminalHandler.getActiveTerminal().isConnected())
                {
                    if (terminalHandler.getActiveTerminal().getName().compareTo(Jbaxi.NETS) == 0)
                    {
                        path = "C:\\Temp\\receipt_shop";

                        Map<Integer, String> terminalOutput = new HashMap<>();

                        terminalOutput.put(ReceiptOutputType.CUSTOMER.ordinal(), ((Jbaxi) terminalHandler.getActiveTerminal()).getTerminalOutputCustomer());
                        terminalOutput.put(ReceiptOutputType.SHOP.ordinal(), ((Jbaxi) terminalHandler.getActiveTerminal()).getTerminalOutputShop());
                        terminalOutput.put(ReceiptOutputType.DEFAULT.ordinal(), ((Jbaxi) terminalHandler.getActiveTerminal()).getTerminalOutput());

                        report.setTerminalOutput(terminalOutput);

                        report.setTerminalResponse(((Jbaxi) terminalHandler.getActiveTerminal()).getStatus());

                        buildReceipt(report, receiptType, paymentType, path);
                    }
                } else
                {
                    Core.getInstance().getLog().log("Terminalhandler or activeterminal is null", Log.LogLevel.CRITICAL);
                }

                break;

            case PROFO:
                path = "C:\\Temp\\receipt_profo";

                report.setReceiptProfoNo(receiptService.getProfoNo());
                buildReceipt(report, receiptType, paymentType, path);

                break;

            case REFUND:
                path = "C:\\Temp\\receipt_refund_customer";

                nr = receiptService.updateReceiptWithNextReceiptNo(this);
                report.setReceiptNo(nr);

                buildReceipt(report, receiptType, paymentType, path);

                path = "C:\\Temp\\receipt_refund_shop";

                buildReceipt(report, receiptType, paymentType, path);

                break;
        }
    }

    private void buildReceipt(ReceiptReportBuilder report, ReceiptType receiptType, PaymentType paymnetType, String path)
    {
        String pdf = ".pdf";

        File file = new File();
        int nr = 0;
        String pathTmp = path + "_" + General.getInstance().int2Str(nr);
        while (file.fileExists(pathTmp + pdf))
        {
            pathTmp = path + "_" + General.getInstance().int2Str(++nr);
        }
        //General.getInstance().fileDelete(path);

        JasperReportBuilder receiptPrint = report.build(receiptType, paymnetType);
        
        try
        {
            SettingsDAO settingsDAO = new SettingsDAO();
            String selectedPrinter = settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_PRINTER_RECEIPT);
            Core.getInstance().getPrinterHandler().print(selectedPrinter, receiptPrint.toJasperPrint());
        } catch (Exception ex)
        {
            Core.getInstance().getLog().log("printTestBong - Lyckades inte skriva ut...", Log.LogLevel.CRITICAL);
        }

        try
        {
            FileOutputStream out = new FileOutputStream(pathTmp + pdf);
            receiptPrint.toPdf(out);
            out.close();
        } catch (FileNotFoundException ex)
        {
            Core.getInstance().getLog().log("createReceipt - Error while creating receipt: " + ex.toString(), Log.LogLevel.CRITICAL);
        } catch (DRException ex)
        {
            Logger.getLogger(Receipt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Receipt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getCompanyName()
    {
        BoutiqueDAO boutiqueDAO = new BoutiqueDAO();
        return boutiqueDAO.getCompanyName();
    }

    public String getCompanyAdress1()
    {
        BoutiqueDAO boutiqueDAO = new BoutiqueDAO();
        return boutiqueDAO.getCompanyAdress1();
    }

    public String getCashRegisterNr()
    {
        return receiptDAO.getCashRegisterNr();
    }

    public String getOrgNo()
    {
        BoutiqueDAO boutiqueDAO = new BoutiqueDAO();
        return boutiqueDAO.getOrgNumber();
    }

    public String getPhoneNr()
    {
        BoutiqueDAO boutiqueDAO = new BoutiqueDAO();
        return boutiqueDAO.getPhoneNumber();
    }

    public int getNoOfServices()
    {
        return noOfServices;
    }

    public void setNoOfServices(int noOfServices)
    {
        this.noOfServices = noOfServices;
    }

    public int getUserId()
    {
        return this.user.getId();
    }

    public void setUserId(int userId)
    {
        this.user.setId(userId);
    }

    public int getCopyPrinted()
    {
        return this.copyPrinted;
    }

    public void setCopyPrinted(int copyPrinted)
    {
        this.copyPrinted = copyPrinted;
    }

}
