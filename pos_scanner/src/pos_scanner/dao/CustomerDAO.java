/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pos_scanner.CurrentCustomerModel;
import pos_scanner.CustomerModel;
import pos_scanner.db.Database;

public class CustomerDAO extends DAO
{

    public final String TABLE = "customer";

    private final String COL_FIRSTNAME = "Firstname";
    private final String COL_SURNAME = "Surname";
    private final String COL_PHONE = "Phone";
    private final String COL_CARD = "Card";
    private final String COL_EMAIL = "Email";
    private final String COL_SMSCONTACT = "Sms_contact";
    private final String COL_EMAILCONTACT = "Email_contact";

    List<String> tableCols = Arrays.asList(COL_FIRSTNAME, COL_SURNAME, COL_PHONE, COL_CARD, COL_EMAIL, COL_SMSCONTACT, COL_EMAILCONTACT);
    
    ScansDAO scansDAO = null;
    
    public CustomerDAO(Database db)
    {
        this.db = db;
        scansDAO = new ScansDAO(db);
    }

    public List<String> getColumnNames()
    {
        List<String> colNames = new ArrayList<String>();
        colNames.addAll(mainCols);
        colNames.addAll(tableCols);
                
        return colNames;
    }
        
    public ObservableList<CurrentCustomerModel> getCurrentCustomer(String cardNumber)
    {
        ScansDAO scansDAO = new ScansDAO(db);
        return scansDAO.getScansCustomer(cardNumber);
    }

    public ObservableList<CustomerModel> getCustomers(String searchWord)
    {
        ObservableList<CustomerModel> customers = FXCollections.observableArrayList();

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT * FROM " + TABLE + " WHERE " + COL_FIRSTNAME + " LIKE '%" + searchWord + "%' OR " + COL_SURNAME + " LIKE '%" + searchWord + "%' OR " + COL_PHONE + " LIKE '%" + searchWord + "%' OR " + COL_CARD + " LIKE '%" + searchWord + "%' OR " + COL_EMAIL + " LIKE '%" + searchWord + "%'";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();
                    while (set.next())
                    {
                        int id = set.getInt(COL_ID);
                        String firstname = set.getString(COL_FIRSTNAME);
                        String surname = set.getString(COL_SURNAME);
                        String phone = set.getString(COL_PHONE);
                        String card = set.getString(COL_CARD);
                        String email = set.getString(COL_EMAIL);
                        SyncStatus syncStatus = SyncStatus.values()[set.getInt(COL_SYNC)];

                        CustomerModel customer = new CustomerModel(firstname, surname, phone, card, email);
                        customer.setScans(scansDAO.getScans(card, "", ""));
                        customer.setId(id);
                        customer.setSyncStatus(syncStatus);

                        customers.add(customer);
                    }
                } catch (SQLException ex)
                {
                    log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
                }
            }
        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), TABLE);
        }

        return customers;
    }

    public ObservableList<CustomerModel> getCustomersTopList(String dateFrom, String dateTo)
    {
        ObservableList<CustomerModel> customers = FXCollections.observableArrayList();

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT * FROM " + TABLE;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();
                    while (set.next())
                    {
                        int id = set.getInt(COL_ID);
                        String firstname = set.getString(COL_FIRSTNAME);
                        String surname = set.getString(COL_SURNAME);
                        String phone = set.getString(COL_PHONE);
                        String card = set.getString(COL_CARD);
                        String email = set.getString(COL_EMAIL);
                        SyncStatus syncStatus = SyncStatus.values()[set.getInt(COL_SYNC)];

                        int scans = scansDAO.getScans(card, dateFrom, dateTo);
                        if (scans > 0)
                        {
                            CustomerModel customer = new CustomerModel(firstname, surname, phone, card, email);
                            customer.setScans(scans);
                            customer.setId(id);
                            customer.setSyncStatus(syncStatus);
                            
                            customers.add(customer);
                        }
                    }
                } catch (SQLException ex)
                {
                    log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
                }
            }
        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), TABLE);
        }

        return customers;
    }

    public CustomerModel getCustomer(String cardNumber)
    {
        CustomerModel customer = null;

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT * FROM " + TABLE + " WHERE " + COL_CARD + " = '" + cardNumber + "'";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();
                    while (set.next())
                    {
                        int id = set.getInt(COL_ID);
                        String firstname = set.getString(COL_FIRSTNAME);
                        String surname = set.getString(COL_SURNAME);
                        String phone = set.getString(COL_PHONE);
                        String card = set.getString(COL_CARD);
                        String email = set.getString(COL_EMAIL);
                        SyncStatus syncStatus = SyncStatus.values()[set.getInt(COL_SYNC)];

                        customer = new CustomerModel(firstname, surname, phone, card, email);
                        customer.setScans(scansDAO.getScans(card, "", ""));
                        customer.setId(id);
                        customer.setSyncStatus(syncStatus);
                    }
                } catch (SQLException ex)
                {
                    log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
                }
            }
        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), TABLE);
        }

        return customer;
    }

    public String getCustomerFullName(String cardNumber)
    {
        CustomerModel customer = getCustomer(cardNumber);
        return customer.getFirstname() + " " + customer.getSurname();
    }

    public boolean update(CustomerModel customer)
    {
        if (db.isConnected() && db.containsTable(TABLE))
        {
            customer.setSyncStatus(SyncStatus.MODIFIED);
            
            String query = "UPDATE " + TABLE
                    + " SET"
                    + " " + COL_FIRSTNAME + " = '" + customer.getFirstname()
                    + "', " + COL_SURNAME + " = '" + customer.getSurname()
                    + "', " + COL_PHONE + " = '" + customer.getPhone()
                    + "', " + COL_CARD + " = '" + customer.getCard()
                    + "', " + COL_EMAIL + " = '" + customer.getEmail()
                    + "', " + COL_SMSCONTACT + " = '0"
                    + "', " + COL_EMAILCONTACT + " = '0"
                    + "', " + COL_SYNC + " = '" + customer.getSyncStatus().ordinal()
                    + "' WHERE ID = '" + customer.getId() + "'";

            return db.query(query, true);

        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), TABLE);
        }

        return false;
    }

    public boolean insert(CustomerModel customer)
    {
        if (db.isConnected() && db.containsTable(TABLE))
        {
            CustomerModel customerCheck = getCustomer(customer.getCard());
            if (customerCheck == null)
            {
                customer.setSyncStatus(SyncStatus.ADDED);

                String query = "INSERT INTO " + TABLE + " (" + COL_FIRSTNAME + ", " + COL_SURNAME + ", " + COL_PHONE + ", " + COL_CARD + ", " + COL_EMAIL + ", " + COL_SMSCONTACT + ", " + COL_EMAILCONTACT + ", " + COL_SYNC + ")"
                        + " VALUES"
                        + "( "
                        + "'" + customer.getFirstname() + "',"
                        + "'" + customer.getSurname() + "',"
                        + "'" + customer.getPhone() + "',"
                        + "'" + customer.getCard() + "',"
                        + "'" + customer.getEmail() + "',"
                        + "'0'" + ","
                        + "'0'" + ","
                        + "'" + customer.getSyncStatus().ordinal() + "'"
                        + ");";

                return db.query(query, true);
            }
            else
                log.outCustomerAlreadyExists(Thread.currentThread().getStackTrace()[1].getMethodName());

        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), TABLE);
        }

        return false;
    }
}
