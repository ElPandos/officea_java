package pos_client.reporting;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableSet;
import javafx.print.Printer;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.junit.Test;
import pos_client.common.General;

/**
 *
 * @author alves
 */
public class ReceiptTest
{

    @Test
    public void testReport() throws Exception
    {

        // Compile jrxml file.
        JasperReport jasperReport = JasperCompileManager.compileReport("report1Ztest.jrxml");

//       // Parameters for report
        Map<String, Object> parameters = new HashMap<String, Object>();
//
//        // DataSource
//        // This is simple example, no database.
//        // then using empty datasource.
        JRDataSource dataSource = new JREmptyDataSource();
//
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//
//        // Make sure the output directory exists.
//        File outDir = new File("C:/temp");
//        outDir.mkdirs();

        // Export to PDF.
        JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/temp/StyledTextReport.pdf");

        System.out.println("Done!");
    }

    @Test
    public void detectPrinters()
    {
        ObservableSet<Printer> allPrinters = Printer.getAllPrinters();
        for (Printer allPrinter : allPrinters)
        {
            System.out.println(allPrinter.getName());
        }
    }

    @Test
    public void createReceipt() throws Exception
    {
        LocalTime before = LocalTime.now();
        ReceiptReportBuilder report = new ReceiptReportBuilder();
        report.setAdress("Störjagårdsvägen 29");
        report.setCompanyName("Alvesta AB");
        report.setCounterId("1");
        report.setNoOfArticles(3);
        report.setNoOfServices(0);
        report.setOrgNo("3333-4444");
        report.setPhoneNumber("0472-47494");
        report.setReceiptDate(new Date());
        report.setReceiptNo(1);
        report.setTotalAmount(500);
        report.setVat3Amount(12);
        report.setVat3Excluded(5.55555);
        report.setVat3Included(6);
        report.setVat25Amount(0);
        report.setVat2Excluded(0);
        report.setVat2Included(0);
        report.setVat4Amount(7);
        report.setVat4Excluded(8);
        report.setVat4Included(9);
        report.setVatToalAmount(499);
        report.setVatTotalExcluded(300);
        report.setVatTotalIncluded(249);

        List rows = new ArrayList<>();
        rows.add(new ReceiptRow("Test 1", General.decimalFormat.format(30.10)));
        rows.add(new ReceiptRow("Test 2", General.decimalFormat.format(30.10)));
        report.setSalesRows(rows);

        List transRows = new ArrayList<>();
        transRows.add(new ReceiptRow("Kontant", General.decimalFormat.format(45.01)));
        transRows.add(new ReceiptRow("Kort", General.decimalFormat.format(30.01)));
        report.setTransactionRows(transRows);

        report.build(true).toPdf(new FileOutputStream("C:\\Temp\\test.pdf"));
        LocalTime after = LocalTime.now();
        Duration elapsed = Duration.between(before, after);
        System.out.println("Generated in: " + elapsed.toMillis());
    }

}
