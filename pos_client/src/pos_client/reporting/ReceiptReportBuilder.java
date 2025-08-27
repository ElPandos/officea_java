package pos_client.reporting;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import java.util.List;
import java.util.Map;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.Units;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.Styles;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import pos_client.common.General;
import pos_client.db.dao.VatDAO.VatType;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.fxml.payment.PaymentController;
import pos_client.fxml.payment.PaymentController.PaymentType;

/**
 *
 * @author alves
 */
public class ReceiptReportBuilder
{

    SimpleDateFormat receiptDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static final String decimalFormatPattern = "###,##0.00";
    private static final DecimalFormat decimalFormat = new DecimalFormat(decimalFormatPattern);
    public static final String RECEIPT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final Number REPORT_WIDTH = 72.1;
    private static final Number REPORT_HEIGHT = 200; // Jasper automaticly adjusts height
    private String companyName;
    private String adress;
    private String phoneNumber;
    private String counterId;
    private String userName;
    private String table;
    private String orgNo;

    private Integer receiptNo;
    private Integer receiptProfoNo;

    private Date receiptDate; // date + time
    private double totalAmount;
    private Integer noOfServices;
    private Integer noOfArticles;

    private float[] vatAmount = new float[4];
    private float[] vatExcluded = new float[4];
    private float[] vatIncluded = new float[4];

    private List<ReceiptRow> salesRows;
    private List<ReceiptRow> transactionRows;

    Map<Integer, String> terminalOutput = new HashMap<>();

    private String terminalResponse;
    private String controlUnitNr;

    enum SizeType
    {
        SMALL_INFO,
        SMALL,
        RECEIPT,
        MEDIUM,
        LARGE,
        TITLE
    };

    public ReceiptReportBuilder()
    {
        salesRows = new ArrayList();
        transactionRows = new ArrayList();
    }

    private ReceiptType receiptType = null;
    private PaymentType paymentType = null;

    public JasperReportBuilder build(ReceiptType receiptType, PaymentController.PaymentType paymentType)
    {
        this.receiptType = receiptType;
        this.paymentType = paymentType;

        JasperReportBuilder reportBuilder = report();

        /* Report settings */
        reportBuilder.setPageFormat(Units.mm(REPORT_WIDTH), Units.mm(REPORT_HEIGHT), PageOrientation.PORTRAIT);
        reportBuilder.setIgnorePagination(true);
        reportBuilder.setShowColumnTitle(false);

        switch (receiptType)
        {
            case ADMIN:
                reportBuilder.title(createAdmin());
                break;
            case ONGOING:
                break;
            case PARKED:
                break;
            case CANCELED:
                break;
            case NORMAL:
                reportBuilder.title(createNormal("Kassakvitto"));
            case PRACTICE:
                break;
            case COPY:
                reportBuilder.title(createNormal("KvittoKopia"));
                break;
            case REFUND:
                reportBuilder.title(createRefund());
                break;
            case PROFO:
                reportBuilder.title(createProfo());
                break;
        }

        return reportBuilder;
    }

    private JasperReportBuilder createReceiptRows()
    {
        JasperReportBuilder report = report();

        report.setDataSource(salesRows);
        report.setShowColumnTitle(false);

        StyleBuilder styleLeft = stl.style();
        styleLeft.setFontSize(6);
        styleLeft.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);

        StyleBuilder styleRight = stl.style();
        styleRight.setFontSize(6);
        styleRight.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);

        report.columns(Columns.column("Item", "articleItem", type.stringType()).setStyle(styleLeft).setWidth(80),
                Columns.column("Amount", "amount", type.stringType()).setStyle(styleRight).setWidth(20));

        return report;
    }

    private JasperReportBuilder createTransactionRows()
    {
        JasperReportBuilder rows = report();

        rows.setDataSource(transactionRows);
        rows.setShowColumnTitle(false);

        StyleBuilder styleLeft = stl.style();
        styleLeft.setFontSize(4);
        styleLeft.setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);

        rows.addColumn(col.column("Item", "item", type.stringType()).setStyle(styleLeft));

        StyleBuilder styleRight = stl.style();
        styleRight.setFontSize(4);
        styleRight.setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT);

        rows.addColumn(col.column("Amount", "amount", type.stringType()).setWidth(10).setStyle(styleRight));

        return rows;
    }

    private ComponentBuilder<?, ?> createPosBoutiqeInfo()
    {
        return cmp.verticalList(
                createTextField(companyName, HorizontalTextAlignment.CENTER, SizeType.TITLE, true),
                createTextField("Orgnr: " + orgNo, HorizontalTextAlignment.CENTER, SizeType.MEDIUM),
                createTextField("Tel: " + phoneNumber, HorizontalTextAlignment.CENTER, SizeType.MEDIUM),
                createTextField(adress, HorizontalTextAlignment.CENTER, SizeType.MEDIUM)
        );
    }

    private ComponentBuilder<?, ?> createPosInfo()
    {
        return cmp.horizontalList(
                createTextField("Kassa: ", HorizontalTextAlignment.LEFT, SizeType.SMALL, true),
                createTextField(counterId, HorizontalTextAlignment.LEFT, SizeType.SMALL),
                createTextField("Kassör: ", HorizontalTextAlignment.RIGHT, SizeType.SMALL, true),
                createTextField(userName, HorizontalTextAlignment.RIGHT, SizeType.SMALL)
        );
    }

    private ComponentBuilder<?, ?> createReceiptInfo(ReceiptType type)
    {
        String name = "Kvitto nr: ";
        String value = "";
        if (receiptNo != null)
        {
            value = General.getInstance().int2Str(receiptNo);
        }

        if (type == ReceiptType.PROFO)
        {
            name = "Profo nr: ";
            if (receiptProfoNo != null)
            {
                value = General.getInstance().int2Str(receiptProfoNo);
            }
        }
        if (table != null && !table.isEmpty())
        {
            return cmp.verticalList(
                    cmp.horizontalList(
                            createTextField(name, HorizontalTextAlignment.LEFT, SizeType.SMALL, true),
                            createTextField(value, HorizontalTextAlignment.LEFT, SizeType.SMALL),
                            createTextField("Datum/Tid: ", HorizontalTextAlignment.RIGHT, SizeType.SMALL, true),
                            createTextField(receiptDateFormatter.format(receiptDate), HorizontalTextAlignment.RIGHT, SizeType.SMALL)
                    ),
                    cmp.horizontalList(
                            createTextField("Bord: ", HorizontalTextAlignment.LEFT, SizeType.SMALL, true),
                            createTextField(table, HorizontalTextAlignment.LEFT, SizeType.SMALL),
                            createTextField("", HorizontalTextAlignment.RIGHT, SizeType.SMALL, true),
                            createTextField("", HorizontalTextAlignment.RIGHT, SizeType.SMALL)
                    ));
        }

        return cmp.verticalList(
                cmp.horizontalList(
                        createTextField(name, HorizontalTextAlignment.LEFT, SizeType.SMALL, true),
                        createTextField(value, HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField("Datum/Tid: ", HorizontalTextAlignment.RIGHT, SizeType.SMALL, true),
                        createTextField(receiptDateFormatter.format(receiptDate), HorizontalTextAlignment.RIGHT, SizeType.SMALL)
                ));
    }

    private ComponentBuilder<?, ?> createAdmin()
    {

        return cmp.verticalList(
                createPosBoutiqeInfo(),
                cmp.horizontalList(
                        createTextField(receiptDateFormatter.format(receiptDate), HorizontalTextAlignment.CENTER, SizeType.MEDIUM)
                ),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                createPosInfo(),
                createReceiptInfo(ReceiptType.ADMIN),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                cmp.horizontalList(
                        createTextField("ADMINKVITTO", HorizontalTextAlignment.CENTER, SizeType.LARGE, true)),
                cmp.verticalGap(Units.mm(2)),
                createFooterTerminal(),
                cmp.verticalGap(Units.mm(2))
        );
    }

    private ComponentBuilder<?, ?> createNormal(String title)
    {
        SimpleDateFormat receiptDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return cmp.verticalList(
                createPosBoutiqeInfo(),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                createPosInfo(),
                createReceiptInfo(ReceiptType.NORMAL),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                /* Receipt rows */
                cmp.horizontalList(
                        createTextField(title, HorizontalTextAlignment.CENTER, SizeType.MEDIUM, true)),
                cmp.subreport(createReceiptRows()),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                cmp.horizontalList(
                        createTextField("TOTAL: ", HorizontalTextAlignment.LEFT, SizeType.MEDIUM, true),
                        createTextField(decimalFormat.format(totalAmount), HorizontalTextAlignment.RIGHT, SizeType.MEDIUM, true)
                ),
                /* Transaction rows */
                cmp.subreport(createTransactionRows()),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                createFooterTotalVat(),
                cmp.verticalGap(Units.mm(2)),
                createFooterTerminal(),
                cmp.verticalGap(Units.mm(2)),
                createFooterControlUnit()
        );
    }

    private ComponentBuilder<?, ?> createRefund()
    {
        SimpleDateFormat receiptDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return cmp.verticalList(
                createPosBoutiqeInfo(),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                createPosInfo(),
                createReceiptInfo(ReceiptType.NORMAL),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                /* Receipt rows */
                cmp.horizontalList(
                        createTextField("Återbetalning", HorizontalTextAlignment.CENTER, SizeType.MEDIUM, true)),
                cmp.subreport(createReceiptRows()),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                cmp.horizontalList(
                        createTextField("TOTAL: ", HorizontalTextAlignment.LEFT, SizeType.MEDIUM, true),
                        createTextField(decimalFormat.format(totalAmount), HorizontalTextAlignment.RIGHT, SizeType.MEDIUM, true)
                ),
                /* Transaction rows */
                cmp.subreport(createTransactionRows()),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                createFooterTotalVat(),
                cmp.verticalGap(Units.mm(2)),
                createFooterTerminal(),
                cmp.verticalGap(Units.mm(2)),
                createFooterControlUnit()
        );
    }

    private ComponentBuilder<?, ?> createProfo()
    {
        SimpleDateFormat receiptDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return cmp.verticalList(
                createPosBoutiqeInfo(),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                createPosInfo(),
                createReceiptInfo(ReceiptType.PROFO),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                /* Receipt rows */
                cmp.horizontalList(
                        createTextField("Nota", HorizontalTextAlignment.CENTER, SizeType.MEDIUM, true)),
                cmp.subreport(createReceiptRows()),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                cmp.horizontalList(
                        createTextField("TOTAL: ", HorizontalTextAlignment.LEFT, SizeType.MEDIUM, true),
                        createTextField(decimalFormat.format(totalAmount), HorizontalTextAlignment.RIGHT, SizeType.MEDIUM, true)
                ),
                /* Transaction rows */
                /*
                cmp.subreport(createTransactionRows()),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                createFooterTotalVat(),
                cmp.verticalGap(Units.mm(2)),
                 */
                cmp.verticalGap(Units.mm(2)),
                createFooterControlUnit()
        );
    }

    private ComponentBuilder<?, ?> createFooterTotalVat()
    {
        return cmp.verticalList(cmp.horizontalList(
                createTextField("Antal varor: ", HorizontalTextAlignment.CENTER, SizeType.SMALL, true),
                createTextField(noOfArticles.toString(), HorizontalTextAlignment.CENTER, SizeType.SMALL)
        ),
                cmp.horizontalList(
                        createTextField("Antal tjänster: ", HorizontalTextAlignment.CENTER, SizeType.SMALL, true),
                        createTextField(noOfServices.toString(), HorizontalTextAlignment.CENTER, SizeType.SMALL)
                ),
                cmp.verticalGap(Units.mm(2)),
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                cmp.horizontalList(
                        createTextField("MOMS %", HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField("MOMS", HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField("NETTO", HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField("BRUTTO", HorizontalTextAlignment.RIGHT, SizeType.SMALL)
                ),
                cmp.horizontalList(createTextField("0%", HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatAmount(VatType.VAT1)), HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatExcluded(VatType.VAT1)), HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatIncluded(VatType.VAT1)), HorizontalTextAlignment.RIGHT, SizeType.SMALL)
                ),
                cmp.horizontalList(createTextField("25%", HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatAmount(VatType.VAT2)), HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatExcluded(VatType.VAT2)), HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatIncluded(VatType.VAT2)), HorizontalTextAlignment.RIGHT, SizeType.SMALL)
                ),
                cmp.horizontalList(createTextField("12%", HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatAmount(VatType.VAT3)), HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatExcluded(VatType.VAT3)), HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatIncluded(VatType.VAT3)), HorizontalTextAlignment.RIGHT, SizeType.SMALL)
                ),
                cmp.horizontalList(createTextField("6%", HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatAmount(VatType.VAT4)), HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatExcluded(VatType.VAT4)), HorizontalTextAlignment.LEFT, SizeType.SMALL),
                        createTextField(decimalFormat.format(getVatIncluded(VatType.VAT4)), HorizontalTextAlignment.RIGHT, SizeType.SMALL)
                ),
                cmp.verticalGap(Units.mm(2)),
                cmp.horizontalList(
                        createTextField("TOTAL: ", HorizontalTextAlignment.LEFT, SizeType.SMALL, true),
                        createTextField(decimalFormat.format(getVatTotalAmount()), HorizontalTextAlignment.LEFT, SizeType.SMALL, true),
                        createTextField(decimalFormat.format(getVatTotalExcluded()), HorizontalTextAlignment.LEFT, SizeType.SMALL, true),
                        createTextField(decimalFormat.format(getVatTotalIncluded()), HorizontalTextAlignment.RIGHT, SizeType.SMALL, true)
                )
        );
    }

    private ComponentBuilder<?, ?> createFooterTerminal()
    {
        switch (this.paymentType)
        {
            case CARD:

                if (terminalOutput.containsKey(receiptType.ordinal()))
                {
                    return cmp.verticalList(
                            cmp.line().setPen(Styles.penThin()),
                            cmp.verticalGap(Units.mm(2)),
                            cmp.horizontalList(createTextField("Terminal", HorizontalTextAlignment.CENTER, SizeType.MEDIUM, true)),
                            cmp.horizontalList(createTextField(terminalOutput.get(receiptType.ordinal()), HorizontalTextAlignment.LEFT, SizeType.SMALL)),
                            cmp.verticalGap(Units.mm(2)),
                            cmp.horizontalList(createTextField(terminalResponse, HorizontalTextAlignment.CENTER, SizeType.SMALL)),
                            cmp.verticalGap(Units.mm(2))
                    );
                }

            case CASH:

                if (this.receiptType == ReceiptType.REFUND)
                {
                    return cmp.verticalList(
                            cmp.line().setPen(Styles.penThin()),
                            cmp.verticalGap(Units.mm(2)),
                            cmp.verticalList(createTextField("Uppgifter", HorizontalTextAlignment.CENTER, SizeType.MEDIUM, true),
                                    createTextField("", HorizontalTextAlignment.LEFT, SizeType.MEDIUM),
                                    createTextField("Namn:", HorizontalTextAlignment.LEFT, SizeType.MEDIUM),
                                    createTextField("", HorizontalTextAlignment.LEFT, SizeType.MEDIUM),
                                    createTextField("Personnummer:", HorizontalTextAlignment.LEFT, SizeType.MEDIUM),
                                    createTextField("", HorizontalTextAlignment.LEFT, SizeType.MEDIUM),
                                    createTextField("Signatur:", HorizontalTextAlignment.LEFT, SizeType.MEDIUM)),
                            createTextField("", HorizontalTextAlignment.LEFT, SizeType.MEDIUM),
                            cmp.verticalGap(Units.mm(2))
                    );
                }
        }

        return cmp.gap(0, 0);
    }

    private ComponentBuilder<?, ?> createFooterControlUnit()
    {
        return cmp.verticalList(
                cmp.line().setPen(Styles.penThin()),
                cmp.verticalGap(Units.mm(2)),
                createTextField("Kontroll nr:", HorizontalTextAlignment.CENTER, SizeType.SMALL, true),
                createTextField(controlUnitNr, HorizontalTextAlignment.CENTER, SizeType.SMALL_INFO)
        );
    }

    private TextFieldBuilder<String> createTextField(String label, HorizontalTextAlignment horizontalTextAlignment, SizeType type)
    {
        return createTextField(label, horizontalTextAlignment, type, false);
    }

    private TextFieldBuilder<String> createTextField(String label, HorizontalTextAlignment horizontalTextAlignment, SizeType type, boolean bold)
    {
        StyleBuilder styleBuilder = stl.style().setHorizontalTextAlignment(horizontalTextAlignment);

        switch (type)
        {
            case SMALL_INFO:
                styleBuilder.setFontSize(4);
                break;
            case SMALL:
                styleBuilder.setFontSize(5);
                break;
            case RECEIPT:
                styleBuilder.setFontSize(6);
                break;
            case MEDIUM:
                styleBuilder.setFontSize(8);
                break;
            case LARGE:
                styleBuilder.setFontSize(12);
                break;
            case TITLE:
                styleBuilder.setFontSize(18);
                break;
        }

        if (bold)
        {
            styleBuilder.bold();
        }

        return cmp.text(label).setStyle(styleBuilder);
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public void setAdress(String adress)
    {
        this.adress = adress;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public void setCounterId(String counterId)
    {
        this.counterId = counterId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setTableNr(String table)
    {
        if (table.contains("-"))
        {
            table = "";
        }

        this.table = table;
    }

    public void setOrgNo(String orgNo)
    {
        this.orgNo = orgNo;
    }

    public void setControlUnitNr(String controlUnitNr)
    {
        this.controlUnitNr = controlUnitNr;
    }

    public void setReceiptNo(Integer receiptNo)
    {
        this.receiptNo = receiptNo;
    }

    public void setReceiptProfoNo(Integer receiptProfoNo)
    {
        this.receiptProfoNo = receiptProfoNo;
    }

    public void setReceiptDate(Date receiptDate)
    {
        this.receiptDate = receiptDate;
    }

    public void setTerminalOutput(Map<Integer, String> output)
    {
        this.terminalOutput = output;
    }

    public void setTerminalResponse(String output)
    {
        this.terminalResponse = output;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public void setNoOfServices(int noOfServices)
    {
        this.noOfServices = noOfServices;
    }

    public void setNoOfArticles(int noOfArticles)
    {
        this.noOfArticles = noOfArticles;
    }

    public void setVatAmount(VatType vatType, float amount)
    {
        this.vatAmount[vatType.ordinal()] = amount;
    }

    public double getVatAmount(VatType vatType)
    {
        return this.vatAmount[vatType.ordinal()];
    }

    public void setVatExcluded(VatType vatType, float amount)
    {
        this.vatExcluded[vatType.ordinal()] = amount;
    }

    public double getVatExcluded(VatType vatType)
    {
        return this.vatExcluded[vatType.ordinal()];
    }

    public void setVatIncluded(VatType vatType, float amount)
    {
        this.vatIncluded[vatType.ordinal()] = amount;
    }

    public double getVatIncluded(VatType vatType)
    {
        return this.vatIncluded[vatType.ordinal()];
    }

    public float getVatTotalAmount()
    {
        float vatTotalAmount = 0;
        for (VatType vatType : VatType.values())
        {
            vatTotalAmount += getVatAmount(vatType);
        }

        return vatTotalAmount;
    }

    public float getVatTotalExcluded()
    {
        float vatTotalAmountExcluded = 0;
        for (VatType vatType : VatType.values())
        {
            vatTotalAmountExcluded += getVatExcluded(vatType);
        }

        return vatTotalAmountExcluded;
    }

    public float getVatTotalIncluded()
    {
        float vatTotalAmountIncluded = 0;
        for (VatType vatType : VatType.values())
        {
            vatTotalAmountIncluded += getVatIncluded(vatType);
        }

        return vatTotalAmountIncluded;
    }

    public void setSalesRows(List<ReceiptRow> itemRows)
    {
        this.salesRows = itemRows;
    }

    public void setTransactionRows(List<ReceiptRow> transactionRows)
    {
        this.transactionRows = transactionRows;
    }

}
