/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.mainInfoPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pos_client.ResourcesHandler;
import pos_client.common.Core;
import pos_client.common.DigitalClock;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class MainInfoPaneController implements Initializable
{

    @FXML
    private Text txtCashier;
    @FXML
    public Text txtBordnumber;
    @FXML
    private Text txtTime;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        init();
    }

    public void init()
    {
        digitalClock();
    }

    public void refresh()
    {
        activeUser();
        activeTable();
    }

    public Scene getScene()
    {
        return txtTime.getScene();
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

    public void activeUser()
    {
        txtCashier.setText(Core.getInstance().getLoginHandler().getUser().getName());
    }

    public void activeTable()
    {
        txtBordnumber.setText(Core.getInstance().getLoginHandler().getUser().getCurrentReceipt().getTableNumber());
    }

    private void digitalClock()
    {
        DigitalClock clock = new DigitalClock();
        txtTime.textProperty().bind(clock.textProperty());
    }

}
