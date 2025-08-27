/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.about;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pos_client.db.dao.AboutDAO;

/**
 * FXML Controller class
 *
 * @author Laptop
 */
public class AboutController implements Initializable
{

    @FXML
    private Button btnClose;
    @FXML
    private TextArea txtManufacturer;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        AboutDAO aboutDAO = new AboutDAO();
        aboutDAO.getInfo(txtManufacturer);
    }

    private void onlabelclick(MouseEvent event)
    {

        int a = 0;
    }

    private void OnUrl(ActionEvent event) throws IOException, URISyntaxException
    {

        Desktop.getDesktop().browse(new URI("http://www.officea.se"));
    }

    @FXML
    private void OnClose(ActionEvent event)
    {

        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

}
