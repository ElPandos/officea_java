/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import pos_client.common.DefinedVariables;

/**
 *
 * @author Server
 */
public class CashBoxDAO extends DAO
{

    public CashBoxDAO()
    {

    }

    public String getCashBoxOpenManual()
    {
        return reportQuery("SELECT SUM(OPEN_MANUAL) AS MANUAL FROM " + DefinedVariables.getInstance().TABLE_CASHBOX, "manual", ReturnType.ReturnInt);
    }

    public String getCashBoxOpenAutomatic()
    {
        return reportQuery("SELECT SUM(OPEN_AUTOMATIC) AS AUTOMATIC FROM " + DefinedVariables.getInstance().TABLE_CASHBOX, "automatic", ReturnType.ReturnInt);
    }
}
