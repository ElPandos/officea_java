/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class VatDAO extends DAO
{

    public VatDAO()
    {

    }

    public enum VatType
    {
        VAT1,
        VAT2,
        VAT3,
        VAT4
    };

    class Vats
    {

        int id;
        float amount;
    };

    public VatType getVat(float vat)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar moms id med värde: " + vat, Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_VAT
                    + " WHERE value = " + vat;

            if (db.getInstance().query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        return VatType.values()[set.getInt(DefinedVariables.getInstance().TABLE_VAT + "_id")];
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getVat() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getVat() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getVat() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return null;
    }

    public float getVat(VatType vatType)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar moms-sats: " + vatType.ordinal(), Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_VAT;

            if (db.getInstance().query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();

                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_VAT + "_id");
                        if (id == vatType.ordinal())
                        {
                            return set.getFloat("value");
                        }
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getVat() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getVat() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getVat() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return -1;
    }

}
