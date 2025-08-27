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
import pos_client.Category;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.windows.Log;

public class ArticleCategoryDAO extends DAO
{

    public ArticleCategoryDAO()
    {

    }

    public Map<Integer, Category> getArticleCategories()
    {
        Map<Integer, Category> articleCategories = new HashMap<>();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar alla artikel kategorier", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + "_id");
                        String name = set.getString("name");
                        int order = set.getInt("orderNr");

                        articleCategories.put(id, new Category(id, name, order));
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getArticleCategories() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getArticleCategories() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getArticleCategories() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return articleCategories;
    }

    public ObservableList<Category> getCategories()
    {
        ObservableList<Category> categories = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar alla Artikelkategorier", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();

                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + "_id");
                        String name = set.getString("name");
                        int order = set.getInt("ordernr");

                        categories.add(new Category(id, name, order));
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

        return categories;
    }

    public void add(Category category)
    {

        add(category.getName(), Integer.toString(category.getOrder()));
    }

    public void add(String name, String order)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Lägger till artikelkategori", Log.LogLevel.NORMAL);

            String query = "INSERT INTO " + DefinedVariables.TABLE_ARTICLES_CATEGORY + " (name, ordernr) VALUES"
                    + " ('" + name
                    + "', '" + order
                    + "');";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade arikelkategori: " + name, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("add() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("add() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public boolean update(Category updatedCategory)
    {
        boolean result = true;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Uppdaterar vald artikelkategori " + updatedCategory.getId() + " i db...", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY
                    + " SET name = '" + updatedCategory.getName()
                    + "' WHERE " + DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + "_id = '" + General.getInstance().int2Str(updatedCategory.getId()) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade artikelkategori: " + updatedCategory.getName() + " med id: " + updatedCategory.getId(), Log.LogLevel.DESCRIPTIVE);
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

    public boolean remove(int id)
    {
        if (db.isConnected())
        {
            return db.deleteRow(DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY, id);
        } else
        {
            Core.getInstance().getLog().log("remove() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
        return false;
    }

    /*
    public int getArticleCategory(int category) {

        int ret = General.getInstance().INVALID_VALUE;

        if (db.isConnected()) {

            Core.getInstance().getLog().log("Hämtar artikel kategori: " + category, Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + " WHERE category = " + General.getInstance().int2Str(category);

            if (db.query(query, false)) {
                try {
                    ResultSet set = db.getResult();
                    while (set.next()) {
                        ret = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + "_id");
                        break;
                    }
                } catch (SQLException ex) {
                    Core.getInstance().getLog().log("getArticleCategory() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else {
                Core.getInstance().getLog().log("getArticleCategory() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else {
            Core.getInstance().getLog().log("getArticleCategory() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return ret;
    }
     */
}
