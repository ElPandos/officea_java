/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.communication;

import pos_client.UserModel;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class comHandler
{

    private VFD VFD = null;
    private ControlUnit controlUnit = null;
    private CashBox cashBox = null;

    private Com activeComm = null;

    public enum ComType
    {
        NONE,
        VFD,
        CONTROLUNIT,
        CASHBOX
    }

    public comHandler()
    {
        VFD = new VFD();
        controlUnit = new ControlUnit();
        cashBox = new CashBox();
    }

    public void loadComStatus()
    {
        Core.getInstance().getLog().log("Laddar statusen på sparade COM-enheter", Log.LogLevel.DESCRIPTIVE);

        if (VFD.loadStatus())
        {
            Core.getInstance().getLog().log("Lyckades autoconnectade med " + DefinedVariables.SETTING_COM_VFD, Log.LogLevel.DESCRIPTIVE);
        } else
        {
            Core.getInstance().getLog().log("Lyckades INTE autoconnectade med " + DefinedVariables.SETTING_COM_VFD, Log.LogLevel.CRITICAL);
        }

        if (controlUnit.loadStatus())
        {
            Core.getInstance().getLog().log("Lyckades autoconnectade med " + DefinedVariables.SETTING_COM_CONTROLUNIT, Log.LogLevel.DESCRIPTIVE);
        } else
        {
            Core.getInstance().getLog().log("Lyckades INTE autoconnectade med " + DefinedVariables.SETTING_COM_CONTROLUNIT, Log.LogLevel.CRITICAL);
        }

        if (cashBox.loadStatus())
        {
            Core.getInstance().getLog().log("Lyckades autoconnectade med " + DefinedVariables.SETTING_COM_CASHBOX, Log.LogLevel.DESCRIPTIVE);
        } else
        {
            Core.getInstance().getLog().log("Lyckades INTE autoconnectade med " + DefinedVariables.SETTING_COM_CASHBOX, Log.LogLevel.CRITICAL);
        }

    }

    public boolean isConnected(ComType type)
    {
        switch (type)
        {
            case VFD:
                return (VFD != null && VFD.isOpen());

            case CONTROLUNIT:
                return (controlUnit != null && controlUnit.isOpen());

            case CASHBOX:
                return (cashBox != null && cashBox.isOpen());

            default:
                return false;
        }
    }

    public boolean connect(ComType type)
    {
        switch (type)
        {
            case VFD:
                if (!VFD.isOpen())
                {
                    return VFD.connect();
                }
                break;
            case CONTROLUNIT:
                if (!controlUnit.isOpen())
                {
                    return controlUnit.connect();
                }
                break;
            case CASHBOX:
                if (!cashBox.isOpen())
                {
                    return cashBox.connect();
                }
                break;
        }

        return false;
    }

    public void write(ComType type, String data)
    {
        switch (type)
        {
            case VFD:
                if (VFD.isOpen())
                {
                    VFD.write(data);
                }
                break;
            case CONTROLUNIT:
                if (controlUnit.isOpen())
                {
                    controlUnit.write(data);
                }
                break;
            case CASHBOX:
                if (cashBox.isOpen())
                {
                    cashBox.write(data);
                }
                break;
        }
    }

    public String read(ComType type)
    {
        switch (type)
        {
            case VFD:
                if (VFD.isOpen())
                {
                    return VFD.read();
                }
                break;

            case CONTROLUNIT:
                if (controlUnit.isOpen())
                {
                    return controlUnit.read();
                }
                break;

            case CASHBOX:
                if (cashBox.isOpen())
                {
                    return cashBox.read();
                }
                break;
        }

        return "Error...";
    }

    public boolean close(ComType type)
    {
        switch (type)
        {
            case VFD:
                return VFD.close();
            case CONTROLUNIT:
                return controlUnit.close();
            case CASHBOX:
                return cashBox.close();
        }

        return false;
    }

    public boolean closeAll()
    {
        return (VFD.close() && controlUnit.close() && cashBox.close());
    }

    public ComType str2ComType(String type)
    {
        if (type.compareTo(DefinedVariables.SETTING_COM_VFD) == 0)
        {
            return ComType.VFD;
        }

        if (type.compareTo(DefinedVariables.SETTING_COM_CONTROLUNIT) == 0)
        {
            return ComType.CONTROLUNIT;
        }

        if (type.compareTo(DefinedVariables.SETTING_COM_CASHBOX) == 0)
        {
            return ComType.CASHBOX;
        }

        return ComType.NONE;
    }

    public String ComType2Str(ComType type)
    {
        switch (type)
        {
            case VFD:
                return DefinedVariables.SETTING_COM_VFD;
            case CONTROLUNIT:
                return DefinedVariables.SETTING_COM_CONTROLUNIT;
            case CASHBOX:
                return DefinedVariables.SETTING_COM_CASHBOX;
            default:
                return "Error...";
        }
    }

    public void openCashBox()
    {
        Core.getInstance().getLoginHandler().getUser().cashboxOpen(UserModel.CashboxType.AUTOMATIC);
        write(ComType.CASHBOX, "open");
    }

    public ControlUnit getControlUnit()
    {
        return controlUnit;
    }
}
