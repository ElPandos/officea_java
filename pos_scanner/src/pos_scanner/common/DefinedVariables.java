/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner.common;

/**
 *
 * @author Laptop
 */
public class DefinedVariables
{

    private static DefinedVariables instance = null;

    protected DefinedVariables()
    {
        // Exists only to defeat instantiation.
    }

    public static DefinedVariables getInstance()
    {
        if (instance == null)
        {
            instance = new DefinedVariables();
        }
        return instance;
    }

    // Status
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    // Database tables
    public static final String TABLE_ARTICLES = "ARTICLES";
    public static final String TABLE_ARTICLES_TYPE = "ARTICLES_TYPE";
    public static final String TABLE_ARTICLES_CATEGORY = "ARTICLES_CATEGORY";

    public static final String TABLE_ARTICLES_SPECIAL = "ARTICLES_SPECIAL";
    public static final String TABLE_ARTICLES_SPECIAL_CATEGORY = "ARTICLES_SPECIAL_CATEGORY";

    public static final String TABLE_INGREDIENCES = "INGREDIENCES";
    public static final String TABLE_INGREDIENCES_CATEGORY = "INGREDIENCES_CATEGORY";

    public static final String TABLE_USERS = "USERS";
    public static final String TABLE_CUSTOMER = "CUSTOMER";
    public static final String TABLE_EMPLOYEE = "EMPLOYEES";

    public static final String TABLE_SECURITY_LEVELS = "SECURITY_LEVELS";

    public static final String TABLE_RECEIPTS = "RECEIPTS";
    public static final String TABLE_RECEIPTS_TYPE = "RECEIPTS_TYPE";

    public static final String TABLE_PROFO = "PROFO";

    public static final String TABLE_SALES = "SALES";
    public static final String TABLE_SALES_STATIC = "SALES_STATIC";
    public static final String TABLE_SALES_TYPE = "SALES_TYPE";

    public static final String TABLE_SETTINGS_DATABASE = "SETTINGS_DATABASE";
    public static final String TABLE_SETTINGS_LOCAL = "SETTINGS_LOCAL";

    public static final String TABLE_VAT = "VAT";
    public static final String TABLE_EXCHANGE = "EXCHANGE";

    public static final String TABLE_SUPPLEMENTS = "SUPPLEMENTS";

    public static final String TABLE_CASHBOX = "CASHBOX";
    public static final String TABLE_CASHREGISTER = "CASHREGISTER";
    public static final String TABLE_BOUTIQUE = "BOUTIQUE";
    public static final String TABLE_RECEIPTINFO = "RECEIPTINFO";

    public static final String SETTING_DATABASE_STATUS = "Database-Status";

    public static final String SETTING_DISPLAY_TYPE = "Display-Type";
    public static final String SETTING_DISPLAY_FULLSCREEN = "Display-Fullscreen";
    public static final String SETTING_UPDATE_PATH_CSS = "Update-Path-CSS";
    public static final String SETTING_UPDATE_THREAD = "Update-Thread";

    public static final String SETTING_DISCOUNT_1 = "Discount-1";
    public static final String SETTING_DISCOUNT_2 = "Discount-2";
    public static final String SETTING_DISCOUNT_3 = "Discount-3";
    public static final String SETTING_DISCOUNT_4 = "Discount-4";

    public static final String SETTING_COM_VFD = "VFD";
    public static final String SETTING_COM_VFD_USE = SETTING_COM_VFD + "-Use";
    public static final String SETTING_COM_VFD_CONNECT = SETTING_COM_VFD + "-Connect";

    public static final String SETTING_COM_CASHBOX = "CashBox";
    public static final String SETTING_COM_CASHBOX_USE = SETTING_COM_CASHBOX + "-Use";
    public static final String SETTING_COM_CASHBOX_CONNECT = SETTING_COM_CASHBOX + "-Connect";

    public static final String SETTING_COM_CONTROLUNIT = "ControlUnit";
    public static final String SETTING_COM_CONTROLUNIT_USE = SETTING_COM_CONTROLUNIT + "-Use";
    public static final String SETTING_COM_CONTROLUNIT_CONNECT = SETTING_COM_CONTROLUNIT + "-Connect";

    public static final String TABLE_COM_SUPPORTED = "COM_SUPPORTED";

    public static final String SETTING_COM_TYPE = "-Comm-Type";
    public static final String SETTING_COM_PORT = "-Comm-Port";
    public static final String SETTING_COM_BAUDRATE = "-Comm-Baudrate";
    public static final String SETTING_COM_DATABITS = "-Comm-DataBits";
    public static final String SETTING_COM_STOPBIT = "-Comm-StopBit";
    public static final String SETTING_COM_PARITY = "-Comm-Parity";

    public static final String SETTING_PRINTER_REPORT = "Printer-Report";
    public static final String SETTING_PRINTER_RECEIPT = "Printer-Receipt";
    public static final String SETTING_PRINTER_BONG_1 = "Printer-Bong-1";
    public static final String SETTING_PRINTER_BONG_2 = "Printer-Bong-2";
    public static final String SETTING_PRINTER_BONG_3 = "Printer-Bong-3";
    public static final String SETTING_PRINTER_BONG_4 = "Printer-Bong-4";

    public static final String SETTING_TERMINALS_USE = "Terminals-Use";
    public static final String SETTING_TERMINALS_CONNECT = "Terminals-Connect";
    public static final String TABLE_TERMINALS_SUPPORTED = "TERMINALS_SUPPORTED";

}
