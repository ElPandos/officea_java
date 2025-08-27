/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.main;

import java.io.File;
import pos_client.common.Core;
import pos_client.db.Database;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JRException;
import pos_client.Receipt;
import pos_client.ResourcesHandler;
import pos_client.UserModel;
import pos_client.common.DialogHandler;
import pos_client.common.General;
import pos_client.common.Language;
import pos_client.common.Language.LanguageType;
import pos_client.components.reporting.JRViewerFxController;
import pos_client.db.dao.CashBoxDAO;
import pos_client.db.dao.ExchangeDAO;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.db.dao.SalesDAO;
import pos_client.fxml.keyboard.large.KeyboardController;
import pos_client.fxml.mainButtonsRight.MainButtonsRightController;
import pos_client.fxml.mainCenter.MainCenterController;
import pos_client.fxml.mainInfoPane.MainInfoPaneController;
import pos_client.fxml.mainPaneLeft.MainPaneLeftController;
import pos_client.reporting.DailyReportBuilder;
import pos_client.reporting.datasource.DaylieReportZDataSource;
import pos_client.windows.Log;

public class MainController implements Initializable
{

    /*
    	initTable();
        populate();
        status();
	update()
	
	initTable();
	refresh();
	refreshCSS();
	clear();
	populate();
	status();
	update();
	language();
     */
    public MainButtonsRightController mainButtonsRightController;
    public MainPaneLeftController mainPaneLeftController;
    public MainInfoPaneController mainInfoPaneController;
    public MainCenterController mainCenterController;
    public KeyboardController keyboardController;

    @FXML
    private Menu menuSystem;
    @FXML
    private CheckMenuItem connectDB;
    @FXML
    private CheckMenuItem langSwe;
    @FXML
    private CheckMenuItem langEng;
    @FXML
    private TextField txtUserField;
    @FXML
    private PasswordField txtPasswordField;
    @FXML
    private Text txtLoginInfo;
    @FXML
    private AnchorPane anchMainData;
    @FXML
    private BarChart<String, Number> chartPayMeans;
    @FXML
    private LineChart<String, Number> chartSales;
    @FXML
    public TabPane tabMain;
    @FXML
    private Tab tabCashier;
    @FXML
    private Tab tabStatistics;
    @FXML
    private Tab tabReport;
    @FXML
    public Tab tabLogin;
    @FXML
    private Tab tabCustomer;
    @FXML
    private AnchorPane mainpaneright;
    @FXML
    public AnchorPane mainbottomrightanchor;
    @FXML
    private AnchorPane anchLoginMain;
    @FXML
    private Label lblTotalSales;
    @FXML
    private Label lblTotalMoms;
    @FXML
    private Label lblTotalExchange;
    @FXML
    private Label lblAmoutProducts;
    @FXML
    private Label lblAmoutServices;
    @FXML
    private Label lblAmountReceipts;
    @FXML
    private Label lblCASHBOXOpenings;
    @FXML
    private Label lblAmountReciptCopies;
    @FXML
    private Label lblAmountOngoing;
    @FXML
    private Label lblAmountAborted;
    @FXML
    private Label lblAmountParked;
    @FXML
    private VBox mainvbox;
    @FXML
    private MenuItem subMenuCustomer;
    @FXML
    private MenuItem subMenuIdle;
    @FXML
    private AnchorPane mainAnchTop;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
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

    @FXML
    private void OnBtnDefault(ActionEvent event)
    {
        txtUserField.setText("DEFAULT");
        txtPasswordField.setText("DATABASE");

        try
        {
            OnLogin(null);
        } catch (SQLException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        txtUserField.setText("JOHN");
        txtPasswordField.setText("1");

    }

    public void init()
    {
        // Enable communication between nested fxmls
        Core.getInstance().getControlHandler().setMainController(this);

        // Setup the keyboard for login
        keyboardController.setUserTextField(txtUserField);
        keyboardController.setPasswordTextField(txtPasswordField);
        keyboardController.addListener();
        Core.getInstance().getControlHandler().setKeyboardController(keyboardController);

        Core.getInstance().getControlHandler().setMainButtonsRightController(mainButtonsRightController);
        Core.getInstance().getControlHandler().setMainInfoPaneController(mainInfoPaneController);
        Core.getInstance().getControlHandler().setMainPaneLeftController(mainPaneLeftController);
        Core.getInstance().getControlHandler().setMainCenterController(mainCenterController);

        // Kills the threades
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent e)
            {

                UserModel user = Core.getInstance().getLoginHandler().getUser();
                if (user != null)
                {
                    Receipt receipt = user.getCurrentReceipt();
                    if (receipt != null)
                    {
                        if (receipt.hasSales())
                        {
                            receipt.park();
                        } else
                        {
                            receipt.cancel();
                        }
                    }
                }

                Platform.exit();
                System.exit(0);
            }
        });
    }

    public Stage primaryStage;

    public void setStage(Stage stage)
    {
        this.primaryStage = stage;
    }

    public AnchorPane getAnchor()
    {
        return anchMainData;
    }

    public Scene getScene()
    {
        return txtPasswordField.getScene();
    }

    private Parent parent;

    public void setParent(Parent parent) // This is for gaussian blur
    {
        this.parent = parent;
    }

    public Parent getParent()
    {
        return this.parent;
    }

    public void logout()
    {
        tabLogin.setDisable(false);
        tabMain.getSelectionModel().select(tabLogin);

        enableGUI(Core.getInstance().getLoginHandler().getUser().getSecurityLevel(), false);

        txtPasswordField.clear();

        Core.getInstance().getLog().log("Loggar ut Användare: " + txtUserField.getText(), Log.LogLevel.NORMAL);

        Core.getInstance().getControlHandler().getMainPaneLeftController().clear();

        Core.getInstance().getScreenHandler().getCustomer().hide();
        Core.getInstance().getScreenHandler().getIdle().show();

    }

    private void updateQuickReport()
    {
        ReceiptDAO receiptDAO = new ReceiptDAO();
        ExchangeDAO exchangeDAO = new ExchangeDAO();
        SalesDAO salesDAO = new SalesDAO();
        CashBoxDAO cashBoxDAO = new CashBoxDAO();

        lblTotalSales.setText(receiptDAO.getSumTotalSales() + " (Kort: " + receiptDAO.getSumTotalCard() + ") (Kontant: " + receiptDAO.getSumTotalCash() + ")");
        lblTotalMoms.setText(receiptDAO.getSumTotalMoms());
        lblTotalExchange.setText("ej klar");
        lblAmoutProducts.setText(salesDAO.getSoldItems());
        lblAmoutServices.setText(salesDAO.getSoldServices());
        lblAmountReceipts.setText(receiptDAO.getReceiptsNormal());
        lblAmountParked.setText(receiptDAO.getReceiptsParked());
        lblCASHBOXOpenings.setText(cashBoxDAO.getCashBoxOpenAutomatic() + cashBoxDAO.getCashBoxOpenManual());
        lblAmountReciptCopies.setText(receiptDAO.getReceiptsCopy());
        lblAmountOngoing.setText(receiptDAO.getReceiptsOngoing());
        lblAmountAborted.setText(receiptDAO.getReceiptsCanceled());

        chartSales.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.getData().add(new XYChart.Data<String, Number>("Jan", 100));
        series.getData().add(new XYChart.Data<String, Number>("Feb", 120));
        series.getData().add(new XYChart.Data<String, Number>("Mar", 60));
        series.getData().add(new XYChart.Data<String, Number>("Apr", 200));
        series.getData().add(new XYChart.Data<String, Number>("May", 200));
        series.setName("Months");
        chartSales.getData().addAll(series);

        chartPayMeans.getData().clear();
        XYChart.Series<String, Number> series2 = new XYChart.Series<String, Number>();
        series2.getData().add(new XYChart.Data<String, Number>("Kontant", 9500));
        series2.setName("Kontant: " + "9 500kr");

        XYChart.Series<String, Number> series3 = new XYChart.Series<String, Number>();
        series3.getData().add(new XYChart.Data<String, Number>("Kort", 11500));
        series3.setName("Kort: " + "11 500,00kr");

        XYChart.Series<String, Number> series4 = new XYChart.Series<String, Number>();
        series4.getData().add(new XYChart.Data<String, Number>("Faktura", 600));
        series4.setName("Faktura: " + "600kr");
        chartPayMeans.getData().addAll(series2, series3, series4);

    }

    void enableGUI(UserModel.SecurityLevel level, boolean loggedIn)
    {
        menuSystem.setDisable(!loggedIn); // Disable menues if we are not logged in
        subMenuIdle.setDisable(!loggedIn);
        subMenuCustomer.setDisable(!loggedIn);

        tabLogin.setDisable(loggedIn); // Disabled sine we have logged in

        Core.getInstance().getControlHandler().getMainButtonsRightController().updateCashBox();
        Core.getInstance().getControlHandler().getMainPaneLeftController().activateTab(MainPaneLeftController.TabType.ACTIVE);

        Core.getInstance().getControlHandler().getMainCenterController().loadCenterAnchs();

        if (!loggedIn)
        {
            tabCashier.setDisable(true);
            tabCustomer.setDisable(true); // This will be enable after we finished PILOT
            tabStatistics.setDisable(true); // This will be enable after we finished PILOT
            tabReport.setDisable(true); // This will be enable after we finished PILOT
            return;
        }

        switch (level)
        {

            case ADMIN:
                tabCashier.setDisable(false);
                tabCustomer.setDisable(false); // This will be enable after we finished PILOT
                tabStatistics.setDisable(false); // This will be enable after we finished PILOT
                tabReport.setDisable(false); // This will be enable after we finished PILOT
                break;
            case DEVELOPER:
                tabCashier.setDisable(false);
                tabCustomer.setDisable(false); // This will be enable after we finished PILOT
                tabStatistics.setDisable(false); // This will be enable after we finished PILOT
                tabReport.setDisable(false); // This will be enable after we finished PILOT
                break;
            case CONTROLLER:
                tabCashier.setDisable(false);
                tabCustomer.setDisable(false); // This will be enable after we finished PILOT
                tabStatistics.setDisable(true); // This will be enable after we finished PILOT
                tabReport.setDisable(false); // This will be enable after we finished PILOT
                break;
            case USER:
                tabCashier.setDisable(false);
                tabCustomer.setDisable(true); // This will be enable after we finished PILOT
                tabStatistics.setDisable(true); // This will be enable after we finished PILOT
                tabReport.setDisable(false); // This will be enable after we finished PILOT
                break;
        };
    }

    private void loadExchange() throws IOException, InterruptedException
    {
        UserModel user = Core.getInstance().getLoginHandler().getUser();
        ExchangeDAO exchangeDAO = new ExchangeDAO();
        int userValue = exchangeDAO.isUserAlreadyAdded(user);

        if (userValue == 0)
        {
            Core.getInstance().getLog().log("Laddar växel-dialogen...", Log.LogLevel.NORMAL);

            DialogHandler dialogHandler = new DialogHandler();
            String output = dialogHandler.input(Core.getInstance().getControlHandler().getMainController().getScene(), "Växelkassa", "Registrera växelkassa:", "", "SPARA");

            user.setExchange(General.getInstance().str2Float(output, 2));
            exchangeDAO.add(user, General.getInstance().str2Int(output));
        } else
        {
            user.setExchange(userValue);
        }
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
    
    public void checkOngoingReceipt()
    {
        ReceiptDAO receiptDAO = new ReceiptDAO();
        ObservableList<Receipt> receiptOngoing = receiptDAO.getReceipts(ReceiptType.ONGOING);
        
        if (receiptOngoing.size() > 0)
        {
            receiptOngoing.get(0).park();
        }
    }

    @FXML
    private void OnUpdate(ActionEvent event) throws URISyntaxException, JRException
    {
        Core.getInstance().getControlHandler().refreshAllCSS();
        Core.getInstance().getControlHandler().refreshAllGUI();
    }

    @FXML
    private void OnLocalSettings(ActionEvent event) throws IOException
    {
        Core.getInstance().getLog().log("Laddar inställningar...", Log.LogLevel.NORMAL);

        FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlSettings());
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);

        scene.getStylesheets().add(ResourcesHandler.getInstance().getThemeSettings());

        Stage stage = new Stage();

        stage.setTitle("ALLMÄNNA INSTÄLLNINGAR");
        stage.setScene(scene);
        stage.setMaximized(true);

        stage.show();
    }

    /*
    private void OnAlt_payment_button(ActionEvent event) throws IOException {

        Core.getInstance().getLog().log("Väljer betalningsalternativ...", Log.LogLevel.NORMAL);

        FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlAltPayment());
        Parent root = (Parent) loader.load();

        Scene logScene = new Scene(root);

        logScene.getStylesheets().add(ResourcesHandler.getInstance().getThemeMain());

        Stage setupDBStage = new Stage();

        setupDBStage.setTitle("ALTERNATIVA BETALNINGSSÄTT");
        setupDBStage.setScene(logScene);

        setupDBStage.show();
    }
     */
    @FXML
    private void OnLangSwe(ActionEvent event)
    {
        Language.getInstance().changeLanguage(LanguageType.SWEDISH);
        langEng.setSelected(false);

        Core.getInstance().getControlHandler().refreshAllGUI();
    }

    @FXML
    private void OnLangEng(ActionEvent event)
    {
        Language.getInstance().changeLanguage(LanguageType.ENGLISH);
        langSwe.setSelected(false);

        Core.getInstance().getControlHandler().refreshAllGUI();
    }

    @FXML
    private void OnPrintXReport(ActionEvent event) throws IOException
    {
        DaylieReportZDataSource dataSource = new DaylieReportZDataSource();

        FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlDaylieReport());
        Parent root = (Parent) loader.load();

        JRViewerFxController jRViewerFxController = loader.getController();
        try
        {
            jRViewerFxController.setJasperPrint(new DailyReportBuilder(dataSource).build().toJasperPrint());

        } catch (Exception ex)
        {
            Core.getInstance().getLog().log("Kundfe inte skruiva ut X-rapport", Log.LogLevel.CRITICAL);
        }

        jRViewerFxController.show();
        Scene LogDaylieReport = new Scene(root);
        Stage Stage = new Stage();
        Stage.setTitle("Kassarapporter");
        Stage.setScene(LogDaylieReport);

        Stage.show();
        updateQuickReport();
    }

    @FXML
    private void OnPrintZReport(ActionEvent event) throws IOException
    {
        Core.getInstance().getLog().log("NOT YET IMPLEMENTED - y y y y", Log.LogLevel.CRITICAL);

        ExchangeDAO exchangeDAO = new ExchangeDAO();
        exchangeDAO.clear();
    }

    @FXML
    public void OnLogin(ActionEvent event) throws SQLException, IOException, InterruptedException
    {
        Database.DatabaseStatusInfo info = Core.getInstance().getLoginHandler().login(txtUserField.getText(), txtPasswordField.getText());

        switch (info)
        {
            case NORMAL:

                checkOngoingReceipt();
                
                Core.getInstance().getTerminalHandler().loadTerminalStatus(); // Laddar autoconnected terminal
                Core.getInstance().getComHandler().loadComStatus(); // Laddar in alla COM-units med autoconnect 
                Core.getInstance().getPrinterHandler().loadPrinterStatus(); // Laddar in alla sparade skrivare

                loadExchange();

                UserModel user = Core.getInstance().getLoginHandler().getUser();
                user.newReceipt();
                Core.getInstance().getControlHandler().getMainInfoPaneController().refresh();

                //txtRecieptNr.setText(General.getInstance().int2Str(user.getCurrentReceipt().getNr()));
                //mainCenterController.workController.createArticlesCategories();
                enableGUI(user.getSecurityLevel(), true);

                tabMain.getSelectionModel().select(tabCashier);

                Core.getInstance().getScreenHandler().getIdle().hide();
                Core.getInstance().getScreenHandler().getCustomer().show();

                break;
            case PASS_USER_WRONG:
            case DATABASE_DEFAULT:
            case DATABASE_NOT_CONNECTED:
            case ERROR:
                txtLoginInfo.setText(Database.getInstance().databaseInfo(info));
                break;
        }
    }

    @FXML
    private void OnOpenLog(ActionEvent event)
    {
        if (!Core.getInstance().getLog().isShowing())
        {
            Core.getInstance().getLog().show();
        } else
        {
            Core.getInstance().getLog().hide();
        }
    }

    @FXML
    private void OnOpenScreenCustomer(ActionEvent event)
    {
        if (!Core.getInstance().getScreenHandler().getCustomer().isShowing())
        {
            Core.getInstance().getScreenHandler().getCustomer().show();
        } else
        {
            Core.getInstance().getScreenHandler().getCustomer().hide();
        }
    }

    @FXML
    private void OnOpenIdle(ActionEvent event)
    {
        if (!Core.getInstance().getScreenHandler().getIdle().isShowing())
        {
            Core.getInstance().getScreenHandler().getIdle().show();
        } else
        {
            Core.getInstance().getScreenHandler().getIdle().hide();
        }
    }

    @FXML
    private void OnConnectDB(ActionEvent event)
    {
        if (Database.getInstance().isConnected())
        {
            if (Core.getInstance().getLoginHandler().isLoggedIn())
            {
                Core.getInstance().getLog().log("Kan inte koppla från databasen när du är inloggad!!", Log.LogLevel.CRITICAL);
                connectDB.setSelected(true);
            } else
            {
                Database.getInstance().disconnect();
                menuSystem.setDisable(true);
            }
        } else
        {
            //Database.getInstance().setup("root", "hemmahemma", Database.DatabaseType.H2);
            Database.getInstance().setup("root", "hemmahemma", Database.DatabaseType.H2_SERVER);
            //DatabaseObj.Setup("admin", "fonster71", Database.DatabaseType.FILEMAKER);
            //Database.getInstance().setup("root", "hemmahemma", Database.DatabaseType.MYSSQL);
            //Database.getInstance().setup("root", "hemmahemma", Database.DatabaseType.MYSSQL_LITE);
            Database.getInstance().connect();
        }

        connectDB.setSelected(Database.getInstance().isConnected());
    }

    @FXML
    private void OnAbout(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlAbout());
        Parent root = (Parent) loader.load();

        Scene scene = new Scene(root);
        Stage Stage = new Stage();

        Stage.setTitle("Om Officea");
        Stage.setScene(scene);
        Stage.initStyle(StageStyle.UNDECORATED);
        Stage.show();

    }

    @FXML
    private void OnTabStatistics_sales(Event event)
    {
    }

}
