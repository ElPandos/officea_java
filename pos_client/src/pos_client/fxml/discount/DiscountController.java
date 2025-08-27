/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.discount;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pos_client.ArticleModel;
import pos_client.common.Core;
import pos_client.ResourcesHandler;
import pos_client.common.General;
import pos_client.common.InputHandler;
import pos_client.db.dao.SalesDAO;

public class DiscountController implements Initializable
{

    private InputHandler inputHandler = null;
    private ImageView emptyReceipt;

    @FXML
    private ToggleGroup toggelgroup1;
    @FXML
    private TableView<ArticleModel> tblCurrentReceipt;
    @FXML
    private TextField txtDiscount;
    @FXML
    private Label lblDiscountOrgPrice;
    @FXML
    private Label lblDiscountNewPrice;
    @FXML
    private Label lblDiscountArticleName;
    @FXML
    private TableColumn<ArticleModel, String> colArticle;
    @FXML
    private TableColumn<ArticleModel, String> colNewPrice;
    @FXML
    private TableColumn<ArticleModel, String> colDiscountType;
    @FXML
    private TableColumn<ArticleModel, String> colDiscountAmount;
    @FXML
    private Button btnDone;
    @FXML
    private ToggleButton tglDiscountFixed;
    @FXML
    private ToggleButton tglDiscountPercent;
    @FXML
    private Text txtOrigPrice;
    @FXML
    private Text txtDiscountPrice;
    @FXML
    private Text txtDiscountArticleName;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        General.getInstance().gaussianBlur(true, Core.getInstance().getControlHandler().getMainController().getParent());

        inputHandler = new InputHandler();

        emptyReceipt = new ImageView(new Image(ResourcesHandler.getInstance().getRecieptImage()));
        tblCurrentReceipt.setPlaceholder(emptyReceipt);

        initTable();
        populate();
        update();
    }

    private void initTable()
    {
        colArticle.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("NameAndCategory"));
        colNewPrice.setCellValueFactory(new PropertyValueFactory<ArticleModel, String>("PriceTotalIncmStr"));

        addListenerTable();
    }

    private void addListenerTable()
    {
        tblCurrentReceipt.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                ArticleModel article = tblCurrentReceipt.getSelectionModel().getSelectedItem();
                if (article != null)
                {
                    txtDiscountArticleName.setText(article.getName());
                    txtOrigPrice.setText(article.getSale().getPriceTotalIncmStr());
                }
            }
        });
    }

    private void populate()
    {
        tblCurrentReceipt.setItems(Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().getAllArticles());
    }

    public void refresh()
    {
        clear();
        populate();
        update();
    }

    public void update()
    {
        ArticleModel article = tblCurrentReceipt.getSelectionModel().getSelectedItem();
        if (article != null)
        {
            txtOrigPrice.setText(General.getInstance().float2Str(article.getSale().getPriceTotalIncm(false), 2));
            txtDiscountPrice.setText(article.getSale().getPriceTotalIncmStr());
        }

        General.getInstance().updateTable(tblCurrentReceipt);

        language();
    }

    private void language()
    {

    }

    private void clear()
    {
        tblCurrentReceipt.getItems().clear();
    }

    @FXML
    private void OnBtnRegister(ActionEvent event)
    {
        ArticleModel article = tblCurrentReceipt.getSelectionModel().getSelectedItem();

        if (article != null)
        {
            float value = General.getInstance().str2Float(txtDiscount.getText(), 2);

            if (tglDiscountFixed.isSelected())
            {
                if (value > article.getSale().getPriceTotalIncm(false))
                {
                    value = article.getSale().getPriceTotalIncm(false);
                }

                article.getSale().setDiscount(value / article.getSale().getPriceTotalIncm(false));
            }

            if (tglDiscountPercent.isSelected())
            {
                if (value > 1)
                {
                    value = 1;
                }

                tblCurrentReceipt.getSelectionModel().getSelectedItem().getSale().setDiscount(value);
            }

            SalesDAO salesDAO = new SalesDAO();
            salesDAO.update(article.getSale());

            refresh();
        }
    }

    @FXML
    private void OnButton_9(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("9", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_8(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("8", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_7(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("7", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_6(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("6", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_5(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("5", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_4(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("4", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_3(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("3", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_2(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("2", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_1(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("1", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_0(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append("0", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_Comma(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.append(",", txtDiscount.getText()));
    }

    @FXML
    private void OnButton_Delete(ActionEvent event)
    {
        txtDiscount.setText(inputHandler.delete(txtDiscount.getText()));
    }

    @FXML
    private void OnBtnDone(ActionEvent event)
    {
        General.getInstance().gaussianBlur(false, Core.getInstance().getControlHandler().getMainController().getParent());

        Stage stage = (Stage) btnDone.getScene().getWindow();
        stage.close();
    }

}
