package pos_client.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import pos_client.common.General;
import pos_client.Receipt;
import pos_client.SaleModel;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.communication.ControlUnit;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.ReceiptTypeDAO;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.db.dao.SalesDAO;
import pos_client.db.dao.SettingsDAO;
import pos_client.db.dao.VatDAO.VatType;
import pos_client.fxml.payment.PaymentController;
import pos_client.reporting.ReceiptReportBuilder;
import pos_client.reporting.ReceiptRow;
import pos_client.windows.Log;

/**
 *
 * @author ola
 */
public class PrintReceiptServiceImpl implements PrintReceiptService
{

    public enum PrintReceiptCopyStatus
    {
        OK,
        ALREADY_PRINTED,
        RECEIPT_NOT_FOUND
    }

    ReceiptDAO receiptDAO;

    public PrintReceiptServiceImpl()
    {
        receiptDAO = new ReceiptDAO();
    }

    @Override
    public PrintReceiptCopyStatus printReceiptCopy(int receiptNo, ReceiptPrinter receiptPrinter) throws ParseException, Exception
    {
        int receiptId = receiptDAO.getReceiptId(receiptNo);
        Receipt receipt = receiptDAO.getReceipt(receiptId);

        if (receipt.getType() == ReceiptType.NORMAL)
        {
            if (receipt == null)
            {
                return PrintReceiptCopyStatus.RECEIPT_NOT_FOUND;
            } else
            {
                if (receipt.getCopyPrinted() == ReceiptDAO.RECEIPT_COPY_PRINTED)
                {
                    return PrintReceiptCopyStatus.ALREADY_PRINTED;
                } else
                {
                    // Create report
                    JasperReportBuilder report = createReceiptReport(receipt);

                    try
                    {
                        SettingsDAO settingsDAO = new SettingsDAO();
                        String selectedPrinter = settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_PRINTER_RECEIPT);
                        Core.getInstance().getPrinterHandler().print(selectedPrinter, report.toJasperPrint());
                    } catch (Exception ex)
                    {
                        Core.getInstance().getLog().log("printTestBong - Lyckades inte skriva ut...", Log.LogLevel.CRITICAL);
                    }

                    // Print report TODO! For now print to file system
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    report.toPdf(outputStream);
                    printReport(receiptPrinter, outputStream);

                    // Update receipt as printed
                    receiptDAO.updateReceiptAsPrinted(receipt.getReceiptId());

                    return PrintReceiptCopyStatus.OK;
                }
            }
        } else
        {
            Core.getInstance().getLog().log("Valt kvitto har fel kvittotyp", Log.LogLevel.CRITICAL);
        }

        return PrintReceiptCopyStatus.RECEIPT_NOT_FOUND;
    }

    private void printReport(ReceiptPrinter receiptPrinter, ByteArrayOutputStream receiptStream) throws FileNotFoundException, IOException
    {
        try (FileOutputStream fileOutputStream = new FileOutputStream("c:/temp/kvitto_kopia.pdf"))
        {
            fileOutputStream.write(receiptStream.toByteArray());
        }
    }

    private JasperReportBuilder createReceiptReport(Receipt receipt) throws Exception
    {

        SalesDAO salesDAO = new SalesDAO();
        List<SaleModel> sales = salesDAO.getAllSales(receipt);

        ReceiptReportBuilder reportBuilder = new ReceiptReportBuilder();

        reportBuilder.setOrgNo(receipt.getOrgNo());
        reportBuilder.setPhoneNumber(receipt.getPhoneNr());

        reportBuilder.setReceiptDate(new Date());

        reportBuilder.setAdress(receipt.getCompanyAdress1());
        reportBuilder.setCompanyName(receipt.getCompanyName());
        reportBuilder.setUserName(Integer.toString(receipt.getUserId()));
        reportBuilder.setCounterId(receipt.getCashRegisterNr());

        reportBuilder.setNoOfArticles(receipt.countSales());
        reportBuilder.setNoOfServices(receipt.getNoOfServices());
        reportBuilder.setReceiptNo(receipt.getNr());
        float totalSum = receipt.getSumTotalIncm();
        reportBuilder.setTotalAmount(totalSum);

        for (VatType vatType : VatType.values())
        {
            float sumTotalVat = receipt.getSumTotalVat(vatType);
            float sumTotal = receipt.getSumSalesIncm(vatType);
            reportBuilder.setVatAmount(vatType, sumTotalVat);
            reportBuilder.setVatExcluded(vatType, sumTotal - sumTotalVat);
            reportBuilder.setVatIncluded(vatType, receipt.getSumSalesIncm(vatType));
        }

        List receiptRows = new ArrayList<>();
        for (SaleModel sale : sales)
        {
            ReceiptRow newReceiptRow = new ReceiptRow(sale.getArticle().getName(), sale.getPriceTotalIncmStr(), "", "", "");
            receiptRows.add(newReceiptRow);
        }
        reportBuilder.setSalesRows(receiptRows);

        List transRows = new ArrayList<>();
        transRows.add(new ReceiptRow("Kontant", General.decimalFormat.format(receipt.getCash()), "", "", ""));
        transRows.add(new ReceiptRow("Kort", General.decimalFormat.format(receipt.getCard()), "", "", ""));
        transRows.add(new ReceiptRow("Växel", General.decimalFormat.format(receipt.getPaymentPayed() - totalSum), "", "", ""));
        reportBuilder.setTransactionRows(transRows);

        ControlUnit controlUnit = Core.getInstance().getComHandler().getControlUnit();
        reportBuilder.setControlUnitNr(controlUnit.getControlUnitNr(controlUnit.controlUnitData(ReceiptType.COPY, receipt)));

        // Build report
        return reportBuilder.build(ReceiptTypeDAO.ReceiptType.COPY, PaymentController.PaymentType.NONE);

    }

}
