/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.db.dao;

import pos_client.common.Core;
import static pos_client.common.DefinedVariables.TABLE_PROFO;
import pos_client.common.General;
import pos_client.windows.Log;

public class ProfoDAO extends DAO
{

    public ProfoDAO()
    {

    }

    public int getProfoNr()
    {
        Core.getInstance().getLog().log("Hämtar profo nr", Log.LogLevel.DESCRIPTIVE);

        return General.getInstance().str2Int(getTableColumn(TABLE_PROFO, "nr", ReturnType.ReturnInt));
    }

    public void increaseProfoNr(int currentNr)
    {
        Core.getInstance().getLog().log("Uppdaterar profo nr med +1", Log.LogLevel.DESCRIPTIVE);

        updateTableColumn(TABLE_PROFO, "nr", General.getInstance().int2Str(getProfoNr() + 1));
    }

}
