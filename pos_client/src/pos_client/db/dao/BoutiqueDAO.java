/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import static pos_client.common.DefinedVariables.TABLE_BOUTIQUE;
import pos_client.windows.Log;

/**
 *
 * @author eit-asn
 */
public class BoutiqueDAO extends DAO
{

    public BoutiqueDAO()
    {

    }

    public void add(String org_no, String name, String address1, String address2, String postal, String city, String phone, String website)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Lägger till butiksinfo i databasen", Log.LogLevel.NORMAL);

            String query = "UPDATE " + TABLE_BOUTIQUE + " SET "
                    + "org_no='" + org_no
                    + "', name='" + name
                    + "', address1='" + address1
                    + "', address2='" + address2
                    + "', postal='" + postal
                    + "', city='" + city
                    + "', phone='" + phone
                    + "', website='" + website
                    + "' WHERE BOUTIQUE_ID = '1'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade Butiksinfo i databasen: ", Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("add() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("add() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public void set(String org_no, String name, String address1, String address2, String postal, String city, String phone, String website)
    {
        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Lägger till butiksinfo i databasen", Log.LogLevel.NORMAL);

            String query = "UPDATE " + TABLE_BOUTIQUE + " SET "
                    + "org_no='" + org_no
                    + "', name='" + name
                    + "', address1='" + address1
                    + "', address2='" + address2
                    + "', postal='" + postal
                    + "', city='" + city
                    + "', phone='" + phone
                    + "', website='" + website
                    + "' WHERE BOUTIQUE_ID = '1'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade Butiksinfo i databasen: ", Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("add() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("add() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public String getCompanyName()
    {
        return reportQuery("SELECT * FROM " + DefinedVariables.getInstance().TABLE_BOUTIQUE, "name", ReturnType.ReturnString);
    }

    public String getCompanyAdress1()
    {
        return reportQuery("SELECT * FROM " + DefinedVariables.getInstance().TABLE_BOUTIQUE, "address1", ReturnType.ReturnString);
    }

    public String getCompanyAdress2()
    {
        return reportQuery("SELECT * FROM " + DefinedVariables.getInstance().TABLE_BOUTIQUE, "address2", ReturnType.ReturnString);
    }

    public String getCompanyPostal()
    {
        return reportQuery("SELECT * FROM " + DefinedVariables.getInstance().TABLE_BOUTIQUE, "postal", ReturnType.ReturnString);
    }

    public String getCompanyCity()
    {
        return reportQuery("SELECT * FROM " + DefinedVariables.getInstance().TABLE_BOUTIQUE, "city", ReturnType.ReturnString);
    }

    public String getOrgNumber()
    {
        String orgNr = reportQuery("SELECT * FROM " + DefinedVariables.getInstance().TABLE_BOUTIQUE, "org_no", ReturnType.ReturnString);

        // TODO lägg in en koll där man skriver in den ist
        if (orgNr.contains("SE"))
        {
            orgNr = orgNr.replace("SE", "");
        }

        if (orgNr.length() < 10)
        {
            while (orgNr.length() != 10)
            {
                orgNr += "0";
            }
        }

        return orgNr;
    }

    public String getPhoneNumber()
    {
        return reportQuery("SELECT * FROM " + DefinedVariables.getInstance().TABLE_BOUTIQUE, "phone", ReturnType.ReturnString);
    }

    public String getCompanyWebsite()
    {
        return reportQuery("SELECT * FROM " + DefinedVariables.getInstance().TABLE_BOUTIQUE, "website", ReturnType.ReturnString);
    }

}
