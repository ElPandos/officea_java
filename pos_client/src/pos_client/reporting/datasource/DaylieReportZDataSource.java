package pos_client.reporting.datasource;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import pos_client.db.dao.BoutiqueDAO;
import pos_client.db.dao.CashBoxDAO;
import pos_client.db.dao.ReceiptDAO;

/**
 *
 * @author alves
 */
public class DaylieReportZDataSource extends AbstractDataSource
{

    private final static String firstColumnName = "Händelser";
    private final static String secondColumnName = "Antal";
    private final static String thirdColumnName = "Summa";

    private ReceiptDAO receiptDAO = null;
    private BoutiqueDAO boutiqueDAO = null;
    private CashBoxDAO cashBoxDAO = null;

    public DaylieReportZDataSource()
    {
        receiptDAO = new ReceiptDAO();
        boutiqueDAO = new BoutiqueDAO();
        cashBoxDAO = new CashBoxDAO();
    }

    @Override
    public List<String[]> getRows()
    {
        List rows = new ArrayList();

        rows.add(new String[]
        {
            "Total försälningssumma", "", getSumTotalSales()
        });
        rows.add(new String[]
        {
            "           - varav kontant", "", getSumTotalCash()
        });
        rows.add(new String[]
        {
            "           - varav kortköp", "", getSumTotalCard()
        });
        rows.add(new String[]
        {
            "           - varav övrigt", "", getSumTotalCredit()
        });
        rows.add(new String[]
        {
            "Ingående växelkassa", "", "tabell för växelkassa saknas?"
        });
        rows.add(new String[]
        {
            "Rabatter", "", "Vi behöver titta på detta"
        });
        rows.add(new String[]
        {
            "Moms 6%", "", getSumMoms1()
        });
        rows.add(new String[]
        {
            "Moms 12%", "", getSumMoms2()
        });
        rows.add(new String[]
        {
            "Moms 25%", "", getSumMoms3()
        });
        rows.add(new String[]
        {
            "Grant total retur", "", getSumReceiptRefund()
        });
        rows.add(new String[]
        {
            "Grand total netto", "", "757"
        });
        rows.add(new String[]
        {
            "--------------------------------------------------------", "--------------------------------------------------------", "--------------------------------------------------------"
        });
        rows.add(new String[]
        {
            "Sålda varor", getSoldArticles(), ""
        });
        rows.add(new String[]
        {
            "Sålda tjänster", "0", ""
        });
        rows.add(new String[]
        {
            "Lådöppningar automatisk", getCashBoxOpenAutomatic(), ""
        });
        rows.add(new String[]
        {
            "Lådöppningar manuell", getCashBoxOpenManual(), ""
        });
        rows.add(new String[]
        {
            "Kvitton - Normal", getReceiptsNormal(), ""
        });
        rows.add(new String[]
        {
            "Kvitton - Kopior", getReceiptsCopy(), ""
        });
        rows.add(new String[]
        {
            "Kvitton - Parkerade", getReceiptsParked(), ""
        });
        rows.add(new String[]
        {
            "Kvitton - ProForma (Nota)", getReceiptsProForma(), ""
        });
        rows.add(new String[]
        {
            "Kvitton - Övningsläge", getReceiptsPractice(), ""
        });
        rows.add(new String[]
        {
            "Returer", getReceiptsRefund(), ""
        });
        rows.add(new String[]
        {
            "Oavslutade / Abrutna försäljningar", getReceiptsCanceled(), ""
        });

        return rows;
    }

    @Override
    public List<String> getColumnNames()
    {
        List<String> columnNames = new ArrayList();
        columnNames.add(firstColumnName);
        columnNames.add(secondColumnName);
        columnNames.add(thirdColumnName);

        return columnNames;
    }

    @Override
    public String getReportTitle()
    {
        return "Z-Dagrapport";
    }

    @Override
    public String getCompanyName()
    {
        return boutiqueDAO.getCompanyName();
    }

    @Override
    public String getOrgNo()
    {
        return boutiqueDAO.getOrgNumber();
    }

    @Override
    public String getReportId()
    {
        String printTime = LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        printTime = printTime.replace(":", "");
        printTime = printTime.replace(".", "");
        return printTime;
    }

    @Override
    public String getCashRegisterId()
    {
        return receiptDAO.getCashRegisterNr();
    }

    private String getSumTotalSales()
    {
        return receiptDAO.getSumTotalSales();
    }

    private String getSumTotalCash()
    {
        return receiptDAO.getSumTotalCash();
    }

    private String getSumTotalCard()
    {
        return receiptDAO.getSumTotalCard();
    }

    private String getSumTotalCredit()
    {
        return receiptDAO.getSumTotalCredit();
    }

    private String getCashBoxOpenManual()
    {
        return cashBoxDAO.getCashBoxOpenManual();
    }

    private String getCashBoxOpenAutomatic()
    {
        return cashBoxDAO.getCashBoxOpenAutomatic();
    }

    private String getSoldArticles()
    {
        return receiptDAO.getSoldArticles();
    }

    private String getReceiptsParked()
    {
        return receiptDAO.getReceiptsParked();
    }

    private String getReceiptsCanceled()
    {
        return receiptDAO.getReceiptsCanceled();
    }

    private String getReceiptsNormal()
    {
        return receiptDAO.getReceiptsNormal();
    }

    private String getReceiptsPractice()
    {
        return receiptDAO.getReceiptsPractice();
    }

    private String getReceiptsCopy()
    {
        return receiptDAO.getReceiptsCopy();
    }

    private String getReceiptsRefund()
    {
        return receiptDAO.getReceiptsRefund();
    }

    private String getReceiptsProForma()
    {
        return receiptDAO.getReceiptsProForma();
    }

    private String getSumMoms1()
    {
        return receiptDAO.getSumMoms1();
    }

    private String getSumMoms2()
    {
        return receiptDAO.getSumMoms2();
    }

    private String getSumMoms3()
    {
        return receiptDAO.getSumMoms3();
    }

    private String getSumReceiptRefund()
    {
        return receiptDAO.getSumReceiptRefund();
    }

}
