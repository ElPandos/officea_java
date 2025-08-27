/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.windows;

import pos_client.ResourcesHandler;
import pos_client.common.General;
import pos_client.common.Core;
import pos_client.common.Updater;
import pos_client.communication.comHandler;
import pos_client.db.Database;
import pos_client.fxml.log.LogController;
import pos_client.terminals.TerminalHandler;

public class Log extends Screen
{

    LogController logController = null;
    Updater updater = null;

    public enum LogLevel
    {
        SYSTEM, // System output som alla ska se
        CRITICAL, // Fel i funktioner
        NORMAL, // Headlines för funktioner
        DESCRIPTIVE, // Vad som är resultatet av funktioner
        ALL // Allt
    }

    public Log()
    {
        this.start(); // Startar updateraren

        title = "POS Log";
        loadScreen(ResourcesHandler.getInstance().getFxmlLog(), ResourcesHandler.getInstance().getThemeMain());

        stage.setAlwaysOnTop(true);

        logController = loader.getController();
    }

    public void setLogLevel(LogLevel level)
    {
        logController.setLogLevel(level);
    }

    public void logSpeed(String text)
    {
        if (logController.isSpeedChecked())
        {
            logController.getLogTextArea().appendText(text + General.getInstance().newLine);
        }
    }

    public void log(String text, LogLevel level)
    {
        if (level.ordinal() <= logController.getLogLevel().ordinal())
        {
            if (logController != null && logController.getLogTextArea() != null)
            {
                logController.getLogTextArea().appendText("[" + logLevelPrefix(level) + "] " + timeLog() + " > " + text + General.getInstance().newLine);
            }
        }
    }

    private String timeLog()
    {
        return General.getInstance().date() + " : " + General.getInstance().time();
    }

    public LogController getLogController()
    {
        return logController;
    }

    public String logLevel2String(LogLevel level)
    {
        String ret = "ERROR";

        switch (level)
        {
            case ALL:
                ret = "All";
                break;
            case NORMAL:
                ret = "Normal";
                break;
            case DESCRIPTIVE:
                ret = "Descriptive";
                break;
            case CRITICAL:
                ret = "Critical";
                break;
        }

        return ret;
    }

    public LogLevel string2LogLevel(String level)
    {
        LogLevel ret = LogLevel.CRITICAL;

        if (level.contains("All"))
        {
            ret = LogLevel.ALL;
        }

        if (level.contains("Normal"))
        {
            ret = LogLevel.NORMAL;
        }

        if (level.contains("Descriptive"))
        {
            ret = LogLevel.DESCRIPTIVE;
        }

        if (level.contains("Critical"))
        {
            ret = LogLevel.CRITICAL;
        }

        return ret;
    }

    public String logLevelPrefix(LogLevel level)
    {
        String ret = "ERROR";

        switch (level)
        {
            case ALL:
                ret = "All";
                break;
            case NORMAL:
                ret = "Norm";
                break;
            case DESCRIPTIVE:
                ret = "Desc";
                break;
            case CRITICAL:
                ret = "<CRIT>";
                break;
            case SYSTEM:
                ret = "SYSTEM";
                break;
        }

        return ret;
    }

    @Override
    public void update()
    {
        logController.setDatabaseStatus(Database.getInstance().isConnected());

        TerminalHandler terminalHandler = Core.getInstance().getTerminalHandler();
        if (terminalHandler != null)
        {
            logController.setTerminalStatus(terminalHandler.getActiveTerminal() != null && terminalHandler.getActiveTerminal().isConnected());
        }

        comHandler commHandler = Core.getInstance().getComHandler();
        logController.setControlUnitStatus(commHandler != null && commHandler.isConnected(comHandler.ComType.CONTROLUNIT));
        logController.setCashBoxStatus(commHandler != null && commHandler.isConnected(comHandler.ComType.CASHBOX));

    }

}
