/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.common;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import pos_client.db.dao.ReceiptTypeDAO;
import pos_client.db.dao.SettingsDAO;
import pos_client.db.dao.VatDAO;
import pos_client.fxml.payment.PaymentController;
import pos_client.reporting.DailyReportBuilder;
import pos_client.reporting.ReceiptReportBuilder;
import pos_client.reporting.ReceiptRow;
import pos_client.reporting.datasource.DaylieReportZDataSource;
import pos_client.windows.Log;

/**
 *
 * @author Server
 */
public class PrinterHandler
{

    private ArrayList<PrinterModel> printers = new ArrayList();

    public PrinterHandler()
    {

    }

    public void add(String printer, PrinterModel.PrinterType type)
    {
        printers.add(new PrinterModel(printer, type));
    }

    public PrinterModel get(PrinterModel.PrinterType type)
    {
        for (PrinterModel printer : printers)
        {
            if (printer.getType() == type)
            {
                return printer;
            }
        }

        return null;
    }

    private void clear()
    {
        printers.clear();
    }

    public void refresh()
    {
        clear();
        loadPrinterStatus();
    }

    public void loadPrinterStatus()
    {
        Core.getInstance().getLog().log("Laddar in sparade printer val", Log.LogLevel.DESCRIPTIVE);

        SettingsDAO settingsDAO = new SettingsDAO();

        add(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_PRINTER_REPORT), PrinterModel.PrinterType.REPORT);
        add(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_PRINTER_RECEIPT), PrinterModel.PrinterType.RECEIPT);
        add(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_PRINTER_BONG_1), PrinterModel.PrinterType.BONG1);
        add(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_PRINTER_BONG_2), PrinterModel.PrinterType.BONG2);
        add(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_PRINTER_BONG_3), PrinterModel.PrinterType.BONG3);
        add(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_PRINTER_BONG_4), PrinterModel.PrinterType.BONG4);
    }

    public void print(String printer, JasperPrint jasperPrint)
    {
        long start = System.currentTimeMillis();

        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(new PrinterName(printer, null));

        SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
        configuration.setDisplayPageDialog(false);
        configuration.setDisplayPrintDialog(false);

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        exporter.setConfiguration(configuration);
        try
        {
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        } catch (Exception ex)
        {
            Core.getInstance().getLog().log("Lyckades inte skriva ut: " + ex.toString(), Log.LogLevel.DESCRIPTIVE);
        }

        try
        {
            exporter.exportReport();
        } catch (JRException ex)
        {
            Core.getInstance().getLog().log("Fel i export av rapport: " + ex.toString(), Log.LogLevel.CRITICAL);
        }

        Core.getInstance().getLog().log("Printing time : " + (System.currentTimeMillis() - start), Log.LogLevel.DESCRIPTIVE);
    }

    public JasperPrint createTestReport()
    {
        JasperPrint jasperPrint = null;

        try
        {
            jasperPrint = new DailyReportBuilder(new DaylieReportZDataSource()).build().toJasperPrint();
        } catch (Exception ex)
        {
            Core.getInstance().getLog().log("Fel i skapning av test rapport: " + ex.toString(), Log.LogLevel.CRITICAL);
        }

        return jasperPrint;
    }

    public JasperPrint createTestReceipt()
    {
        LocalTime now = LocalTime.now();

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

        for (VatDAO.VatType vatType : VatDAO.VatType.values())
        {
            report.setVatAmount(vatType, 1);
            report.setVatExcluded(vatType, 10 - 1);
            report.setVatIncluded(vatType, 11);
        }

        List rows = new ArrayList<>();
        rows.add(new ReceiptRow("Test 1", General.decimalFormat.format(30.10), "", "", ""));
        rows.add(new ReceiptRow("Test 2", General.decimalFormat.format(30.10), "", "", ""));
        report.setSalesRows(rows);

        List transRows = new ArrayList<>();
        transRows.add(new ReceiptRow("Kontant", General.decimalFormat.format(45.01), "", "", ""));
        transRows.add(new ReceiptRow("Kort", General.decimalFormat.format(30.01), "", "", ""));
        report.setTransactionRows(transRows);

        JasperPrint jasperPrint = null;
        try
        {
            jasperPrint = report.build(ReceiptTypeDAO.ReceiptType.ADMIN, PaymentController.PaymentType.NONE).toJasperPrint();
        } catch (Exception ex)
        {
            Core.getInstance().getLog().log("Fel i skapning av test kvitto: " + ex.toString(), Log.LogLevel.CRITICAL);
        }

        LocalTime after = LocalTime.now();
        Duration elapsed = Duration.between(now, after);

        Core.getInstance().getLog().log("Genererade kvitto på " + elapsed.toMillis() + "mSek", Log.LogLevel.DESCRIPTIVE);

        return jasperPrint;
    }

    public void detectPrinters()
    {
        ObservableSet<Printer> allPrinters = Printer.getAllPrinters();
        for (Printer allPrinter : allPrinters)
        {
            System.out.println(allPrinter.getName());
        }
    }

}
