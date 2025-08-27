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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pos_client.Category;
import pos_client.common.General;
import pos_client.Ingredience;
import pos_client.common.Core;
import pos_client.db.dao.IngredienceDAO;
import pos_client.db.dao.IngredientsCategoryDAO;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsIngredienceController implements Initializable
{

    @FXML
    private TableView<Ingredience> tblIngrediences;
    @FXML
    private TableColumn<Ingredience, Integer> colIngrediences_id;
    @FXML
    private TableColumn<Ingredience, String> colIngrediences_name;
    @FXML
    private TableColumn<Ingredience, Float> colIngrediences_price;
    @FXML
    private TableColumn<Ingredience, Integer> colIngrediences_category;
    @FXML
    private TableColumn<Ingredience, String> colIngrediences_categoryname;
    @FXML
    private TextField txtIngrediences_name;
    @FXML
    private TextField txtIngrediences_price;
    @FXML
    private Button btnIngrediences_add;
    @FXML
    private Button btnIngrediences_update;
    @FXML
    private Button btnIngrediences_delete;
    @FXML
    private ComboBox<String> cbxCategory;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        initTable();
        populate();
        status();
    }

    private void initTable()
    {

        colIngrediences_id.setCellValueFactory(new PropertyValueFactory<Ingredience, Integer>("id"));
        colIngrediences_name.setCellValueFactory(new PropertyValueFactory<Ingredience, String>("name"));
        colIngrediences_price.setCellValueFactory(new PropertyValueFactory<Ingredience, Float>("price"));
        colIngrediences_category.setCellValueFactory(new PropertyValueFactory<Ingredience, Integer>("category"));
        colIngrediences_categoryname.setCellValueFactory(new PropertyValueFactory<Ingredience, String>("category_name"));

        addListenerTable();
    }

    public void refresh()
    {

        clear();
        status();
        populate();

        General.getInstance().updateColumn(tblIngrediences);
    }

    private void clear()
    {

        tblIngrediences.getItems().clear();

        txtIngrediences_name.clear();
        txtIngrediences_price.clear();
    }

    private void populate()
    {

        loadIngrediences();

        loadCategoryCbx();
    }

    private void status()
    {

        btnIngrediences_delete.setDisable(true);
        btnIngrediences_update.setDisable(true);
    }

    private void addListenerTable()
    {

        tblIngrediences.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {

                btnIngrediences_delete.setDisable(false);
                btnIngrediences_update.setDisable(false);

                Ingredience selectedIngredience = tblIngrediences.getSelectionModel().getSelectedItem();
                if (selectedIngredience != null)
                {
                    txtIngrediences_name.setText(selectedIngredience.getName());
                    txtIngrediences_price.setText(selectedIngredience.getPriceStr());

                    IngredientsCategoryDAO ingredientsCategoryDAO = new IngredientsCategoryDAO();
                    cbxCategory.setValue(ingredientsCategoryDAO.getIngredientsCategories().get(selectedIngredience.getCategory()).getName());
                }
            }
        });
    }

    @FXML
    private void OnBtnIngrediences_update(ActionEvent event)
    {

        Core.getInstance().getLog().log("Uppdaterar vald ingredience i databasen", Log.LogLevel.NORMAL);

        Ingredience updateIngredience = getIngredientValuesFromInputFields();
        updateIngredience.setId(tblIngrediences.getSelectionModel().getSelectedItem().getId());

        IngredienceDAO ingredienceDAO = new IngredienceDAO();
        ingredienceDAO.update(updateIngredience);

        refresh();
    }

    @FXML
    private void OnBtnIngrediences_add(ActionEvent event)
    {

        Core.getInstance().getLog().log("Lägger till ingredienser i Databasen", Log.LogLevel.DESCRIPTIVE);

        Map<String, Category> categories = (Map) cbxCategory.getUserData();

        IngredienceDAO ingredienceDAO = new IngredienceDAO();
        ingredienceDAO.add(txtIngrediences_name.getText(), txtIngrediences_price.getText(), categories.get(cbxCategory.getValue()).getId());

        refresh();
    }

    @FXML
    private void OnBtnIngrediences_delete(ActionEvent event) throws SQLException
    {

        IngredienceDAO ingredienceDAO = new IngredienceDAO();
        if (ingredienceDAO.remove(tblIngrediences.getSelectionModel().getSelectedItem().getId()))
        {
            Core.getInstance().getLog().log("Tog bort vald ingredients", Log.LogLevel.NORMAL);
        } else
        {
            Core.getInstance().getLog().log("Kunde inte ta bort ingrediensen", Log.LogLevel.CRITICAL);
        }

        refresh();
    }

    private void loadIngrediences()
    {

        Core.getInstance().getLog().log("Laddar ingredienser i inställningar", Log.LogLevel.DESCRIPTIVE);

        IngredienceDAO ingredienceDAO = new IngredienceDAO();
        tblIngrediences.setItems(ingredienceDAO.getIngrediences());

        General.getInstance().updateTable(tblIngrediences);
    }

    private Ingredience getIngredientValuesFromInputFields()
    {

        Ingredience ingredience = new Ingredience();

        ingredience.setName(txtIngrediences_name.getText());
        ingredience.setPrice(General.getInstance().str2Float(txtIngrediences_price.getText(), 2));
        ingredience.setCategory(General.getInstance().str2Int(cbxCategory.getValue()));

        return ingredience;
    }

    private void loadCategoryCbx()
    {
        cbxCategory.getItems().clear();

        Core.getInstance().getLog().log("Laddar kategorier till comboboxen", Log.LogLevel.DESCRIPTIVE);

        IngredientsCategoryDAO ingredientsCategoryDAO = new IngredientsCategoryDAO();
        Map<Integer, Category> categories = ingredientsCategoryDAO.getIngredientsCategories();

        Map<String, Category> categoryData = new HashMap<>();
        for (Map.Entry<Integer, Category> entry : categories.entrySet())
        {
            cbxCategory.getItems().add(entry.getValue().getName());
            categoryData.put(entry.getValue().getName(), entry.getValue());
        }

        cbxCategory.setUserData(categoryData);
    }

}
