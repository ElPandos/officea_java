/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.db.Database;
import pos_client.windows.Log;

/**
 *
 * @author Server
 */
public class DAO
{

    Database db = null;

    public DAO()
    {
        db = Database.getInstance();
    }

    public enum ReturnType
    {
        ReturnString,
        ReturnInt,
        ReturnFloat
    };

    public String reportQuery(String query, String columnName, ReturnType type)
    {
        String value = "ERROR";

        if (db.isConnected())
        {
            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        switch (type)
                        {
                            case ReturnInt:
                                value = General.getInstance().int2Str(set.getInt(columnName));
                                break;
                            case ReturnFloat:
                                value = General.getInstance().float2Str(set.getFloat(columnName), 2);
                                break;
                            case ReturnString:
                                value = set.getString(columnName);
                                break;
                        }
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("reportQuery() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            }
        } else
        {
            Core.getInstance().getLog().log("reportQuery() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return value;
    }

    public String getTableColumn(String table, String column, ReturnType type)
    {
        String query = "SELECT * FROM " + table;

        return reportQuery(query, column, type);
    }

    public void updateTableColumn(String table, String column, String value)
    {
        if (db.isConnected())
        {
            String query = "UPDATE " + table + " SET " + column + "= " + value;

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Uppdaterade " + column + " i tabell: " + table + ", med värde: " + value, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("updateTableColumn() -  Lyckades inte uppdatera tabell", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("updateTableColumn() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }
}
