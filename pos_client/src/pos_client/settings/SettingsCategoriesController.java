/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pos_client.Category;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.db.dao.ArticleCategoryDAO;
import pos_client.db.dao.IngredientsCategoryDAO;
import pos_client.db.dao.SpecialCategoryDAO;
import pos_client.windows.Log;

public class SettingsCategoriesController implements Initializable
{

    @FXML
    private TableView<Category> tblArticleCategories;
    @FXML
    private TableView<Category> tblIngredienceCategories;
    @FXML
    private TableView<Category> tblSpecialCategories;
    @FXML
    private TextField txtArticleCategoryName;
    @FXML
    private Button btnAddArticleCategory;
    @FXML
    private Button btnUpdateArticleCategory;
    @FXML
    private TextField txtIngredienceCategoryName;
    @FXML
    private Button btnAddIngredienceCategory;
    @FXML
    private Button btnUpdateIngredienceCategory;
    @FXML
    private TextField txtSpecialCategoryName;
    @FXML
    private Button btnAddSpecialCategory;
    @FXML
    private Button btnUpdateSpecialCategory;
    @FXML
    private TableColumn<Category, String> colArticleCategoryName;
    @FXML
    private TableColumn<Category, String> colIngredienceCategoryName;
    @FXML
    private TableColumn<Category, String> colSpecialCategoryName;
    @FXML
    private TableColumn<Category, Integer> colArticleCategoryOrderNr;
    @FXML
    private TableColumn<Category, Integer> colIngredienceCategoryOrderNr;
    @FXML
    private TableColumn<Category, Integer> colSpecialCategoryOrderNr;
    @FXML
    private TextField txtArticleCategoryOrderNr;
    @FXML
    private TextField txtIngredienceCategoryOrderNr;
    @FXML
    private TextField txtSpecialCategoryOrderNr;
    @FXML
    private Button btnDeleteArticleCategory;
    @FXML
    private Button btnDeleteIngredienceCategory;
    @FXML
    private Button btnDeleteSpecialCategory;

    enum TypeCatergory
    {
        ALL,
        ARTICLE,
        INGREDIENCE,
        SPECIAL
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initTables();
        populate(TypeCatergory.ALL);
        status(TypeCatergory.ALL);
    }

    private void initTables()
    {
        colArticleCategoryName.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
        colIngredienceCategoryName.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
        colSpecialCategoryName.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));

        colArticleCategoryOrderNr.setCellValueFactory(new PropertyValueFactory<Category, Integer>("order"));
        colIngredienceCategoryOrderNr.setCellValueFactory(new PropertyValueFactory<Category, Integer>("order"));
        colSpecialCategoryOrderNr.setCellValueFactory(new PropertyValueFactory<Category, Integer>("order"));

        addListenerArticleCategoriesTable();
        addListenerIngredienceCategoriesTable();
        addListenerSpecialCategoriesTable();
    }

    public void refresh(TypeCatergory type)
    {
        clear(type);
        status(type);
        populate(type);

        if (type == TypeCatergory.ARTICLE || type == TypeCatergory.ALL)
        {
            General.getInstance().updateColumn(tblArticleCategories);
        }

        if (type == TypeCatergory.INGREDIENCE || type == TypeCatergory.ALL)
        {
            General.getInstance().updateColumn(tblIngredienceCategories);
        }

        if (type == TypeCatergory.SPECIAL || type == TypeCatergory.ALL)
        {
            General.getInstance().updateColumn(tblSpecialCategories);
        }
    }

    private void clear(TypeCatergory type)
    {
        if (type == TypeCatergory.ARTICLE || type == TypeCatergory.ALL)
        {
            txtArticleCategoryName.clear();
            txtArticleCategoryOrderNr.clear();
        }

        if (type == TypeCatergory.INGREDIENCE || type == TypeCatergory.ALL)
        {
            txtIngredienceCategoryName.clear();
            txtIngredienceCategoryOrderNr.clear();
        }

        if (type == TypeCatergory.SPECIAL || type == TypeCatergory.ALL)
        {
            txtSpecialCategoryName.clear();
            txtSpecialCategoryOrderNr.clear();
        }
    }

    private void populate(TypeCatergory type)
    {
        if (type == TypeCatergory.ARTICLE || type == TypeCatergory.ALL)
        {
            loadArticleCategories();
        }

        if (type == TypeCatergory.INGREDIENCE || type == TypeCatergory.ALL)
        {
            loadIngredienceCategories();
        }

        if (type == TypeCatergory.SPECIAL || type == TypeCatergory.ALL)
        {
            loadSpecialCategories();
        }
    }

    private void status(TypeCatergory type)
    {
        if (type == TypeCatergory.ARTICLE || type == TypeCatergory.ALL)
        {
            btnUpdateArticleCategory.setDisable(true);
            btnDeleteArticleCategory.setDisable(true);
        }

        if (type == TypeCatergory.INGREDIENCE || type == TypeCatergory.ALL)
        {
            btnUpdateIngredienceCategory.setDisable(true);
            btnDeleteIngredienceCategory.setDisable(true);
        }

        if (type == TypeCatergory.SPECIAL || type == TypeCatergory.ALL)
        {
            btnUpdateSpecialCategory.setDisable(true);
            btnDeleteSpecialCategory.setDisable(true);
        }
    }

    private void addListenerArticleCategoriesTable()
    {
        tblArticleCategories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                btnAddArticleCategory.setDisable(false);
                btnUpdateArticleCategory.setDisable(false);
                btnDeleteArticleCategory.setDisable(false);

                Category selectedCategory = tblArticleCategories.getSelectionModel().getSelectedItem();
                if (selectedCategory != null)
                {
                    txtArticleCategoryName.setText(selectedCategory.getName());
                    txtArticleCategoryOrderNr.setText(Integer.toString(selectedCategory.getOrder()));
                }
            }
        });
    }

    private void addListenerIngredienceCategoriesTable()
    {
        tblIngredienceCategories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                btnAddIngredienceCategory.setDisable(false);
                btnUpdateIngredienceCategory.setDisable(false);
                btnDeleteIngredienceCategory.setDisable(false);

                Category selectedCategory = tblIngredienceCategories.getSelectionModel().getSelectedItem();
                if (selectedCategory != null)
                {
                    txtIngredienceCategoryName.setText(selectedCategory.getName());
                    txtIngredienceCategoryOrderNr.setText(Integer.toString(selectedCategory.getOrder()));
                }
            }
        });
    }

    private void addListenerSpecialCategoriesTable()
    {
        tblSpecialCategories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {

                btnAddSpecialCategory.setDisable(false);
                btnUpdateSpecialCategory.setDisable(false);
                btnDeleteSpecialCategory.setDisable(false);

                Category selectedCategory = tblSpecialCategories.getSelectionModel().getSelectedItem();
                if (selectedCategory != null)
                {
                    txtSpecialCategoryName.setText(selectedCategory.getName());
                    txtSpecialCategoryOrderNr.setText(Integer.toString(selectedCategory.getOrder()));
                }
            }
        });
    }

    private void loadArticleCategories()
    {
        Core.getInstance().getLog().log("Laddar artikelkategorier i inställningar", Log.LogLevel.DESCRIPTIVE);

        ArticleCategoryDAO articlecategoryDAO = new ArticleCategoryDAO();
        tblArticleCategories.setItems(articlecategoryDAO.getCategories());

        General.getInstance().updateTable(tblArticleCategories);
    }

    private void loadIngredienceCategories()
    {
        Core.getInstance().getLog().log("Laddar ingredienskategorier i inställningar", Log.LogLevel.DESCRIPTIVE);

        IngredientsCategoryDAO ingredientsCategoryDAO = new IngredientsCategoryDAO();
        tblIngredienceCategories.setItems(ingredientsCategoryDAO.getCategories());

        General.getInstance().updateTable(tblIngredienceCategories);
    }

    private void loadSpecialCategories()
    {
        Core.getInstance().getLog().log("Laddar specialkategorier i inställningar", Log.LogLevel.DESCRIPTIVE);

        SpecialCategoryDAO specialCategoryDAO = new SpecialCategoryDAO();
        tblSpecialCategories.setItems(specialCategoryDAO.getCategories());

        General.getInstance().updateTable(tblSpecialCategories);
    }

    @FXML
    private void onBtnArticleCategory_add(ActionEvent event)
    {

        Core.getInstance().getLog().log("Lägger till artikelkategori i databasen", Log.LogLevel.NORMAL);

        ArticleCategoryDAO articleCategoryDAO = new ArticleCategoryDAO();
        articleCategoryDAO.add(txtArticleCategoryName.getText(), txtArticleCategoryOrderNr.getText());

        refresh(TypeCatergory.ARTICLE);
    }

    @FXML
    private void onBtnArticleCategory_update(ActionEvent event)
    {
        Core.getInstance().getLog().log("Uppdaterar vald artikelkategori i databasen", Log.LogLevel.NORMAL);

        Category updatedCategory = getArticleCategoryValuesFromInputFields();
        updatedCategory.setId(tblArticleCategories.getSelectionModel().getSelectedItem().getId());

        ArticleCategoryDAO articleCategoryDAO = new ArticleCategoryDAO();
        articleCategoryDAO.update(updatedCategory);

        refresh(TypeCatergory.ARTICLE);
    }

    @FXML
    private void onBtnArticleCategory_delete(ActionEvent event)
    {
        ArticleCategoryDAO articleCategoryDAO = new ArticleCategoryDAO();

        if (articleCategoryDAO.remove(tblArticleCategories.getSelectionModel().getSelectedItem().getId()))
        {
            Core.getInstance().getLog().log("Tog bort vald artikelkategori", Log.LogLevel.NORMAL);
        } else
        {
            Core.getInstance().getLog().log("Kunde inte ta bort artikelkategori", Log.LogLevel.CRITICAL);
        }

        refresh(TypeCatergory.ARTICLE);
    }

    @FXML
    private void onBtnIngredientsCategory_add(ActionEvent event)
    {
        Core.getInstance().getLog().log("Lägger till ingredienskategori i databasen", Log.LogLevel.NORMAL);

        IngredientsCategoryDAO ingredientsCategoryDAO = new IngredientsCategoryDAO();
        ingredientsCategoryDAO.add(txtIngredienceCategoryName.getText(), txtIngredienceCategoryOrderNr.getText());

        refresh(TypeCatergory.INGREDIENCE);
    }

    @FXML
    private void onBtnIngredientsCategory_update(ActionEvent event)
    {
        Core.getInstance().getLog().log("Uppdaterar vald ingredienskategori i databasen", Log.LogLevel.NORMAL);

        Category updatedCategory = getIngredientCategoryValuesFromInputFields();
        updatedCategory.setId(tblIngredienceCategories.getSelectionModel().getSelectedItem().getId());

        IngredientsCategoryDAO ingredientsCategoryDAO = new IngredientsCategoryDAO();
        ingredientsCategoryDAO.update(updatedCategory);

        refresh(TypeCatergory.INGREDIENCE);
    }

    @FXML
    private void onBtnIngredientsCategory_delete(ActionEvent event)
    {
        IngredientsCategoryDAO ingredientsCategoryDAO = new IngredientsCategoryDAO();

        if (ingredientsCategoryDAO.remove(tblIngredienceCategories.getSelectionModel().getSelectedItem().getId()))
        {
            Core.getInstance().getLog().log("Tog bort vald ingredienskategori", Log.LogLevel.NORMAL);
        } else
        {
            Core.getInstance().getLog().log("Kunde inte ta bort ingredienskategori", Log.LogLevel.CRITICAL);
        }

        refresh(TypeCatergory.INGREDIENCE);
    }

    @FXML
    private void onBtnSpecialCategory_add(ActionEvent event)
    {
        Core.getInstance().getLog().log("Lägger till specialkategori i databasen", Log.LogLevel.NORMAL);

        SpecialCategoryDAO specialCategoryDAO = new SpecialCategoryDAO();
        specialCategoryDAO.add(txtSpecialCategoryName.getText(), txtSpecialCategoryOrderNr.getText());

        refresh(TypeCatergory.SPECIAL);
    }

    @FXML
    private void onBtnSpecialCategory_update(ActionEvent event)
    {
        Core.getInstance().getLog().log("Uppdaterar vald specialkategori i databasen", Log.LogLevel.NORMAL);

        Category updatedCategory = getSpecialCategoryValuesFromInputFields();
        updatedCategory.setId(tblSpecialCategories.getSelectionModel().getSelectedItem().getId());

        SpecialCategoryDAO specialCategoryDAO = new SpecialCategoryDAO();
        specialCategoryDAO.update(updatedCategory);

        refresh(TypeCatergory.SPECIAL);
    }

    @FXML
    private void onBtnSpecialCategory_delete(ActionEvent event)
    {
        SpecialCategoryDAO specialCategoryDAO = new SpecialCategoryDAO();

        if (specialCategoryDAO.remove(tblSpecialCategories.getSelectionModel().getSelectedItem().getId()))
        {
            Core.getInstance().getLog().log("Tog bort vald specialkategori", Log.LogLevel.NORMAL);
        } else
        {
            Core.getInstance().getLog().log("Kunde inte ta bort specialkategori", Log.LogLevel.CRITICAL);
        }

        refresh(TypeCatergory.SPECIAL);
    }

    private Category getArticleCategoryValuesFromInputFields()
    {

        Category category = new Category();

        category.setName(txtArticleCategoryName.getText());
        category.setOrder(General.getInstance().str2Int(txtArticleCategoryOrderNr.getText()));

        return category;
    }

    private Category getIngredientCategoryValuesFromInputFields()
    {

        Category category = new Category();

        category.setName(txtIngredienceCategoryName.getText());
        category.setOrder(General.getInstance().str2Int(txtIngredienceCategoryOrderNr.getText()));

        return category;
    }

    private Category getSpecialCategoryValuesFromInputFields()
    {

        Category category = new Category();

        category.setName(txtSpecialCategoryName.getText());
        category.setOrder(General.getInstance().str2Int(txtSpecialCategoryOrderNr.getText()));

        return category;
    }
}
