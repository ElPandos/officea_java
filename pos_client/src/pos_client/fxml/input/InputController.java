/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.input;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pos_client.common.InputHandler;
import pos_client.UserModel;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class InputController implements Initializable
{

    private InputHandler inputHandler = null;
    private UserModel user;

    @FXML
    private Button btnRecepitOpen;
    @FXML
    private Label lblMessage;
    @FXML
    private TextField txtInput;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        inputHandler = new InputHandler();
    }

    public void setMessage(String msg)
    {
        lblMessage.setText(msg);
    }

    public void setCurrentInput(String currentInput)
    {
        txtInput.setText(currentInput);
    }

    public void setButtonName(String name)
    {
        btnRecepitOpen.setText(name);
    }

    public String getInput()
    {
        return txtInput.getText();
    }

    @FXML
    private void OnButton_0(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("0", txtInput.getText()));
    }

    @FXML
    private void OnButton_1(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("1", txtInput.getText()));
    }

    @FXML
    private void OnButton_2(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("2", txtInput.getText()));
    }

    @FXML
    private void OnButton_3(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("3", txtInput.getText()));
    }

    @FXML
    private void OnButton_4(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("4", txtInput.getText()));
    }

    @FXML
    private void OnButton_5(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("5", txtInput.getText()));
    }

    @FXML
    private void OnButton_6(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("6", txtInput.getText()));
    }

    @FXML
    private void OnButton_7(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("7", txtInput.getText()));
    }

    @FXML
    private void OnButton_8(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("8", txtInput.getText()));
    }

    @FXML
    private void OnButton_9(ActionEvent event)
    {
        txtInput.setText(inputHandler.append("9", txtInput.getText()));
    }

    @FXML
    private void OnButton_Delete(ActionEvent event)
    {
        txtInput.setText(inputHandler.delete(txtInput.getText()));
    }

    @FXML
    private void OnButton_Cancel(ActionEvent event)
    {
        txtInput.setText("");

        Stage stage = (Stage) btnRecepitOpen.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void OnOpen(ActionEvent event) throws IOException
    {
        Stage stage = (Stage) btnRecepitOpen.getScene().getWindow();
        stage.close();
    }

}
