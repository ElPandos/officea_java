/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.refund;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import pos_client.UserModel;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class RefundController implements Initializable
{

    private UserModel user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //TODO
    }

    public void setUser(UserModel user)
    {
        this.user = user;
    }

}
