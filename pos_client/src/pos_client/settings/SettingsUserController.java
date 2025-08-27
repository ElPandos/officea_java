/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pos_client.Employee;
import pos_client.common.General;
import pos_client.UserModel;
import pos_client.UserModel.SecurityLevel;
import pos_client.common.Core;
import pos_client.db.dao.EmployeeDAO;
import pos_client.db.dao.UserDAO;
import pos_client.windows.Log;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsUserController implements Initializable
{

    @FXML
    private TableView<UserModel> tblUsers;
    @FXML
    private Button btnUsers_update;
    @FXML
    private Button btnUsers_add;
    @FXML
    private Button btnUsers_delete;
    @FXML
    private TextField txtUsers_name;
    @FXML
    private TextField txtUsers_password;
    @FXML
    private ComboBox<String> cbxUsers_securitylevel;
    @FXML
    private ComboBox<String> cbxUsers_employee;
    @FXML
    private TableColumn<UserModel, Integer> colUsers_id;
    @FXML
    private TableColumn<UserModel, String> colUsers_name;
    @FXML
    private TableColumn<UserModel, String> colUsers_password;
    @FXML
    private TableColumn<UserModel, String> colUsers_securitylevel;
    @FXML
    private TableColumn<UserModel, Integer> colUsers_employee;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initTable();
        populate();
        status();
    }

    private void initTable()
    {
        colUsers_id.setCellValueFactory(new PropertyValueFactory<UserModel, Integer>("id"));
        colUsers_name.setCellValueFactory(new PropertyValueFactory<UserModel, String>("name"));
        colUsers_password.setCellValueFactory(new PropertyValueFactory<UserModel, String>("password"));
        colUsers_securitylevel.setCellValueFactory(new PropertyValueFactory<UserModel, String>("securityLevelStr"));
        colUsers_employee.setCellValueFactory(new PropertyValueFactory<UserModel, Integer>("employeeId"));

        addListenerTable();
    }

    public void refresh()
    {
        clear();
        status();
        populate();

        General.getInstance().updateColumn(tblUsers);
    }

    private void clear()
    {
        tblUsers.getItems().clear();

        txtUsers_name.clear();
        txtUsers_password.clear();
    }

    private void populate()
    {
        loadUsers();

        loadUsersSecuritylevelCbx();
        loadUsersEmployeesCbx();
    }

    private void status()
    {
        btnUsers_delete.disableProperty().set(true);
        btnUsers_update.disableProperty().set(true);
    }

    private void addListenerTable()
    {
        tblUsers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue)
            {
                btnUsers_delete.disableProperty().set(false);
                btnUsers_update.disableProperty().set(false);

                UserModel selectedUser = tblUsers.getSelectionModel().getSelectedItem();
                if (selectedUser != null)
                {
                    txtUsers_name.setText(selectedUser.getName());
                    txtUsers_password.setText(selectedUser.getPassword());
                    cbxUsers_securitylevel.setValue(selectedUser.getSecurityLevelStr());

                    EmployeeDAO employeeDAO = new EmployeeDAO();
                    cbxUsers_employee.setValue(employeeDAO.getEmployee(selectedUser.getEmployeeId()).getFullname());
                }
            }
        });
    }

    @FXML
    private void OnBtnUsers_update(ActionEvent event)
    {
        Core.getInstance().getLog().log("Uppdaterar vald användare i databasen", Log.LogLevel.NORMAL);

        UserModel updatedUser = getUserValuesFromInputFields();
        updatedUser.setId(tblUsers.getSelectionModel().getSelectedItem().getId());

        UserDAO userDAO = new UserDAO();
        userDAO.update(updatedUser);

        refresh();
    }

    @FXML
    private void OnBtnUsers_add(ActionEvent event)
    {

        Core.getInstance().getLog().log("Lägger till användare i databasen", Log.LogLevel.NORMAL);

        UserModel user = getUserValuesFromInputFields();

        UserDAO userDAO = new UserDAO();
        userDAO.add(user);

        refresh();
    }

    @FXML
    private void OnBtnUsers_delete(ActionEvent event) throws SQLException
    {

        UserDAO userDAO = new UserDAO();
        if (userDAO.remove(tblUsers.getSelectionModel().getSelectedItem().getId()))
        {
            Core.getInstance().getLog().log("Tog bort vald användare", Log.LogLevel.NORMAL);
        } else
        {
            Core.getInstance().getLog().log("Kunde inte ta bort användare", Log.LogLevel.CRITICAL);
        }

        refresh();
    }

    private UserModel getUserValuesFromInputFields()
    {
        Map<String, SecurityLevel> securityLevel = (Map) cbxUsers_securitylevel.getUserData();
        Map<String, Employee> employees = (Map) cbxUsers_employee.getUserData();

        UserModel user = new UserModel();

        user.setName(txtUsers_name.getText().toUpperCase());
        user.setPassword(txtUsers_password.getText().toUpperCase());
        user.setSecuritylevel(securityLevel.get(cbxUsers_securitylevel.getValue()));
        user.setEmployeeId(employees.get(cbxUsers_employee.getValue()).getId());

        return user;
    }

    private void loadUsers()
    {
        Core.getInstance().getLog().log("Laddar användare i inställningar", Log.LogLevel.DESCRIPTIVE);

        UserDAO userDAO = new UserDAO();
        tblUsers.setItems(userDAO.getUsers());

        General.getInstance().updateTable(tblUsers);
    }

    private void loadUsersEmployeesCbx()
    {
        Core.getInstance().getLog().log("Laddar anställda till comboboxen", Log.LogLevel.DESCRIPTIVE);

        cbxUsers_employee.getItems().clear();

        EmployeeDAO employeeDAO = new EmployeeDAO();
        ObservableList<Employee> employees = employeeDAO.getEmployees();

        Map<String, Employee> employeesData = new HashMap<>();
        for (Employee employee : employees)
        {
            cbxUsers_employee.getItems().add(employee.getFullname());
            employeesData.put(employee.getFullname(), employee);
        }

        cbxUsers_employee.setUserData(employeesData);
    }

    private void loadUsersSecuritylevelCbx()
    {
        Core.getInstance().getLog().log("Laddar säkerhetsnivå till comboboxen", Log.LogLevel.DESCRIPTIVE);

        cbxUsers_securitylevel.getItems().clear();

        UserDAO userDAO = new UserDAO();
        Map<String, SecurityLevel> securityLevel = userDAO.getSecuritylevel();

        for (Map.Entry<String, SecurityLevel> entry : securityLevel.entrySet())
        {
            cbxUsers_securitylevel.getItems().add(entry.getKey());
        }

        cbxUsers_securitylevel.setUserData(securityLevel);
    }
}
