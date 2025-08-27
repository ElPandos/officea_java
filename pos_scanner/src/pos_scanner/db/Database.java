package pos_scanner.db;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Types;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pos_scanner.common.General.*;
import static pos_scanner.common.Converter.*;
import pos_scanner.common.Log;

public class Database
{

    Log log = null;

    public Database()
    {
        log = new Log();
    }

    public enum Status
    {
        UNDEFINED,
        NORMAL,
        PASS_USER_WRONG,
        ERROR
    };

    private Database.Status STATUS = Database.Status.UNDEFINED;

    //  Database credentials
    private String USER = "";
    private String PASS = "";

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    DatabaseSetup setup = null;

    public ResultSet getResultSet()
    {
        return resultSet;
    }

    public int getResultSetSize()
    {
        int resultSize = 0;

        try
        {
            if (resultSet != null)
            {
                resultSet.beforeFirst();
                resultSet.last();
                resultSize = resultSet.getRow();
                resultSet.beforeFirst();
            }
        } catch (SQLException ex)
        {
            log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
        }

        return resultSize;
    }

    public DatabaseSetup.Type getType()
    {
        return setup.type;
    }

    public String getDbName()
    {
        return setup.db_name;
    }

    public boolean hasSetup()
    {
        return (setup != null);
    }

    private void initDriver()
    {
        try
        {
            if (setup != null)
            {
                Class.forName(setup.driver);
            } else
            {
                log.outDatabaseInit(Thread.currentThread().getStackTrace()[1].getMethodName());
            }
        } catch (ClassNotFoundException ex)
        {
            log.outDatabase(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
        }
    }

    public void setup(String user, String password, String driver, String url, String db_name, String port, String config)
    {
        if (setup == null)
        {
            setup = new DatabaseSetup();
        }

        setup.clear();

        USER = user;
        PASS = password;

        setup.driver = driver;
        setup.url = url;
        setup.db_name = db_name;
        setup.port = port;
        setup.config = config;

        initDriver();
    }

    public void setup(String user, String password, DatabaseSetup setup)
    {
        this.setup = setup;

        USER = user;
        PASS = password;

        initDriver();
    }

    public boolean connect()
    {
        if (hasSetup())
        {
            try
            {
                //Core.getInstance().getLog().log("Ansluter till databasen...", Log.LogLevel.DESCRIPTIVE);

                String URL = setup.url + setup.db_name + setup.config;
                if (!setup.port.isEmpty())
                {
                    // remove last slash if we have ports
                    if (setup.url.endsWith("/"))
                    {
                        setup.url = setup.url.substring(0, setup.url.length() - 1);
                    }

                    URL = setup.url + ":" + setup.port + "/" + setup.db_name;
                }

                connection = DriverManager.getConnection(URL, USER, PASS);
                if (connection != null)
                {
                    //Core.getInstance().getLog().log("Ansluten till databas: " + DB_TYPE + " (" + URL + ".mv.db" + " )", Log.LogLevel.DESCRIPTIVE);
                    //Core.getInstance().getLog().getLogController().setDatabaseStatus(true);
                } else
                {
                    //Core.getInstance().getLog().log("Kunde inte ansluta till databasen! Felaktig setup?", Log.LogLevel.CRITICAL);
                }
            } catch (SQLException ex)
            {
                log.outDatabase(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
            }
        } else
        {
            //Core.getInstance().getLog().log("Kan inte ansluta till databasen saknar setup", Log.LogLevel.CRITICAL);
        }

        return isConnected();
    }

    public boolean isConnected()
    {
        try
        {
            return (connection != null && !connection.isClosed());
        } catch (SQLException ex)
        {
            log.outDatabase(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
        }

        return false;
    }

    public void disconnect()
    {
        try
        {
            if (isConnected())
            {
                //Core.getInstance().getLog().log("Stänger anslutning till databas...", Log.LogLevel.DESCRIPTIVE);
                if (statement != null && !statement.isClosed())
                {
                    statement.close();
                }

                if (connection != null && !connection.isClosed())
                {
                    connection.close();
                }
                //Core.getInstance().getLog().getLogController().setDatabaseStatus(false);
            } else
            {
                //Core.getInstance().getLog().log("Ingen anslutning finns!", Log.LogLevel.CRITICAL);
            }
        } catch (SQLException ex)
        {
            log.outDatabase(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
        }
    }

    public boolean query(String Query, boolean ModifyData)
    {
        try
        {
            if (isConnected())
            {
                //Core.getInstance().getLog().log("Skapar uttalande...", Log.LogLevel.DESCRIPTIVE);

                statement = connection.createStatement();
                //Core.getInstance().getLog().log("Utför fråga... : " + Query, Log.LogLevel.DESCRIPTIVE);
                if (ModifyData)
                {
                    String[] QuerySplit = Query.split(" ");
                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet tables = metaData.getTables(null, null, QuerySplit[2], null);

                    if (tables.next())
                    {
                        if (!"INSERT".equals(QuerySplit[0]) && !"DELETE".equals(QuerySplit[0]) && !"TRUNCATE".equals(QuerySplit[0]))
                        {
                            //Core.getInstance().getLog().log("Lyckades inte att utföra " + QuerySplit[0] + " på tabell: " + QuerySplit[2], Log.LogLevel.CRITICAL);
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
                    resultSet = statement.executeQuery(Query);
                }

                return true;
            }
        } catch (SQLException ex)
        {
            log.outError(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
        }

        return false;
    }

    public boolean deleteRow(String table, int id)
    {
        String query = "DELETE FROM " + table + " WHERE id = '" + id + "';";
        return query(query, true);
    }

    public ResultSet getRow(String table, String col, int id)
    {
        ResultSet set = null;

        if (isConnected() && containsTable(table))
        {
            String query = "SELECT * FROM " + table + " WHERE " + col + "='" + id + "'";

            if (query(query, false))
            {
                set = getResultSet();
            }
        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), table);
        }

        return set;
    }

    public String statusStr(Status status)
    {
        String info;

        switch (status)
        {
            case PASS_USER_WRONG:
                info = "Fel användare eller lösenord!";
                break;
            case NORMAL:
                info = "Databasen är initierad samt fylld med default data";
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
                + " WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='" + setup.db_name + "'";

        if (setup.type == DatabaseSetup.Type.H2)
        {
            setForeignKeyCheck(true);
            query = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'TABLE'";
        }

        if (query(query, false))
        {
            try
            {
                while (resultSet.next())
                {
                    String name = resultSet.getString("TABLE_NAME");
                    dropTable(name);
                }
            } catch (SQLException ex)
            {
                log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
            }
        }

        setForeignKeyCheck(false);
    }

    private void setForeignKeyCheck(boolean disable)
    {
        String query = query = "SET REFERENTIAL_INTEGRITY " + (disable ? "TRUE" : "FALSE");

        boolean res = query(query, true);
        if (res)
        {
            //Core.getInstance().getLog().log("Foreign key check = " + !disable, Log.LogLevel.DESCRIPTIVE);
        } else
        {
            //Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
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
                while (resultSet.next())
                {
                    dropTable(resultSet.getString("TABLE_NAME"));
                }

            } catch (SQLException ex)
            {
                log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
            }
        }
    }

    public void dropTable(String table)
    {
        String query = "DROP TABLE `" + table + "`";

        if (setup.type == DatabaseSetup.Type.MYSQL)
        {
            dropForeignKey(table);
        }

        if (query(query, true))
        {
            //Core.getInstance().getLog().log("Raderade tabell : " + tableName, Log.LogLevel.DESCRIPTIVE);
        } else
        {
            //Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }
    }

    public void clearTable(String table)
    {
        String query = "TRUNCATE TABLE `" + table + "`";

        if (query(query, true))
        {
            System.out.println("Cleared table: " + table);
            //Core.getInstance().getLog().log("Raderade tabell: " + tableName, Log.LogLevel.DESCRIPTIVE);
        } else
        {
            System.out.println("Failed clearing table: " + table);
            //Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }
    }

    public boolean containsTable(String table)
    {
        String query = "SELECT TABLE_NAME"
                + " FROM INFORMATION_SCHEMA.TABLES"
                + " WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='" + setup.db_name + "'";

        if (setup.type == DatabaseSetup.Type.H2)
        {
            query = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'TABLE'";
        }

        if (query(query, false))
        {
            try
            {
                while (resultSet.next())
                {
                    String name = resultSet.getString("TABLE_NAME");
                    if (table.toUpperCase().compareTo(name.toUpperCase()) == 0)
                    {
                        return true;
                    }
                }
            } catch (SQLException ex)
            {
                log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
            }
        }

        return false;
    }

    public List<String> getColumnName(ResultSet set)
    {
        List<String> colNames = new ArrayList<>();

        try
        {
            ResultSetMetaData metaData = set.getMetaData();

            for (int i = 1; i <= metaData.getColumnCount(); i++)
            {
                colNames.add(metaData.getColumnLabel(i));
            }
        } catch (SQLException ex)
        {
            log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
        }

        return colNames;
    }

    public String getColumnNameStr(List<String> colNames)
    {
        String data = "(";
        for (String item : colNames)
        {
            data += item + ",";
        }
        data = data.substring(0, data.length() - 1) + ")";

        return data;
    }

    public List<String> getColumnData(ResultSet set, String colName, String newValue)
    {
        List<String> columnData = new ArrayList<>();
        try
        {
            ResultSetMetaData metaData = set.getMetaData();

            for (int i = 1; i <= metaData.getColumnCount(); i++)
            {
                String label = metaData.getColumnLabel(i);
                int type = metaData.getColumnType(i);
                String name = metaData.getColumnName(i);
                if (!colName.isEmpty() && label.toUpperCase().compareTo(colName.toUpperCase()) == 0)
                {
                    columnData.add(newValue);
                } else
                {
                    columnData.add(getValue(set, type, name));
                }
            }
        } catch (SQLException ex)
        {
            log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
        }

        return columnData;
    }

    public String getColumnDataStr(List<String> columnData)
    {
        String data = "('";
        for (String item : columnData)
        {
            data += item + "','";
        }
        data = data.substring(0, data.length() - 2) + ")";

        return data;
    }

    public String getMergedColumnNameAndDataStr(List<String> colNames, List<String> colData, String colNameSkip)
    {
        String merge = "";
        if (colNames.size() == colData.size())
        {
            for (int i = 0; i < colNames.size(); i++)
            {
                if (colNames.get(i).toUpperCase().compareTo(colNameSkip.toUpperCase()) != 0)
                {
                    merge += colNames.get(i) + "='" + colData.get(i) + "',";
                }
            }
            merge = merge.substring(0, merge.length() - 1);
        }

        return merge;
    }

    public void copyTable(String table, Database copyTo)
    {
        String query = "SELECT * FROM " + table;

        if (copyTo != null && query(query, false))
        {
            String queryTo = "INSERT INTO " + table + " " + getColumnNameStr(getColumnName(resultSet));
            queryTo += " VALUES ";

            try
            {
                int ack = 0;
                int size = getResultSetSize();
                while (resultSet.next())
                {
                    copyTo.query(queryTo + getColumnDataStr(getColumnData(resultSet, "", "")), true);
                    log.outCopyTable(Thread.currentThread().getStackTrace()[1].getMethodName(), table, ++ack, size);
                }
            } catch (SQLException ex)
            {
                log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
            }

        } else
        {
            //Core.getInstance().getLog().log("Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
        }
    }

    private String getValue(ResultSet set, int type, String name) throws SQLException
    {
        String value = "";
        switch (type)
        {
            case Types.DATE:
                value = date(set.getDate(name));
                break;
            case Types.TIME:
                value = time(set.getTime(name));
                break;
            case Types.REAL:
            case Types.FLOAT:
                value = float2StrDB(set.getFloat(name), 2);
                break;
            case Types.VARCHAR:
                value = set.getString(name);
                break;
            case Types.INTEGER:
                value = int2Str(set.getInt(name));
                break;
        };

        return value;
    }

    public ArrayList<String> getTableColumns(String table)
    {
        ArrayList<String> columns = new ArrayList<>();

        String query = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='" + table + "'";

        if (query(query, false))
        {
            try
            {
                while (resultSet.next())
                {
                    columns.add(resultSet.getString("TABLE_NAME"));
                }
            } catch (SQLException ex)
            {
                log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
            }
        }

        return columns;
    }

    public String getTableColumnType(String table, String column)
    {
        String query = "SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='" + table + "' AND COLUMN_NAME = '" + column + "'";

        if (query(query, false))
        {
            try
            {
                while (resultSet.next())
                {
                    return resultSet.getString("DATA_TYPE");
                }
            } catch (SQLException ex)
            {
                log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
            }
        }

        return "ERROR";
    }

    public ArrayList<String> getTables()
    {
        ArrayList<String> columns = new ArrayList<>();

        String query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA = '" + setup.db_name + "'";

        if (query(query, false))
        {
            try
            {
                while (resultSet.next())
                {
                    columns.add(resultSet.getString("TABLE_NAME"));
                }
            } catch (SQLException ex)
            {
                log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
            }
        }

        return columns;
    }

}
