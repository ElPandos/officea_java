/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.screens;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pos_client.ArticleModel;
import pos_client.Receipt;
import pos_client.ResourcesHandler;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.db.dao.BoutiqueDAO;
import pos_client.fxml.mainButtonsRight.MainButtonsRightController;

/**
 * FXML Controller class
 *
 * @author Server
 */
public class CustomerController implements Initializable
{

    public MainButtonsRightController mainButtonsRightController;

    @FXML
    private TableColumn<ArticleModel, String> colReceiptArticleName;
    @FXML
    private TableColumn<ArticleModel, String> colNewPriceId;
    @FXML
    private Label lblCardTotal;
    @FXML
    private Label lblCashTotal;
    @FXML
    private TableView<ArticleModel> tblCurrentReceipt;
    @FXML
    private Label lblBoutiqueInfo;
    @FXML
    private Label lblBoutiqueName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initTable();
        language();
    }

    private ImageView emptyReceipt;

    private void initTable()
    {
        colReceiptArticleName.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("NameAndCategory"));
        colNewPriceId.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("PriceTotalIncmStr"));

        emptyReceipt = new ImageView(new Image(ResourcesHandler.getInstance().getRecieptImage()));
        tblCurrentReceipt.setPlaceholder(emptyReceipt);
    }

    public void refresh()
    {
        update();
    }

    private void clear()
    {
        tblCurrentReceipt.getItems().clear();
    }

    public void update()
    {
        updateCustomerTotals();
        selectLastAddedArticle();
        setBoutiqueInfo();

        General.getInstance().updateTable(tblCurrentReceipt);

        language();
    }

    private void language()
    {
    }

    private void setBoutiqueInfo()
    {
        BoutiqueDAO boutiqueDAO = new BoutiqueDAO();

        lblBoutiqueName.setText(boutiqueDAO.getCompanyName());
        lblBoutiqueInfo.setText("Org: " + boutiqueDAO.getOrgNumber()
                + "\n" + boutiqueDAO.getCompanyAdress1()
                + "\n" + boutiqueDAO.getCompanyPostal()
                + ", " + boutiqueDAO.getCompanyCity()
                + "\n" + "Tel: " + boutiqueDAO.getPhoneNumber());
//        txtName.setText(receiptDAO.getCompanyName());
//        txtAddress1.setText(receiptDAO.getCompanyAdress1());
//        txtAddress2.setText(receiptDAO.getCompanyAdress2());
//        txtPostal.setText(receiptDAO.getCompanyPostal());
//        txtCity.setText(receiptDAO.getCompanyCity());
//        txtPhone.setText(receiptDAO.getPhoneNumber());
//        txtWebsite.setText(receiptDAO.getCompanyWebsite());
    }

    public void setCustomerReceiptData(ObservableList<ArticleModel> receiptData)
    {
        tblCurrentReceipt.setItems(receiptData);

        refresh();
    }

    public void updateCustomerTotals()
    {
        Receipt receipt = Core.getInstance().getLoginHandler().getUser().getCurrentReceipt();

        lblCardTotal.setText("(" + receipt.getSumTotalIncmStr() + ")");
        lblCashTotal.setText(General.getInstance().float2Str(Math.round(receipt.getSumTotalIncm()), 2));
    }

    public void selectLastAddedArticle()
    {
        tblCurrentReceipt.requestFocus();
        tblCurrentReceipt.scrollTo(tblCurrentReceipt.getItems().size() - 1);
        tblCurrentReceipt.getSelectionModel().selectLast();
    }

}
