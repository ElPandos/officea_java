/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pos_client.ArticleModel;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.Receipt;
import pos_client.SaleModel;
import pos_client.SaleModel.SaleType;
import pos_client.UserModel;
import pos_client.common.Core;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.windows.Log;

public class SalesDAO extends DAO
{

    ArticleDAO articleDAO = new ArticleDAO();

    public SalesDAO()
    {

    }

    public int SaleTypeToDb(SaleModel.SaleType type)
    {
        return (type.ordinal() + 1);
    }

    public SaleModel.SaleType SaleTypeFromDb(int type)
    {
        return SaleModel.SaleType.values()[type - 1];
    }

    public String SaleType2Str(SaleModel.SaleType type)
    {
        String ret = "ERROR";
        switch (type)
        {
            case NORMAL:
                ret = "NORMAL";
                break;
            case REMOVED:
                ret = "REMOVED";
                break;
        }

        return ret;
    }

    public SaleModel.SaleType str2SaleType(String type)
    {
        if (type.compareTo("NORMAL") != 0)
        {
            return SaleModel.SaleType.NORMAL;
        }

        if (type.compareTo("REMOVED") != 0)
        {
            return SaleModel.SaleType.REMOVED;
        }

        return SaleModel.SaleType.NONE;
    }

    public double getAllSalesValue(Receipt receipt)
    {
        float total = 0;
        for (SaleModel sale : getAllSales(receipt))
        {
            total += sale.getPriceIncm(true);
        }

        return total;
    }

    public ObservableList<ArticleModel> getAllSalesArticles(Receipt receipt)
    {
        ObservableList<ArticleModel> articles = FXCollections.observableArrayList();
        for (SaleModel sale : getAllSales(receipt))
        {
            articles.add(sale.getArticle());
        }

        return articles;
    }

    public ObservableList<SaleModel> getAllSales(Receipt receipt)
    {
        ObservableList<SaleModel> sales = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar alla försäljningar på kvittot", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_SALES
                    + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id = " + General.getInstance().int2Str(receipt.getReceiptId())
                    + " AND " + DefinedVariables.getInstance().TABLE_SALES_TYPE + "_id = " + General.getInstance().int2Str(SaleTypeToDb(SaleModel.SaleType.NORMAL));

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        /*
                            + TABLE_SALES + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                            + "createdate DATE,"
                            + "createtime TIME ,"
                            + "amount FLOAT ,"
                            + "ingredientsExtra VARCHAR(255),"
                            + "ingredientsExcluded VARCHAR(255),"
                            + "specials VARCHAR(255),"
                            + TABLE_RECEIPTS + "_id INTEGER,"
                            + TABLE_ARTICLES + "_id INTEGER,"
                            + TABLE_ARTICLES + "_name VARCHAR(255),"
                            + TABLE_USERS + "_id INTEGER,"
                            + TABLE_USERS + "_name VARCHAR(255),"
                            + TABLE_SALES_TYPE + "_id INTEGER, "
                            + TABLE_SALES_TYPE + "_name VARCHAR(255),"
                            + TABLE_RECEIPTS_TYPE + "_id INTEGER,"
                            + TABLE_RECEIPTS_TYPE + "_name VARCHAR(255),"
                            + "discountValue"
                         */

                        int salesId = set.getInt(DefinedVariables.getInstance().TABLE_SALES + "_id");
                        Date createDate = set.getDate("createdate");
                        Time createTime = set.getTime("createtime");
                        float amount = set.getFloat("amount");

                        String ingredientsExtra = set.getString("ingredientsExtra");
                        String ingredientsExcluded = set.getString("ingredientsExcluded");
                        String specials = set.getString("specials");

                        int receiptId = set.getInt(DefinedVariables.getInstance().TABLE_RECEIPTS + "_id");
                        int articleId = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES + "_id");
                        int userId = set.getInt(DefinedVariables.getInstance().TABLE_USERS + "_id");
                        int salesTypeId = set.getInt(DefinedVariables.getInstance().TABLE_SALES_TYPE + "_id");
                        int receiptTypeId = set.getInt(DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_id");

                        float discountValue = set.getFloat("discountValue");

                        ArticleModel article = articleDAO.getArticle(articleId);
                        
                        article.getSale().setAmount(amount);
                        article.getSale().setCreateDate(createDate);
                        article.getSale().setCreateTime(createTime);
                        article.getSale().setSaleType(SaleType.values()[salesTypeId]);
                        
                        if (article != null)
                        {
                            article.getSale().setIngrediencesExcluded(ingredientsExcluded);
                            article.getSale().setIngrediencesExtra(ingredientsExtra);
                            article.getSale().setSpecials(specials);
                        }

                        UserDAO userDAO = new UserDAO();
                        UserModel user = userDAO.getUser(userId);

                        article.getSale().setSaleId(salesId);
                        article.getSale().setReceiptId(receiptId);
                        article.getSale().setArticle(article);
                        article.getSale().setDiscount(discountValue);
                        article.getSale().setUser(user);
                        
                        sales.add(article.getSale());
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getAllSales() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getAllSales() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getAllSales() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return sales;
    }

    public ObservableList<SaleModel> getAllSalesInDiscount(Receipt receipt)
    {
        ObservableList<SaleModel> sales = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar alla försäljningar på ett kvitto", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_SALES
                    + " WHERE " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id = " + General.getInstance().int2Str(receipt.getReceiptId())
                    + " AND " + DefinedVariables.getInstance().TABLE_SALES_TYPE + "_id = " + General.getInstance().int2Str(SaleTypeToDb(SaleModel.SaleType.NORMAL));

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int salesId = set.getInt(DefinedVariables.getInstance().TABLE_SALES + "_id");
                        int receipId = set.getInt(DefinedVariables.getInstance().TABLE_RECEIPTS + "_id");
                        int articleId = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES + "_id");
                        String articleName = set.getString(DefinedVariables.getInstance().TABLE_ARTICLES + "_name");
                        int userId = set.getInt(DefinedVariables.getInstance().TABLE_USERS + "_id");
                        double amount = set.getLong("amount");
                        String ingredientsExtra = set.getString("ingredientsExtra");
                        String ingredientsExcluded = set.getString("ingredientsExcluded");
                        String specials = set.getString("specials");

                        ArticleDAO articleDAO = new ArticleDAO();
                        ArticleModel article = articleDAO.getArticle(articleId);
                        article.getSale().setIngrediencesExcluded(ingredientsExcluded);
                        article.getSale().setIngrediencesExtra(ingredientsExtra);
                        article.getSale().setSpecials(specials);

                        UserDAO userDAO = new UserDAO();
                        UserModel user = userDAO.getUser(userId);

                        sales.add(new SaleModel(salesId, receipId, user, article));
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getAllSales() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getAllSales() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getAllSales() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return sales;
    }

    public boolean changeSaleType(SaleType changeToType, Receipt receipt, int salesId)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Byter försäljnings typ", Log.LogLevel.NORMAL);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_SALES + " SET " + DefinedVariables.getInstance().TABLE_SALES_TYPE + "_id='" + General.getInstance().int2Str(SaleTypeToDb(changeToType)) + "'," + DefinedVariables.getInstance().TABLE_SALES_TYPE + "_name= '" + SaleType2Str(changeToType)+ "' WHERE " + DefinedVariables.getInstance().TABLE_SALES + "_id='" + General.getInstance().int2Str(salesId) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Ändrade säljtyp: " + General.getInstance().int2Str(SaleTypeToDb(changeToType)) + ", på kvitto nr: " + General.getInstance().int2Str(receipt.getNr()), Log.LogLevel.DESCRIPTIVE);
                return true;
            } else
            {
                Core.getInstance().getLog().log("changeSaleType() - Lyckades inte att ändra typen på försäljningen i databasen", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("changeSaleType() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return false;
    }

    public boolean changeReceiptId(int receiptId, int salesId)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Byter kvitto nr på sales", Log.LogLevel.NORMAL);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_SALES + " SET " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id='" + General.getInstance().int2Str(receiptId) + "' WHERE " + DefinedVariables.getInstance().TABLE_SALES + "_id='" + General.getInstance().int2Str(salesId) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Byter kvitto id: " + General.getInstance().int2Str(receiptId) + ", på sales id: " + General.getInstance().int2Str(salesId), Log.LogLevel.DESCRIPTIVE);
                return true;
            } else
            {
                Core.getInstance().getLog().log("changeSaleType() - Lyckades inte att ändra typen på försäljningen i databasen", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("changeSaleType() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return false;
    }

    public int getLastestSalesId()
    {
        String queryResult = reportQuery("SELECT MAX(" + DefinedVariables.getInstance().TABLE_SALES + "_ID) AS MAX_SALES_ID FROM " + DefinedVariables.getInstance().TABLE_SALES, "MAX_SALES_ID", ReturnType.ReturnInt);
        return Integer.parseInt(queryResult);
    }

    public int add(ArticleModel article, Receipt receipt)
    {
        if (db.isConnected())
        {
            ReceiptTypeDAO receiptTypeDAO = new ReceiptTypeDAO();

            Core.getInstance().getLog().log("Lägger till artikel i försäljning", Log.LogLevel.NORMAL);

            String query = "INSERT INTO " + DefinedVariables.getInstance().TABLE_SALES + " (createdate, createtime, amount, ingredientsExtra, ingredientsExcluded, specials, " + DefinedVariables.getInstance().TABLE_RECEIPTS + "_id, " + DefinedVariables.getInstance().TABLE_ARTICLES + "_id, " + DefinedVariables.getInstance().TABLE_ARTICLES + "_name, " + DefinedVariables.getInstance().TABLE_USERS + "_id, " + DefinedVariables.getInstance().TABLE_USERS + "_name, " + DefinedVariables.getInstance().TABLE_SALES_TYPE + "_id, " + DefinedVariables.getInstance().TABLE_SALES_TYPE + "_name, " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_id, " + DefinedVariables.getInstance().TABLE_RECEIPTS_TYPE + "_name) VALUES "
                    + "('" + General.getInstance().date() + "',"
                    + "'" + General.getInstance().time() + "',"
                    + "'" + General.getInstance().float2StrDB((article.getSale() != null ? article.getSale().getPriceTotalIncm(false) : article.getPriceIncm()), 2) + "',"
                    + "'" + (article.getSale() != null ? article.getSale().getIngredienceExtraStrDB() : "") + "',"
                    + "'" + (article.getSale() != null ? article.getSale().getIngredienceExcludedStrDB() : "") + "',"
                    + "'" + (article.getSale() != null ? article.getSale().getSpecialsStrDB() : "") + "',"
                    + "'" + General.getInstance().int2Str(receipt.getReceiptId()) + "',"
                    + "'" + General.getInstance().int2Str(article.getArticleId()) + "',"
                    + "'" + article.getName() + "',"
                    + "'" + General.getInstance().int2Str(receipt.getUser().getId()) + "',"
                    + "'" + receipt.getUser().getName() + "',"
                    + "'" + General.getInstance().int2Str(SaleTypeToDb(SaleModel.SaleType.NORMAL)) + "',"
                    + "'" + SaleType2Str(SaleModel.SaleType.NORMAL) + "',"
                    + "'" + General.getInstance().int2Str(receiptTypeDAO.receiptTypeToDb(receipt.getType())) + "',"
                    + "'" + receiptTypeDAO.receiptType2Str(ReceiptType.ONGOING) + "'"
                    + ");";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade artikel " + article.getName() + " i tabellen SALES!", Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("add() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("add() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return getLastestSalesId();
    }

    public void update(SaleModel sale)
    {
        if (db.isConnected())
        {
            if (sale != null)
            {
                Core.getInstance().getLog().log("Updaterar sales_id" + sale.getSaleId(), Log.LogLevel.NORMAL);

                String query = "UPDATE " + DefinedVariables.getInstance().TABLE_SALES
                        + " SET ingredientsExtra='" + sale.getIngredienceExtraStrDB() + "'"
                        + " , ingredientsExcluded='" + sale.getIngredienceExcludedStrDB() + "'"
                        + " , specials='" + sale.getSpecialsStrDB() + "'"
                        + " , amount='" + General.getInstance().float2StrDB(sale.getPriceTotalIncm(false), 2) + "'"
                        + " , discountValue ='" + General.getInstance().float2StrDB(sale.getDiscount(), 2) + "'"
                        + " WHERE " + DefinedVariables.getInstance().TABLE_SALES + "_id='" + sale.getSaleId() + "'";

                if (db.query(query, true))
                {
                    Core.getInstance().getLog().log("Updaterade sales_id: " + sale.getSaleId() + " i tabellen sales!", Log.LogLevel.DESCRIPTIVE);
                } else
                {
                    Core.getInstance().getLog().log("update() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("update() - Sale är null", Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("update() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public String getSoldItems()
    {
        return reportQuery("SELECT COUNT(T.ARTICLES_ID) AS SOLDARTICLES FROM " + DefinedVariables.getInstance().TABLE_SALES + " AS T LEFT JOIN " + DefinedVariables.getInstance().TABLE_ARTICLES + " AS T1 ON T.ARTICLES_ID = T1.ARTICLES_ID LEFT JOIN " + DefinedVariables.getInstance().TABLE_RECEIPTS + " AS T2 ON T.RECEIPTS_ID = T2.RECEIPTS_ID WHERE T2.RECEIPTS_TYPE_ID ='3' AND T1.ARTICLES_TYPE_ID ='1'", "soldarticles", ReturnType.ReturnInt);
    }

    public String getSoldServices()
    {
        return reportQuery("SELECT COUNT(T.ARTICLES_ID) AS SOLDARTICLES FROM " + DefinedVariables.getInstance().TABLE_SALES + " AS T LEFT JOIN " + DefinedVariables.getInstance().TABLE_ARTICLES + " AS T1 ON T.ARTICLES_ID = T1.ARTICLES_ID LEFT JOIN " + DefinedVariables.getInstance().TABLE_RECEIPTS + " AS T2 ON T.RECEIPTS_ID = T2.RECEIPTS_ID WHERE T2.RECEIPTS_TYPE_ID ='3' AND T1.ARTICLES_TYPE_ID ='2'", "soldarticles", ReturnType.ReturnInt);
    }

}
