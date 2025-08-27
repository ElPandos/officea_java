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
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Rectangle;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.communication.comHandler;
import pos_client.db.dao.SettingsDAO;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsIntegrationCashBoxController implements Initializable {

    @FXML
    private ComboBox<String> cbxType;
    @FXML
    private ComboBox<String> cbxComport;
    @FXML
    private Rectangle recStatus;

    private String name = DefinedVariables.getInstance().SETTING_COM_CASHBOX;
    private SettingsDAO settingsDAO = new SettingsDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        populate();
        status();
    }

    public void refresh() {

        clear();
        status();
        populate();
    }

    private void clear() {

        cbxType.getItems().clear();
        cbxComport.getItems().clear();
    }

    private void populate() {

        loadCommPortsCbx();
        loadSavedSettings();
    }

    private void status() {
        isConnected();
    }

    private void loadSavedSettings() {
        String comType = settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_TYPE);
        cbxType.getItems().add(comType);
        cbxType.setValue(comType);

        cbxComport.setValue(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_PORT));

    }

    private void loadCommPortsCbx() {
        for (int i = 0; i < 101; i++) {
            cbxComport.getItems().add(General.getInstance().int2Str(i));
        }
    }

    private void isConnected() {
        if (Core.getInstance().getComHandler().isConnected(comHandler.ComType.CASHBOX)) {
            recStatus.setFill(General.getInstance().CONNECTED);
        } else {
            recStatus.setFill(General.getInstance().DISCONNECTED);
        }
    }

    @FXML
    private void OnTest(ActionEvent event) {

        if (!Core.getInstance().getComHandler().isConnected(comHandler.ComType.CASHBOX)) {
            if (Core.getInstance().getComHandler().connect(comHandler.ComType.CASHBOX)) {
                recStatus.setFill(General.getInstance().CONNECTED);
                Core.getInstance().getComHandler().write(comHandler.ComType.CASHBOX, "open");
            }
        } else {
            Core.getInstance().getComHandler().write(comHandler.ComType.CASHBOX, "open");
        }

        if (Core.getInstance().getComHandler().close(comHandler.ComType.CASHBOX)) {
            recStatus.setFill(General.getInstance().DISCONNECTED);
        } else {
            Core.getInstance().getLog().log("OnTest - Failed to close port", Log.LogLevel.CRITICAL);
        }
    }

    @FXML
    private void OnSaveAndConnect(ActionEvent event) {

        ArrayList<String> controlUnitSettings = new ArrayList();
        controlUnitSettings.add(DefinedVariables.getInstance().SETTING_COM_TYPE);
        controlUnitSettings.add(DefinedVariables.getInstance().SETTING_COM_PORT);

        ArrayList<String> inputData = new ArrayList();
        inputData.add(cbxType.getSelectionModel().getSelectedItem());
        inputData.add(cbxComport.getSelectionModel().getSelectedItem());

        for (int i = 0; i < controlUnitSettings.size(); i++) {
            settingsDAO.updateSettingValue(name + controlUnitSettings.get(i), inputData.get(i));
        }
        
        if (!Core.getInstance().getComHandler().isConnected(comHandler.ComType.CASHBOX)) {
            if (Core.getInstance().getComHandler().connect(comHandler.ComType.CASHBOX)) {
                recStatus.setFill(General.getInstance().CONNECTED);
            }
        } 
    }

}
