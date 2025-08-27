/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.common;

import pos_client.windows.Log;
import pos_client.LoginHandler;
import pos_client.communication.comHandler;
import pos_client.terminals.TerminalHandler;
import pos_client.windows.ScreenHandler;

/**
 *
 * @author Laptop
 */
public class Core extends General
{

    private static LoginHandler loginHandler = null;
    private static TerminalHandler terminalHandler = null;
    private static comHandler commHandler = null;
    private static PrinterHandler printerHandler = null;
    private static Core instance = null;
    private static Log log = null;
    private static ScreenHandler screenHandler = null;
    private static ControlHandler controlHandler = null;

    protected Core()
    {
        // Exists only to defeat instantiation.
    }

    public static Core getInstance()
    {
        if (instance == null)
        {
            instance = new Core();

            log = new Log();
            log.getLogController().loadLogLevel();

            screenHandler = new ScreenHandler();
            terminalHandler = new TerminalHandler();
            loginHandler = new LoginHandler();
            commHandler = new comHandler();
            printerHandler = new PrinterHandler();
            screenHandler = new ScreenHandler();
            controlHandler = new ControlHandler();
        }

        return instance;
    }

    public TerminalHandler getTerminalHandler()
    {
        return this.terminalHandler;
    }

    public LoginHandler getLoginHandler()
    {
        return this.loginHandler;
    }

    public comHandler getComHandler()
    {
        return this.commHandler;
    }

    public ScreenHandler getScreenHandler()
    {
        return this.screenHandler;
    }

    public PrinterHandler getPrinterHandler()
    {
        return this.printerHandler;
    }

    public Log getLog()
    {
        return this.log;
    }

    public ControlHandler getControlHandler()
    {
        return this.controlHandler;
    }
}
