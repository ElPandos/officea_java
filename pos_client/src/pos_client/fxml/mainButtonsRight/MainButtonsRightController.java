/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.mainButtonsRight;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pos_client.Receipt;
import pos_client.UserModel;
import pos_client.common.Core;
import pos_client.common.DialogHandler;
import pos_client.common.General;
import pos_client.ResourcesHandler;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.ReceiptTypeDAO;
import pos_client.db.dao.UserDAO;
import pos_client.fxml.mainCenter.MainCenterController.AnchType;
import pos_client.fxml.payment.PaymentController;
import pos_client.fxml.split.SplitController;
import pos_client.services.PrintReceiptService;
import pos_client.services.PrintReceiptServiceImpl;
import pos_client.services.ReceiptPrinter;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class MainButtonsRightController implements Initializable
{

    @FXML
    private FlowPane flwMainButtonsRight;
    @FXML
    private Button btnEdit;
    @FXML
    private ImageView btn_open_edit;
    @FXML
    private Button btnSpecial;
    @FXML
    private Button btnArticles;
    @FXML
    private Button btnScan;
    @FXML
    private Button btnRefund;
    @FXML
    private Button btnPrintReceiptCopy;
    @FXML
    private Button btnSetTableNumber;
    @FXML
    private Button logout_button;
    @FXML
    private Button btnCustomers;
    @FXML
    private Button btnCASHBOX;
    @FXML
    private Button btnHistory;
    @FXML
    private Button btnPrintBill;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    public void refresh()
    {
        update();
    }

    private void update()
    {
        updateCashBox();
        language();
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
        return btnRefund.getScene();
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

    public void updateCashBox()
    {
        UserDAO.CashboxOpened status = Core.getInstance().getLoginHandler().getUser().cashboxOpened();

        String info = "A: " + General.getInstance().int2Str(status.automatic);
        info += " / M: " + General.getInstance().int2Str(status.manual);

        btnCASHBOX.setText(info);
        btnCASHBOX.textAlignmentProperty().set(TextAlignment.CENTER);
    }

    public void setDisableButtons(AnchType anchType, boolean disable)
    {
        switch (anchType)
        {
            case SPECIAL:
                btnEdit.setDisable(disable);
                break;
            case EDIT:
                btnSpecial.setDisable(disable);
                break;
        }

        btnArticles.setDisable(disable);
        btnCASHBOX.setDisable(disable);
        btnCustomers.setDisable(disable);
        btnRefund.setDisable(disable);
        btnScan.setDisable(disable);
        btnPrintReceiptCopy.setDisable(disable);
        btnPrintBill.setDisable(disable);
        btnSetTableNumber.setDisable(disable);
        btnHistory.setDisable(disable);
        logout_button.setDisable(disable);
    }

    private class ActivateButton
    {

        public AnchType type = AnchType.NONE;

        public String btnNormalText;
        public String btnActivatedText;

        public boolean normal;
    }

    private ArrayList<ActivateButton> buttons = new ArrayList<>();

    private void changeButton(AnchType anchType)
    {
        ActivateButton activateButton = null;
        for (ActivateButton button : buttons)
        {
            if (button.type == anchType)
            {
                activateButton = button;
                break;
            }
        }

        switch (anchType)
        {
            case SPECIAL:
                if (activateButton != null)
                {
                    if (activateButton.normal)
                    {
                        btnSpecial.setText(activateButton.btnNormalText);
                        activateButton.normal = false;
                        Core.getInstance().getControlHandler().getMainCenterController().toFront(anchType.WORK);
                    } else
                    {
                        btnSpecial.setText(activateButton.btnActivatedText);
                        activateButton.normal = true;
                    }
                } else
                {
                    activateButton = new ActivateButton();
                    activateButton.type = anchType;
                    activateButton.btnNormalText = btnSpecial.getText();
                    activateButton.btnActivatedText = "SPARA";
                    btnSpecial.setText(activateButton.btnActivatedText);
                    buttons.add(activateButton);

                    activateButton.normal = true;
                }

                setDisableButtons(anchType, activateButton.normal);

                break;

            case EDIT:
                if (activateButton != null)
                {
                    if (activateButton.normal)
                    {
                        btnEdit.setText(activateButton.btnNormalText);
                        activateButton.normal = false;
                        Core.getInstance().getControlHandler().getMainCenterController().toFront(anchType.WORK);

                    } else
                    {
                        btnEdit.setText(activateButton.btnActivatedText);
                        activateButton.normal = true;
                    }
                } else
                {
                    activateButton = new ActivateButton();
                    activateButton.type = anchType;
                    activateButton.btnNormalText = btnEdit.getText();
                    activateButton.btnActivatedText = "SPARA";
                    btnEdit.setText(activateButton.btnActivatedText);
                    buttons.add(activateButton);

                    activateButton.normal = true;
                }

                setDisableButtons(anchType, activateButton.normal);

                break;

        }
    }

    @FXML
    private void OnBtnSpecial(ActionEvent event)
    {
        if (Core.getInstance().getControlHandler().getMainPaneLeftController().hasSelectedArticle())
        {
            Core.getInstance().getControlHandler().getSpecialController().createCategories();
            Core.getInstance().getControlHandler().getMainCenterController().toFront(AnchType.SPECIAL);
            changeButton(AnchType.SPECIAL);
        } else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.setTitle("Information");
            alert.setHeaderText("Välj en artikel");
            alert.setContentText("Markera en artikel i tabellen som du vill redigera!");
            alert.showAndWait();
            Core.getInstance().getLog().log("Ingen vald artikel att editera", Log.LogLevel.CRITICAL);
        }
    }

    @FXML
    private void OnEditArticles(ActionEvent event)
    {
        if (Core.getInstance().getControlHandler().getMainPaneLeftController().hasSelectedArticle())
        {
            Core.getInstance().getControlHandler().getEditController().createCategories();
            Core.getInstance().getControlHandler().getMainCenterController().toFront(AnchType.EDIT);
            changeButton(AnchType.EDIT);
        } else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Välj en artikel");
            alert.setContentText("Markera en artikel i tabellen som du vill redigera!");
            alert.showAndWait();

            Core.getInstance().getLog().log("Ingen vald artikel att editera", Log.LogLevel.CRITICAL);
        }
    }

    @FXML
    private void OnReceiptRefund(ActionEvent event) throws IOException, InterruptedException
    {
        Core.getInstance().getLog().log("Laddar kvitto återbetalning", Log.LogLevel.NORMAL);

        General.getInstance().gaussianBlur(true, Core.getInstance().getControlHandler().getMainController().getParent());

        DialogHandler dialogHandler = new DialogHandler();
        String output = dialogHandler.input(Core.getInstance().getControlHandler().getMainController().getScene(), "Återbetalning", "Leta efter kvitto nr:", "", "SÖK");

        if (!output.isEmpty())
        {
            ReceiptDAO receiptDAO = new ReceiptDAO();
            Receipt receipt = receiptDAO.getReceiptObj(General.getInstance().str2Int(output));

            if (receipt != null && receipt.getType() == ReceiptTypeDAO.ReceiptType.NORMAL)
            {
                FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlSplit());
                Parent root = (Parent) loader.load();

                SplitController splitController = loader.getController();
                splitController.setTitleName("Återbetaning av kvitto med nr: " + output);
                splitController.setType(SplitController.SplitType.REFUND);
                splitController.setReceipt(receipt);

                Scene scene = new Scene(root);

                scene.getStylesheets().add(ResourcesHandler.getInstance().getThemeMain());

                Stage stage = new Stage();

                stage.setScene(scene);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initOwner(((Node) event.getSource()).getScene().getWindow());

                stage.show();
            }
        } else
        {
            Core.getInstance().getLog().log("Måste ange ett kvittonummer", Log.LogLevel.CRITICAL);
        }

        General.getInstance().gaussianBlur(false, Core.getInstance().getControlHandler().getMainController().getParent());
    }

    @FXML
    private void OnOpenCashier(ActionEvent event)
    {
        Core.getInstance().getLoginHandler().getUser().cashboxOpen(UserModel.CashboxType.MANUAL);
        updateCashBox();
    }

    @FXML
    private void OnReceiptCopy(ActionEvent event) throws Exception
    {
        Core.getInstance().getLog().log("Laddar kvitto utskrift dialog...", Log.LogLevel.NORMAL);

        DialogHandler dialogHandler = new DialogHandler();
        String output = dialogHandler.input(Core.getInstance().getControlHandler().getMainController().getScene(), "Kvitto", "Skriv ut kvittokopia:", "", "SÖK");

        if (!output.isEmpty())
        {
            PrintReceiptService receiptService = new PrintReceiptServiceImpl();
            int receiptNo = General.getInstance().str2Int(output);

            PrintReceiptServiceImpl.PrintReceiptCopyStatus status = receiptService.printReceiptCopy(receiptNo, new ReceiptPrinter());

            if (null != status)
            {
                switch (status)
                {
                    case ALREADY_PRINTED:
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Warning");
                        alert.setHeaderText("Kvitto " + receiptNo + " är redan utskrivet");
                        alert.setContentText("Kan bara skriva ut kvittot en gång");
                        alert.showAndWait();
                        break;
                    }
                    case OK:
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Warning");
                        alert.setHeaderText("Skrev ut kvitto " + receiptNo);
                        alert.showAndWait();
                        break;
                    }
                    case RECEIPT_NOT_FOUND:
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Warning");
                        alert.setHeaderText("Hittade inte inget kvitto nr: " + receiptNo);
                        alert.showAndWait();
                        break;
                    }
                    default:
                        break;
                }
            }
            else {
                Core.getInstance().getLog().log("Fel i kvitto kopia status...", Log.LogLevel.CRITICAL);
            }
        }
    }

    @FXML
    private void OnButtonLogout(ActionEvent event)
    {
        if (Core.getInstance().getLoginHandler().logout(Core.getInstance().getControlHandler().getMainController().getScene()))
        {
            Core.getInstance().getControlHandler().getMainController().logout();
        }
    }

    @FXML
    private void OnSetTableNumber(ActionEvent event) throws IOException, InterruptedException
    {
        Core.getInstance().getLog().log("Öppnar dialog för att registrera bordsnummer...", Log.LogLevel.NORMAL);

        String currentInput = Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().getTableNumber();
        DialogHandler dialogHandler = new DialogHandler();
        String output = dialogHandler.input(Core.getInstance().getControlHandler().getMainController().getScene(), "Bordsnummer", "Registrera bordsnummer:", currentInput, "SPARA");

        if (!output.isEmpty())
        {
            Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().setTableNumber(output);

            ReceiptDAO receiptDAO = new ReceiptDAO();
            receiptDAO.updateTableNr(Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().getReceiptId(), output);
        }

        Core.getInstance().getControlHandler().getMainInfoPaneController().refresh();
    }

    @FXML
    private void OnBtnHistory(ActionEvent event) throws IOException
    {
        Core.getInstance().getLog().log("Öppnar dialog för att se försäljningshistorik", Log.LogLevel.NORMAL);

        FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlHistory());
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);

        scene.getStylesheets().add(ResourcesHandler.getInstance().getThemeMain());
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());

        stage.show();
    }

    @FXML
    private void OnBtnPrintBill(ActionEvent event)
    {
        Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().createReceipt(ReceiptTypeDAO.ReceiptType.PROFO, PaymentController.PaymentType.NONE);
    }
}
