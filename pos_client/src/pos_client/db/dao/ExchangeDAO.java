/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import pos_client.UserModel;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.db.Database;
import pos_client.windows.Log;

/**
 *
 * @author Server
 */
public class ExchangeDAO extends DAO
{

    Database db = null;

    public ExchangeDAO()
    {
        db = Database.getInstance();
    }

    public int add(UserModel user, int value)
    {

        int userValue = 0;

        if (db.isConnected())
        {

            userValue = isUserAlreadyAdded(user);

            if (userValue == 0)
            {

                Core.getInstance().getLog().log("Lägger till handkassa i databasen för användare: " + user.getName(), Log.LogLevel.NORMAL);

                String query = "INSERT INTO " + DefinedVariables.getInstance().TABLE_EXCHANGE + " (date, time, value, " + DefinedVariables.getInstance().TABLE_USERS + "_id) VALUES "
                        + "('" + General.getInstance().date() + "','"
                        + General.getInstance().time() + "','"
                        + General.getInstance().int2Str(value) + "','"
                        + user.getId() + "');";

                if (db.query(query, true))
                {
                    Core.getInstance().getLog().log("Sparade handkassa: " + General.getInstance().int2Str(value), Log.LogLevel.DESCRIPTIVE);
                } else
                {
                    Core.getInstance().getLog().log("add - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
                }

                return value;
            } else
            {
                Core.getInstance().getLog().log("Användaren har redan registrerat sin handkassa", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("add - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return userValue;
    }

    public int isUserAlreadyAdded(UserModel user)
    {

        int value = 0;

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Kollar om användaren redan finns...", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_EXCHANGE + " WHERE " + DefinedVariables.getInstance().TABLE_USERS + "_id = " + user.getId();

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        value = set.getInt("value");
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("isUserAlreadyAdded - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("isUserAlreadyAdded - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("isUserAlreadyAdded - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return value;
    }

    public boolean clear()
    {

        boolean ret = false;

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Tömmer " + DefinedVariables.getInstance().TABLE_EXCHANGE + " tabellen", Log.LogLevel.NORMAL);

            String query = "TRUNCATE TABLE " + DefinedVariables.getInstance().TABLE_EXCHANGE;

            ret = db.query(query, true);
            if (ret)
            {
                Core.getInstance().getLog().log("Tömde " + DefinedVariables.getInstance().TABLE_EXCHANGE + " tabellen", Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("isUserAlreadyAdded - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("isUserAlreadyAdded - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return ret;

    }

}
