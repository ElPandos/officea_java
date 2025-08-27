/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.history;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pos_client.ArticleModel;
import pos_client.Receipt;
import pos_client.ResourcesHandler;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.db.dao.SalesDAO;
import pos_client.windows.Log;
import pos_client.db.dao.ReceiptDAO;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class HistoryController implements Initializable
{

    @FXML
    private TableView<Receipt> tblHisoryReceipts;
    @FXML
    private TableColumn<Receipt, Integer> ColHistoryReceiptNr;
    @FXML
    private TableColumn<Receipt, String> ColHistoryReceiptTimeStamp;
    @FXML
    private TableColumn<Receipt, String> ColHistoryReceiptTotal;

    @FXML
    private TableView<ArticleModel> tblHistoryReceiptsArticles;
    @FXML
    private TableColumn<ArticleModel, String> ColHistoryReceiptArticleName;
    @FXML
    private TableColumn<ArticleModel, String> colHistoryReceiptArticlePrice;
    @FXML
    private Button btnHistoryCancel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initTable();
        populate();

        General.getInstance().gaussianBlur(true, Core.getInstance().getControlHandler().getMainController().getParent());
    }

    private void initTable()
    {
        ColHistoryReceiptNr.setCellValueFactory(new PropertyValueFactory<Receipt, Integer>("Nr"));
        ColHistoryReceiptTimeStamp.setCellValueFactory(new PropertyValueFactory<Receipt, String>("ModifyTime"));
        ColHistoryReceiptTotal.setCellValueFactory(new PropertyValueFactory<Receipt, String>("SumTotalIncmStr"));

        ColHistoryReceiptArticleName.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("NameAndCategory"));
        colHistoryReceiptArticlePrice.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("PriceTotalIncmStr"));

        addListenerTable();
    }

    private void addListenerTable()
    {

        tblHisoryReceipts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                loadSales(tblHisoryReceipts.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void refresh()
    {
        clear();
        populate();
        update();
    }

    private void clear()
    {
        //tblHisoryReceipts.getItems().clear();
        tblHistoryReceiptsArticles.getItems().clear();
    }

    private void populate()
    {
        loadReceipts();
    }

    public void loadReceipts()
    {
        Core.getInstance().getLog().log("Laddar dagens kvitton ", Log.LogLevel.DESCRIPTIVE);

        ReceiptDAO receiptDAO = new ReceiptDAO();
        tblHisoryReceipts.setItems(receiptDAO.getReceiptsToday());
    }

    public void loadSales(Receipt receipt)
    {
        if (receipt != null)
        {
            tblHistoryReceiptsArticles.getItems().clear();

            Core.getInstance().getLog().log("Laddar dagens kvitton ", Log.LogLevel.DESCRIPTIVE);

            SalesDAO salesDAO = new SalesDAO();
            tblHistoryReceiptsArticles.setItems(salesDAO.getAllSalesArticles(receipt));
        }
    }

    private void update()
    {
        //General.getInstance().updateTable(tblHisoryReceipts);
        General.getInstance().updateTable(tblHistoryReceiptsArticles);
    }

    private void language()
    {
        /*
        btnCancelReceipt.setText(Language.getInstance().getBtnReceiptCancel());
        btnDiscount.setText(Language.getInstance().getBtnReceiptDiscount());
        String str = Language.getInstance().getBtnReceiptPark();
        btnParkReceipt.setText(Language.getInstance().getBtnReceiptPark());
        btnSplitReceipt.setText(Language.getInstance().getBtnReceiptSplit());
         */
    }

    public Scene getScene()
    {
        return btnHistoryCancel.getScene();
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

    @FXML
    private void OnBtnCancel(ActionEvent event)
    {
        Stage stage = (Stage) btnHistoryCancel.getScene().getWindow();
        stage.close();
        General.getInstance().gaussianBlur(false, Core.getInstance().getControlHandler().getMainController().getParent());
    }

}
