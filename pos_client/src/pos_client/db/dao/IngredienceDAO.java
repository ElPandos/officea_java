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
import pos_client.Ingredience;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class IngredienceDAO extends DAO
{

    Database db = null;

    public IngredienceDAO()
    {
        db = Database.getInstance();
    }

    public ObservableList<Ingredience> getIngrediences(int category_id)
    {
        ObservableList<Ingredience> ingredients = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar ingredienser för kategori: " + category_id, Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_INGREDIENCES + " WHERE " + DefinedVariables.getInstance().TABLE_INGREDIENCES_CATEGORY + "_id = " + category_id;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_INGREDIENCES + "_id");
                        String name = set.getString("name");
                        float price = set.getFloat("price");

                        ingredients.add(new Ingredience(id, name, price, category_id));
                    }

                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getIngrediences() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getIngrediences() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getIngrediences() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return ingredients;
    }

    public ObservableList<Ingredience> getIngrediences()
    {
        ObservableList<Ingredience> ingrediences = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Laddar ingrediences i inställningar", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_INGREDIENCES;

            if (db.getInstance().query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();

                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_INGREDIENCES + "_id");
                        String name = set.getString("name");
                        float price = set.getFloat("price");
                        int category_id = set.getInt(DefinedVariables.getInstance().TABLE_INGREDIENCES_CATEGORY + "_id");

                        ingrediences.add(new Ingredience(id, name, price, category_id));
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getIngrediences() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getIngrediences() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getIngrediences() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return ingrediences;
    }

    public void add(String name, String price, int category_id)
    {

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Lägger till ingrediens i databasen", Log.LogLevel.NORMAL);

            String query = "INSERT INTO " + DefinedVariables.TABLE_INGREDIENCES + " (name, price, " + DefinedVariables.TABLE_INGREDIENCES_CATEGORY + "_id) VALUES"
                    + " ('" + name
                    + "', '" + price.replace(',', '.') // Db cant handle , so convert to
                    + "', '" + General.getInstance().int2Str(category_id)
                    + "');";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade ingredience: " + name, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("add - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("add - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public boolean remove(int id)
    {
        if (db.isConnected())
        {
            return db.deleteRow(DefinedVariables.getInstance().TABLE_INGREDIENCES, id);
        } else
        {
            Core.getInstance().getLog().log("remove() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
        return false;
    }

    public boolean update(Ingredience updatedIngredience)
    {

        boolean result = true;

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Uppdaterar vald ingrediens " + updatedIngredience.getId() + " i db...", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_INGREDIENCES
                    + " SET name = '" + updatedIngredience.getName()
                    + "', price = '" + General.getInstance().float2StrDB(updatedIngredience.getPrice(), 2)
                    + "', " + DefinedVariables.getInstance().TABLE_INGREDIENCES_CATEGORY + "_id = '" + General.getInstance().int2Str(updatedIngredience.getCategory())
                    + "' WHERE " + DefinedVariables.getInstance().TABLE_INGREDIENCES + "_id = '" + General.getInstance().int2Str(updatedIngredience.getId()) + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade ingrediens: " + updatedIngredience.getName() + " med id: " + updatedIngredience.getId(), Log.LogLevel.DESCRIPTIVE);
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
