/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pos_client.Category;
import pos_client.SpecialModel;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.db.dao.SpecialCategoryDAO;
import pos_client.db.dao.SpecialDAO;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsSpecialController implements Initializable
{

    @FXML
    private TextField txtSpecial_name;
    @FXML
    private TextField txtSpecial_price;
    @FXML
    private Button btnSpecial_add;
    @FXML
    private Button btnSpecial_update;
    @FXML
    private Button btnSpecial_delete;
    @FXML
    private TableColumn<SpecialModel, Integer> colSpecial_id;
    @FXML
    private TableColumn<SpecialModel, String> colSpecial_name;
    @FXML
    private TableColumn<SpecialModel, Float> colSpecial_price;
    @FXML
    private TableColumn<SpecialModel, String> colSpecial_category;
    @FXML
    private TableView<SpecialModel> tblSpecial;
    @FXML
    private ChoiceBox<String> cbxSpecial_category;
    @FXML
    private TableColumn<SpecialModel, String> colSpecial_description;
    @FXML
    private TextField txtSpecial_description;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initTable();
        populate();
        status();
    }

    private void initTable()
    {
        colSpecial_id.setCellValueFactory(new PropertyValueFactory<SpecialModel, Integer>("id"));
        colSpecial_name.setCellValueFactory(new PropertyValueFactory<SpecialModel, String>("name"));
        colSpecial_price.setCellValueFactory(new PropertyValueFactory<SpecialModel, Float>("price"));
        colSpecial_category.setCellValueFactory(new PropertyValueFactory<SpecialModel, String>("categoryId"));
        colSpecial_description.setCellValueFactory(new PropertyValueFactory<SpecialModel, String>("description"));

        addListenerTable();
    }

    public void refresh()
    {
        clear();
        populate();
        status();

        General.getInstance().updateColumn(tblSpecial);
    }

    public void clear()
    {
        txtSpecial_description.clear();
        txtSpecial_name.clear();
        txtSpecial_price.clear();
    }

    private void populate()
    {
        loadSpecial();
        loadSpecialCategoryCbx();
    }

    private void status()
    {
        btnSpecial_delete.disableProperty().set(true);
        btnSpecial_update.disableProperty().set(true);
    }

    private void addListenerTable()
    {
        tblSpecial.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {

                btnSpecial_delete.disableProperty().set(false);
                btnSpecial_update.disableProperty().set(false);
                btnSpecial_add.disableProperty().set(false);

                SpecialModel selectedSpecial = tblSpecial.getSelectionModel().getSelectedItem();
                if (selectedSpecial != null)
                {
                    txtSpecial_name.setText(selectedSpecial.getName());
                    txtSpecial_price.setText(General.getInstance().float2Str(selectedSpecial.getPrice(), 2));
                    txtSpecial_description.setText(selectedSpecial.getDescription());

                    SpecialCategoryDAO specialCategoryDAO = new SpecialCategoryDAO();
                    cbxSpecial_category.setValue(specialCategoryDAO.getSpecialCategory(selectedSpecial.getCategoryId()).getName());
                }
            }
        });
    }

    private void loadSpecial()
    {
        Core.getInstance().getLog().log("Laddar Special i inställningar", Log.LogLevel.DESCRIPTIVE);

        SpecialDAO specialDAO = new SpecialDAO();
        tblSpecial.setItems(specialDAO.getSpecials());

        General.getInstance().updateTable(tblSpecial);
    }

    private void loadSpecialCategoryCbx()
    {
        cbxSpecial_category.getItems().clear();

        Core.getInstance().getLog().log("Laddar specialkategorier till comboboxen", Log.LogLevel.DESCRIPTIVE);

        SpecialCategoryDAO specialcategoryDAO = new SpecialCategoryDAO();
        Map<Integer, Category> categories = specialcategoryDAO.getSpecialCategories();

        Map<String, Category> categoriesData = new HashMap<>();
        for (Map.Entry<Integer, Category> entry : categories.entrySet())
        {
            cbxSpecial_category.getItems().add(entry.getValue().getName());
            categoriesData.put(entry.getValue().getName(), entry.getValue());
        }

        cbxSpecial_category.setUserData(categoriesData);
    }

    private SpecialModel getSpecialValuesFromInputFields()
    {
        Map<String, Category> specialCategory = (Map) cbxSpecial_category.getUserData();

        SpecialModel special = new SpecialModel();
        special.setName(txtSpecial_name.getText());
        special.setPrice(General.getInstance().str2Float(txtSpecial_price.getText(), 2));
        special.setDescription(txtSpecial_description.getText());
        special.setCategory(specialCategory.get(cbxSpecial_category.getValue()).getId());

        return special;
    }

    @FXML
    private void onBtnSpecial_add(ActionEvent event)
    {
        Core.getInstance().getLog().log("Lägger till Special i databasen", Log.LogLevel.DESCRIPTIVE);

        SpecialModel special = getSpecialValuesFromInputFields();

        SpecialDAO specialDAO = new SpecialDAO();
        specialDAO.add(special);

        refresh();

    }

    @FXML
    private void onBtnSpecial_update(ActionEvent event)
    {
        Core.getInstance().getLog().log("Uppdaterar vald Special i databasen", Log.LogLevel.NORMAL);

        SpecialModel updatedSpecial = getSpecialValuesFromInputFields();
        updatedSpecial.setId(tblSpecial.getSelectionModel().getSelectedItem().getId());

        SpecialDAO specialDAO = new SpecialDAO();
        specialDAO.update(updatedSpecial);

        refresh();
    }

    @FXML
    private void onBtnSpecial_delete(ActionEvent event) throws SQLException
    {
        SpecialDAO specialDAO = new SpecialDAO();
        if (specialDAO.remove(tblSpecial.getSelectionModel().getSelectedItem().getId()))
        {
            Core.getInstance().getLog().log("Tog bort vald special", Log.LogLevel.NORMAL);
        } else
        {
            Core.getInstance().getLog().log("Kunde inte ta bort special", Log.LogLevel.CRITICAL);
        }

        refresh();
    }

}
