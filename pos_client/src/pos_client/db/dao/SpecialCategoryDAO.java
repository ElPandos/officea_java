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

/**
 *
 * @author Laptop
 */
public class SpecialCategoryDAO extends DAO
{

    public SpecialCategoryDAO()
    {

    }

    public Map<Integer, Category> getSpecialCategories()
    {
        Map<Integer, Category> specialCategories = new HashMap<>();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Laddar special kategorier", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY + "_id");
                        String name = set.getString("name");
                        int order = set.getInt("orderNr");

                        specialCategories.put(id, new Category(id, name, order));
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getSpecialCategories() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getSpecialCategories() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getSpecialCategories() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return specialCategories;
    }

    public ObservableList<Category> getCategories()
    {

        ObservableList<Category> categories = FXCollections.observableArrayList();

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Hämtar alla specialkategorier", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();

                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY + "_id");
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

            String query = "INSERT INTO " + DefinedVariables.TABLE_ARTICLES_SPECIAL_CATEGORY + " (name, ordernr) VALUES"
                    + " ('" + name
                    + "', '" + order
                    + "');";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade specialkategori: " + name, Log.LogLevel.DESCRIPTIVE);
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

            Core.getInstance().getLog().log("Uppdaterar vald specialkategor8 " + updatedCategory.getId() + " i db...", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY
                    + " SET name = '" + updatedCategory.getName()
                    + "' WHERE " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY + "_id = '" + General.getInstance().int2Str(updatedCategory.getId()) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade specialkategori: " + updatedCategory.getName() + " med id: " + updatedCategory.getId(), Log.LogLevel.DESCRIPTIVE);
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
            return db.deleteRow(DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY, id);
        } else
        {
            Core.getInstance().getLog().log("remove() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
        return false;
    }

    /*
    public int getSpecialCategory(int category) {

        int ret = General.getInstance().INVALID_VALUE;

        if (db.isConnected()) {

            Core.getInstance().getLog().log("Hämtar special kategorier", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY + " WHERE category = " + General.getInstance().int2Str(category);

            if (db.query(query, false)) {
                try {
                    ResultSet set = db.getResult();
                    while (set.next()) {
                        ret = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY + "_id");
                        break;
                    }
                } catch (SQLException ex) {
                    Core.getInstance().getLog().log("getSpecialCategory() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else {
                Core.getInstance().getLog().log("getSpecialCategory() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else {
            Core.getInstance().getLog().log("getSpecialCategory() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return ret;
    }
     */
    public Category getSpecialCategory(int id)
    {

        ObservableList<Category> specialsCategories = getCategories();

        for (Category specialCategory : specialsCategories)
        {
            if (specialCategory.getId() == id)
            {
                return specialCategory;
            }
        }

        return null;
    }
}
