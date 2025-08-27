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
import pos_scanner.ShopModel;
import pos_scanner.db.Database;

public class ShopDAO extends DAO
{

    public final String TABLE = "shop";

    private final String COL_NAME = "Name";
    private final String COL_ADRESS = "Adress";
    private final String COL_POSTAL = "Postal";
    private final String COL_CITY = "City";
    private final String COL_INTERNAL_IP = "Internal_ip";
    private final String COL_EXTERNAL_IP = "External_ip";
    private final String COL_MAC = "MAC";
    private final String COL_PREFIX = "Prefix";

    List<String> tableCols = Arrays.asList(COL_NAME, COL_ADRESS, COL_POSTAL, COL_CITY, COL_INTERNAL_IP, COL_EXTERNAL_IP, COL_MAC, COL_PREFIX);

    public ShopDAO(Database db)
    {
        this.db = db;
    }

    public List<String> getColumnNames()
    {
        List<String> colNames = new ArrayList<String>();
        colNames.addAll(mainCols);
        colNames.addAll(tableCols);

        return colNames;
    }

    public ShopModel getShop(int id)
    {
        ShopModel shopModel = null;

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT * FROM " + TABLE + " WHERE " + COL_ID + "='" + id + "'";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();
                    while (set.next())
                    {
                        String name = set.getString(COL_NAME);
                        String adress = set.getString(COL_ADRESS);
                        String postal = set.getString(COL_POSTAL);
                        String city = set.getString(COL_CITY);
                        String interal_ip = set.getString(COL_INTERNAL_IP);
                        String external_ip = set.getString(COL_EXTERNAL_IP);
                        String mac = set.getString(COL_MAC);
                        String prefix = set.getString(COL_PREFIX);

                        shopModel = new ShopModel(name, adress, postal, city, interal_ip, external_ip, mac, prefix);
                        shopModel.setId(id);
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

        return shopModel;
    }

    public String getShopName(String cardNumber)
    {
        String name = "Not Found";

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT * FROM " + TABLE + " WHERE " + COL_PREFIX + "='" + getPrefix(cardNumber) + "'";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();
                    while (set.next())
                    {
                        name = set.getString(COL_NAME);
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

        return name;
    }

    public boolean addRegistered(String internal_ip, String external_ip, String mac)
    {
        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "UPDATE " + TABLE
                    + " SET"
                    + " " + COL_INTERNAL_IP + " = '" + internal_ip
                    + "', " + COL_EXTERNAL_IP + " = '" + external_ip
                    + "', " + COL_MAC + " = '" + mac
                    + "', " + COL_SYNC + " = '" + SyncStatus.MODIFIED.ordinal()
                    + "' WHERE ID = '1'";

            return db.query(query, true);

        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), TABLE);
        }

        return false;
    }
    
    public boolean isRegistered(Database dbOnline)
    {
        if (dbOnline.isConnected() && dbOnline.containsTable(TABLE))
        {
            String query = "SELECT * FROM " + TABLE + " WHERE "
                    + COL_INTERNAL_IP + " IS NOT NULL AND " + COL_INTERNAL_IP + " != '' AND "
                    + COL_EXTERNAL_IP + " IS NOT NULL AND " + COL_INTERNAL_IP + " != '' AND " 
                    + COL_MAC + " IS NOT NULL AND " + COL_MAC + " != ''";

            if (dbOnline.query(query, false))
            {
                try
                {
                    ResultSet set = dbOnline.getResultSet();
                    while (set.next())
                    {
                        return true;
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

        return false;
    }

}
