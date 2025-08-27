/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pos_client.db.Database;
import pos_client.common.DefinedVariables;
import pos_client.Employee;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class EmployeeDAO extends DAO
{

    Database db = null;

    public EmployeeDAO()
    {

        db = Database.getInstance();
    }

    public ObservableList<Employee> getEmployees()
    {

        ObservableList<Employee> employees = FXCollections.observableArrayList();

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Hämtar anställda från databasen", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_EMPLOYEE;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();

                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_EMPLOYEE + "_id");
                        String firstname = set.getString("firstname");
                        String lastname = set.getString("lastname");
                        String adress = set.getString("adress");
                        String postalcode = set.getString("postalcode");
                        String city = set.getString("city");
                        String phone = set.getString("phone");
                        String mobile = set.getString("mobile");
                        String email = set.getString("email");
                        String gender = set.getString("gender");

                        employees.add(new Employee(id, firstname, lastname, adress, postalcode, city, phone, mobile, email, gender));
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getEmployees() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getEmployees() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getEmployees() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return employees;
    }

    public ArrayList<String> getEmployeeGender()
    {

        ArrayList<String> employeeGender = new ArrayList<>();

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Laddar anställdas kön", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT DISTINCT gender FROM " + DefinedVariables.getInstance().TABLE_EMPLOYEE;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();

                    while (set.next())
                    {
                        String value = set.getString("gender");
                        employeeGender.add(value);
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getEmployeeGender() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getEmployeeGender() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getEmployeeGender() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return employeeGender;
    }

    public void add(String firstName, String lastName, String adress, String postalCode, String city, String phone, String mobile, String email, String gender)
    {

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Lägger till en anstäld i databasen", Log.LogLevel.NORMAL);

            String query = "INSERT INTO " + DefinedVariables.TABLE_EMPLOYEE + " (firstname, lastname, adress, postalcode, city, phone, mobile, email, gender) VALUES"
                    + " ('" + firstName
                    + "', '" + lastName
                    + "', '" + adress
                    + "', '" + postalCode
                    + "', '" + city
                    + "', '" + phone
                    + "', '" + mobile
                    + "', '" + email
                    + "', '" + gender
                    + "');";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade anställd: " + firstName + " " + lastName, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("add() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("add() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public boolean remove(int id)
    {

        if (db.isConnected())
        {
            return db.deleteRow(DefinedVariables.getInstance().TABLE_EMPLOYEE, id);
        } else
        {
            Core.getInstance().getLog().log("remove() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
        return false;
    }

    public Employee getEmployee(int id)
    {

        ObservableList<Employee> employees = getEmployees();

        for (Employee employee : employees)
        {
            if (employee.getId() == id)
            {
                return employee;
            }
        }

        return null;
    }

    public boolean update(Employee updatedEmployee)
    {

        boolean result = true;

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Uppdaterar vald anställd " + updatedEmployee.getId() + " i db...", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_EMPLOYEE
                    + " SET firstname = '" + updatedEmployee.getFirstname()
                    + "', lastname = '" + updatedEmployee.getLastname()
                    + "', adress = '" + updatedEmployee.getAdress()
                    + "', postalcode = '" + updatedEmployee.getPostalcode()
                    + "', city = '" + updatedEmployee.getCity()
                    + "', phone = '" + updatedEmployee.getPhone()
                    + "', mobile = '" + updatedEmployee.getMobile()
                    + "', email = '" + updatedEmployee.getEmail()
                    + "', gender = '" + updatedEmployee.getGender()
                    + "' WHERE " + DefinedVariables.getInstance().TABLE_EMPLOYEE + "_id = '" + General.getInstance().int2Str(updatedEmployee.getId()) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade anställd: " + updatedEmployee.getFullname() + " med id: " + updatedEmployee.getId(), Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("update - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
                result = false;
            }
        } else
        {
            Core.getInstance().getLog().log("update - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
            result = false;
        }

        return result;
    }

}
