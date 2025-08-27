/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.work;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
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
import pos_client.ArticleModel;
import pos_client.CategoryHandler;
import pos_client.Receipt;
import pos_client.ResourcesHandler;
import pos_client.common.Core;
import pos_client.db.dao.SalesDAO;

/**
 * FXML Controller class
 *
 * @author Server
 */
public class WorkController implements Initializable
{

    @FXML
    private AnchorPane anchMain;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }

    public void init()
    {
        createCategories();
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

    private Map<Integer, FlowPane> flowMap = new HashMap<Integer, FlowPane>();
    TabPane tabPane = new TabPane();

    public void createCategories()
    {
        tabPane.setId("article_tabpane");
        anchMain.setId("article_anchorpane");

        CategoryHandler categoryHandler = new CategoryHandler();
        categoryHandler.createCategories(tabPane, anchMain, flowMap, CategoryHandler.CategoryType.ARTICLE);
    }

    public void addArticle(ArticleModel article)
    {
        LocalTime before = LocalTime.now();

        Receipt receipt = Core.getInstance().getLoginHandler().getUser().getCurrentReceipt();
        SalesDAO salesDAO = new SalesDAO();
        salesDAO.add(article, receipt);

        Core.getInstance().getControlHandler().getMainPaneLeftController().addArticle(new ArticleModel(article)); // måste ladda in ett nytt objekt annars kopieras det in i alla inladdade artiklar

        LocalTime after = LocalTime.now();
        Duration elapsed = Duration.between(before, after);
        Core.getInstance().getLog().logSpeed("Article added in: " + elapsed.toMillis() + " mSek");
    }

}
