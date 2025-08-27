/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pos_client.common.Core;
import pos_client.db.Database;
import pos_client.common.DefinedVariables;
import pos_client.settings.Setting;
import pos_client.terminals.TerminalData;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class SettingsDAO extends DAO
{

    public SettingsDAO()
    {

    }

    public String getSetting(String name)
    {
        String settingValue = "";

        ObservableList<Setting> settings = getSettings();

        for (Setting setting : settings)
        {
            if (setting.getVariable().compareTo(name) == 0)
            {
                settingValue = setting.getValue();
                break;
            }
        }

        return settingValue;
    }

    public ObservableList<Setting> getSettings()
    {
        ObservableList<Setting> settingsData = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar inställningar", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_SETTINGS_LOCAL;

            if (db.query(query, false))
            {
                try
                {
                    while (db.getResult().next())
                    {

                        Setting settings = new Setting();

                        settings.setId(db.getResult().getInt(DefinedVariables.getInstance().TABLE_SETTINGS_LOCAL + "_id"));
                        settings.setVariable(db.getResult().getString("variable"));
                        settings.setValue(db.getResult().getString("setting"));
                        settings.setUnit(db.getResult().getString("unit"));

                        settingsData.add(settings);
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getSettings() - Avvikelse i ReslustatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getSettings() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getSettings() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return settingsData;
    }

    public void updateSettingValue(String name, String value)
    {
        if (db.isConnected())
        {
            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_SETTINGS_LOCAL + " "
                    + "SET setting='" + value + "' "
                    + "WHERE variable='" + name + "';";

            if (Database.getInstance().query(query, true))
            {
                Core.getInstance().getLog().log("Ändrade " + name + " setting till: " + value, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("updateSettingValue() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("updateSettingValue() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public Map<Integer, TerminalData> getTerminals()
    {
        Map<Integer, TerminalData> terminals = new HashMap<>();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar alla terminaler", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * from " + DefinedVariables.getInstance().TABLE_TERMINALS_SUPPORTED;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_TERMINALS_SUPPORTED + "_id");
                        String name = set.getString("name");
                        String imageUrl = set.getString("image_url");

                        TerminalData terminalData = new TerminalData(name, imageUrl);

                        terminals.put(id, terminalData);
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getTerminals() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getTerminals() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getTerminals() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return terminals;
    }

    public void saveTerminal(String terminal, String autoConnect)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Sparar terminal inställningar", Log.LogLevel.NORMAL);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_SETTINGS_LOCAL + " "
                    + "SET setting='" + terminal + "' "
                    + "WHERE variable='" + DefinedVariables.getInstance().SETTING_TERMINALS_CONNECT + "';";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Ändrade " + DefinedVariables.getInstance().SETTING_TERMINALS_USE + " setting till: " + terminal, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("add - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }

            query = "UPDATE " + DefinedVariables.getInstance().TABLE_SETTINGS_LOCAL + " "
                    + "SET setting='" + autoConnect + "' "
                    + "WHERE variable='" + DefinedVariables.getInstance().SETTING_TERMINALS_CONNECT + "';";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade autoConnect status: " + autoConnect, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("saveTerminal() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("saveTerminal() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

}
