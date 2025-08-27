/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.warning;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Laptop
 */
public class WarningController implements Initializable
{

    @FXML
    private Label txtMessage;
    @FXML
    private Label txtDetails;
    @FXML
    private HBox actionParent;
    @FXML
    private Button btnYes;
    @FXML
    private HBox okParent;
    @FXML
    private Button btnNo;

    public boolean choice = false;

    public void setMessage(String msg)
    {
        txtMessage.setText(msg);
    }

    public void setDetails(String msg)
    {
        txtDetails.setText(msg);
    }

    public boolean getChoice()
    {
        return choice;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    @FXML
    private void OnBtnYes(ActionEvent event)
    {
        choice = true;
    }

    @FXML
    private void onBtnNo(ActionEvent event)
    {
        choice = false;
    }

}
