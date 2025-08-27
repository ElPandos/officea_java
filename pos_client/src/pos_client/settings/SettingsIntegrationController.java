/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import pos_client.common.General;
import pos_client.terminals.Jbaxi;
import pos_client.terminals.Terminal;
import pos_client.common.Core;
import pos_client.db.dao.SettingsDAO;
import pos_client.terminals.TerminalData;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsIntegrationController implements Initializable {

    @FXML
    private ImageView imgActiveCardTerminal;
    @FXML
    private ChoiceBox<String> cbxCardTerminals;
    @FXML
    private Rectangle terminalStatus;
    @FXML
    private Label txtTerminalStatus;
    @FXML
    private CheckBox chkAutoConnect;
    @FXML
    private VBox vboxTerminalFunc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populate();
        status();
    }

    private void clear() {
        cbxCardTerminals.getItems().clear();
    }

    public void refresh() {
        clear();
        status();
        populate();
    }

    private void populate() {
        loadTerminalsCbx();
    }

    private void status() {
        isConnected();
    }

    private void isConnected() {
        if (Core.getInstance().getTerminalHandler().isConnected()) {
            terminalStatus.setFill(General.getInstance().CONNECTED);
        } else {
            terminalStatus.setFill(General.getInstance().DISCONNECTED);
        }
    }

    private void loadTerminalStatus() {
        Core.getInstance().getLog().log("Laddar statusen på sparad terminal", Log.LogLevel.DESCRIPTIVE);

        Terminal terminal = Core.getInstance().getTerminalHandler().getActiveTerminal();
        if (terminal != null) {
            cbxCardTerminals.getSelectionModel().select(terminal.getName());

            int selectedIndex = cbxCardTerminals.getSelectionModel().getSelectedIndex();
            TerminalData data = getValue((Map<Integer, TerminalData>) cbxCardTerminals.getUserData(), selectedIndex);
            imgActiveCardTerminal.setImage(data.image());

            isConnected();
        }
    }

    private TerminalData getValue(Map<Integer, TerminalData> map, int key) {
        for (Map.Entry<Integer, TerminalData> entry : map.entrySet()) {
            if (entry.getKey() == key + 1) { // +1 because index start from 0 and id from 1
                return entry.getValue();
            }
        }
        return null;
    }

    private void loadTerminalsCbx() {
        cbxCardTerminals.getItems().clear();

        Core.getInstance().getLog().log("Laddar alla terminaler till comboboxen", Log.LogLevel.DESCRIPTIVE);

        SettingsDAO settingsDAO = new SettingsDAO();
        Map<Integer, TerminalData> terminals = settingsDAO.getTerminals();

        for (Map.Entry<Integer, TerminalData> entry : terminals.entrySet()) {
            cbxCardTerminals.getItems().add(entry.getValue().name());
        }

        cbxCardTerminals.setUserData(terminals);

        cbxCardTerminals.getSelectionModel().select(0); // default

        TerminalData data = getValue((Map<Integer, TerminalData>) cbxCardTerminals.getUserData(), 1);
        imgActiveCardTerminal.setImage(data.image()); // default 

        cbxCardTerminals.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TerminalData data = getValue((Map<Integer, TerminalData>) cbxCardTerminals.getUserData(), (int) newValue);
                if (data != null) {
                    imgActiveCardTerminal.setImage(data.image());
                }
            }
        });

        loadTerminalStatus();
    }

    @FXML
    private void OnTerminalConnect(ActionEvent event) {
        if (Core.getInstance().getTerminalHandler().getActiveTerminal() != null
                && !Core.getInstance().getTerminalHandler().getActiveTerminal().isConnected()) {

            String terminal = cbxCardTerminals.getValue();

            if (chkAutoConnect.selectedProperty().getValue()) {
                saveTerminalStatus();
            }

            if (Core.getInstance().getTerminalHandler().connect(Core.getInstance().getTerminalHandler().str2TerminalType(terminal))) {

                Core.getInstance().getLog().log(Core.getInstance().getTerminalHandler().getActiveTerminal().getName() + " is connected!", Log.LogLevel.NORMAL);

                if (terminal.compareTo(Jbaxi.NETS) == 0) {
                    terminalFuncions();
                }
            } else {
                Core.getInstance().getLog().log(Core.getInstance().getTerminalHandler().getActiveTerminal().getName() + " failed to connect!", Log.LogLevel.CRITICAL);
            }
        }

        isConnected();
    }

    @FXML
    private void OnTerminalClose(ActionEvent event) {
        if (Core.getInstance().getTerminalHandler().getActiveTerminal() != null
                && Core.getInstance().getTerminalHandler().isConnected()) {

            if (Core.getInstance().getTerminalHandler().close()) {
                Core.getInstance().getLog().log(Core.getInstance().getTerminalHandler().getActiveTerminal().getName() + " has closed!", Log.LogLevel.NORMAL);
            } else {
                Core.getInstance().getLog().log(Core.getInstance().getTerminalHandler().getActiveTerminal().getName() + " failed to close terminal!", Log.LogLevel.CRITICAL);
            }

            isConnected();

            vboxTerminalFunc.getChildren().clear();

            //Core.getInstance().getTerminalHandler().getActiveTerminal().connected(Core.getInstance().getTerminalHandler().isOpen());
            //Core.getInstance().getLog().getLogController().setTerminalStatus(Core.getInstance().getTerminalHandler().getActiveTerminal().isConnected());
        }
    }

    private void saveTerminalStatus() {
        SettingsDAO settingsDAO = new SettingsDAO();
        settingsDAO.saveTerminal(cbxCardTerminals.getValue(), chkAutoConnect.selectedProperty().getValue().toString());
    }

    private void terminalFuncions() {
        vboxTerminalFunc.getChildren().clear();

        vboxTerminalFunc.setPadding(new Insets(10, 10, 10, 10));
        vboxTerminalFunc.setAlignment(Pos.TOP_CENTER);
        vboxTerminalFunc.setPrefWidth(400);
        vboxTerminalFunc.spacingProperty().setValue(10);

        vboxTerminalFunc.getChildren().addAll(Core.getInstance().getTerminalHandler().getActiveTerminal().getTerminalFuncions());
    }

    @FXML
    private void onPdf(ActionEvent event) throws IOException, URISyntaxException {

        Desktop.getDesktop().browse(new URI("http://www.cimco.com/docs/cimco_edit/v6/se/#PortSetup"));
    }

}
