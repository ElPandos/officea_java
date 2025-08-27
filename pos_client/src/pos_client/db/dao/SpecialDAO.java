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
import pos_client.SpecialModel;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class SpecialDAO extends DAO
{

    public SpecialDAO()
    {
    }

    public ObservableList<SpecialModel> getSpecials(int category_id)
    {
        ObservableList<SpecialModel> specials = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar special med kategori: " + category_id, Log.LogLevel.DESCRIPTIVE);

            //SpecialCategoryDAO specialCategoryDAO = new SpecialCategoryDAO();
            //int categoryFromId = specialCategoryDAO.getSpecialCategory(category);
            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL + " WHERE " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY + "_id = " + category_id;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL + "_id");
                        String name = set.getString("name");
                        float price = set.getFloat("price");
                        String description = set.getString("description");

                        specials.add(new SpecialModel(id, name, price, description, category_id));
                    }

                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getArticles() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getArticles() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getArticles() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return specials;
    }

    public ObservableList<SpecialModel> getSpecials()
    {
        ObservableList<SpecialModel> specials = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar alla specials", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL + "_id");
                        String name = set.getString("name");
                        float price = set.getFloat("price");
                        String description = set.getString("description");
                        int category_id = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY + "_id");

                        specials.add(new SpecialModel(id, name, price, description, category_id));
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getSpecials() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getSpecials() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getSpecials() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return specials;
    }

    public boolean add(SpecialModel addedSpecial)
    {
        boolean result = true;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Lägger till en anstäld i databasen", Log.LogLevel.NORMAL);

            String query = "INSERT INTO " + DefinedVariables.TABLE_ARTICLES_SPECIAL + " (name, price, description, " + DefinedVariables.TABLE_ARTICLES_SPECIAL + "_category_id) VALUES"
                    + " ('" + addedSpecial.getName()
                    + "', '" + addedSpecial.getPrice()
                    + "', '" + addedSpecial.getDescription()
                    + "', '" + addedSpecial.getCategoryId()
                    + "');";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade Special: " + addedSpecial.getName(), Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("add() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
                result = false;
            }
        } else
        {
            Core.getInstance().getLog().log("add() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
            result = false;
        }

        return result;
    }

    public boolean update(SpecialModel updatedSpecial)
    {
        boolean result = true;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Uppdaterar vald Special " + updatedSpecial.getId() + " i db...", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL
                    + " SET name = '" + updatedSpecial.getName()
                    + "', price = '" + updatedSpecial.getPrice()
                    + "', " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL_CATEGORY + "_id = '" + updatedSpecial.getCategoryId()
                    + "', description = '" + updatedSpecial.getDescription()
                    + "' WHERE " + DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL + "_id = '" + General.getInstance().int2Str(updatedSpecial.getId()) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade Special: " + updatedSpecial.getName() + " med id: " + updatedSpecial.getId(), Log.LogLevel.DESCRIPTIVE);
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
            return db.deleteRow(DefinedVariables.getInstance().TABLE_ARTICLES_SPECIAL, id);
        } else
        {
            Core.getInstance().getLog().log("remove() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return false;
    }

    public SpecialModel getSpecial(int id)
    {
        ObservableList<SpecialModel> specials = getSpecials();

        for (SpecialModel special : specials)
        {
            if (special.getId() == id)
            {
                return special;
            }
        }

        return null;
    }
}
