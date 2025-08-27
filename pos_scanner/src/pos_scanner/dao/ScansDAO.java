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

import static pos_scanner.common.General.*;
import pos_scanner.db.Database;
import pos_scanner.db.DatabaseHandler;

public class ScansDAO extends DAO
{

    public final String TABLE = "scans";

    private final String COL_CARD = "Card";
    private final String COL_DATE = "Date";
    private final String COL_TIME = "Time";
    private final String COL_LOCATION = "Location";

    List<String> tableCols = Arrays.asList(COL_CARD, COL_DATE, COL_TIME, COL_LOCATION);

    public ScansDAO(Database db)
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

    public List<String> getYears()
    {
        List<String> years = new ArrayList<>();

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT EXTRACT(YEAR FROM " + COL_DATE + ") AS " + COL_DATE + " FROM " + TABLE + " GROUP BY " + COL_DATE + " ORDER BY " + COL_DATE + " ASC";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        years.add(set.getString(COL_DATE));
                    }
                } catch (SQLException ex)
                {
                    //Core.getInstance().getLog().log("getEmployeeGender() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                // Core.getInstance().getLog().log("getEmployeeGender() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            //Core.getInstance().getLog().log("getEmployeeGender() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return years;
    }

    public List<String> getMonths()
    {
        List<String> months = new ArrayList<>();

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT EXTRACT(MONTH FROM " + COL_DATE + ") AS " + COL_DATE + " FROM " + TABLE + " GROUP BY " + COL_DATE + " ORDER BY " + COL_DATE + " ASC";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        months.add(set.getString(COL_DATE));
                    }
                } catch (SQLException ex)
                {
                    //Core.getInstance().getLog().log("getEmployeeGender() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                // Core.getInstance().getLog().log("getEmployeeGender() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            //Core.getInstance().getLog().log("getEmployeeGender() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return months;
    }
        
    public List<String> getWeeks()
    {
        List<String> weeks = new ArrayList<>();

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT EXTRACT(WEEK FROM " + COL_DATE + ") AS " + COL_DATE + " FROM " + TABLE + " GROUP BY " + COL_DATE + " ORDER BY " + COL_DATE + " ASC";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        weeks.add(set.getString(COL_DATE));
                    }
                } catch (SQLException ex)
                {
                    //Core.getInstance().getLog().log("getEmployeeGender() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                // Core.getInstance().getLog().log("getEmployeeGender() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            //Core.getInstance().getLog().log("getEmployeeGender() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return weeks;
    }

    public List<String> getDays()
    {
        List<String> days = new ArrayList<>();

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT DISTINCT DAYOFWEEK (" + COL_DATE + ") as dayorder, DAYNAME(" + COL_DATE + ") as daynames FROM " + TABLE + " ORDER BY dayorder ASC";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        days.add(set.getString(COL_DATE));
                    }
                } catch (SQLException ex)
                {
                    //Core.getInstance().getLog().log("getEmployeeGender() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                // Core.getInstance().getLog().log("getEmployeeGender() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            //Core.getInstance().getLog().log("getEmployeeGender() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return days;
    }

    public int getScans(int year, MonthName month, int week)
    {
        int scans = 0;

        if (db.isConnected() && db.containsTable(TABLE))
        {
            //String query = "SELECT COUNT(*) AS " + TABLE + " FROM " + TABLE + " WHERE WEEKDAY(" + COL_DATE + ") IN (" + day.ordinal() + ")"; // MSQL
            String query = "SELECT COUNT(*) AS " + TABLE + " FROM " + TABLE + " WHERE YEAR(" + COL_DATE + ") IN (" + year + ") AND WHERE MONTH(" + COL_DATE + ") IN (" + month.ordinal() + ") AND WHERE WEEK(" + COL_DATE + ") IN (" + week + ")";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        scans = set.getInt(TABLE);
                    }
                } catch (SQLException ex)
                {
                    //Core.getInstance().getLog().log("getEmployeeGender() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                // Core.getInstance().getLog().log("getEmployeeGender() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            //Core.getInstance().getLog().log("getEmployeeGender() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return scans;
    }
        
    public int getScansDay(DayName day, String year)
    {
        int scans = 0;

        if (db.isConnected() && db.containsTable(TABLE))
        {
            //String query = "SELECT COUNT(*) AS " + TABLE + " FROM " + TABLE + " WHERE WEEKDAY(" + COL_DATE + ") IN (" + day.ordinal() + ")"; // MSQL
            String query = "SELECT COUNT(*) AS " + TABLE + " FROM " + TABLE + 
                    " WHERE DAY_OF_WEEK(" + COL_DATE + ") IN (" + (day.ordinal() + 2) % 7 + ") AND "
                    + "YEAR (" + COL_DATE + ") = '" + year + "'";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        scans = set.getInt(TABLE);
                    }
                } catch (SQLException ex)
                {
                    //Core.getInstance().getLog().log("getEmployeeGender() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                // Core.getInstance().getLog().log("getEmployeeGender() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            //Core.getInstance().getLog().log("getEmployeeGender() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return scans;
    }

    public int getScansMonth(MonthName month, String year)
    {
        int scans = 0;

        if (db.isConnected() && db.containsTable(TABLE))
        {
            //String query = "SELECT COUNT(*) AS " + TABLE + " FROM " + TABLE + " WHERE WEEKDAY(" + COL_DATE + ") IN (" + day.ordinal() + ")"; // MSQL
            String query = "SELECT COUNT(*) AS " + TABLE + " FROM " + TABLE + 
                    " WHERE MONTH (" + COL_DATE + ") IN (" + (month.ordinal() + 1) + ") AND"
                     + " YEAR (" + COL_DATE + ") = '" + year + "'";
            
            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        scans = set.getInt(TABLE);
                    }
                } catch (SQLException ex)
                {
                    //Core.getInstance().getLog().log("getEmployeeGender() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                // Core.getInstance().getLog().log("getEmployeeGender() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            //Core.getInstance().getLog().log("getEmployeeGender() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return scans;
    }

    public int getScansYear(int year)
    {
        int scans = 0;

        if (db.isConnected() && db.containsTable(TABLE))
        {
            //String query = "SELECT COUNT(*) AS " + TABLE + " FROM " + TABLE + " WHERE WEEKDAY(" + COL_DATE + ") IN (" + day.ordinal() + ")"; // MSQL
            String query = "SELECT COUNT(*) AS " + TABLE + " FROM " + TABLE + " WHERE YEAR (" + COL_DATE + ") = '" + year + "'"; // H2

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        scans = set.getInt(TABLE);
                    }
                } catch (SQLException ex)
                {
                    //Core.getInstance().getLog().log("getEmployeeGender() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                // Core.getInstance().getLog().log("getEmployeeGender() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            //Core.getInstance().getLog().log("getEmployeeGender() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return scans;
    }

    public int getScans(String cardNumber, String dateFrom, String dateTo)
    {
        int scans = INVALID_VALUE;

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String addCard = (!cardNumber.isEmpty() ? COL_CARD + "='" + cardNumber + "'" : "");
            String addDate = ((!dateFrom.isEmpty() && !dateTo.isEmpty()) ? COL_DATE + " BETWEEN '" + dateFrom + "' AND '" + dateTo + "' " : "");
            String both = (!addCard.isEmpty() && !dateFrom.isEmpty()) ? " AND " : "";

            String query = "SELECT COUNT(*) AS " + TABLE + " FROM " + TABLE + " WHERE " + addCard + both + addDate;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        scans = set.getInt(TABLE);
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

        return scans;
    }

    public ObservableList<CurrentCustomerModel> getScansCustomer(String cardNumber)
    {
        ObservableList<CurrentCustomerModel> currentCustomerVisit = FXCollections.observableArrayList();

        if (db.isConnected() && db.containsTable(TABLE))
        {
            String query = "SELECT * FROM " + TABLE + " WHERE " + COL_CARD + " = " + cardNumber + " ORDER BY " + COL_DATE + " DESC, " + COL_TIME + " DESC";

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResultSet();

                    while (set.next())
                    {
                        currentCustomerVisit.add(new CurrentCustomerModel(set.getString("date"), set.getString("time")));
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

        return currentCustomerVisit;
    }

    public boolean insert(String cardNumber)
    {
        if (DatabaseHandler.getInstance().verifyRegister())
        {
            if (db.isConnected() && db.containsTable(TABLE))
            {
                ShopDAO shopDAO = new ShopDAO(db);
                String query = "INSERT INTO " + TABLE + " (" + COL_CARD + ", " + COL_DATE + ", " + COL_TIME + ", " + COL_LOCATION + ", " + COL_SYNC + ") VALUES ('" + cardNumber + "', '" + date() + "', '" + time() + "', '" + shopDAO.getShopName(cardNumber) + "', '" + SyncStatus.ADDED.ordinal() + "');";
                return db.query(query, true);
            } else
            {
                log.outDatabaseConnectedTableExists(Thread.currentThread().getStackTrace()[1].getMethodName(), TABLE);
            }
        } else
        {
            log.outRegisterFail(Thread.currentThread().getStackTrace()[1].getMethodName());
        }

        return false;
    }
}
