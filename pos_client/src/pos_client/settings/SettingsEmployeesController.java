/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pos_client.common.General;
import pos_client.Employee;
import pos_client.common.Core;
import pos_client.db.dao.EmployeeDAO;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsEmployeesController implements Initializable
{

    @FXML
    private TableView<Employee> tblEmployees;
    @FXML
    private TableColumn<Employee, Integer> colEmployees_id;
    @FXML
    private TableColumn<Employee, String> colEmployees_firstname;
    @FXML
    private TableColumn<Employee, String> colEmployees_lastname;
    @FXML
    private TableColumn<Employee, String> colEmployees_address;
    @FXML
    private TableColumn<Employee, String> colEmployees_postalcode;
    @FXML
    private TableColumn<Employee, String> colEmployees_city;
    @FXML
    private TableColumn<Employee, String> colEmployees_phone;
    @FXML
    private TableColumn<Employee, String> colEmployees_mobile;
    @FXML
    private TableColumn<Employee, String> colEmployees_email;
    @FXML
    private TableColumn<Employee, String> colEmployees_gender;
    @FXML
    private TextField txtEmployees_firstname;
    @FXML
    private TextField txtEmployees_lastname;
    @FXML
    private TextField txtEmployees_postalcode;
    @FXML
    private TextField txtEmployees_phone;
    @FXML
    private TextField txtEmployees_mobile;
    @FXML
    private TextField txtEmployees_address;
    @FXML
    private TextField txtEmployees_city;
    @FXML
    private TextField txtEmployees_email;
    @FXML
    private ComboBox<String> cbxEmployees_gender;
    @FXML
    private Button btnEmployees_update;
    @FXML
    private Button btnEmployees_add;
    @FXML
    private Button btnEmployees_delete;
    @FXML
    private Button btnEmployees_makeuser;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        initTable();
        populate();
        status();
    }

    private void initTable()
    {

        colEmployees_id.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("id"));
        colEmployees_firstname.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstname"));
        colEmployees_lastname.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastname"));
        colEmployees_address.setCellValueFactory(new PropertyValueFactory<Employee, String>("adress"));
        colEmployees_postalcode.setCellValueFactory(new PropertyValueFactory<Employee, String>("postalcode"));
        colEmployees_city.setCellValueFactory(new PropertyValueFactory<Employee, String>("city"));
        colEmployees_phone.setCellValueFactory(new PropertyValueFactory<Employee, String>("phone"));
        colEmployees_mobile.setCellValueFactory(new PropertyValueFactory<Employee, String>("mobile"));
        colEmployees_email.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        colEmployees_gender.setCellValueFactory(new PropertyValueFactory<Employee, String>("gender"));

        addListenerTable();
    }

    public void refresh()
    {

        clear();
        status();
        populate();

        General.getInstance().updateColumn(tblEmployees);
    }

    private void clear()
    {

        tblEmployees.getItems().clear();

        cbxEmployees_gender.getItems().clear();

        txtEmployees_firstname.clear();
        txtEmployees_lastname.clear();
        txtEmployees_address.clear();
        txtEmployees_postalcode.clear();
        txtEmployees_city.clear();
        txtEmployees_phone.clear();
        txtEmployees_mobile.clear();
        txtEmployees_email.clear();

    }

    private void populate()
    {

        loadEmployees();

        loadEmployeeGenderCbx();
    }

    private void status()
    {

        btnEmployees_delete.disableProperty().set(true);
        btnEmployees_update.disableProperty().set(true);
    }

    private void addListenerTable()
    {

        tblEmployees.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {

                btnEmployees_delete.disableProperty().set(false);
                btnEmployees_update.disableProperty().set(false);

                Employee selectedEmployee = tblEmployees.getSelectionModel().getSelectedItem();
                if (selectedEmployee != null)
                {
                    txtEmployees_firstname.setText(selectedEmployee.getFirstname());
                    txtEmployees_lastname.setText(selectedEmployee.getLastname());
                    txtEmployees_address.setText(selectedEmployee.getAdress());
                    txtEmployees_postalcode.setText(selectedEmployee.getPostalcode());
                    txtEmployees_city.setText(selectedEmployee.getCity());
                    txtEmployees_phone.setText(selectedEmployee.getPhone());
                    txtEmployees_mobile.setText(selectedEmployee.getMobile());
                    txtEmployees_email.setText(selectedEmployee.getEmail());
                    cbxEmployees_gender.setValue(selectedEmployee.getGender());
                }
            }
        });
    }

    @FXML
    private void OnBtnEmployees_update(ActionEvent event)
    {

        Core.getInstance().getLog().log("Uppdaterar vald användare i databasen", Log.LogLevel.NORMAL);

        Employee updatedEmployee = getEmployeeValuesFromInputFields();
        updatedEmployee.setId(tblEmployees.getSelectionModel().getSelectedItem().getId());

        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.update(updatedEmployee);

        refresh();
    }

    @FXML
    private void OnBtnEmployees_add(ActionEvent event)
    {

        Core.getInstance().getLog().log("Lägger till anställd i databasen", Log.LogLevel.DESCRIPTIVE);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        employeeDAO.add(txtEmployees_firstname.getText(),
                txtEmployees_lastname.getText(),
                txtEmployees_address.getText(),
                txtEmployees_postalcode.getText(),
                txtEmployees_city.getText(),
                txtEmployees_phone.getText(),
                txtEmployees_mobile.getText(),
                txtEmployees_email.getText(),
                cbxEmployees_gender.getValue());

        refresh();
    }

    @FXML
    private void OnBtnEmployees_delete(ActionEvent event) throws SQLException
    {

        EmployeeDAO employeeDAO = new EmployeeDAO();
        if (employeeDAO.remove(tblEmployees.getSelectionModel().getSelectedItem().getId()))
        {
            Core.getInstance().getLog().log("Tog bort vald anställd", Log.LogLevel.NORMAL);
        } else
        {
            Core.getInstance().getLog().log("Kunde inte ta bort anställd", Log.LogLevel.CRITICAL);
        }

        refresh();
    }

    private Employee getEmployeeValuesFromInputFields()
    {

        Employee employee = new Employee();

        employee.setFirstname(txtEmployees_firstname.getText());
        employee.setLastname(txtEmployees_lastname.getText());
        employee.setAdress(txtEmployees_address.getText());
        employee.setPostalcode(txtEmployees_postalcode.getText());
        employee.setCity(txtEmployees_city.getText());
        employee.setPhone(txtEmployees_phone.getText());
        employee.setMobile(txtEmployees_mobile.getText());
        employee.setEmail(txtEmployees_email.getText());
        employee.setGender(cbxEmployees_gender.getValue());

        return employee;
    }

    private void loadEmployees()
    {

        Core.getInstance().getLog().log("Laddar anställda i inställningar", Log.LogLevel.DESCRIPTIVE);

        EmployeeDAO employeeDAO = new EmployeeDAO();
        tblEmployees.setItems(employeeDAO.getEmployees());

        General.getInstance().updateTable(tblEmployees);
    }

    private void loadEmployeeGenderCbx()
    {

        Core.getInstance().getLog().log("Laddar kön till comboboxen", Log.LogLevel.DESCRIPTIVE);

        cbxEmployees_gender.getItems().clear();

        EmployeeDAO employeeDAO = new EmployeeDAO();
        for (String gender : employeeDAO.getEmployeeGender())
        {
            cbxEmployees_gender.getItems().add(gender);
        }
    }
}
