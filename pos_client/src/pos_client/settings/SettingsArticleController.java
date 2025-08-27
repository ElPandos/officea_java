package pos_client.settings;

import pos_client.db.dao.ArticleDAO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pos_client.ArticleModel;
import pos_client.common.General;
import pos_client.Ingredience;
import pos_client.common.Core;
import pos_client.db.dao.IngredienceDAO;
import pos_client.db.dao.VatDAO;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author ola
 */
public class SettingsArticleController implements Initializable
{

    @FXML
    private TableView<ArticleModel> tblArticles;
    @FXML
    private TableColumn<ArticleModel, Integer> colArticles_id;
    @FXML
    private TableColumn<ArticleModel, String> colArticles_name;
    @FXML
    private TableColumn<ArticleModel, Integer> colArticles_layoutindex;
    @FXML
    private TableColumn<ArticleModel, String> colArticles_purchaseheader;
    @FXML
    private TableColumn<ArticleModel, String> colArticles_purchasein;
    @FXML
    private TableColumn<ArticleModel, String> colArticles_purchaseexclusivetax;
    @FXML
    private TableColumn<ArticleModel, String> colArticles_purchaseinclusivetax;
    @FXML
    private TableColumn<ArticleModel, Integer> colArticles_purchasetaxgroup;
    @FXML
    private TableColumn<ArticleModel, String> colArticles_barcode;
    @FXML
    private TableColumn<ArticleModel, Integer> colArticles_type;
    @FXML
    private TableColumn<ArticleModel, Integer> colArticles_category;
    @FXML
    private TableColumn<ArticleModel, String> colArticles_description;
    @FXML
    private TableColumn<ArticleModel, String> colArticles_imageurl;
    @FXML
    private TextField txtArticles_name;
    @FXML
    private TextField txtArticles_priceinclusivetax;
    @FXML
    private TextField txtArticles_priceexclusivetax;
    @FXML
    private TextField txtArticles_purchase;
    @FXML
    private TextField txtArticles_layoutindex;
    @FXML
    private TextField txtArticles_barcode;
    @FXML
    private TextField txtArticles_description;
    @FXML
    private CheckBox chkArticles_bongwrite;
    @FXML
    private ComboBox<String> cbxArticleTab;
    @FXML
    private Text txtLayoutIndex;
    @FXML
    private Button btnArticles_update;
    @FXML
    private Button btnArticles_add;
    @FXML
    private Button btnArticles_delete;
    @FXML
    private Button btnArticles_addpicture;
    @FXML
    private ComboBox<String> cbxArticleType;
    @FXML
    private ComboBox<String> cbxArticleTax;
    @FXML
    private FlowPane flowIngredience;
    @FXML
    private ImageView picArticles_show;

    @FXML
    private ComboBox<?> cbxBongWriter;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initTable();
        populate();
        status();
    }

    private Stage primaryStage = null;

    ObservableList<ArticleModel> articles = FXCollections.observableArrayList();

    private void initTable()
    {
        colArticles_id.setCellValueFactory(new PropertyValueFactory<ArticleModel, Integer>("id"));
        colArticles_name.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("name"));
        colArticles_layoutindex.setCellValueFactory(new PropertyValueFactory<ArticleModel, Integer>("layoutIndex"));
        colArticles_purchasein.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("PurchaseStr"));
        colArticles_purchaseexclusivetax.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("priceExcmStr"));
        colArticles_purchaseinclusivetax.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("priceIncmStr"));
        colArticles_purchasetaxgroup.setCellValueFactory(new PropertyValueFactory<ArticleModel, Integer>("VatType"));
        colArticles_barcode.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("barcode"));
        colArticles_type.setCellValueFactory(new PropertyValueFactory<ArticleModel, Integer>("articleTypeId"));
        colArticles_category.setCellValueFactory(new PropertyValueFactory<ArticleModel, Integer>("articleCategoryId"));
        colArticles_description.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("description"));
        colArticles_imageurl.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("imageUrl"));

        addListenerTable();
    }

    public void refresh()
    {
        clear();
        status();
        populate();
        update();
    }

    private void clear()
    {
        articles.clear();
        txtArticles_layoutindex.clear();
        txtArticles_name.clear();
        txtArticles_purchase.clear();
        txtArticles_priceinclusivetax.clear();
        txtArticles_priceexclusivetax.clear();
        txtArticles_barcode.clear();
        txtArticles_description.clear();

        chkArticles_bongwrite.setIndeterminate(false);
        chkArticles_bongwrite.setSelected(false);

        flowIngredience.getChildren().clear();
    }

    private void populate()
    {
        loadArticles();

        loadArticleMomsCbx();
        loadArticleTabCbx();
        loadArticleTypeCbx();
        loadBongWriterCbx();
    }

    private void status()
    {
        cbxBongWriter.setDisable(true); // TODO
        chkArticles_bongwrite.setDisable(true); // TODO

        btnArticles_delete.disableProperty().set(true);
        btnArticles_update.disableProperty().set(true);
    }

    private void update()
    {
        General.getInstance().updateTable(tblArticles);
    }

    private int getKey(Map<Integer, String> map, String value)
    {
        for (Map.Entry<Integer, String> entry : map.entrySet())
        {
            if (entry.getValue().equalsIgnoreCase(value))
            {
                return entry.getKey();
            }
        }

        return -1;
    }

    private void calculatePriceexclusivetax(Object momsValue, Object priceExclusiveTax)
    {
        if (General.getInstance().isFloat(momsValue) && General.getInstance().isFloat(priceExclusiveTax))
        {
            float priceexclusivetaxValue = Float.parseFloat(priceExclusiveTax.toString()) / (Float.parseFloat(momsValue.toString()) + 1);
            txtArticles_priceexclusivetax.textProperty().set(General.decimalFormat.format(priceexclusivetaxValue));
        }
    }

    private void loadArticles()
    {
        Core.getInstance().getLog().log("Laddar artiklar i inställningar", Log.LogLevel.DESCRIPTIVE);

        ArticleDAO articleDAO = new ArticleDAO();
        tblArticles.setItems(articleDAO.getArticles());
    }

    private void loadArticleMomsCbx()
    {
        cbxArticleTax.getItems().clear();

        Core.getInstance().getLog().log("Laddar moms grupper till comboboxen", Log.LogLevel.DESCRIPTIVE);

        ArticleDAO articleDAO = new ArticleDAO();
        Map<Integer, String> momsTypes = articleDAO.getArticleMoms();

        for (Map.Entry<Integer, String> entry : momsTypes.entrySet())
        {
            cbxArticleTax.getItems().add(entry.getValue());
        }

        cbxArticleTax.setUserData(momsTypes);
    }

    private void loadArticleTypeCbx()
    {
        cbxArticleType.getItems().clear();

        Core.getInstance().getLog().log("Laddar artikel typer till comboboxen", Log.LogLevel.DESCRIPTIVE);

        ArticleDAO articleDAO = new ArticleDAO();
        Map<Integer, String> articleTypes = articleDAO.getArticleType();

        for (Map.Entry<Integer, String> entry : articleTypes.entrySet())
        {
            cbxArticleType.getItems().add(entry.getValue());
        }

        cbxArticleType.setUserData(articleTypes);
    }

    private void loadBongWriterCbx()
    {
        cbxBongWriter.getItems().clear();

        Core.getInstance().getLog().log("Laddar bong skrivare till comboboxen TODO", Log.LogLevel.DESCRIPTIVE);

        /*
        ArticleDAO articleDAO = new ArticleDAO();
        Map<Integer, String> articleTab = articleDAO.getArticleCategories();

        for (Map.Entry<Integer, String> entry : articleTab.entrySet()) {
            cbxArticleTab.getItems().add(entry.getValue());
        }

        cbxArticleTab.setUserData(articleTab);
         */
    }

    private void loadArticleTabCbx()
    {
        cbxArticleTab.getItems().clear();

        Core.getInstance().getLog().log("Laddar typer till comboboxen", Log.LogLevel.DESCRIPTIVE);

        ArticleDAO articleDAO = new ArticleDAO();
        Map<Integer, String> articleTab = articleDAO.getArticleCategories();

        for (Map.Entry<Integer, String> entry : articleTab.entrySet())
        {
            cbxArticleTab.getItems().add(entry.getValue());
        }

        cbxArticleTab.setUserData(articleTab);
    }

    ObservableList<GridPane> ingrediencesGrid = FXCollections.observableArrayList();

    private void loadArticleIngredience(ArticleModel article)
    {
        ingrediencesGrid.clear();
        flowIngredience.getChildren().clear(); // empty flowpane

        flowIngredience.setPadding(new Insets(5, 0, 5, 0));
        flowIngredience.setVgap(4);
        flowIngredience.setHgap(4);

        IngredienceDAO ingredience = new IngredienceDAO();
        ObservableList<Ingredience> ingrediences = ingredience.getIngrediences();

        for (Ingredience ing : ingrediences)
        {
            GridPane grid = new GridPane();
            grid.setPrefWidth(200);

            CheckBox box = new CheckBox();

            if (article.containsIngredience(ing))
            {
                box.setSelected(true);
            }

            Label name = new Label();
            name.setText(ing.getName());

            grid.add(box, 0, 0);
            grid.add(name, 1, 0);

            ingrediencesGrid.add(grid);
        }

        flowIngredience.getChildren().addAll(ingrediencesGrid);
    }

    private ArrayList<Ingredience> getArticleIngredienceFromGrid()
    {
        ArrayList<Ingredience> ingrediencesList = new ArrayList<>();

        IngredienceDAO ingredience = new IngredienceDAO();
        ObservableList<Ingredience> ingrediences = ingredience.getIngrediences();

        for (GridPane IngGrid : ingrediencesGrid)
        {
            if (((CheckBox) IngGrid.getChildren().get(0)).isSelected())
            {
                for (Ingredience ing : ingrediences)
                {
                    if (((Labeled) IngGrid.getChildren().get(1)).getText().equals(ing.getName()))
                    {
                        ingrediencesList.add(ing);
                    }
                }
            }
        }

        return ingrediencesList;
    }

    public void setPrimaryStage(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }

    private ArticleModel getArticleValuesFromInputFields()
    {
        ArticleModel article = new ArticleModel();
        article.setLayoutIndex(Integer.parseInt(txtArticles_layoutindex.getText()));
        article.setName(txtArticles_name.getText());
        article.setPurchase(Float.parseFloat(txtArticles_purchase.getText().replace(",", ".")));;
        article.setPriceExcm(Float.parseFloat(txtArticles_priceexclusivetax.getText().replace(",", ".")));
        article.setPriceIncm(Float.parseFloat(txtArticles_priceinclusivetax.getText().replace(",", ".")));
        article.setBarcode(txtArticles_barcode.getText());
        article.setDescription(txtArticles_description.getText());
        article.setIngrediences(getArticleIngredienceFromGrid());

        Map articleMomsMap = (Map) cbxArticleTax.getUserData();
        article.setVatType(VatDAO.VatType.values()[getKey(articleMomsMap, cbxArticleTax.getValue().toString())]);

        Map articleTabMap = (Map) cbxArticleTab.getUserData();
        int typeId = getKey(articleTabMap, cbxArticleTab.getValue());
        article.setArticleCategoryId(typeId);

        Map articleTypeMap = (Map) cbxArticleType.getUserData();
        int articleTypeId = getKey(articleTypeMap, cbxArticleType.getValue());
        article.setArticleTypeId(articleTypeId);

        String imagePath = picArticles_show.getUserData().toString();
        article.setImageUrl(imagePath);

        return article;
    }

    private void addListenerTable()
    {
        tblArticles.getSelectionModel().selectedItemProperty().addListener(new ArticleRowSelectChangeListener());

        // Add listeners                
        txtArticles_priceinclusivetax.textProperty().addListener((o, oldVal, newVal)
                -> 
                {
                    calculatePriceexclusivetax(cbxArticleTax.getValue(), txtArticles_priceinclusivetax.getText());
        });

        cbxArticleTax.setOnAction((event)
                -> 
                {
                    calculatePriceexclusivetax(cbxArticleTax.getSelectionModel().getSelectedItem(), txtArticles_priceinclusivetax.getText());
        });
    }

    private class ArticleRowSelectChangeListener implements ChangeListener
    {

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue)
        {
            btnArticles_delete.disableProperty().set(false);
            btnArticles_update.disableProperty().set(false);

            ArticleModel selectedArticle = tblArticles.getSelectionModel().getSelectedItem();
            if (selectedArticle != null)
            {
                txtArticles_layoutindex.setText(Integer.toString(selectedArticle.getLayoutIndex()));
                txtArticles_name.setText(selectedArticle.getName());
                txtArticles_purchase.setText(General.getInstance().float2Str(selectedArticle.getPurchase(), 2));
                txtArticles_priceinclusivetax.setText(General.getInstance().float2Str(selectedArticle.getSale().getPriceIncm(false), 2));
                txtArticles_priceexclusivetax.setText(General.getInstance().float2Str(selectedArticle.getSale().getPriceExcm(false), 2));
                txtArticles_barcode.setText(selectedArticle.getBarcode());
                txtArticles_description.setText(selectedArticle.getDescription());

                // cbxArticleTab
                Integer articleTabId = selectedArticle.getArticleCategoryId();
                Map articleTabMap = (Map) cbxArticleTab.getUserData();
                cbxArticleTab.setValue(articleTabMap.get(articleTabId).toString());

                //cbxArticleType
                Integer articleTypeId = selectedArticle.getArticleTypeId();
                Map articleTypeMap = (Map) cbxArticleType.getUserData();
                cbxArticleType.setValue(articleTypeMap.get(articleTypeId).toString());

                //cbxArticleType
                Map articleTaxMap = (Map) cbxArticleTax.getUserData();
                cbxArticleTax.setValue(articleTaxMap.get(selectedArticle.getVatType().ordinal()).toString());

                Image articleImage = General.getInstance().loadPicture(selectedArticle.getImageUrl());
                picArticles_show.setUserData(selectedArticle.getImageUrl());
                picArticles_show.setImage(articleImage);

                loadArticleIngredience(selectedArticle);
            }
        }
    }

    @FXML
    private void OnBtnArticles_add(ActionEvent event)
    {
        Core.getInstance().getLog().log("Lägger till artikel i databasen", Log.LogLevel.NORMAL);

        ArticleModel article = getArticleValuesFromInputFields();

        if (article.hasIngrediences())
        {
            ArticleDAO articleDAO = new ArticleDAO();
            articleDAO.add(article);

            refresh();
        } else
        {
            Core.getInstance().getLog().log("Måste selektera ingredieser för att lägga till artikel", Log.LogLevel.CRITICAL);
        }

    }

    @FXML
    private void OnBtnArticles_delete(ActionEvent event) throws SQLException
    {
        ArticleDAO articleDAO = new ArticleDAO();
        if (articleDAO.remove(tblArticles.getSelectionModel().getSelectedItem().getArticleId()))
        {
            Core.getInstance().getLog().log("Tog bort vald artikel", Log.LogLevel.NORMAL);
        } else
        {
            Core.getInstance().getLog().log("Kunde inte ta bort artikel", Log.LogLevel.CRITICAL);
        }

        refresh();
    }

    @FXML
    private void OnBtnArticles_update(ActionEvent event)
    {
        Core.getInstance().getLog().log("Updaterar artikel i databasen", Log.LogLevel.NORMAL);

        ArticleModel updatedArticle = getArticleValuesFromInputFields();
        updatedArticle.setId(tblArticles.getSelectionModel().getSelectedItem().getArticleId());

        ArticleDAO articleDAO = new ArticleDAO();
        articleDAO.update(updatedArticle);

        refresh();
    }

    @FXML
    private void OnBtnArticles_addpicture(ActionEvent event) throws IOException
    {
        FileChooser pictureFile = new FileChooser();
        pictureFile.setTitle("Öppna bild...");
        pictureFile.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"));

        File path = pictureFile.showOpenDialog(primaryStage);

        if (path != null)
        {
            String picturePath = "file:" + path.getCanonicalPath();
            picArticles_show.setUserData(picturePath);
            Image articleImage = General.getInstance().loadPicture(picturePath);
            picArticles_show.setImage(articleImage);

        } else
        {
            Core.getInstance().getLog().log("Ingen bildfil är vald!", Log.LogLevel.CRITICAL);
        }
    }
}
