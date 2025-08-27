/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.keyboard.large;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pos_client.ResourcesHandler;
import pos_client.common.InputHandler;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class KeyboardController implements Initializable
{

    private InputHandler inputHandler = null;

    @FXML
    private AnchorPane keyboard_back;
    @FXML
    private Button clear_button;
    @FXML
    private Button delete_character_button;

    private TextField txtSelectedTextField;
    private TextField txtUserTextField;
    private TextField txtPasswordTextField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        inputHandler = new InputHandler();
    }

    public void refresh()
    {
        update();
    }

    private void update()
    {
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
        return clear_button.getScene();
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

    public void setUserTextField(TextField textField)
    {
        txtUserTextField = textField;
    }

    public void setPasswordTextField(TextField textField)
    {
        txtPasswordTextField = textField;
    }

    public void addListener()
    {
        txtSelectedTextField = txtUserTextField;

        txtUserTextField.focusedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1)
            {
                if (txtUserTextField.isFocused())
                {
                    txtSelectedTextField = txtUserTextField;
                }
            }
        });

        txtPasswordTextField.focusedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1)
            {
                if (txtPasswordTextField.isFocused())
                {
                    txtSelectedTextField = txtPasswordTextField;
                }
            }
        });
    }

    @FXML
    private void OnButton_Q(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("Q", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_W(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("W", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_E(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("E", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_R(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("R", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_T(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("T", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_Y(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("Y", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_U(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("U", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_I(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("I", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_O(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("O", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_P(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("P", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_Å(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("Å", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_Ä(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("Ä", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_Ö(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("Ö", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_A(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("A", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_S(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("S", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_D(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("D", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_F(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("F", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_G(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("G", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_H(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("H", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_J(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("J", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_K(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("K", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_L(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("L", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_Z(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("Z", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_X(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("X", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_C(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("C", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_V(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("V", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_B(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("B", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_N(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("N", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_M(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("M", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_1(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("1", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_2(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("2", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_3(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("3", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_4(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("4", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_5(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("5", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_6(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("6", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_7(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("7", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_8(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("8", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_9(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("9", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_0(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.append("0", txtSelectedTextField.getText()));
    }

    @FXML
    private void OnButton_Clear(ActionEvent event)
    {
        txtSelectedTextField.clear();
    }

    @FXML
    private void OnButton_Delete(ActionEvent event)
    {
        txtSelectedTextField.setText(inputHandler.delete(txtSelectedTextField.getText()));
    }

}
