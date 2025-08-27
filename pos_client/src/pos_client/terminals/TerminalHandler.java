/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.terminals;

import pos_client.common.DefinedVariables;
import pos_client.common.Core;
import pos_client.db.dao.SettingsDAO;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class TerminalHandler {

    private Terminal activeTerminal = null;

    public enum TerminalType {
        NONE,
        NETS,
        SAMPORT,
        POINT,
        SAM4S
    }

    public TerminalHandler() {
    }

    public Jbaxi jbaxi() {
        if (activeTerminal == null) {
            activeTerminal = new Jbaxi();
        }

        return (Jbaxi) activeTerminal;
    }

    public Samport samport() {
        if (activeTerminal == null) {
            activeTerminal = new Samport();
        }

        return (Samport) activeTerminal;
    }

    public boolean isConnected() {
        if (activeTerminal != null) {
            return activeTerminal.isConnected();
        } else {
            return false;
        }
    }

    public boolean close() {
        if (activeTerminal != null) {
            return activeTerminal.close();
        } else {
            return false;
        }
    }

    public Terminal getActiveTerminal() {
        return activeTerminal;
    }

    public boolean connect(TerminalType type) {
        switch (type) {
            case NETS:
                if (jbaxi().connect()) {
                    Core.getInstance().getLog().log("Trying to connect to: " + getActiveTerminal().getName() + " with version: " + getActiveTerminal().getVersion(), Log.LogLevel.DESCRIPTIVE);
                    return true;
                } else {
                    Core.getInstance().getLog().log(getActiveTerminal().getName() + " failed to connect!", Log.LogLevel.CRITICAL);
                }
                break;
            case SAMPORT:
                break;
            case POINT:
                break;
            case SAM4S:
                break;
        }

        return false;
    }

    public void loadTerminalStatus() {

        Core.getInstance().getLog().log("Laddar statusen på sparad terminal", Log.LogLevel.DESCRIPTIVE);

        SettingsDAO settingsDAO = new SettingsDAO();

        String terminalType = settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_TERMINALS_USE);
        String connected = settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_TERMINALS_CONNECT);

        if (connected.compareTo(DefinedVariables.TRUE) == 0) {
            if (connect(str2TerminalType(terminalType))) {
                Core.getInstance().getLog().log("Auto connectat till sparad terminal", Log.LogLevel.NORMAL);
            }
        }
    }

    public TerminalType str2TerminalType(String type) {
        if (type.compareTo(Jbaxi.NETS) == 0) {
            return TerminalType.NETS;
        }

        if (type.compareTo(Samport.SAMPORT) == 0) {
            return TerminalType.SAMPORT;
        }

        /*
        if (type.compareTo(DefinedVariables.TRUE) == 0) {

        }

        if (type.compareTo(DefinedVariables.TRUE) == 0) {

        }
         */
        return TerminalType.NONE;
    }

    public String terminalType2Str(TerminalType type) {
        switch (type) {
            case NETS:
                return Jbaxi.NETS;
            case SAMPORT:
                return Samport.SAMPORT;
            case POINT:
                break;
            case SAM4S:
                break;
        }

        return null;
    }

}
