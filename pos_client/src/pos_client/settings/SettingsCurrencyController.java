/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsCurrencyController implements Initializable
{

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        populate();
    }

    public void refresh()
    {

        clear();
        populate();
    }

    private void clear()
    {

    }

    private void populate()
    {

    }

}
