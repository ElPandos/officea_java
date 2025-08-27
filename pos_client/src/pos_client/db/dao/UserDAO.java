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
import pos_client.db.Database;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.UserModel;
import pos_client.UserModel.SecurityLevel;
import pos_client.common.Core;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class UserDAO
{

    Database db = null;

    public UserDAO()
    {
        db = Database.getInstance();
    }

    public ObservableList<UserModel> getUsers()
    {
        ObservableList<UserModel> users = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar alla användare", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_USERS;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();

                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_USERS + "_id");
                        String username = set.getString("username");
                        String password = set.getString("password");
                        int securityLevel = set.getInt(DefinedVariables.getInstance().TABLE_SECURITY_LEVELS + "_id");
                        int employeeId = set.getInt(DefinedVariables.getInstance().TABLE_EMPLOYEE + "_id");

                        users.add(new UserModel(id, username, password, UserModel.SecurityLevel.values()[securityLevel], employeeId));
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getUsers() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getUsers() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getUsers() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return users;
    }

    public UserModel getUser(String username, String password)
    {
        UserModel user = null;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar användare", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_USERS + " WHERE username='" + username + "'" + " AND password='" + password + "'";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_USERS + "_id");

                        int securityLevel = set.getInt(DefinedVariables.getInstance().TABLE_SECURITY_LEVELS + "_id");
                        int employeeId = set.getInt(DefinedVariables.getInstance().TABLE_EMPLOYEE + "_id");

                        user = new UserModel(id, username, password, UserModel.SecurityLevel.values()[securityLevel], employeeId);

                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("GetUser() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getUser() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("GetUser() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return user;
    }

    public UserModel getUser(int userId)
    {
        UserModel user = null;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar användare med id: " + General.getInstance().int2Str(userId), Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_USERS + " WHERE " + DefinedVariables.getInstance().TABLE_USERS + "_id = " + General.getInstance().int2Str(userId);

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_USERS + "_id");
                        String username = set.getString("username");
                        String password = set.getString("password");
                        int securityLevel = set.getInt(DefinedVariables.getInstance().TABLE_SECURITY_LEVELS + "_id");
                        int employeeId = set.getInt(DefinedVariables.getInstance().TABLE_EMPLOYEE + "_id");

                        user = new UserModel(id, username, password, UserModel.SecurityLevel.values()[securityLevel], employeeId);
                    }

                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("GetUser() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getUser() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("GetUser() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return user;
    }

    public class CashboxOpened
    {

        public int manual;
        public int automatic;
    }

    public CashboxOpened cashboxOpened(UserModel user)
    {
        CashboxOpened opened = new CashboxOpened();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Loggar hur många gånger kasslådan öppnats av " + user.getName(), Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_CASHBOX + " WHERE " + DefinedVariables.getInstance().TABLE_USERS + "_id='" + General.getInstance().int2Str(user.getId()) + "'";

            if (db.query(query, false))
            {
                ResultSet set = db.getResult();
                try
                {
                    while (set.next())
                    {
                        opened.manual = set.getInt("OPEN_MANUAL");
                        opened.automatic = set.getInt("OPEN_AUTOMATIC");
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("Avvikelse i ResultatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("cashboxOpen() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return opened;
    }

    public void cashboxOpen(UserModel user, UserModel.CashboxType type)
    {
        int count = 0;
        String query = "";

        String openType = "";
        switch (type)
        {
            case MANUAL:
                openType = "OPEN_MANUAL";
                break;
            case AUTOMATIC:
                openType = "OPEN_AUTOMATIC";
                break;
        }

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Loggar kasslåda " + openType, Log.LogLevel.DESCRIPTIVE);

            query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_CASHBOX + " WHERE " + DefinedVariables.getInstance().TABLE_USERS + "_id='" + General.getInstance().int2Str(user.getId()) + "'";

            boolean create = false;
            if (db.query(query, false))
            {
                ResultSet set = db.getResult();
                try
                {
                    while (set.next())
                    {
                        count = set.getInt(openType);
                    }

                    if (count > 0)
                    {
                        query = "UPDATE " + DefinedVariables.getInstance().TABLE_CASHBOX + " SET " + openType + "='" + General.getInstance().int2Str(count + 1) + "' WHERE " + DefinedVariables.getInstance().TABLE_USERS + "_id='" + General.getInstance().int2Str(user.getId()) + "'";
                        if (db.query(query, true))
                        {
                            Core.getInstance().getLog().log("Uppdaterade " + openType + ": " + General.getInstance().int2Str(count) + " -> " + General.getInstance().int2Str(count + 1) + " , för användare: " + user.getName() + "(" + General.getInstance().int2Str(user.getId()) + ")", Log.LogLevel.DESCRIPTIVE);
                        } else
                        {
                            Core.getInstance().getLog().log("Misslyckade att uppdatera " + openType + " för användare: " + user.getName() + " (" + General.getInstance().int2Str(user.getId()) + ")", Log.LogLevel.CRITICAL);
                        }
                    } else
                    {
                        create = true;
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("Avvikelse i ResultatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }

            if (create)
            {
                query = "INSERT INTO " + DefinedVariables.getInstance().TABLE_CASHBOX + " (" + DefinedVariables.getInstance().TABLE_USERS + "_id, open_automatic, open_manual) VALUES";
                String nameType = "";
                switch (type)
                {
                    case MANUAL:
                        nameType = "manual";
                        query = query + "('" + General.getInstance().int2Str(user.getId()) + "', '0', '1');";
                        break;
                    case AUTOMATIC:
                        nameType = "automatic";
                        query = query + "('" + General.getInstance().int2Str(user.getId()) + "', '1', '0');";
                        break;
                }

                if (db.query(query, true))
                {
                    Core.getInstance().getLog().log("Skapade cashbox_" + nameType + " för användare: " + user.getName() + " (" + General.getInstance().int2Str(user.getId()) + ")", Log.LogLevel.DESCRIPTIVE);
                } else
                {
                    Core.getInstance().getLog().log("Misslyckade att skapa cashbox_" + nameType + " för användare: " + user.getName() + " (" + General.getInstance().int2Str(user.getId()) + ")", Log.LogLevel.CRITICAL);
                }
            }
        } else
        {
            Core.getInstance().getLog().log("cashboxOpen() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public Map<String, SecurityLevel> getSecuritylevel()
    {
        Map<String, SecurityLevel> securityLevel = new HashMap<>();

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Hämtar säkerhetsnivåer", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_SECURITY_LEVELS;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int level = set.getInt(DefinedVariables.getInstance().TABLE_SECURITY_LEVELS + "_id");
                        String name = set.getString("name");

                        securityLevel.put(name, SecurityLevel.values()[level]);
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getSecuritylevel() - Avvikelse i ResulstatSet: " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getSecuritylevel() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getSecuritylevel() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return securityLevel;
    }

    public void add(UserModel user)
    {
        add(user.getName(), user.getPassword(), user.getSecurityLevel(), user.getEmployeeId());
    }

    public boolean add(String userName, String password, SecurityLevel securityLevel, Integer employeeId)
    {
        boolean result = false;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Lägger till användare", Log.LogLevel.NORMAL);

            String query = "INSERT INTO " + DefinedVariables.TABLE_USERS + " (username, password, " + DefinedVariables.getInstance().TABLE_SECURITY_LEVELS + "_id, " + DefinedVariables.getInstance().TABLE_EMPLOYEE + "_id) VALUES"
                    + " ('" + userName
                    + "', '" + password
                    + "', '" + General.getInstance().int2Str(securityLevel.ordinal())
                    + "', '" + employeeId.toString()
                    + "');";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade användare: " + userName, Log.LogLevel.DESCRIPTIVE);
                result = true;
            } else
            {
                Core.getInstance().getLog().log("add() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("add() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return result;
    }

    public boolean remove(int id)
    {
        if (db.isConnected())
        {
            return db.deleteRow(DefinedVariables.getInstance().TABLE_USERS, id);
        } else
        {
            Core.getInstance().getLog().log("remove() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
        return false;
    }

    public boolean update(UserModel updatedUser)
    {
        boolean result = true;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Uppdaterar vald användare " + updatedUser.getId() + " i db...", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_USERS
                    + " SET username = '" + updatedUser.getName()
                    + "', password = '" + updatedUser.getPassword()
                    + "', " + DefinedVariables.getInstance().TABLE_SECURITY_LEVELS + "_id = '" + General.getInstance().int2Str(updatedUser.getSecurityLevel().ordinal())
                    + "', " + DefinedVariables.getInstance().TABLE_EMPLOYEE + "_id = '" + General.getInstance().int2Str(updatedUser.getEmployeeId())
                    + "' WHERE " + DefinedVariables.getInstance().TABLE_USERS + "_id = '" + General.getInstance().int2Str(updatedUser.getId()) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade användare: " + updatedUser.getName() + " med id: " + updatedUser.getId(), Log.LogLevel.DESCRIPTIVE);
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
