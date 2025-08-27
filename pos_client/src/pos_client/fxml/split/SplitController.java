/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.split;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import pos_client.ArticleModel;
import pos_client.Receipt;
import pos_client.ResourcesHandler;
import pos_client.SaleModel;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.db.dao.ReceiptTypeDAO;
import pos_client.db.dao.SalesDAO;
import pos_client.fxml.mainButtonsRight.MainButtonsRightController;
import pos_client.fxml.mainCenter.MainCenterController;
import pos_client.terminals.Terminal;
import pos_client.windows.Log;

/**
 *
 * @author eit-asn
 */
public class SplitController implements Initializable
{

    public MainButtonsRightController mainButtonsRightController;

    // Images
    private ImageView emptyReceipt;

    @FXML
    private TableView<ArticleModel> tblCurrentReceipt;
    @FXML
    private TableView<ArticleModel> tblSplittedReceipt;
    @FXML
    private Button btnLeft;
    @FXML
    private Button btnRight;
    @FXML
    private Button btnSplitCancel;
    @FXML
    private TableColumn<ArticleModel, String> colCurrentReceiptArticleName;
    @FXML
    private TableColumn<ArticleModel, String> colCurrentReceiptArticlePrice;
    @FXML
    private TableColumn<ArticleModel, String> colSplittedReceiptArticleName;
    @FXML
    private TableColumn<ArticleModel, String> colSplittedReceiptArticlePrice;
    @FXML
    private TitledPane tpTitle;
    @FXML
    private Button btnReceiptAction;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        emptyReceipt = new ImageView(new Image(ResourcesHandler.getInstance().getRecieptImage()));

        tblCurrentReceipt.setPlaceholder(emptyReceipt);
        tblSplittedReceipt.setPlaceholder(emptyReceipt);

        btnReceiptAction.setTextAlignment(TextAlignment.CENTER);

        initTable();
        refresh();
        General.getInstance().gaussianBlur(true, Core.getInstance().getControlHandler().getMainController().getParent());
    }

    private void initTable()
    {
        initTableCurrentReceipt();
        initTableSplittedReceipt();
    }

    private void initTableCurrentReceipt()
    {
        colCurrentReceiptArticleName.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("NameAndCategory"));
        colCurrentReceiptArticlePrice.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("PriceTotalIncmStr"));
    }

    private void initTableSplittedReceipt()
    {
        colSplittedReceiptArticleName.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("NameAndCategory"));
        colSplittedReceiptArticlePrice.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("PriceTotalIncmStr"));
    }

    public void refresh()
    {
        update();
    }

    public void update()
    {
        updateSplittedReceipt();
        updateCurrentReceipt();

        General.getInstance().updateTable(tblCurrentReceipt);
        General.getInstance().updateTable(tblSplittedReceipt);

        language();
    }

    private void language()
    {

    }

    Receipt receiptSplit = null;

    private void updateSplittedReceipt()
    {
        String totalSum = "0,00";

        if (receiptSplit != null)
        {
            SalesDAO salesDAO = new SalesDAO();
            tblSplittedReceipt.setItems(salesDAO.getAllSalesArticles(receiptSplit));

            totalSum = receiptSplit.getSumTotalIncmStr();
        }

        if (type != null)
        {
            String append = "\n" + "(" + totalSum + ")";

            String typePayed = "";
            switch (receipt.getPaymentType())
            {
                case CASH:
                    typePayed = "kontant";
                    break;
                case CARD:
                    typePayed = "kort";
                    break;
                case CASH_CARD:
                    typePayed = "kontant + kort";
                    break;
            }

            String payInfo = typePayed.isEmpty() ? "" : "(" + typePayed + ")";

            switch (type)
            {
                case SPLIT:
                    btnReceiptAction.setText("BETALA " + payInfo + append);
                    break;

                case REFUND:
                    btnReceiptAction.setText("ÅTERBETALA " + payInfo + append);
                    break;
            }
        }
    }

    Receipt receipt = null;

    public void setReceipt(Receipt receipt)
    {
        this.receipt = receipt;

        refresh();
    }

    public enum SplitType
    {
        SPLIT,
        REFUND
    }

    SplitType type;

    public void setType(SplitType type)
    {
        this.type = type;
    }

    public void setTitleName(String title)
    {
        tpTitle.setText(title);
    }

    private void updateCurrentReceipt()
    {
        if (receipt != null)
        {
            SalesDAO salesDAO = new SalesDAO();
            tblCurrentReceipt.setItems(salesDAO.getAllSalesArticles(receipt));
        }
    }

    @FXML
    private void OnBtnCancel(ActionEvent event)
    {
        if (receiptSplit != null)
        {
            receiptSplit.resetArticles(receipt);
        }

        close();
    }

    public void close()
    {
        Stage stage = (Stage) btnSplitCancel.getScene().getWindow();
        stage.close();
        General.getInstance().gaussianBlur(false, Core.getInstance().getControlHandler().getMainController().getParent());
    }

    @FXML
    private void OnBtnLeft(ActionEvent event)
    {
        ArticleModel article = tblSplittedReceipt.getSelectionModel().getSelectedItem();
        if (article != null)
        {
            int index = tblSplittedReceipt.getSelectionModel().getSelectedIndex();
            SalesDAO salesDAO = new SalesDAO();
            SaleModel sales = salesDAO.getAllSales(receiptSplit).get(index);
            salesDAO.changeReceiptId(receipt.getReceiptId(), sales.getSaleId());

            refresh();
        }
    }

    @FXML
    private void OnBtnRight(ActionEvent event)
    {
        ArticleModel article = tblCurrentReceipt.getSelectionModel().getSelectedItem();
        if (article != null)
        {
            if (receiptSplit == null)
            {
                receiptSplit = new Receipt(Core.getInstance().getLoginHandler().getUser(), true);
            }

            int index = tblCurrentReceipt.getSelectionModel().getSelectedIndex();
            SalesDAO salesDAO = new SalesDAO();
            SaleModel sales = salesDAO.getAllSales(receipt).get(index);
            salesDAO.changeReceiptId(receiptSplit.getReceiptId(), sales.getSaleId());

            refresh();
        }
    }

    @FXML
    private void OnBtnReceiptAction(ActionEvent event)
    {
        boolean result = true;

        switch (type)
        {
            case SPLIT:

                Core.getInstance().getControlHandler().getPaymentController().setReceipt(receiptSplit);
                Core.getInstance().getControlHandler().getMainCenterController().toFront(MainCenterController.AnchType.PAYMENT);
                break;

            case REFUND:

                receiptSplit.setType(ReceiptTypeDAO.ReceiptType.REFUND);

                switch (receipt.getPaymentType())
                {
                    case CASH:
                        Core.getInstance().getComHandler().openCashBox();
                        receiptSplit.finish();
                        break;
                    case CARD:
                        Terminal terminal = Core.getInstance().getTerminalHandler().getActiveTerminal();
                        if (terminal != null && terminal.isConnected())
                        {
                            String id = General.getInstance().int2Str(Core.getInstance().getLoginHandler().getUser().getId());
                            terminal.sendData(id, receiptSplit.getSumTotalIncm(), 0, 0, Terminal.TransferType.REFUND);
                            receiptSplit.finish();
                        } else
                        {
                            result = false;
                            Core.getInstance().getLog().log("Ingen terminal är uppkopplad!", Log.LogLevel.CRITICAL);
                        }
                        break;
                    case CASH_CARD:
                        Core.getInstance().getComHandler().openCashBox();
                        // hur göra här?
                        break;
                }

                receiptSplit.createReceipt(ReceiptTypeDAO.ReceiptType.REFUND, receipt.getPaymentType());

                break;
        }

        if (!result)
        {
            if (receiptSplit != null)
            {
                receiptSplit.resetArticles(receipt);
            }
        }

        close();

    }
}
