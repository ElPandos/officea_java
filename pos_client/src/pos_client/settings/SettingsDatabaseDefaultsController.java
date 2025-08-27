/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pos_client.common.General;
import pos_client.db.dao.SettingsDAO;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsDatabaseDefaultsController implements Initializable
{

    @FXML
    private TableView<Setting> tblSettings;
    @FXML
    private TableColumn<Setting, Integer> colId;
    @FXML
    private TableColumn<Setting, String> colVariable;
    @FXML
    private TableColumn<Setting, String> colSetting;
    @FXML
    private TableColumn<Setting, String> colType;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        initTable();
        populate();
    }

    private void initTable()
    {

        colId.setCellValueFactory(new PropertyValueFactory<Setting, Integer>("id"));
        colVariable.setCellValueFactory(new PropertyValueFactory<Setting, String>("variable"));
        colSetting.setCellValueFactory(new PropertyValueFactory<Setting, String>("value"));
        colType.setCellValueFactory(new PropertyValueFactory<Setting, String>("unit"));
    }

    public void refresh()
    {

        populate();

        General.getInstance().updateColumn(tblSettings);
    }

    private void populate()
    {

        SettingsDAO settingsDAO = new SettingsDAO();
        tblSettings.setItems(settingsDAO.getSettings());

        General.getInstance().updateTable(tblSettings);
    }
}
