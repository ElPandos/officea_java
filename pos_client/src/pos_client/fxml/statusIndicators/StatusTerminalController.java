/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.statusIndicators;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.shape.Rectangle;
import pos_client.common.General;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class StatusTerminalController implements Initializable
{

    @FXML
    private Rectangle recTerminal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }

    public void setTerminalStatus(boolean connected)
    {
        status(connected, recTerminal);
    }

    public void status(boolean connected, Rectangle rect)
    {
        if (connected)
        {
            rect.setFill(General.getInstance().CONNECTED);
        } else
        {
            rect.setFill(General.getInstance().DISCONNECTED);
        }
    }

}
