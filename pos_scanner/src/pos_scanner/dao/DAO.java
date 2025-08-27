/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import static pos_scanner.common.Converter.int2Str;
import pos_scanner.common.Log;
import pos_scanner.common.Utils;
import pos_scanner.db.Database;

/**
 *
 * @author Server
 */
public class DAO
{

    protected Database db = null;
    protected Log log = null;

    protected String COL_ID = "id";
    protected String COL_SYNC = "Sync";

    List<String> mainCols;

    public DAO()
    {
        
        log = new Log();

        mainCols = Arrays.asList(COL_ID, COL_SYNC);
    }

    public enum SyncStatus
    {
        SAME,
        MODIFIED,
        ADDED,
        DELETED
    }

    public ResultSet getSyncStatus(Database db, String table)
    {
        ResultSet set = null;

        if (db.isConnected() && db.containsTable(table))
        {
            String query = "SELECT * FROM " + table + " WHERE " + COL_SYNC + " > '" + SyncStatus.SAME.ordinal() + "'";

            if (db.query(query, false))
            {
                set = db.getResultSet();
            }
        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), table);
        }

        return set;
    }

    public void sync(Database dbTo, String table)
    {
        ResultSet localResultSet = getSyncStatus(db, table);

        try
        {
            while (localResultSet.next())
            {
                int id = localResultSet.getInt(COL_ID);
                SyncStatus sync = SyncStatus.values()[localResultSet.getInt(COL_SYNC)];

                List<String> colNames = db.getColumnName(localResultSet);
                List<String> colData = db.getColumnData(localResultSet, COL_SYNC, int2Str(SyncStatus.SAME.ordinal()));

                switch (sync)
                {
                    case MODIFIED:
                        if (update(dbTo, table, colNames, colData, id))
                        {
                             log.outUpdate(Thread.currentThread().getStackTrace()[1].getMethodName(), dbTo.getDbName(), id, table);
                        }
                        break;
                    case DELETED:
                        if (delete(dbTo, table, id))
                        {
                            log.outInsert(Thread.currentThread().getStackTrace()[1].getMethodName(), dbTo.getDbName(), table);
                        }
                        break;
                    case ADDED:
                        if (insert(dbTo, table, colNames, colData))
                        {
                            log.outInsert(Thread.currentThread().getStackTrace()[1].getMethodName(), dbTo.getDbName(), table);
                        }
                        break;
                }

                if (update(db, table, colNames, colData, id))
                {
                    log.outUpdate(Thread.currentThread().getStackTrace()[1].getMethodName(), db.getDbName(), id, table);
                }
            }

        } catch (SQLException ex)
        {
            log.outResultset(Thread.currentThread().getStackTrace()[1].getMethodName(), ex.toString());
        }
    }

    public ResultSet getRow(String table, int id)
    {
        return db.getRow(table, COL_ID, id);
    }

    public boolean insert(Database dbTo, String table, List<String> colNames, List<String> colData)
    {
        if (dbTo.isConnected() && dbTo.containsTable(table))
        {
            String query = "INSERT INTO " + table + " " + dbTo.getColumnNameStr(colNames) + " VALUES " + dbTo.getColumnDataStr(colData);
            return dbTo.query(query, true);
        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), table);
        }

        return false;
    }

    public boolean update(Database dbTo, String table, List<String> colNames, List<String> colData, int id)
    {
        if (dbTo.isConnected() && dbTo.containsTable(table))
        {
            if (colNames.size() == colData.size())
            {
                String merge = dbTo.getMergedColumnNameAndDataStr(colNames, colData, COL_ID);
                String query = "UPDATE " + table + " SET " + merge + " WHERE " + COL_ID + "='" + id + "'";;
                return dbTo.query(query, true);
            }
            else
            {
               log.outColNameDataSize(Thread.currentThread().getStackTrace()[1].getMethodName(), colNames.size(), colData.size());
            }
        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), table);
        }

        return false;
    }

    public boolean delete(Database dbTo, String table, int id)
    {
        if (dbTo.isConnected() && dbTo.containsTable(table))
        {
            return dbTo.deleteRow(table, id);
        } else
        {
            log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), table);
        }

        return false;
    }

    public String getPrefix(String scannedNumber)
    {
        return (scannedNumber.length() > 3 ? scannedNumber.substring(0, 3) : "");
    }

}
