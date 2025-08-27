/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import java.net.URL;

/**
 *
 * Låt denna vara kvar i rooten annars blir alla sökvägar fel
 */
public class ResourcesHandler
{

    private static ResourcesHandler instance = null;

    protected ResourcesHandler()
    {
        // Exists only to defeat instantiation.
    }

    public static ResourcesHandler getInstance()
    {
        if (instance == null)
        {
            instance = new ResourcesHandler();
        }
        return instance;
    }

    private final String RESOURCE_DIR = "";

    // OBS KOLLA VERSALER OCH GEMENER PÅ FILNAMNEN (OBS! denna filen måste ligga i huvud katalogen så getclass() kommer fungera!)
    private final String FXML_REPORT_DAILY = "components/reporting/DaylieReport.fxml";
    private final String FXML_REPORT_Z = "reporting/zreport.jrxml";
    private final String FXML_SETTINGS = "settings/Settings.fxml";

    private final String FXML_SCREEN_CUSTOMER = "fxml/screens/Customer.fxml";
    private final String FXML_SCREEN_IDLE = "fxml/screens/Idle.fxml";

    private final String FXML_LOG = "fxml/log/Log.fxml";
    private final String FXML_ABOUT = "fxml/about/About.fxml";
    private final String FXML_SPLIT = "fxml/split/Split.fxml";

    private final String FXML_WORK = "fxml/work/Work.fxml";
    private final String FXML_PAYMENT = "fxml/payment/Payment.fxml";
    private final String FXML_SPECIAL = "fxml/special/Special.fxml";
    private final String FXML_EDIT = "fxml/edit/Edit.fxml";

    private final String FXML_PAYMENT_ALTERNATIVE = "fxml/alt_payment/Alt_payment.fxml";

    private final String FXML_DISCOUNT = "fxml/discount/Discount.fxml";
    private final String FXML_RECEIPT_REFUND = "fxml/refund/Refund.fxml";
    private final String FXML_TABLE = "fxml/table/Table.fxml";
    private final String FXML_HISTORY = "fxml/history/History.fxml";

    private final String FXML_DIALOG_QUESTION = "fxml/question/Question.fxml";
    private final String FXML_DIALOG_WARNING = "fxml/warning/Warning.fxml";
    private final String FXML_DIALOG_INPUT = "fxml/input/Input.fxml";

    private final String CSS_THEME_UPDATE = "c://css//theme.css";
    private final String CSS_THEME_SETTINGS_UPDATE = "css/settingstheme.css";
    private final String CSS_THEME = "css/theme.css";
    private final String CSS_THEME_SETTINGS = "css/settings/theme.css";

    private final String IMAGE_RECIEPT = "images/officea-systems_small.png";
    private final String IMAGE_FOLDER = "images/";
    private final String IMAGE_CARDREADERS = "images/cardReaders/";
    private final String IMAGE_ICON = "images/icon/POSicon.png";

    public String getThemeUpdate()
    {
        return CSS_THEME_UPDATE;
    }

    public String getThemeSettingsUpdate()
    {
        return CSS_THEME_SETTINGS_UPDATE;
    }

    public String getThemeMain()
    {
        return getClass().getResource(RESOURCE_DIR + CSS_THEME).toExternalForm();
    }

    public String getThemeSettings()
    {
        return getClass().getResource(RESOURCE_DIR + CSS_THEME_SETTINGS).toExternalForm();
    }

    public String getZreport()
    {
        return FXML_REPORT_Z;
    }

    public URL getFxmlLog()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_LOG);
    }

    public URL getFxmlScreenCustomer()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_SCREEN_CUSTOMER);
    }

    public URL getFxmlScreenIdle()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_SCREEN_IDLE);
    }

    public URL getFxmlAbout()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_ABOUT);
    }

    public URL getFxmlSplit()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_SPLIT);
    }

    public URL getFxmlPayment()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_PAYMENT);
    }

    public URL getFxmlWork()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_WORK);
    }

    public URL getFxmlSpecial()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_SPECIAL);
    }

    public URL getFxmlEdit()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_EDIT);
    }

    public URL getFxmlAltPayment()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_PAYMENT_ALTERNATIVE);
    }

    public URL getFxmlDiscount()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_DISCOUNT);
    }

    public URL getFxmlSettings()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_SETTINGS);
    }

    public URL getFxmlTable()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_TABLE);
    }

    public URL getFxmlHistory()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_HISTORY);
    }

    public URL getFxmlReceiptRefund()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_RECEIPT_REFUND);
    }

    public URL getFxmlQuestionDialog()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_DIALOG_QUESTION);
    }

    public URL getFxmlWarningDialog()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_DIALOG_WARNING);
    }

    public URL getFxmlInputDialog()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_DIALOG_INPUT);
    }

    public URL getFxmlDaylieReport()
    {
        return getClass().getResource(RESOURCE_DIR + FXML_REPORT_DAILY);
    }

    // Images and CSS files need return strings
    public String getRecieptImage()
    {
        return getClass().getResource(RESOURCE_DIR + IMAGE_RECIEPT).toExternalForm();
    }

    public String getImageFolder()
    {
        return getClass().getResource(RESOURCE_DIR + IMAGE_FOLDER).toExternalForm();
    }

    public String getImageTerminal()
    {
        return getClass().getResource(RESOURCE_DIR + IMAGE_CARDREADERS).toExternalForm();
    }

    public String getSystemIcon()
    {
        return getClass().getResource(RESOURCE_DIR + IMAGE_ICON).toExternalForm();
    }

}
