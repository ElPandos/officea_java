/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.TextArea;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class AboutDAO extends DAO
{

    public AboutDAO()
    {

    }

    public void getInfo(TextArea txtManufacturer)
    {
        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Laddar system/kassa info", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_CASHREGISTER;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        txtManufacturer.setText("Tillverkare: " + set.getString("man_name") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Adress: " + set.getString("man_address") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Post.nr: " + set.getString("man_postal") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Kontakt: " + set.getString("man_contact") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Org.nr: " + set.getString("man_org_no") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Mobil: " + set.getString("man_phone") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Mail: " + set.getString("man_email") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Kassa.nr: " + set.getString("cashregister_nr") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Model: " + set.getString("cashregister_model") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Version: " + set.getString("cashregister_version") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Senaste updatering: " + set.getString("cashregister_last_update") + General.getInstance().newLine);
                        txtManufacturer.setText(txtManufacturer.getText() + "Senaste backup: " + set.getString("cashregister_last_backup") + General.getInstance().newLine);
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getInfo() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getInfo() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getInfo() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }
}
