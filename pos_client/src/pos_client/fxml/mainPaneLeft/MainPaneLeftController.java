/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.mainPaneLeft;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pos_client.ActivateReceipt;
import pos_client.ArticleModel;
import pos_client.DeleteArticle;
import pos_client.common.Core;
import pos_client.Receipt;
import pos_client.ResourcesHandler;
import pos_client.SaleModel;
import pos_client.UserModel;
import pos_client.common.DialogHandler;
import pos_client.common.General;
import pos_client.common.Language;
import pos_client.communication.comHandler;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.ReceiptTypeDAO;
import pos_client.windows.Log;
import pos_client.fxml.mainCenter.MainCenterController.AnchType;
import pos_client.fxml.split.SplitController;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class MainPaneLeftController implements Initializable
{

    @FXML
    private FlowPane flwMainpay;
    @FXML
    private Button btnParkReceipt;
    @FXML
    private Button btnSplitReceipt;
    @FXML
    private Button btnDiscount;
    @FXML
    private Button btnCancelReceipt;
    @FXML
    private Button btnPayReceipt;
    @FXML
    private Label labelMOMS;
    @FXML
    private Label labelDISCOUNT;
    @FXML
    private Label labelTOTAL;
    @FXML
    private Label labelAmount;
    @FXML
    private AnchorPane splitleft_main;
    @FXML
    private TabPane tabReceipt;
    @FXML
    private Tab tabReceiptActive;
    @FXML
    private Tab tabReceiptParked;
    @FXML
    private AnchorPane anchReceiptTop;
    @FXML
    private TableView<ArticleModel> tblCurrentReceipt;
    @FXML
    private TableView<Receipt> tblCurrentReceiptParked;
    @FXML
    private TableColumn<ArticleModel, Boolean> colReceiptArticleDelete;
    @FXML
    private TableColumn<ArticleModel, String> colReceiptArticlePrice;
    @FXML
    private TableColumn<ArticleModel, String> colReceiptArticleName;
    @FXML
    private TableColumn<ArticleModel, Integer> colReceiptArticleId;
    @FXML
    private TableColumn<Receipt, Boolean> colReceiptParkedActivate;
    @FXML
    private TableColumn<Receipt, String> colReceiptParkedInfo;
    @FXML
    private TableColumn<Receipt, String> colReceiptParkedTime;
    @FXML
    private TableColumn<Receipt, Integer> colReceiptParkedId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initTable();
        populate();
        status();
        language();

        emptyReceipt = new ImageView(new Image(ResourcesHandler.getInstance().getRecieptImage()));

        tblCurrentReceipt.setPlaceholder(emptyReceipt);
        tblCurrentReceiptParked.setPlaceholder(emptyReceipt);

        tblCurrentReceiptParked.setId("Receipt_table");
        tblCurrentReceipt.setId("Receipt_table");
    }

    public enum TabType
    {
        ACTIVE,
        PARKED
    };

    // Images
    private ImageView emptyReceipt;

    private void initTable()
    {
        initTableActive();
        initTableParked();
    }

    private void initTableActive()
    {
        colReceiptArticleId.setCellValueFactory(new PropertyValueFactory<ArticleModel, Integer>("id"));
        colReceiptArticleName.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("NameAndCategory"));
        colReceiptArticlePrice.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("PriceTotalIncmStr"));

        addDeleteButton();
    }

    private void initTableParked()
    {
        colReceiptParkedId.setCellValueFactory(new PropertyValueFactory<Receipt, Integer>("idInfo"));
        colReceiptParkedTime.setCellValueFactory(new PropertyValueFactory<Receipt, String>("modifyInfo"));
        colReceiptParkedInfo.setCellValueFactory(new PropertyValueFactory<Receipt, String>("receiptInfo"));

        addActivateButton();
    }

    public void refresh()
    {
        if (tblCurrentReceipt != null && tblCurrentReceiptParked != null)
        {
            clear();
            populate();
            status(); // Have to populate before  checking status
            update();
        }
    }

    public void clear()
    {
        tblCurrentReceipt.getItems().clear();
        tblCurrentReceiptParked.getItems().clear();
    }

    private void populate()
    {
        loadActiveReceipt();
        loadParkedReceipts();
    }

    private void status()
    {
        setDisableParkButton(!hasArticles());
    }

    private void update()
    {
        Receipt receipt = Core.getInstance().getLoginHandler().getUser().getCurrentReceipt();

        labelMOMS.setText(receipt.getSumTotalVatStr());
        labelDISCOUNT.setText(receipt.getSumTotalDiscountStr());
        labelTOTAL.setText(receipt.getSumTotalIncmStr());
        labelAmount.setText("Artiklar: " + receipt.countSales());

        selectLastAddedArticle();

        Core.getInstance().getScreenHandler().getCustomer().setReceiptData(tblCurrentReceipt.getItems());
        Core.getInstance().getControlHandler().getMainInfoPaneController().refresh();

        General.getInstance().updateColumn(tblCurrentReceipt);
        General.getInstance().updateColumn(tblCurrentReceiptParked);

        language();
    }

    private void language()
    {
        btnCancelReceipt.setText(Language.getInstance().getBtnReceiptCancel());
        btnDiscount.setText(Language.getInstance().getBtnReceiptDiscount());
        btnParkReceipt.setText(Language.getInstance().getBtnReceiptPark());
        btnSplitReceipt.setText(Language.getInstance().getBtnReceiptSplit());
    }

    public Scene getScene()
    {
        return btnCancelReceipt.getScene();
    }

    public void refreshCSS()
    {
        Scene scene = getScene();
        Stage stage = (Stage) scene.getWindow();
        scene.getStylesheets().clear();

        File f = new File(ResourcesHandler.getInstance().getThemeUpdate());
        String path = "file:///" + f.getAbsolutePath().replace("\\", "/");

        scene.getStylesheets().add(path);
        stage.setScene(scene);
    }

    private void loadActiveReceipt()
    {
        UserModel user = Core.getInstance().getLoginHandler().getUser();
        if (user != null)
        {
            tblCurrentReceipt.setItems(user.getCurrentReceipt().getAllArticles());
        }
    }

    private void loadParkedReceipts()
    {
        ReceiptDAO receiptDAO = new ReceiptDAO();
        tblCurrentReceiptParked.setItems(receiptDAO.getReceipts(ReceiptTypeDAO.ReceiptType.PARKED));
    }

    public ArticleModel getSelectedArticle()
    {
        ArticleModel article = null;
        if (hasSelectedArticle())
        {
            article = tblCurrentReceipt.getSelectionModel().getSelectedItem();
        }
        return article;
    }

    public SaleModel getSelectedSale()
    {
        SaleModel sale = null;
        if (hasSelectedArticle())
        {
            sale = Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().getAllSales().get(getSelectedIndex());
        }
        return sale;
    }

    public boolean hasArticles()
    {
        int i = tblCurrentReceipt.getItems().size();
        return (tblCurrentReceipt.getItems().size() > 0);
    }

    public boolean hasSelectedArticle()
    {
        return (tblCurrentReceipt.getSelectionModel().getSelectedItems().size() > 0);
    }

    public int getSelectedIndex()
    {
        return tblCurrentReceipt.getSelectionModel().getSelectedIndex();
    }

    public void selectLastAddedArticle()
    {
        tblCurrentReceipt.requestFocus();
        tblCurrentReceipt.scrollTo(tblCurrentReceipt.getItems().size() - 1);
        tblCurrentReceipt.getSelectionModel().selectLast();
    }

    public void activateTab(TabType type)
    {
        tabReceipt.getSelectionModel().select(type.ordinal());
        refresh();
    }

    public void setActiveArticle(int index)
    {
        tblCurrentReceipt.getSelectionModel().select(index);
    }

    public void setDisableParkButton(boolean status)
    {
        btnParkReceipt.setDisable(status);
    }

    public void setDisableAnchor(boolean choice)
    {
        splitleft_main.setDisable(choice);
    }

    private void addActivateButton()
    {
        // Define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        colReceiptParkedActivate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Receipt, Boolean>, ObservableValue<Boolean>>()
        {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Receipt, Boolean> features)
            {
                return new SimpleBooleanProperty(features.getValue() != null);
            }
        });

        // Create a cell value factory with an add button for each row in the table.
        colReceiptParkedActivate.setCellFactory(new Callback<TableColumn<Receipt, Boolean>, TableCell<Receipt, Boolean>>()
        {
            @Override
            public TableCell<Receipt, Boolean> call(TableColumn<Receipt, Boolean> personBooleanTableColumn)
            {
                ActivateReceipt activate = new ActivateReceipt();
                //Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().getActivateReceiptData().add(activate);
                return activate;
            }
        });
    }

    private void addDeleteButton()
    {
        // Define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        colReceiptArticleDelete.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArticleModel, Boolean>, ObservableValue<Boolean>>()
        {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<ArticleModel, Boolean> features)
            {
                return new SimpleBooleanProperty(features.getValue() != null);
            }
        });

        // Create a cell value factory with an add button for each row in the table.
        colReceiptArticleDelete.setCellFactory(new Callback<TableColumn<ArticleModel, Boolean>, TableCell<ArticleModel, Boolean>>()
        {
            @Override
            public TableCell<ArticleModel, Boolean> call(TableColumn<ArticleModel, Boolean> personBooleanTableColumn)
            {
                DeleteArticle deleteArticle = new DeleteArticle();
                return deleteArticle;
            }
        });
    }

    /*
    private void addDiscountButtons() {

        ToggleGroup group = new ToggleGroup();

        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        colReceiptArticleDiscount.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArticleModel, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<ArticleModel, Boolean> features) {
                return new SimpleBooleanProperty(features.getValue() != null);
            }
        });

        // create a cell value factory with an add button for each row in the table.
        colReceiptArticleDiscount.setCellFactory(new Callback<TableColumn<ArticleModel, Boolean>, TableCell<ArticleModel, Boolean>>() {
            @Override
            public TableCell<ArticleModel, Boolean> call(TableColumn<ArticleModel, Boolean> personBooleanTableColumn) {
                DiscountArticle discount = new DiscountArticle(tblCurrentReceipt, Core.getInstance().getLoginHandler().getUser().getCurrentReceipt(), group, btnPayReceipt);
                return discount;
            }
        });
    }
     */
    public void addArticle(ArticleModel article)
    {
        tblCurrentReceipt.getItems().add(article);
        refresh();
    }

    @FXML
    private void OnReceiptTableActive(Event event)
    {
        if (tabReceiptActive.isSelected())
        {
            refresh();
        }
    }

    @FXML
    private void OnReceiptTablePark(Event event)
    {
        if (tabReceiptParked.isSelected())
        {
            refresh();
        }
    }

    @FXML
    private void OnReceiptPark(ActionEvent event)
    {
        Core.getInstance().getLoginHandler().getUser().parkReceipt();
        refresh();
    }

    @FXML
    private void OnReceiptCancel(ActionEvent event) throws IOException, InterruptedException
    {
        String title = "Avbryt kvitto";
        String msg = "Vill du avbryta nuvarande kvitto?";
        String detail = "Alla artiklar kommer tas bort...";

        DialogHandler dialogHandler = new DialogHandler();
        if (dialogHandler.question(Core.getInstance().getControlHandler().getMainController().getScene(), title, msg, detail))
        {
            Core.getInstance().getLoginHandler().getUser().cancelReceipt();
            refresh();
        } else
        {
            Core.getInstance().getLog().log("Avbryta kvitto, avbröts!", Log.LogLevel.DESCRIPTIVE);
        }
    }

    @FXML
    private void OnReceiptPay(ActionEvent event) throws IOException
    {
        if (!Core.getInstance().getComHandler().isConnected(comHandler.ComType.CONTROLUNIT))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Varning");
            alert.setHeaderText("Kontrollenheten ej inkopplad! Kan inte fortsätta köp...");
            alert.setContentText("Vänligen gå till inställningar och kontrollera att kontrollenheten är inkopplad korrekt!");
            alert.showAndWait();
            Core.getInstance().getLog().log("Kontrollenheten är inte uppkopplad! Kan inte fortsätta köp...", Log.LogLevel.CRITICAL);
            return;
        }

        Core.getInstance().getLog().log("Betalningsterminal öppen! Registrerar betalning...", Log.LogLevel.NORMAL);
        Core.getInstance().getControlHandler().getPaymentController().setReceipt(Core.getInstance().getLoginHandler().getUser().getCurrentReceipt());

        Core.getInstance().getControlHandler().getMainCenterController().toFront(AnchType.PAYMENT);
    }

    @FXML
    private void OnReceiptSplit(ActionEvent event) throws IOException
    {
        Core.getInstance().getLog().log("Öppnar dialog för att dela nota...", Log.LogLevel.NORMAL);

        FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlSplit());
        Parent root = (Parent) loader.load();

        SplitController splitController = loader.getController();
        splitController.setTitleName("Dela nuvarande nota");
        splitController.setType(SplitController.SplitType.SPLIT);
        splitController.setReceipt(Core.getInstance().getLoginHandler().getUser().getCurrentReceipt());

        Scene scene = new Scene(root);

        scene.getStylesheets().add(ResourcesHandler.getInstance().getThemeMain());

        Stage stage = new Stage();

        stage.setTitle("Dela nota");
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());

        stage.showAndWait();

        refresh();
    }

    @FXML
    private void OnReceiptDiscount(ActionEvent event) throws IOException
    {
        Core.getInstance().getLog().log("Öppnar dialog för att registrerar rabatt...", Log.LogLevel.NORMAL);

        FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlDiscount());
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(ResourcesHandler.getInstance().getThemeMain());

        Stage stage = new Stage();

        stage.setTitle("Rabatt");
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());

        stage.showAndWait();

        refresh();
    }

}
