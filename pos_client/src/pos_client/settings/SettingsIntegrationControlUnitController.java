/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.db.dao.SettingsDAO;
import pos_client.communication.comHandler;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author Server
 */
public class SettingsIntegrationControlUnitController implements Initializable
{

    @FXML
    private Rectangle recStatus;
    @FXML
    private Button btnSaveAndConnect;
    @FXML
    private Button btnTest;
    @FXML
    private ComboBox<String> cbxType;
    @FXML
    private ComboBox<String> cbxBits;
    @FXML
    private ComboBox<String> cbxStopBit;
    @FXML
    private ComboBox<String> cbxBaudrate;
    @FXML
    private ComboBox<String> cbxParity;
    @FXML
    private ComboBox<String> cbxComport;

    private String name = DefinedVariables.getInstance().SETTING_COM_CONTROLUNIT;
    private SettingsDAO settingsDAO = new SettingsDAO();
    @FXML
    private Label lblControlUnitResult;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        populate();
        status();
    }

    public void refresh()
    {

        clear();
        status();
        populate();
    }

    private void clear()
    {

        cbxBaudrate.getItems().clear();
        cbxComport.getItems().clear();
        cbxBits.getItems().clear();
        cbxParity.getItems().clear();
        cbxStopBit.getItems().clear();
    }

    private void populate()
    {

        loadControlUnitCbx();
        loadSavedSettings();
    }

    private void status()
    {

        isConnected();
    }

    private void isConnected()
    {
        if (Core.getInstance().getComHandler().isConnected(comHandler.ComType.CONTROLUNIT))
        {
            recStatus.setFill(General.getInstance().CONNECTED);
        } else
        {
            recStatus.setFill(General.getInstance().DISCONNECTED);
        }
    }

    private void loadSavedSettings()
    {

        String comType = settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_TYPE);
        cbxType.getItems().add(comType);
        cbxType.setValue(comType);

        cbxComport.setValue(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_PORT));
        cbxBaudrate.setValue(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_BAUDRATE));
        cbxBits.setValue(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_DATABITS));
        cbxParity.setValue(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_PARITY));
        cbxStopBit.setValue(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_STOPBIT));
    }

    private void loadControlUnitCbx()
    {

        for (int i = 0; i < 101; i++)
        {
            cbxComport.getItems().add(General.getInstance().int2Str(i));
        }

        cbxBaudrate.getItems().addAll(
                "300",
                "1200",
                "2400",
                "4800",
                "9600",
                "14400",
                "19200",
                "28800",
                "38400",
                "57600",
                "115200",
                "230400"
        );

        for (int i = 5; i < 9; i++)
        {
            cbxBits.getItems().add(General.getInstance().int2Str(i));
        }

        for (int i = 0; i < 2; i++)
        {
            cbxParity.getItems().add(General.getInstance().int2Str(i));
        }

        for (int i = 0; i < 2; i++)
        {
            cbxStopBit.getItems().add(General.getInstance().int2Str(i));
        }
    }

    public void writeAndReadTest()
    {
        String testCode = "ver 0000";

        Core.getInstance().getLog().log("OnTest - Kör COM test kod: " + testCode, Log.LogLevel.SYSTEM);

        Core.getInstance().getComHandler().write(comHandler.ComType.CONTROLUNIT, testCode);

        String testResult = Core.getInstance().getComHandler().read(comHandler.ComType.CONTROLUNIT);
        Core.getInstance().getLog().log(testResult, Log.LogLevel.SYSTEM);
        lblControlUnitResult.setText("Control Result: " + testResult);
    }

    @FXML
    private void OnTest(ActionEvent event)
    {

        String ret = "error";

        if (!Core.getInstance().getComHandler().isConnected(comHandler.ComType.CONTROLUNIT))
        {
            if (Core.getInstance().getComHandler().connect(comHandler.ComType.CONTROLUNIT))
            {
                recStatus.setFill(General.getInstance().CONNECTED);
                writeAndReadTest();
            }

            if (Core.getInstance().getComHandler().close(comHandler.ComType.CONTROLUNIT))
            {
                recStatus.setFill(General.getInstance().DISCONNECTED);
            } else
            {
                Core.getInstance().getLog().log("OnTest - Failed to close port", Log.LogLevel.CRITICAL);
            }
        } else
        {
            writeAndReadTest();
        }
    }

    @FXML
    private void OnSaveAndConnect(ActionEvent event)
    {

        if (!Core.getInstance().getComHandler().isConnected(comHandler.ComType.CONTROLUNIT))
        {

            ArrayList<String> controlUnitSettings = new ArrayList();

            controlUnitSettings.add(DefinedVariables.getInstance().SETTING_COM_TYPE);
            controlUnitSettings.add(DefinedVariables.getInstance().SETTING_COM_PORT);
            controlUnitSettings.add(DefinedVariables.getInstance().SETTING_COM_BAUDRATE);
            controlUnitSettings.add(DefinedVariables.getInstance().SETTING_COM_DATABITS);
            controlUnitSettings.add(DefinedVariables.getInstance().SETTING_COM_PARITY);
            controlUnitSettings.add(DefinedVariables.getInstance().SETTING_COM_STOPBIT);

            ArrayList<String> inputData = new ArrayList();
            inputData.add(cbxType.getSelectionModel().getSelectedItem());
            inputData.add(cbxComport.getSelectionModel().getSelectedItem());
            inputData.add(cbxBaudrate.getSelectionModel().getSelectedItem());
            inputData.add(cbxBits.getSelectionModel().getSelectedItem());
            inputData.add(cbxParity.getSelectionModel().getSelectedItem());
            inputData.add(cbxStopBit.getSelectionModel().getSelectedItem());

            for (int i = 0; i < controlUnitSettings.size(); i++)
            {
                settingsDAO.updateSettingValue(name + controlUnitSettings.get(i), inputData.get(i));
            }

            if (Core.getInstance().getComHandler().connect(comHandler.ComType.CONTROLUNIT))
            {
                recStatus.setFill(General.getInstance().CONNECTED);
                btnSaveAndConnect.setText("Koppla från");
            } else
            {
                recStatus.setFill(General.getInstance().DISCONNECTED);
            }
        } else if (Core.getInstance().getComHandler().close(comHandler.ComType.CONTROLUNIT))
        {
            recStatus.setFill(General.getInstance().DISCONNECTED);
            btnSaveAndConnect.setText("Spara och anslut");
        }
    }
}
