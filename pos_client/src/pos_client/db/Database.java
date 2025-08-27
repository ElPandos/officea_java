package pos_client.db;

import java.sql.*;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class Database extends General
{

    private static Database instance = null;

    protected Database()
    {
        // Exists only to defeat instantiation.
    }

    public static Database getInstance()
    {
        if (instance == null)
        {
            instance = new Database();
        }
        return instance;
    }

    public enum DatabaseType
    {
        NONE,
        MYSQL,
        MYSQL_LITE,
        FILEMAKER,
        H2,
        H2_SERVER,
        H2_IN_MEMORY
    }

    public enum DatabaseStatusInfo
    {
        NORMAL,
        PASS_USER_WRONG,
        DATABASE_DEFAULT,
        DATABASE_NOT_CONNECTED,
        ERROR
    };

    // MYSQL JDBC driver name and database URL
    private static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_DB_URL = "jdbc:mysql://localhost";
    private static final String MYSQL_DB_NAME = "test";
    private static final String MYSQL_DB_PORT = "3306";

    // MYSQL_LITE JDBC driver name and database URL
    private static final String MYSQL_LITE_JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String MYSQL_LITE_DB_URL = "jdbc:sqlite:test.db";
    private static final String MYSQL_LITE_DB_NAME = "POS";
    private static final String MYSQL_LITE_DB_PORT = "";

    // H2 JDBC driver name and database URL
    private static final String H2_JDBC_DRIVER = "org.h2.Driver";
    private static final String H2_DB_URL = "jdbc:h2:~/Officea_POS_database/";
    private static final String H2_DB_NAME = "POS";
    private static final String H2_DB_PORT = "";

    // H2 JDBC driver name and database URL
    private static final String H2_SERVER_JDBC_DRIVER = "org.h2.Driver";
    //private static final String H2_SERVER_DB_URL = "jdbc:h2:~/Officea_POS_database/";
    private static final String H2_SERVER_DB_URL = "jdbc:h2:tcp://localhost/~/Officea_POS_database/";
    private static final String H2_SERVER_DB_NAME = "POS";
    private static final String H2_SERVER_DB_PORT = "";
    private static final String H2_SERVER_DB_CONFIG = "";

    // H2 IN MEMORY JDBC driver name and database URL
    private static final String H2_IN_MEMORY_JDBC_DRIVER = "org.h2.Driver";
    private static final String H2_IN_MEMORY_DB_URL = "jdbc:h2:mem:test";
    private static final String H2_IN_MEMORY_DB_NAME = "POS";
    private static final String H2_IN_MEMORY_DB_PORT = "";
    private static final String H2_IN_MEMORY_USERNAME = "test";
    private static final String H2_IN_MEMORY_PASSWORD = "test";
    private static final String H2_IN_MEMORY_DB_TYPE = "H2_IN_MEMORY";

    private static String JDBC_DRIVER = "";
    private static String DB_TYPE = "";
    private static String DB_URL = "";
    private static String DB_CONFIG = "";
    private static String DB_NAME = "";
    private static String DB_PORT = "";

    private DatabaseType DB_ACTIVE = DatabaseType.NONE;

    //  Database credentials
    private String USER = "";
    private String PASS = "";

    Connection connection = null;
    Statement statement = null;

    private ResultSet result;

    private boolean isConnected = false;
    private boolean hasSetup = false;

    private void init(String driver)
    {
        try
        {
            Class.forName(driver);
        } catch (ClassNotFoundException ex)
        {
            Core.getInstance().getLog().log("Kunde inte initiera databasen... : " + ex.toString(), Log.LogLevel.CRITICAL);
        }
    }

    public ResultSet getResult()
    {
        return result;
    }

    public boolean isConnected()
    {
        return isConnected;
    }

    public DatabaseType getActive()
    {
        return DB_ACTIVE;
    }

    public String getType()
    {
        return DB_TYPE;
    }

    public String getName()
    {
        return DB_NAME;
    }

    public void setup(String user, String password, DatabaseType databaseType)
    {
        DB_ACTIVE = databaseType;

        switch (databaseType)
        {
            case MYSQL:
                DB_TYPE = "MYSQL";
                USER = user;
                PASS = password;
                DB_URL = MYSQL_DB_URL;
                DB_NAME = MYSQL_DB_NAME;
                DB_PORT = MYSQL_DB_PORT;
                JDBC_DRIVER = MYSQL_JDBC_DRIVER;
                break;
            case MYSQL_LITE:
                DB_TYPE = "MYSQL";
                USER = user;
                PASS = password;
                DB_URL = MYSQL_LITE_DB_URL;
                DB_NAME = MYSQL_LITE_DB_NAME;
                DB_PORT = MYSQL_LITE_DB_PORT;
                JDBC_DRIVER = MYSQL_LITE_JDBC_DRIVER;
                break;
            case H2:
                DB_TYPE = "H2";
                USER = user;
                PASS = password;
                DB_URL = H2_DB_URL;
                DB_NAME = H2_DB_NAME;
                DB_PORT = H2_DB_PORT;
                JDBC_DRIVER = H2_JDBC_DRIVER;
                break;
            case H2_SERVER:
                DB_TYPE = "H2_SERVER";
                USER = user;
                PASS = password;
                DB_URL = H2_SERVER_DB_URL;
                DB_NAME = H2_SERVER_DB_NAME;
                DB_PORT = H2_SERVER_DB_PORT;
                JDBC_DRIVER = H2_SERVER_JDBC_DRIVER;
                DB_CONFIG = H2_SERVER_DB_CONFIG;
                break;
            case H2_IN_MEMORY:
                DB_TYPE = H2_IN_MEMORY_DB_TYPE;
                USER = H2_IN_MEMORY_USERNAME;
                PASS = H2_IN_MEMORY_PASSWORD;
                DB_URL = H2_IN_MEMORY_DB_URL;
                DB_NAME = H2_IN_MEMORY_DB_NAME;
                DB_PORT = H2_IN_MEMORY_DB_PORT;
                JDBC_DRIVER = H2_IN_MEMORY_JDBC_DRIVER;
                break;
            case FILEMAKER:
                DB_TYPE = "FILEMAKER";
                USER = user;
                PASS = password;
                DB_URL = "";
                DB_NAME = "";
                DB_PORT = "";
                JDBC_DRIVER = "";
                break;

        }

        init(JDBC_DRIVER);

        hasSetup = true;
    }

    public boolean connect()
    {
        if (hasSetup)
        {
            try
            {
                Core.getInstance().getLog().log("Ansluter till databasen...", Log.LogLevel.DESCRIPTIVE);

                String URL = DB_URL + DB_NAME + DB_CONFIG;
                if (DB_PORT.length() > 0)
                {
                    URL = DB_URL + ":" + DB_PORT + "/" + DB_NAME;
                }

                connection = DriverManager.getConnection(URL, USER, PASS);
                if (connection != null)
                {
                    isConnected = true;
                    Core.getInstance().getLog().log("Ansluten till databas: " + DB_TYPE + " (" + URL + ".mv.db" + " )", Log.LogLevel.DESCRIPTIVE);
                    Core.getInstance().getLog().getLogController().setDatabaseStatus(true);
                } else
                {
                    Core.getInstance().getLog().log("Kunde inte ansluta till databasen! Felaktig setup?", Log.LogLevel.CRITICAL);
                }
            } catch (SQLException se)
            {
                Core.getInstance().getLog().log("Kan ej ansluta till databas... : " + se.toString(), Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("Kan inte ansluta till databasen saknar setup", Log.LogLevel.CRITICAL);
        }

        return isConnected;
    }

    public void disconnect()
    {
        try
        {
            if (isConnected)
            {
                Core.getInstance().getLog().log("Stänger anslutning till databas...", Log.LogLevel.DESCRIPTIVE);
                if (statement != null && !statement.isClosed())
                {
                    statement.close();
                }

                if (connection != null && !connection.isClosed())
                {
                    connection.close();
                }

                isConnected = false;
                Core.getInstance().getLog().getLogController().setDatabaseStatus(false);

            } else
            {
                Core.getInstance().getLog().log("Ingen anslutning finns!", Log.LogLevel.CRITICAL);
            }
        } catch (SQLException se)
        {
            Core.getInstance().getLog().log("Kan ej stänga ansluting till databas : " + se.toString(), Log.LogLevel.CRITICAL);
        }
    }

    public boolean query(String Query, boolean ModifyData)
    {
        try
        {
            if (isConnected && connection != null && !connection.isClosed())
            {
                Core.getInstance().getLog().log("Skapar uttalande...", Log.LogLevel.DESCRIPTIVE);

                statement = connection.createStatement();
                Core.getInstance().getLog().log("Utför fråga... : " + Query, Log.LogLevel.DESCRIPTIVE);
                if (ModifyData)
                {
                    String[] QuerySplit = Query.split(" ");

                    DatabaseMetaData DatabaseMetaData = connection.getMetaData();
                    // check if "employee" table is there
                    ResultSet tables = DatabaseMetaData.getTables(null, null, QuerySplit[2], null);
                    if (tables.next())
                    {
                        if (!"INSERT".equals(QuerySplit[0]) && !"DELETE".equals(QuerySplit[0]))
                        {
                            Core.getInstance().getLog().log("Lyckades inte att utföra " + QuerySplit[0] + " på  tabell: " + QuerySplit[2], Log.LogLevel.CRITICAL);
                        } else
                        {
                            statement.executeUpdate(Query);
                        }
                    } else
                    {
                        statement.executeUpdate(Query);
                    }
                } else
                {
                    result = statement.executeQuery(Query);
                }

                return true;
            }
        } catch (SQLException se)
        {
            Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + se.toString(), Log.LogLevel.CRITICAL);
        }

        return false;
    }

    public boolean deleteRow(String table, int id)
    {
        String query = "DELETE FROM " + table + " WHERE " + table + "_id = '" + General.getInstance().int2Str(id) + "';";

        if (query(query, true))
        {
            Core.getInstance().getLog().log("Deleted row " + General.getInstance().int2Str(id) + " from table: " + table, Log.LogLevel.DESCRIPTIVE);
            return true;
        } else
        {
            Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }

        return false;
    }

    public boolean deleteCategoryRow(String table, int id)
    {
        String query = "DELETE FROM " + table + " WHERE " + "ordernr = '" + General.getInstance().int2Str(id) + "';";

        if (query(query, true))
        {
            Core.getInstance().getLog().log("Deleted row " + General.getInstance().int2Str(id) + " from table: " + table, Log.LogLevel.DESCRIPTIVE);
            return true;
        } else
        {
            Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }

        return false;
    }

    public String databaseInfo(DatabaseStatusInfo type)
    {
        String info;

        switch (type)
        {
            case PASS_USER_WRONG:
                info = "Fel användare eller lösenord!";
                break;
            case DATABASE_DEFAULT:
                info = "Databasen är initierad samt fylld med default data";
                break;
            case DATABASE_NOT_CONNECTED:
                info = "Databasen inte uppkopplad... Anslut och försök igen!";
                break;
            default:
                info = "ERROR";
                break;
        }

        return info;
    }
    
    public void dropAllTables()
    {
        String query = "SELECT TABLE_NAME"
                + " FROM INFORMATION_SCHEMA.TABLES"
                + " WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='" + DB_NAME + "'";

        if (DB_ACTIVE == Database.DatabaseType.H2 || DB_ACTIVE == Database.DatabaseType.H2_SERVER)
        {
            setForeignKeyCheck(true);
            query = "SELECT * FROM INFORMATION_SCHEMA.TABLES"
                    + " WHERE TABLE_TYPE = 'TABLE'";
        }

        if (query(query, false))
        {
            try
            {
                while (result.next())
                {
                    String name = result.getString("TABLE_NAME");
                    dropTable(name);
                }
            } catch (SQLException ex)
            {
                Core.getInstance().getLog().log("Avvikelse i ResultatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }
        setForeignKeyCheck(false);
    }

    private void setForeignKeyCheck(boolean disable)
    {
        String query = query = "SET REFERENTIAL_INTEGRITY ";

        String toggle = "TRUE";
        if (disable)
        {
            toggle = "FALSE";
        }

        query = query + toggle;

        if (query(query, true))
        {
            Core.getInstance().getLog().log("Foreign key check = " + !disable, Log.LogLevel.DESCRIPTIVE);
        } else
        {
            Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }
    }

    public void dropTable(String tableName)
    {
        String query = "DROP TABLE `" + tableName + "`";

        if (getActive() == Database.DatabaseType.MYSQL)
        {
            dropForeignKey(tableName);
        }

        if (query(query, true))
        {
            Core.getInstance().getLog().log("Raderade tabell : " + tableName, Log.LogLevel.DESCRIPTIVE);
        } else
        {
            Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }
    }

    public void clearTable(String tableName)
    {
        String query = "TRUNCATE TABLE `" + tableName + "`";

        if (query(query, true))
        {
            Core.getInstance().getLog().log("Raderade tabell: " + tableName, Log.LogLevel.DESCRIPTIVE);
        } else
        {
            Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }
    }

    public void dropForeignKey(String tableName)
    {
        String query = "SELECT *"
                + " FROM information_schema.KEY_COLUMN_USAGE"
                + " WHERE REFERENCED_TABLE_NAME = '" + tableName + "';";

        if (query(query, false))
        {
            try
            {
                while (result.next())
                {
                    dropTable(result.getString("TABLE_NAME"));
                }

            } catch (SQLException ex)
            {
                Core.getInstance().getLog().log("Avvikelse i ResultatSet: " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }
    }

}
