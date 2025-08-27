/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.communication;

import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.communication.comHandler.ComType;
import pos_client.db.dao.SettingsDAO;
import pos_client.windows.Log;

/**
 *
 * @author Server
 */
public class CashBox extends Com
{

    private SettingsDAO settingsDAO = new SettingsDAO();

    private ComType type = ComType.CASHBOX;
    private String name = DefinedVariables.getInstance().SETTING_COM_CASHBOX;

    public CashBox()
    {

    }

    public boolean loadStatus()
    {
        String type = settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_COM_CASHBOX_USE);
        String connect = settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_COM_CASHBOX_CONNECT);

        if (connect.compareTo(DefinedVariables.TRUE) == 0)
        {
            return connect();
        }

        return false;
    }

    public boolean connect()
    {
        if (open(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_PORT)))
        {
            int baud = General.getInstance().str2Int(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_BAUDRATE));
            int dataBits = General.getInstance().str2Int(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_DATABITS));
            int stopBit = General.getInstance().str2Int(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_STOPBIT));
            int parity = General.getInstance().str2Int(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_PARITY));

            return params(baud, dataBits, stopBit, parity);

        } else
        {
            Core.getInstance().getLog().log("connect - Port is not created or opened", Log.LogLevel.CRITICAL);
        }

        return false;
    }

}
