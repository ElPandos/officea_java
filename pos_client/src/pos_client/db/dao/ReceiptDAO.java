/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pos_client.db.Database;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.Receipt;
import pos_client.UserModel;
import pos_client.common.Core;
import static pos_client.common.DefinedVariables.*;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.windows.Log;

public class ReceiptDAO extends DAO
{

    public static final int RECEIPT_COPY_PRINTED = 1;
    public static final int RECEIPT_COPY_NOT_PRINTED = 0;

    Database db = null;

    public void updateBordNo(Receipt receipt, int no)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ReceiptDAO()
    {
        db = Database.getInstance();
    }

    public Receipt getReceiptObj(int receiptNr)
    {
        Receipt receipt = null;

        int id = getReceiptId(receiptNr);
        if (id != General.getInstance().INVALID_VALUE)
        {
            receipt = getReceipt(id);
        } else
        {
            Core.getInstance().getLog().log("Kunde inte hitta ett kvitto med nr: : " + General.getInstance().int2Str(receiptNr), Log.LogLevel.CRITICAL);
        }

        return receipt;
    }

    public int getReceiptId(int nr)
    {
        int currentId = General.getInstance().INVALID_VALUE;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar kvitto id med nr: " + General.getInstance().int2Str(nr), Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_nr = " + General.getInstance().int2Str(nr);

            if (db.query(query, false))
            {
                ResultSet set = db.getResult();
                try
                {
                    while (set.next())
                    {
                        currentId = set.getInt(DefinedVariables.getInstance().TABLE_RECEIPTS + "_id");
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getReceiptId() - Avvikelse i ResultatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getReceiptId() - Lyckades inte att hämta nytt kvitto nr i databasen!", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getReceiptId() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return currentId;
    }

    public Receipt getReceipt(int receiptId)
    {
        Receipt receipt = null;

        String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id = " + General.getInstance().int2Str(receiptId);

        Core.getInstance().getLog().log("Hämtar kvitto med id: " + General.getInstance().int2Str(receiptId), Log.LogLevel.DESCRIPTIVE);

        if (db.query(query, false))
        {
            ResultSet resultSet = db.getResult();
            try
            {
                if (resultSet.next())
                {
                    receipt = mapReceiptDbRowToReceipt(resultSet);
                }

            } catch (SQLException ex)
            {
                Core.getInstance().getLog().log("fetchReceipt() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        }
        return receipt;
    }

    private Receipt mapReceiptDbRowToReceipt(ResultSet resultSet) throws SQLException
    {
        int id = resultSet.getInt(DefinedVariables.getInstance().TABLE_RECEIPTS + "_id");
        int nr = resultSet.getInt(DefinedVariables.getInstance().TABLE_RECEIPTS + "_nr");
        int userId = resultSet.getInt(DefinedVariables.getInstance().TABLE_USERS + "_id");
        String createdTime = resultSet.getString("createTime");
        String createdDate = resultSet.getString("createDate");
        String modifyTime = resultSet.getString("modifyTime");
        String modifyDate = resultSet.getString("modifyDate");
        int receiptType = resultSet.getInt(DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_id");
        float totalAmount = resultSet.getFloat("totalamount");
        float moms1 = resultSet.getFloat("moms1");
        float moms2 = resultSet.getFloat("moms2");
        float moms3 = resultSet.getFloat("moms3");
        float moms4 = resultSet.getFloat("moms4");
        String bordnumber = resultSet.getString("bordnumber");
        int cashBack = resultSet.getInt("cashback");
        int cash = resultSet.getInt("cash");
        float card = resultSet.getFloat("card");
        float credit = resultSet.getFloat("credit");
        int cashResult = resultSet.getInt("cashresult");
        float cardResult = resultSet.getFloat("cardResult");
        int noOfServices = resultSet.getInt("noofservices");
        int noOfArticles = resultSet.getInt("noofarticles");
        int copyPrinted = resultSet.getInt("copyprinted");

        Receipt receipt = new Receipt();
        receipt.SetReceipt(id, nr, userId, createdDate, createdTime, modifyDate, modifyTime, receiptType, cashBack, cash, card, credit, cashResult, cardResult, noOfArticles, noOfServices, totalAmount, moms1, moms2, moms3, moms4, bordnumber, copyPrinted);

        return receipt;
    }

    public ObservableList<Receipt> getReceiptsToday()
    {
        ObservableList<Receipt> receiptsToday = FXCollections.observableArrayList();

        for (Receipt receipt : getReceipts(ReceiptType.NORMAL))
        {
            if (receipt.getModifyDate().compareTo(General.getInstance().date()) == 0)
            {
                receiptsToday.add(receipt);
            }
        }
        return receiptsToday;
    }

    public ObservableList<Receipt> getReceipts(ReceiptType type)
    {
        ObservableList<Receipt> receipts = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar kvitto för en viss typ", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_id = " + General.getInstance().int2Str(ReceiptTypeDAO.receiptTypeToDb(type));

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        Receipt receipt = mapReceiptDbRowToReceipt(set);
                        if (receipt.getType() == type)
                        {
                            receipts.add(receipt);
                        }
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getReceipts() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getReceipts() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getReceipts() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return receipts;
    }

    public boolean finish(Receipt receipt)
    {
        boolean ret = false;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Avslutar kvittot", Log.LogLevel.NORMAL);

            String query = "UPDATE " + TABLE_RECEIPTS + " "
                    + "SET "
                    + TABLE_RECEIPTS + "_nr = '" + receipt.getNr() + "' , "
                    + "modifydate = '" + receipt.getModifyDate() + "' , "
                    + "modifytime = '" + receipt.getModifyTime() + "' , "
                    + "totalamount = '" + General.getInstance().float2StrDB(receipt.getSumTotalIncm(), 2) + "' , "
                    + "moms1 = '" + General.getInstance().float2StrDB(receipt.getSumTotalVat(VatDAO.VatType.VAT1), 2) + "' , "
                    + "moms2 = '" + General.getInstance().float2StrDB(receipt.getSumTotalVat(VatDAO.VatType.VAT2), 2) + "' , "
                    + "moms3 = '" + General.getInstance().float2StrDB(receipt.getSumTotalVat(VatDAO.VatType.VAT3), 2) + "' , "
                    + "moms4 = '" + General.getInstance().float2StrDB(receipt.getSumTotalVat(VatDAO.VatType.VAT4), 2) + "' , "
                    + "cashback = '" + General.getInstance().float2StrDB(receipt.getCashBack(), 2) + "' , "
                    + "cash = '" + General.getInstance().float2StrDB(receipt.getCash(), 2) + "' , "
                    + "card = '" + General.getInstance().float2StrDB(receipt.getCard(), 2) + "' , "
                    + "credit = '" + General.getInstance().float2StrDB(receipt.getCredit(), 2) + "' , "
                    + "cashresult = '" + General.getInstance().float2StrDB(receipt.getCashResult(), 2) + "' , "
                    + "cardresult = '" + General.getInstance().float2StrDB(receipt.getCardResult(), 2) + "' , "
                    + "bordnumber = '" + receipt.getTableNumber() + "' , "
                    + "noofservices = '" + receipt.getNoOfServices() + "' , "
                    + "noofarticles = '" + receipt.countSales() + "' , "
                    + "copyprinted = '" + receipt.getCopyPrinted() + "' , "
                    + TABLE_USERS + "_id = '" + receipt.getUserId() + "' , "
                    + TABLE_RECEIPTS_TYPE + "_id  = '" + receipt.getType().ordinal() + "'"
                    + " WHERE " + TABLE_RECEIPTS + "_id = '" + receipt.getReceiptId() + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade det avslutade kvittot i databasen!", Log.LogLevel.DESCRIPTIVE);

                if (receipt.getType() == ReceiptType.ONGOING)
                {
                    receipt.normal();
                }

                if (receipt.getType() == ReceiptType.REFUND)
                {
                    receipt.refund();
                }

                ret = true;
            } else
            {
                Core.getInstance().getLog().log("finish() - Lyckades inte att spara det avslutade kvittot i databasen", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("finish() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return ret;
    }

    public boolean remove(int id)
    {
        if (db.isConnected())
        {
            return db.deleteRow(DefinedVariables.getInstance().TABLE_RECEIPTS, id);
        } else
        {
            Core.getInstance().getLog().log("remove() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
        return false;
    }

    public void store(Receipt receipt)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Sparar kvitto", Log.LogLevel.NORMAL);

            String query = "INSERT INTO " + DefinedVariables.getInstance().TABLE_RECEIPTS + "(" + DefinedVariables.getInstance().TABLE_USERS + "_id, " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_nr, createdate, createtime, modifydate, modifytime, " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_id, totalamount, moms1, moms2, moms3, moms4, cashback, cash, card, credit, cashresult, cardresult, bordnumber) VALUES"
                    + "('" + General.getInstance().int2Str(receipt.getUserId()) + "',"
                    + "'" + General.getInstance().int2Str(receipt.getNr()) + "',"
                    + "'" + receipt.getCreatedDate() + "',"
                    + "'" + receipt.getCreatedTime() + "',"
                    + "'" + receipt.getModifyDate() + "',"
                    + "'" + receipt.getModifyTime() + "',"
                    + "'" + General.getInstance().int2Str(ReceiptTypeDAO.receiptTypeToDb(receipt.getType())) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getSumTotalIncm(), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getVat(VatDAO.VatType.VAT1), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getVat(VatDAO.VatType.VAT2), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getVat(VatDAO.VatType.VAT3), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getVat(VatDAO.VatType.VAT4), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getCashBack(), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getCash(), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getCard(), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getCredit(), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getCashResult(), 2) + "',"
                    + "'" + General.getInstance().float2StrDB(receipt.getCardResult(), 2) + "',"
                    + "'" + receipt.getTableNumber() + "')";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade kvitto med nr: " + General.getInstance().int2Str(receipt.getNr()) + " i databasen!", Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("store() - Lyckades inte att spara kvittot i databasen", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("store() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public void updateReceiptAsPrinted(int receiptId)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar kvitto för en viss typ", Log.LogLevel.DESCRIPTIVE);
            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_RECEIPTS
                    + " SET copyprinted=" + RECEIPT_COPY_PRINTED
                    + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id=" + General.getInstance().int2Str(receiptId);

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Kvitto med id: " + receiptId + " markerat som återutskrivet", Log.LogLevel.NORMAL);
            } else
            {
                Core.getInstance().getLog().log("Lyckades inte att uppdatera kvittot som återutskrivet ", Log.LogLevel.CRITICAL);
            }

        } else
        {
            Core.getInstance().getLog().log("updateReceiptAsPrinted() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public void updateReceiptUser(Receipt receipt, UserModel user)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Uppdaterar användare på kvittot", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_RECEIPTS
                    + " SET " + TABLE_USERS + "_id=" + General.getInstance().int2Str(user.getId())
                    + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id=" + General.getInstance().int2Str(receipt.getReceiptId());

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Kvitto med id: " + General.getInstance().int2Str(receipt.getReceiptId()) + " bytte användare till " + user.getName(), Log.LogLevel.NORMAL);
            } else
            {
                Core.getInstance().getLog().log("Lyckades inte att uppdatera användare till kvittot ", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("updateReceiptUser() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public void updateReceiptNo(int receiptId, int receiptNo)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Uppdaterar kvittoid " + receiptId + " med kvitto nr " + receiptNo, Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + TABLE_RECEIPTS + " "
                    + "SET " + TABLE_RECEIPTS + "_nr= " + receiptNo + " WHERE " + TABLE_RECEIPTS + "_id=" + receiptId + "";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Uppdaterade kvitto id " + receiptId + " med kvittonr " + receiptNo, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("Lyckades int uppdatera kvitto id" + receiptId + " med kvittonr " + receiptNo, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("updateReceiptNo() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public void updateTableNr(int receiptId, String tableNumber)
    {
        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Uppdaterar kvittoid " + receiptId + " med bordnummer " + tableNumber, Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + TABLE_RECEIPTS + " "
                    + "SET " + TABLE_RECEIPTS + ".bordnumber = " + tableNumber + " WHERE " + TABLE_RECEIPTS + "_id=" + receiptId + "";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Uppdaterade kvitto id " + receiptId + " med bordnummer " + tableNumber, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("Lyckades int uppdatera kvitto id " + receiptId + " med bordnummer " + tableNumber, Log.LogLevel.CRITICAL);
            }

        } else
        {
            Core.getInstance().getLog().log("updateTableNr() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public int getNewReceiptNr()
    {
        int newId = General.getInstance().INVALID_VALUE;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar nytt kvitto nr", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT MAX(" + TABLE_RECEIPTS + "_nr) AS HIGHEST FROM " + TABLE_RECEIPTS;

            if (db.query(query, false))
            {
                ResultSet set = db.getResult();
                try
                {
                    while (set.next())
                    {
                        newId = set.getInt("HIGHEST");
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("Avvikelse i ResultatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }

                newId = newId + 1; // Increase by one for the new id

                Core.getInstance().getLog().log("Nytt kvitto nr: " + newId, Log.LogLevel.DESCRIPTIVE);

            } else
            {
                Core.getInstance().getLog().log("getNewReceiptNr() - Lyckades inte att hämta nytt kvitto nr i databasen!", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getNewReceiptNr() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return newId;
    }

    public int getTableNr(int nr)
    {
        int currentId = General.getInstance().INVALID_VALUE;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar bordnummer med nr: " + General.getInstance().int2Str(nr), Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT BORDNUMBER FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id = " + General.getInstance().int2Str(nr);

            if (db.query(query, false))
            {
                ResultSet set = db.getResult();
                try
                {
                    while (set.next())
                    {
                        currentId = set.getInt(DefinedVariables.getInstance().TABLE_RECEIPTS + "_id");
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getBordNo() - Avvikelse i ResultatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getBordNo() - Lyckades inte att hämta nr i databasen!", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getBordNo() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return currentId;
    }

    public void changeReceiptTypeId(Receipt receipt)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Byter kvitto typ", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_RECEIPTS +
                    " SET " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_id='" + General.getInstance().int2Str(ReceiptTypeDAO.receiptTypeToDb(receipt.getType())) + "'"
                    + ", createdate='" + receipt.getCreatedDate() + "'"
                    + ", createtime='" + receipt.getCreatedTime() + "'"
                    + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id='" + General.getInstance().int2Str(receipt.getReceiptId()) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Ändrade kvittotyp: " + General.getInstance().int2Str(ReceiptTypeDAO.receiptTypeToDb(receipt.getType())) + ", på kvitto: " + General.getInstance().int2Str(receipt.getReceiptId()), Log.LogLevel.DESCRIPTIVE);
                changeReceiptTypeIdinSales(receipt);
            } else
            {
                Core.getInstance().getLog().log("Lyckades inte att ändra typen på kvittot i databasen", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("changeReceiptTypeId() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public void changeReceiptTypeIdinSales(Receipt receipt)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Byter kvitto typ i sales tabellen", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_SALES + " SET " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_id='" + General.getInstance().int2Str(ReceiptTypeDAO.receiptTypeToDb(receipt.getType())) + "', " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_NAME='" + ReceiptTypeDAO.receiptTypeNametoDb(receipt.getType()) + "' WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id='" + General.getInstance().int2Str(receipt.getReceiptId()) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Ändrade kvittotyp: " + General.getInstance().int2Str(ReceiptTypeDAO.receiptTypeToDb(receipt.getType())) + ", på kvitto: " + General.getInstance().int2Str(receipt.getReceiptId()) + " i Sales tabellen", Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("Lyckades inte att ändra typen på kvittot i databasen", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("changeReceiptTypeIdinSales() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public String getCashRegisterNr()
    {
        String ret = "ERROR";

        if (db.isConnected())
        {
            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_CASHREGISTER;

            if (db.query(query, false))
            {
                ResultSet set = db.getResult();
                try
                {
                    while (set.next())
                    {
                        ret = General.getInstance().int2Str(set.getInt("cashregister_nr"));
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
            Core.getInstance().getLog().log("Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return ret;
    }

    public String getSoldArticles()
    {
        /*
         String query = "SELECT COUNT(T.ARTICLES_ID) AS SOLDARTICLES FROM SALES AS T LEFT JOIN RECEIPTS AS T1 ON T.RECEIPTS_ID = T1.RECEIPTS_ID WHERE T1.TYPE_RECEIPT_ID ='4'";
         return reportQuery("SELECT SUM(MOMS1) AS MOMS1 FROM RECEIPTS", "moms1", ReturnType.ReturnFloat);
         int count = 0;
         if (db.query(query, false)) {
         try {
         ResultSet set = db.result();
         while (set.next()) {
         count = set.getInt("soldarticles");
         }
         } catch (SQLException ex) {
         Core.getInstance().getLog().log("buildArticlesTab - Avvikelse i ResulstatSet : " + ex.toString());
         }
         }
         */
        //return reportQuery("SELECT COUNT(" + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID) AS AMOUNTPARKED FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.Canceled.ordinal() + 1) + "'", "amountparked", ReturnType.ReturnInt);
        return "inte klar";
    }

    public String getSumTotalSales()
    {
        return reportQuery("SELECT SUM(TOTALAMOUNT) AS TOTALSALES FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "totalsales", ReturnType.ReturnFloat);
    }

    public String getSumTotalCash()
    {
        return reportQuery("SELECT SUM(CASHRESULT) AS TOTALCASH FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "totalcash", ReturnType.ReturnFloat);
    }

    public String getSumTotalCard()
    {
        return reportQuery("SELECT SUM(CARDRESULT) AS TOTALCARD FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "totalcard", ReturnType.ReturnFloat);
    }

    public String getSumTotalCredit()
    {
        return reportQuery("SELECT SUM(CREDIT) AS TOTALCREDIT FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "totalcredit", ReturnType.ReturnFloat);
    }

    public String getReceiptsParked()
    {
        return reportQuery("SELECT COUNT(" + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID) AS AMOUNTPARKED FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.PARKED.ordinal()) + "'", "amountparked", ReturnType.ReturnInt);
    }

    public String getReceiptsOngoing()
    {
        return reportQuery("SELECT COUNT(" + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID) AS AMOUNTONGOING FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.ONGOING.ordinal()) + "'", "amountongoing", ReturnType.ReturnInt);
    }

    public String getReceiptsCanceled()
    {
        return reportQuery("SELECT COUNT(" + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID) AS AMOUNTCANCELED FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.CANCELED.ordinal()) + "'", "amountcanceled", ReturnType.ReturnInt);
    }

    public String getReceiptsNormal()
    {
        return reportQuery("SELECT COUNT(" + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID) AS AMOUNTNORMAL FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "amountnormal", ReturnType.ReturnInt);
    }

    public String getReceiptsPractice()
    {
        return reportQuery("SELECT COUNT(" + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID) AS AMOUNTPRACTICE FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.PRACTICE.ordinal()) + "'", "amountpractice", ReturnType.ReturnInt);
    }

    public String getReceiptsCopy()
    {
        return reportQuery("SELECT COUNT(" + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID) AS AMOUNTCOPY FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.COPY.ordinal()) + "'", "amountcopy", ReturnType.ReturnInt);
    }

    public String getReceiptsRefund()
    {
        return reportQuery("SELECT COUNT(" + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID) AS AMOUNTREFUND FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.REFUND.ordinal()) + "'", "amountrefund", ReturnType.ReturnInt);
    }

    public String getReceiptsProForma()
    {
        return reportQuery("SELECT COUNT(" + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID) AS AMOUNTPROFORMA FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.PROFO.ordinal()) + "'", "amountproforma", ReturnType.ReturnInt);
    }

    public String getSumTotalMoms()
    {
        return reportQuery("SELECT SUM(MOMS1 + MOMS2 + MOMS3 + MOMS4) AS TOTALMOMS FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS  + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "totalmoms", ReturnType.ReturnFloat);
    }

    public String getSumMoms1()
    {
        return reportQuery("SELECT SUM(MOMS1) AS MOMS1 FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "moms1", ReturnType.ReturnFloat);
    }

    public String getSumMoms2()
    {
        return reportQuery("SELECT SUM(MOMS2) AS MOMS2 FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "moms2", ReturnType.ReturnFloat);
    }

    public String getSumMoms3()
    {
        return reportQuery("SELECT SUM(MOMS3) AS MOMS3 FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "moms3", ReturnType.ReturnFloat);
    }

    public String getSumMoms4()
    {
        return reportQuery("SELECT SUM(MOMS4) AS MOMS4 FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '" + General.getInstance().int2Str(ReceiptType.NORMAL.ordinal()) + "'", "moms4", ReturnType.ReturnFloat);
    }

    public String getSumReceiptRefund()
    {
        return reportQuery("SELECT SUM(" + DefinedVariables.getInstance().TABLE_RECEIPTS + "_ID) AS SUMREFUND FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_ID = '7'", "sumrefund", ReturnType.ReturnFloat);
    }

    public int getLastestReceiptId()
    {
        String queryResult = reportQuery("SELECT MAX(" + DefinedVariables.getInstance().TABLE_RECEIPTS + "_ID) AS MAX_RECEIPT_ID FROM " + DefinedVariables.getInstance().TABLE_RECEIPTS, "MAX_RECEIPT_ID", ReturnType.ReturnInt);

        return Integer.parseInt(queryResult);
    }

}
