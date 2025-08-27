/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.payment;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pos_client.common.InputHandler;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.Receipt;
import pos_client.ResourcesHandler;
import pos_client.UserModel;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.fxml.mainCenter.MainCenterController.AnchType;
import pos_client.terminals.Terminal;
import pos_client.terminals.Terminal.TransferType;
import pos_client.windows.Log;
import static java.lang.Math.abs;

/**
 * FXML Controller class
 *
 * @author Laptop
 */
public class PaymentController implements Initializable
{

    private InputHandler inputHandler = null;

    @FXML
    private AnchorPane anchMain;
    @FXML
    private Button btn9;
    @FXML
    private Button btn3;
    @FXML
    private Button btn8;
    @FXML
    private Button btn5;
    @FXML
    private Button btn2;
    @FXML
    private Button btn7;
    @FXML
    private Button btn4;
    @FXML
    private Button btn1;
    @FXML
    private Button btn6;
    @FXML
    private Button btn0;
    @FXML
    private Button btn00;
    @FXML
    private Button btnComma;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnDash;
    @FXML
    private Button btnCash;
    @FXML
    public Button btnCard;
    @FXML
    private Button btnCancel;
    @FXML
    private TextArea txtPaymentInfo;
    @FXML
    private Text txtPaymentRemain;
    @FXML
    private Text txtPaymentPayed;
    @FXML
    private Text txtPaymentPurchase;
    @FXML
    private TextField txtPaymentRegistered;
    @FXML
    private Button btnReverseLast;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        inputHandler = new InputHandler();

        init();
        status();
        update();

        btnDisable(true);
    }

    public enum PaymentType
    {
        NONE,
        CASH,
        CARD,
        CASH_CARD
    };

    private Receipt receipt = null;

    public void setReceipt(Receipt receipt)
    {
        this.receipt = receipt;
    }

    private void init()
    {
        txtPaymentRegistered.focusedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1)
            {
                if (txtPaymentRegistered.isFocused())
                {
                    refresh();
                }
            }
        });
    }

    public void refresh()
    {
        status();
        update();
    }

    private void status()
    {
        Terminal terminal = Core.getInstance().getTerminalHandler().getActiveTerminal();
        btnCard.setDisable(!(terminal != null && terminal.isConnected()));
    }

    private void update()
    {
        if (receipt != null)
        {
            txtPaymentRegistered.setText(receipt.getPaymentRemainStr());
            txtPaymentPurchase.setText(receipt.getSumTotalIncmStr());
            txtPaymentRemain.setText(receipt.getPaymentRemainStr());
            txtPaymentPayed.setText(receipt.getPaymentPayedStr());
        }

        txtPaymentInfo.clear();
    }

    public Scene getScene()
    {
        return anchMain.getScene();
    }

    public AnchorPane getAnchor()
    {
        return anchMain;
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

    private void btnDisable(boolean disable)
    {
        btn0.setDisable(disable);
        btn1.setDisable(disable);
        btn2.setDisable(disable);
        btn3.setDisable(disable);
        btn4.setDisable(disable);
        btn5.setDisable(disable);
        btn6.setDisable(disable);
        btn7.setDisable(disable);
        btn8.setDisable(disable);
        btn9.setDisable(disable);
        btn00.setDisable(disable);
        //btnCancel.setDisable(disable);
        btnDash.setDisable(disable);
        btnDelete.setDisable(disable);
        btnOk.setDisable(disable);
        btnComma.setDisable(disable);
        btnReverseLast.setDisable(disable);
    }

    private void finishedPayment()
    {
        if (receipt.finish())
        {
            if (receipt == Core.getInstance().getLoginHandler().getUser().getCurrentReceipt()) // if it is the same receipt as user then we can reset it .. otherwise it is a splitreceipt and may be sales left in the receipt
            {
                Core.getInstance().getLoginHandler().getUser().resetReceipt();
            }

            Core.getInstance().getControlHandler().getMainCenterController().toFront(AnchType.WORK);
            Core.getInstance().getControlHandler().getMainPaneLeftController().refresh();
        }
    }

    @FXML
    private void OnButton_1(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("1", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_2(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("2", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_3(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("1", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_4(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("4", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_5(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("5", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_6(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("6", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_7(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("7", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_8(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("8", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_9(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("9", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_0(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("0", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_00(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append("00", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_Clear(ActionEvent event)
    {
        txtPaymentRegistered.clear();
        btnDisable(false);
    }

    @FXML
    private void OnButton_Delete(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.delete(txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_Comma(ActionEvent event)
    {
        txtPaymentRegistered.setText(inputHandler.append(",", txtPaymentRegistered.getText()));
    }

    @FXML
    private void OnButton_Ok(ActionEvent event)
    {
    }

    @FXML
    private void OnButton_Dash(ActionEvent event)
    {
    }

    @FXML
    private void OnCash(ActionEvent event)
    {
        receipt.setCash(General.getInstance().str2Float(txtPaymentRegistered.getText(), 2));

        if (receipt.getPaymentRemain() <= 0)
        {
            txtPaymentInfo.appendText("Kontant betalning: " + General.getInstance().float2Str(receipt.getCash(), 2) + " kr" + General.getInstance().newLine);

            Core.getInstance().getLoginHandler().getUser().cashboxOpen(UserModel.CashboxType.AUTOMATIC);
            float cashBack = receipt.getPaymentRemain() < 0 ? receipt.getPaymentRemain() * -1 : 0;
            txtPaymentInfo.appendText("Växel: " + General.getInstance().float2Str(cashBack, 2) + " kr" + General.getInstance().newLine);

            receipt.setCashBack(cashBack);
            receipt.setCashResult(receipt.getCash() - receipt.getCashBack());

            // Öppnar lådan
            txtPaymentInfo.appendText("Öppnar kassalåda..." + General.getInstance().newLine);
            Core.getInstance().getComHandler().openCashBox();

            // Kvitto
            txtPaymentInfo.appendText("Skriver ut kvittot!" + General.getInstance().newLine);
            receipt.createReceipt(ReceiptType.NORMAL, PaymentType.CASH); // TODO kolla Services.java

            finishedPayment();
        } else
        {
            txtPaymentInfo.appendText("Mottagen betalning: " + General.getInstance().float2Str(receipt.getCash(), 2) + " kr" + General.getInstance().newLine);
            txtPaymentInfo.appendText("Kvar att betala: " + General.getInstance().float2Str(receipt.getPaymentRemain(), 2) + " kr" + General.getInstance().newLine);
            txtPaymentRegistered.setText(General.getInstance().float2Str(receipt.getPaymentRemain(), 2));
        }

        refresh();
    }

    @FXML
    private void OnCard(ActionEvent event)
    {
        Terminal terminal = Core.getInstance().getTerminalHandler().getActiveTerminal();

        if (terminal != null && terminal.isConnected())
        {
            receipt.setCard(General.getInstance().str2Float(txtPaymentRegistered.getText(), 2));

            if (receipt.getPaymentRemain() <= 0)
            {
                txtPaymentInfo.appendText("Kort betalning: " + General.getInstance().float2Str(receipt.getCard(), 2) + " kr" + General.getInstance().newLine);

                float cardBack = receipt.getPaymentRemain() < 0 ? receipt.getPaymentRemain() * -1 : 0;
                txtPaymentInfo.appendText("Växel: " + General.getInstance().float2Str(cardBack, 2) + " kr" + General.getInstance().newLine);

                receipt.setCardBack(cardBack);
                receipt.setCardResult(receipt.getCard() - receipt.getCardBack());

                // Öppnar lådan
                txtPaymentInfo.appendText("Öppnar kassalåda..." + General.getInstance().newLine);
                Core.getInstance().getComHandler().openCashBox();

                String id = General.getInstance().int2Str(Core.getInstance().getLoginHandler().getUser().getId());
                double paySum = General.getInstance().str2Dbl(txtPaymentRegistered.getText(), 2);
                double payLeft = General.getInstance().str2Dbl(txtPaymentRemain.getText(), 2);

                float paymentRemain = receipt.getPaymentRemain();
                if (paymentRemain < 0)
                {
                    receipt.setCashBack(abs(paymentRemain));
                    terminal.sendData(id, paySum + payLeft, paySum, 0, TransferType.CASHBACK);
                } else if (paymentRemain == 0)
                {
                    terminal.sendData(id, paySum, 0, 0, TransferType.PURCHASE);
                }

                txtPaymentInfo.appendText("Skriver ut kvittot!" + General.getInstance().newLine);
                receipt.createReceipt(ReceiptType.NORMAL, PaymentType.CARD);

                finishedPayment();
            } else
            {
                txtPaymentInfo.appendText("Mottagen betalning: " + General.getInstance().float2Str(receipt.getCard(), 2) + " kr" + General.getInstance().newLine);
                txtPaymentInfo.appendText("Kvar att betala: " + General.getInstance().float2Str(receipt.getPaymentRemain(), 2) + " kr" + General.getInstance().newLine);
                txtPaymentRegistered.setText(General.getInstance().float2Str(receipt.getPaymentRemain(), 2));
            }
        } else
        {
            Core.getInstance().getLog().log("Terminalen är inte ansluten - avbryter köp!", Log.LogLevel.CRITICAL);
        }

        refresh();
    }

    @FXML
    private void OnCancel(ActionEvent event)
    {
        receipt.resetArticles(Core.getInstance().getLoginHandler().getUser().getCurrentReceipt());

        Core.getInstance().getControlHandler().getMainCenterController().toFront(AnchType.WORK);
        Core.getInstance().getControlHandler().getMainPaneLeftController().setDisableAnchor(false);
    }

    @FXML
    private void OnReverseLast(ActionEvent event)
    {

    }

    @FXML
    private void OnRefundCard(ActionEvent event)
    {
        /*
        Terminal terminal = Core.getInstance().getTerminalHandler().getActiveTerminal();

        if (terminal != null && terminal.isConnected())
        {
            Receipt receipt = Core.getInstance().getLoginHandler().getUser().getCurrentReceipt();
            Core.getInstance().getTerminalHandler().setReceipt(receipt);

            String id = General.getInstance().int2Str(Core.getInstance().getLoginHandler().getUser().getId());
            double paySum = General.getInstance().str2Dbl(txtPaymentRegistered.getText(), 2);

            Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().setCard(paySum);

            Core.getInstance().getTerminalHandler().setReceipt(Core.getInstance().getLoginHandler().getUser().getCurrentReceipt());
            Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(id, paySum, 0, 0, TransferType.Refund);
        }
         */
    }

    @FXML
    private void OnUpdate(ActionEvent event)
    {

    }

    @FXML
    private void OnReversal(ActionEvent event)
    {
        /*

        if (Core.getInstance().getTerminalHandler() != null && Core.getInstance().getTerminalHandler().getActiveTerminal() != null)
        {
            if (Core.getInstance().getTerminalHandler().getActiveTerminal().name().compareTo(Jbaxi.NETS) == 0)
            {

                Receipt receipt = Core.getInstance().getLoginHandler().getUser().getCurrentReceipt();
                Core.getInstance().getTerminalHandler().setReceipt(receipt);

                String id = General.getInstance().int2Str(Core.getInstance().getLoginHandler().getUser().getId());
                double paySum = General.getInstance().str2Dbl(txtPaymentRegistered.getText(), 2);

                Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().setCard(paySum);

                Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(id, paySum, 0, 0, TransferType.Reversal); // CASE 3
            }
        }
         */
    }

    @FXML
    private void OnOfflinePurchase(ActionEvent event)
    {
        /*
        if (Core.getInstance().getTerminalHandler() != null && Core.getInstance().getTerminalHandler().getActiveTerminal() != null)
        {
            if (Core.getInstance().getTerminalHandler().getActiveTerminal().name().compareTo(Jbaxi.NETS) == 0)
            {
                Receipt receipt = Core.getInstance().getLoginHandler().getUser().getCurrentReceipt();
                Core.getInstance().getTerminalHandler().setReceipt(receipt);

                String id = General.getInstance().int2Str(Core.getInstance().getLoginHandler().getUser().getId());
                float paySum = receipt.getSumTotalIncm();

                Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().setCard(paySum);

                Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(id, paySum, 0, 0, TransferType.Offline); // CASE 5

            } else
            {
                Core.getInstance().getLog().log("Stödjer inte vald terminal...", Log.LogLevel.CRITICAL);
            }
        }
         */
    }

    @FXML
    private void OnStoreAndForward(ActionEvent event)
    {
        /*
        if (Core.getInstance().getTerminalHandler() != null && Core.getInstance().getTerminalHandler().getActiveTerminal() != null)
        {
            if (Core.getInstance().getTerminalHandler().getActiveTerminal().name().compareTo(Jbaxi.NETS) == 0)
            {

                Receipt receipt = Core.getInstance().getLoginHandler().getUser().getCurrentReceipt();
                Core.getInstance().getTerminalHandler().setReceipt(receipt);

                String id = General.getInstance().int2Str(Core.getInstance().getLoginHandler().getUser().getId());
                float payment = receipt.getSumTotalIncm();

                //Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(General.getInstance().int2Str(user.getId()), 200, 0, 0, TransferType.Purchase); // CASE 1 OK inte knapp
                //Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(General.getInstance().int2Str(user.getId()), 200, 0, 0, TransferType.Purchase); // CASE 2 OK inte knapp
                //Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(General.getInstance().int2Str(user.getId()), 200, 220, 0, TransferType.Cashback); // CASE Log.LogLevel.CRITICAL
                //Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(General.getInstance().int2Str(user.getId()), 200, 0, 0, TransferType.Refund); // CASE 4
                //Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(General.getInstance().int2Str(user.getId()), 200, 0, 0, TransferType.Purchase); // CASE 5
                //Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(General.getInstance().int2Str(user.getId()), 200, 0, 0, TransferType.Offline); // CASE 6
                //Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(General.getInstance().int2Str(user.getId()), (float) 1.51, 0, 0, TransferType.Purchase); // CASE 7
                //Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(General.getInstance().int2Str(user.getId()), 200, 0, 0, TransferType.Purchase); // CASE 8
                Core.getInstance().getTerminalHandler().jbaxi().TransferAmount(id, payment, 0, 0, TransferType.Offline); // case 10
            } else
            {
                Core.getInstance().getLog().log("Stödjer inte vald terminal...", Log.LogLevel.CRITICAL);
            }
        }
         */
    }

    @FXML
    private void OnReceiptCopy(ActionEvent event) throws Exception
    {

    }

}
