package pos_scanner.fxml.main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import pos_scanner.CurrentCustomerModel;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import pos_scanner.dao.CustomerDAO;
import pos_scanner.dao.DAO;
import pos_scanner.dao.ScansDAO;
import pos_scanner.dao.ShopDAO;
import pos_scanner.CustomerModel;
import pos_scanner.common.Utils;
import pos_scanner.db.Database;
import pos_scanner.db.DatabaseHandler;
import static pos_scanner.common.Converter.*;
import static pos_scanner.common.General.*;

public class MainController implements Initializable
{

    @FXML
    private AnchorPane mainAnchor;
    @FXML
    private TextField txtScannedNr;
    @FXML
    private Label lblCount;
    @FXML
    private Tab tabScanner;
    @FXML
    private Label lblMessage;
    @FXML
    private Label lblCountToday;
    @FXML
    private Label lblScannedCard;
    @FXML
    private Label lblScannedUser;
    @FXML
    private BarChart<String, Number> chrtYear;
    @FXML
    private LineChart<String, Number> chrtMonth;
    @FXML
    private LineChart<String, Number> chrtWeek;
    @FXML
    private Tab tabStatistics;
    @FXML
    private Label lblCountCustomerTotal;
    @FXML
    private TableView<CurrentCustomerModel> tblCurrentCustomer;
    @FXML
    private TableColumn<CurrentCustomerModel, String> colCurrentCustomerDate;
    @FXML
    private TableColumn<CurrentCustomerModel, String> colCurrentCustomerTime;
    @FXML
    private TableColumn<CurrentCustomerModel, String> colTimeSinceToday;
    @FXML
    private TableView<CustomerModel> tblCustomerTopList;
    @FXML
    private TableColumn<CustomerModel, String> colTopListName;
    @FXML
    private TableColumn<CustomerModel, Integer> colTopListNumber;
    @FXML
    private TableColumn<CustomerModel, Integer> colTopListCount;
    @FXML
    private TableView<CustomerModel> tblCustomers;
    @FXML
    private TableColumn<CustomerModel, String> colCard;
    @FXML
    private TableColumn<CustomerModel, String> colForeName;
    @FXML
    private TableColumn<CustomerModel, String> colSurName;
    @FXML
    private TableColumn<CustomerModel, String> colEmail;
    @FXML
    private TableColumn<CustomerModel, String> colPhone;
    @FXML
    private TableColumn<CustomerModel, Integer> colCount;
    @FXML
    private TableColumn<CustomerModel, String> colAcceptSms;
    @FXML
    private TableColumn<CustomerModel, String> colAcceptEmail;
    @FXML
    private Tab tabCustomers;
    @FXML
    private Label lblCountCustomers;
    @FXML
    private TextField txtCard;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtSearch;
    @FXML
    private TextField txtForeName;
    @FXML
    private TextField txtSurName;
    @FXML
    private CheckBox chkSMS;
    @FXML
    private CheckBox chkEmail;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnSendEmail;
    @FXML
    private Button btnSendSMS;
    @FXML
    private Text txtLocalDb;
    @FXML
    private Text txtOnlineDb;
    @FXML
    private CheckBox chkNewCustomer;
    @FXML
    private Label lblNewCustomer;
    @FXML
    private AnchorPane tabScannerAnchor;
    @FXML
    private Button btnNewCustomer;
    @FXML
    private TextField txtCardFront;
    @FXML
    private TextField txtForeNameFront;
    @FXML
    private HBox logoback;
    @FXML
    private FlowPane fixedBottom;
    @FXML
    private FlowPane flowpaneNewCustomer;
    @FXML
    private Label lblNykund;
    @FXML
    private ImageView imgSnabbregistrering;
    @FXML
    private DatePicker dateStatisticStart;
    @FXML
    private DatePicker dateStatisticEnd;
    @FXML
    private TableColumn<CustomerModel, DAO.SyncStatus> colSync;
    @FXML
    private Button btnSync;
    @FXML
    private Button btnRegister;
    @FXML
    private Text txtRegistered;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        init();
        status(false);
        autoupdate();
    }

    CustomerDAO customerDAO = null;
    ScansDAO scansDAO = null;
    ShopDAO shopDAO = null;

    Database db = null;

    private void init()
    {
        initCurrentCustomerTable();
        initCustomerTable();
        initCustomerToplistTable();

        db = DatabaseHandler.getInstance().getLocal(DatabaseHandler.Project.SCANNER);

        customerDAO = new CustomerDAO(db);
        scansDAO = new ScansDAO(db);
        shopDAO = new ShopDAO(db);

        /*
        loadStatisticsYear();
        loadStatisticsMonth();
        loadStatisticsWeek();
         */
        populateCustomersTable(true);
        populateCustomersToplistTable(true, date(), date());

        lblCountToday.setText(Integer.toString(scansDAO.getScans("", date(), date())));
        txtForeNameFront.setVisible(false);
        txtCardFront.setVisible(false);
        lblNykund.setVisible(false);
        imgSnabbregistrering.setVisible(true);
    }

    private void statusCheck(Database db, Text txtField, boolean skipTxt)
    {
        txtField.setFill(DISCONNECTED);
        if (db.isConnected())
        {
            txtField.setFill(CONNECTED);
            if (!skipTxt)
            {
                String txt = txtField.getText();
                txtField.setText(txt + " (" + db.getDbName() + ")");
            }
        }
    }

    private void registerCheck(Database db, Text txtField, boolean skipTxt)
    {
        txtField.setFill(DISCONNECTED);
        if (shopDAO.isRegistered(db))
        {
            txtField.setFill(CONNECTED);
            if (!skipTxt)
            {
                txtField.setText("Registrerad");
                btnRegister.disableProperty().setValue(Boolean.TRUE);
            }
        }
    }

    private void status(boolean skipTxt)
    {
        txtCard.setDisable(true);
        statusCheck(DatabaseHandler.getInstance().getLocal(DatabaseHandler.Project.SCANNER), txtLocalDb, skipTxt);
        statusCheck(DatabaseHandler.getInstance().getOnline(DatabaseHandler.Project.SCANNER), txtOnlineDb, skipTxt);
        registerCheck(DatabaseHandler.getInstance().getOnline(DatabaseHandler.Project.SCANNER), txtRegistered, false);
    }

    public void refresh()
    {
        clear();
        populateCustomersTable(true);
    }

    private void clear()
    {
        tblCustomers.getItems().clear();
        txtCard.clear();
        txtForeName.clear();
        txtSurName.clear();
        txtPhone.clear();
        txtEmail.clear();
        chkEmail.setSelected(false);
        chkSMS.setSelected(false);

        //chkEmail
        //chkSMS
    }

    private void addListenerCustomerTableNewCustomer()
    {
        chkNewCustomer.selectedProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                if (chkNewCustomer.isSelected())
                {
                    txtCard.disableProperty().setValue(Boolean.FALSE);
                    txtCard.clear();
                    txtForeName.clear();
                    txtSurName.clear();
                    txtPhone.clear();
                    txtEmail.clear();
                    chkEmail.setSelected(false);
                    chkSMS.setSelected(false);

                    btnUpdate.setText("Spara");
                    txtCard.requestFocus();
                    tblCustomers.getSelectionModel().clearSelection();
                    tblCustomers.disableProperty().setValue(Boolean.TRUE);
                    btnUpdate.setDisable(false);
                } else
                {
                    txtCard.disableProperty().setValue(Boolean.TRUE);
                    tblCustomers.disableProperty().setValue(Boolean.FALSE);

                    btnUpdate.setText("Uppdatera");
                    lblNewCustomer.setText("");
                }
            }
        });
    }

    private void initCurrentCustomerTable()
    {
        colCurrentCustomerDate.setCellValueFactory(new PropertyValueFactory<CurrentCustomerModel, String>("date"));
        colCurrentCustomerTime.setCellValueFactory(new PropertyValueFactory<CurrentCustomerModel, String>("time"));
        colTimeSinceToday.setCellValueFactory(new PropertyValueFactory<CurrentCustomerModel, String>("DateSinceToday"));

        colCurrentCustomerDate.prefWidthProperty().bind(tblCurrentCustomer.widthProperty().divide(3)); // w * 1/4
        colCurrentCustomerTime.prefWidthProperty().bind(tblCurrentCustomer.widthProperty().divide(3)); // w * 2/4
        colTimeSinceToday.prefWidthProperty().bind(tblCurrentCustomer.widthProperty().divide(3)); // w * 1/4

        addListenerCustomerTableNewCustomer();
    }

    private void populateCurrentCustomer()
    {
        tblCurrentCustomer.setItems(customerDAO.getCurrentCustomer(txtScannedNr.getText()));
        updateTable(tblCurrentCustomer);
    }

    private void addListenerCustomerTable()
    {
        tblCustomers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                CustomerModel selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
                if (selectedCustomer != null)
                {
                    txtCard.setText(selectedCustomer.getCard());
                    txtForeName.setText(selectedCustomer.getFirstname());
                    txtSurName.setText(selectedCustomer.getSurname());
                    txtPhone.setText(selectedCustomer.getPhone());
                    txtEmail.setText(selectedCustomer.getEmail());
                    btnUpdate.setDisable(false);
                }
            }
        });
    }

    private void initCustomerTable()
    {
        colForeName.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("firstname"));
        colSurName.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("surname"));
        colPhone.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("phone"));
        colCard.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("card"));
        colEmail.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("email"));
        colCount.setCellValueFactory(new PropertyValueFactory<CustomerModel, Integer>("scans"));
        colAcceptEmail.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("acceptEmail"));
        colAcceptSms.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("acceptSms"));
        colSync.setCellValueFactory(new PropertyValueFactory<CustomerModel, DAO.SyncStatus>("SyncStatus"));

        addListenerCustomerTable();
    }

    ObservableList<CustomerModel> customers = FXCollections.observableArrayList();

    private void populateCustomersTable(boolean force)
    {
        if (customers.isEmpty() || force)
        {
            customers = customerDAO.getCustomers(txtSearch.getText());
            tblCustomers.setItems(customers);
            lblCountCustomers.setText(Integer.toString(tblCustomers.getItems().size()));
            updateTable(tblCustomers);
        }
    }

    private void initCustomerToplistTable()
    {
        colTopListName.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("firstname"));
        colTopListNumber.setCellValueFactory(new PropertyValueFactory<CustomerModel, Integer>("card"));
        colTopListCount.setCellValueFactory(new PropertyValueFactory<CustomerModel, Integer>("scans"));

        LocalDate date = LocalDate.now();
        dateStatisticEnd.setValue(date);
        dateStatisticStart.setValue(date);
    }

    ObservableList<CustomerModel> customersToplist = FXCollections.observableArrayList();

    private void populateCustomersToplistTable(boolean force, String dateFrom, String dateTo)
    {
        if (customersToplist.isEmpty() || force)
        {
            customersToplist = customerDAO.getCustomersTopList(dateFrom, dateTo);
            tblCustomerTopList.setItems(customersToplist);
            updateTable(tblCustomerTopList);
        }
    }

    @FXML
    private void OnScannedNr(ActionEvent event)
    {
        String scannedNr = txtScannedNr.getText();

        CustomerModel customer = customerDAO.getCustomer(scannedNr);
        if (customer != null /*&& !customer.getFirstname().isEmpty()*/)
        {
            lblScannedUser.setText(customer.getFirstname());
            lblScannedCard.setText(scannedNr);

            if (scansDAO.insert(scannedNr))
            {
                int scans = scansDAO.getScans(scannedNr, "", "");

                boolean freeLunch = (scans % 10 == 0 && scans != 0);

                lblMessage.setText(freeLunch ? "Gratis LUNCH!" : "");

                String update = freeLunch ? "10" : Integer.toString(scans % 10);
                lblCount.setText(update);
            }
        } else
        {
            lblCount.setText("-");
            lblMessage.setText("Hittade inte kundnumret!");
            lblScannedCard.setText("Inget kort scannat");
            lblScannedUser.setText("-");
        }

        populateCurrentCustomer();

        lblCountToday.setText(Integer.toString(scansDAO.getScans("", date(), date())));
        lblCountCustomerTotal.setText("Antal besök totalt: " + Integer.toString(scansDAO.getScans(scannedNr, "", "")));
        txtScannedNr.clear();
        lblNykund.setVisible(false);
        txtScannedNr.requestFocus();
        populateCustomersTable(true);
    }

    @FXML
    private void OnBtnSendEmail(ActionEvent event)
    {
    }

    @FXML
    private void OnBtnSendSMS(ActionEvent event)
    {
    }

    @FXML
    private void OnReleasedTxtSearch(KeyEvent event)
    {
        customers = customerDAO.getCustomers(txtSearch.getText());
        tblCustomers.setItems(customers);
        lblCountCustomers.setText(int2Str(customers.size()));
        updateTable(tblCustomers);
    }

    @FXML
    private void OnTabScanner(Event event)
    {
        Platform.runLater(() -> txtScannedNr.requestFocus());
    }

    @FXML
    private void OnTabCustomers(Event event)
    {
        populateCustomersTable(false);
    }

    @FXML
    private void OnTabLoadCharts(Event event)
    {
        populateCustomersToplistTable(true, dateStatisticStart.getValue().toString(), dateStatisticEnd.getValue().toString());

        chrtWeek.getData().clear();
        chrtMonth.getData().clear();
        chrtYear.getData().clear();

        for (String year : scansDAO.getYears())
        {
            XYChart.Series<String, Number> weekDays = new XYChart.Series();
            weekDays.setName(year);
            weekDays.getData().add(new XYChart.Data<String, Number>("Mån", scansDAO.getScansDay(DayName.MONDAY, year)));
            weekDays.getData().add(new XYChart.Data<String, Number>("Tis", scansDAO.getScansDay(DayName.TUESDAY, year)));
            weekDays.getData().add(new XYChart.Data<String, Number>("Ons", scansDAO.getScansDay(DayName.WEDNESDAY, year)));
            weekDays.getData().add(new XYChart.Data<String, Number>("Tor", scansDAO.getScansDay(DayName.THURSDAY, year)));
            weekDays.getData().add(new XYChart.Data<String, Number>("Fre", scansDAO.getScansDay(DayName.FRIDAY, year)));
            weekDays.getData().add(new XYChart.Data<String, Number>("Lör", scansDAO.getScansDay(DayName.SATURDAY, year)));
            weekDays.getData().add(new XYChart.Data<String, Number>("Sön", scansDAO.getScansDay(DayName.SUNDAY, year)));

            XYChart.Series<String, Number> months = new XYChart.Series();
            months.setName(year);
            months.getData().add(new XYChart.Data("Januari", scansDAO.getScansMonth(MonthName.JANUARY, year)));
            months.getData().add(new XYChart.Data("Februari", scansDAO.getScansMonth(MonthName.FEBRUARY, year)));
            months.getData().add(new XYChart.Data("Mars", scansDAO.getScansMonth(MonthName.MARCH, year)));
            months.getData().add(new XYChart.Data("April", scansDAO.getScansMonth(MonthName.MAY, year)));
            months.getData().add(new XYChart.Data("Maj", scansDAO.getScansMonth(MonthName.APRIL, year)));
            months.getData().add(new XYChart.Data("Juni", scansDAO.getScansMonth(MonthName.JUNE, year)));
            months.getData().add(new XYChart.Data("Juli", scansDAO.getScansMonth(MonthName.JULY, year)));
            months.getData().add(new XYChart.Data("Augusti", scansDAO.getScansMonth(MonthName.AUGUST, year)));
            months.getData().add(new XYChart.Data("September", scansDAO.getScansMonth(MonthName.SEPTEMBER, year)));
            months.getData().add(new XYChart.Data("Oktober", scansDAO.getScansMonth(MonthName.OCTOBER, year)));
            months.getData().add(new XYChart.Data("November", scansDAO.getScansMonth(MonthName.NOVEMBER, year)));
            months.getData().add(new XYChart.Data("December", scansDAO.getScansMonth(MonthName.DECEMBER, year)));

            XYChart.Series<String, Number> years = new XYChart.Series();
            years.setName(year);
            years.getData().add(new XYChart.Data("", scansDAO.getScansYear(str2Int(year))));

            chrtYear.getData().add(years);
            chrtWeek.getData().add(weekDays);
            chrtMonth.getData().add(months);

            for (XYChart.Data<String, Number> data : years.getData())
            {
                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        String value = months.getName() + " " + data.getXValue().toString() + ": "  + data.getYValue().toString();
                        Tooltip.install(data.getNode(), new Tooltip(value));
                    }
                });
            }

            for (XYChart.Data<String, Number> data : weekDays.getData())
            {
                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        String value = months.getName() + " " + data.getXValue().toString() + ": "  + data.getYValue().toString();
                        Tooltip.install(data.getNode(), new Tooltip(value));
                    }
                });
            }

            for (XYChart.Data<String, Number> data : months.getData())
            {
                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent event)
                    {
                        String value = months.getName() + " " + data.getXValue().toString() + ": "  + data.getYValue().toString();
                        Tooltip.install(data.getNode(), new Tooltip(value));
                    }
                });
            }
        }
    }

    @FXML
    private void OnBtnUpdate(ActionEvent event)
    {
        if (btnUpdate.getText().compareTo("Uppdatera") == 0)
        {
            CustomerModel selectedCustomer = tblCustomers.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null)
            {
                CustomerModel customer = new CustomerModel(txtForeName.getText(), txtSurName.getText(), txtPhone.getText(), txtCard.getText(), txtEmail.getText());
                customer.setId(selectedCustomer.getId());

                customerDAO.update(customer);
                refresh();
            }
        } else
        {
            CustomerModel newCustomer = new CustomerModel(txtForeName.getText(), txtSurName.getText(), txtPhone.getText(), txtCard.getText(), txtEmail.getText());
            newCustomer.setSyncStatus(DAO.SyncStatus.ADDED);
            if (customerDAO.insert(newCustomer))
            {
                lblNewCustomer.setText("Ny användare registrerad");
                chkNewCustomer.setSelected(false);
                refresh();
            } else
            {
                lblNewCustomer.setText("Kort redan registrerat");
                chkNewCustomer.setSelected(false);
                txtCard.clear();
                txtForeName.clear();
                txtSurName.clear();
                txtPhone.clear();
                txtEmail.clear();
                chkEmail.setSelected(false);
                chkSMS.setSelected(false);
            }
        }
    }

    @FXML
    private void OnRegisterSystem(ActionEvent event)
    {
        Utils utils = new Utils();

        /*
        String mac = utils.getMAC();
        String internal_ip = utils.getLocalIP();
        String external_ip = utils.getExternalIP();

        System.out.println("Current MAC address: " + mac);
        System.out.println("Current LOCAL IP address: " + internal_ip);
        System.out.println("Current EXTERNAL IP address: " + external_ip);
         */
        if (shopDAO.addRegistered(utils.getLocalIP(), utils.getExternalIP(), utils.getMAC()))
        {
            DatabaseHandler.getInstance().sync(DatabaseHandler.Project.SCANNER);
            populateCustomersTable(true);
            registerCheck(DatabaseHandler.getInstance().getOnline(DatabaseHandler.Project.SCANNER), txtRegistered, false);
        } else
        {
            System.out.println("Registered failed!");
        }
    }

    @FXML
    private void OnBtnNewCustomer(ActionEvent event)
    {
        if (btnNewCustomer.getText().compareTo("+") == 0)
        {
            btnNewCustomer.setText("-");
            txtForeNameFront.setVisible(true);
            txtCardFront.setVisible(true);
            lblNykund.setText("Ny kund:");
            lblNykund.setVisible(true);
            txtCardFront.requestFocus();
            imgSnabbregistrering.setVisible(false);
        } else
        {
            btnNewCustomer.setText("+");
            txtForeNameFront.setVisible(false);
            txtCardFront.setVisible(false);
            lblNykund.setVisible(false);
            txtScannedNr.requestFocus();
            imgSnabbregistrering.setVisible(true);
        }
    }

    @FXML
    private void OnFocusNext(ActionEvent event)
    {
        txtForeNameFront.requestFocus();
    }

    @FXML
    private void OnQuickRegister(ActionEvent event)
    {
        CustomerModel customer = new CustomerModel(txtForeNameFront.getText(), "", "", txtCardFront.getText(), "");
        if (customerDAO.insert(customer))
        {
            lblNykund.setText("Ny kund registrerad - Du kan nu scanna kortet med nummer: " + txtCardFront.getText());
            btnNewCustomer.setText("+");
            txtForeNameFront.setVisible(false);
            txtForeNameFront.clear();
            txtCardFront.setVisible(false);
            txtCardFront.clear();
            lblNykund.setVisible(true);
            txtScannedNr.requestFocus();
            imgSnabbregistrering.setVisible(true);
            refresh();
        } else
        {
            lblNykund.setText("Fel vid registrering - Prova igen");
            btnNewCustomer.setText("+");
            txtForeNameFront.setVisible(false);
            txtForeNameFront.clear();
            txtCardFront.setVisible(false);
            txtCardFront.clear();
            lblNykund.setVisible(true);
            txtScannedNr.requestFocus();
            imgSnabbregistrering.setVisible(true);
        }
    }

    @FXML
    private void OnBtnSyncronize(ActionEvent event)
    {
        DatabaseHandler.getInstance().sync(DatabaseHandler.Project.SCANNER);
        populateCustomersTable(true);
    }

    @FXML
    private void OnDateStatisticStart(ActionEvent event)
    {
        if (dateStatisticStart.getValue().isAfter(dateStatisticEnd.getValue()))
        {
            dateStatisticStart.setValue(dateStatisticEnd.getValue());
        }

        populateCustomersToplistTable(true, dateStatisticStart.getValue().toString(), dateStatisticEnd.getValue().toString());
    }

    @FXML
    private void OnDateStatisticEnd(ActionEvent event)
    {
        if (dateStatisticStart.getValue().isAfter(dateStatisticEnd.getValue()))
        {
            dateStatisticStart.setValue(dateStatisticEnd.getValue());
        }

        populateCustomersToplistTable(true, dateStatisticStart.getValue().toString(), dateStatisticEnd.getValue().toString());
    }

    /*
    private void loadStatisticsYear()
    {
        cbxYears.getItems().clear();
        cbxYears.getItems().add("Alla år");
        for (String year : scansDAO.getYears())
        {
            cbxYears.getItems().add(year);
        }
        
        cbxYears.setValue("Alla år");
    }

    private void loadStatisticsMonth()
    {
        cbxMonth.getItems().clear();
        cbxMonth.getItems().add("Alla månader");
        for (String month : scansDAO.getMonths())
        {
            cbxMonth.getItems().add(month);
        }
        
        cbxMonth.setValue("Alla månader");
    }

    private void loadStatisticsWeek()
    {
        cbxWeek.getItems().clear();
        cbxWeek.getItems().add("Alla veckor");
        for (String week : scansDAO.getWeeks())
        {
            cbxWeek.getItems().add(week);
        }
        
        cbxWeek.setValue("Alla veckor");
    }
     */
    private void autoupdate()
    {
        new Timer().schedule(
                new TimerTask()
        {

            @Override
            public void run()
            {
                System.out.println("Uppdaterar CurrentCustomer tabellen");
                tblCurrentCustomer.refresh();

                status(true);
            }
        }, 0, 60000);
    }

}
