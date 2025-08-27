/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.control.ComboBox;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.common.PrinterHandler;
import pos_client.common.PrinterModel;
import pos_client.db.dao.SettingsDAO;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsReportsController implements Initializable
{

    @FXML
    private ComboBox<String> cbxReceiptPrinter;
    @FXML
    private ComboBox<String> cbxReportPrinter;
    @FXML
    private ComboBox<String> cbxBongPrinter1;
    @FXML
    private ComboBox<String> cbxBongPrinter2;
    @FXML
    private ComboBox<String> cbxBongPrinter3;
    @FXML
    private ComboBox<String> cbxBongPrinter4;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        populate();
    }

    private void clear()
    {
        cbxReceiptPrinter.getItems().clear();
        cbxReportPrinter.getItems().clear();
        cbxBongPrinter1.getItems().clear();
        cbxBongPrinter2.getItems().clear();
        cbxBongPrinter3.getItems().clear();
        cbxBongPrinter4.getItems().clear();
    }

    public void refresh()
    {

        clear();
        populate();

        Core.getInstance().getPrinterHandler().refresh();
    }

    private void populate()
    {

        loadPrinters();
        selectSavedPrinters();
    }

    private void loadNoPrinter()
    {

        cbxReceiptPrinter.getItems().add("-");
        cbxReportPrinter.getItems().add("-");
        cbxBongPrinter1.getItems().add("-");
        cbxBongPrinter2.getItems().add("-");
        cbxBongPrinter3.getItems().add("-");
        cbxBongPrinter4.getItems().add("-");
    }

    private void loadPrinters()
    {

        loadNoPrinter();

        ObservableSet<Printer> allPrinters = Printer.getAllPrinters();

        for (Printer allPrinter : allPrinters)
        {
            cbxReceiptPrinter.getItems().add(allPrinter.getName());
            cbxReportPrinter.getItems().add(allPrinter.getName());
            cbxBongPrinter1.getItems().add(allPrinter.getName());
            cbxBongPrinter2.getItems().add(allPrinter.getName());
            cbxBongPrinter3.getItems().add(allPrinter.getName());
            cbxBongPrinter4.getItems().add(allPrinter.getName());
        }

        selectSavedPrinters();
    }

    private void selectSavedPrinters()
    {

        PrinterHandler printerHandler = Core.getInstance().getPrinterHandler();

        if (printerHandler != null)
        {
            select(cbxReceiptPrinter, printerHandler.get(PrinterModel.PrinterType.RECEIPT).getName());
            select(cbxReportPrinter, printerHandler.get(PrinterModel.PrinterType.REPORT).getName());
            select(cbxBongPrinter1, printerHandler.get(PrinterModel.PrinterType.BONG1).getName());
            select(cbxBongPrinter2, printerHandler.get(PrinterModel.PrinterType.BONG2).getName());
            select(cbxBongPrinter3, printerHandler.get(PrinterModel.PrinterType.BONG3).getName());
            select(cbxBongPrinter4, printerHandler.get(PrinterModel.PrinterType.BONG4).getName());
        }
    }

    private void select(ComboBox<String> cbxItem, String name)
    {

        if (cbxItem.getItems().indexOf(name) != -1)
        {
            cbxItem.getSelectionModel().select(name);
        } else
        {
            cbxItem.getSelectionModel().select("-");
        }
    }

    @FXML
    private void OnBtnReceiptPrinterTest(ActionEvent event)
    {

        printTestBong(cbxReceiptPrinter);
    }

    @FXML
    private void OnBtnReportPrinterTest(ActionEvent event)
    {

        printTestBong(cbxReportPrinter);
    }

    @FXML
    private void OnBtnBongPrinter1Test(ActionEvent event)
    {

        printTestBong(cbxBongPrinter1);
    }

    @FXML
    private void OnBtnBongPrinter2Test(ActionEvent event)
    {

        printTestBong(cbxBongPrinter2);
    }

    @FXML
    private void OnBtnBongPrinter3Test(ActionEvent event)
    {
        printTestBong(cbxBongPrinter3);
    }

    @FXML
    private void OnBtnBongPrinter4Test(ActionEvent event)
    {
        printTestBong(cbxBongPrinter4);
    }

    private void printTestBong(ComboBox<String> cbx)
    {
        String selectedPrinter = cbx.getSelectionModel().getSelectedItem();

        try
        {
            Core.getInstance().getPrinterHandler().print(selectedPrinter, Core.getInstance().getPrinterHandler().createTestReceipt());
        } catch (Exception ex)
        {
            Core.getInstance().getLog().log("printTestBong - Lyckades inte skriva ut...", Log.LogLevel.CRITICAL);
        }
    }

    @FXML
    private void OnSavePrinters(ActionEvent event)
    {
        ArrayList<String> selectedPrinters = new ArrayList();

        selectedPrinters.add(DefinedVariables.getInstance().SETTING_PRINTER_RECEIPT);
        selectedPrinters.add(DefinedVariables.getInstance().SETTING_PRINTER_REPORT);
        selectedPrinters.add(DefinedVariables.getInstance().SETTING_PRINTER_BONG_1);
        selectedPrinters.add(DefinedVariables.getInstance().SETTING_PRINTER_BONG_2);
        selectedPrinters.add(DefinedVariables.getInstance().SETTING_PRINTER_BONG_3);
        selectedPrinters.add(DefinedVariables.getInstance().SETTING_PRINTER_BONG_4);

        ArrayList<String> inputData = new ArrayList();
        inputData.add(cbxReceiptPrinter.getSelectionModel().getSelectedItem());
        inputData.add(cbxReportPrinter.getSelectionModel().getSelectedItem());
        inputData.add(cbxBongPrinter1.getSelectionModel().getSelectedItem());
        inputData.add(cbxBongPrinter2.getSelectionModel().getSelectedItem());
        inputData.add(cbxBongPrinter3.getSelectionModel().getSelectedItem());
        inputData.add(cbxBongPrinter4.getSelectionModel().getSelectedItem());

        SettingsDAO settingsDAO = new SettingsDAO();
        for (int i = 0; i < selectedPrinters.size(); i++)
        {
            settingsDAO.updateSettingValue(selectedPrinters.get(i), inputData.get(i));
        }
    }
}
