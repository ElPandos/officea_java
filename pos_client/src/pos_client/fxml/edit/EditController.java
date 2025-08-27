/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.edit;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import pos_client.CategoryHandler;
import pos_client.CategoryHandler.CategoryType;
import pos_client.ResourcesHandler;

/**
 * FXML Controller class
 *
 * @author Server
 */
public class EditController implements Initializable
{

    @FXML
    private AnchorPane anchMain;
    @FXML
    private FlowPane flowPane;

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
        return anchMain.getScene();
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

    public AnchorPane getAnchor()
    {
        return anchMain;
    }

    private Map<Integer, FlowPane> flowMap = new HashMap<>();
    TabPane tabPane = new TabPane();

    public void createCategories()
    {
        CategoryHandler categoryHandler = new CategoryHandler();
        categoryHandler.createCategories(tabPane, anchMain, flowMap, CategoryType.EDIT);
    }

}
