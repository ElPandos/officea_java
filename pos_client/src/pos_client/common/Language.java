/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.common;

/**
 *
 * @author Server
 */
public class Language
{

    private static Language instance = null;

    protected Language()
    {
        // Exists only to defeat instantiation.
    }

    public static Language getInstance()
    {
        if (instance == null)
        {
            instance = new Language();
        }
        return instance;
    }

    public enum LanguageType
    {
        SWEDISH,
        ENGLISH
    }

    private LanguageType languageType = LanguageType.SWEDISH; // Deafult sweidish

    // Languages [SWEDISH, ENGLISH]
    private static final String[] BTN_RECEIPT_PAYMENT =
    {
        "BETALNING", "PAYMENT"
    };

    public String getBtnReceiptPayment()
    {
        return BTN_RECEIPT_PAYMENT[languageType.ordinal()];
    }

    private static final String[] BTN_RECEIPT_CANCEL =
    {
        "AVBRYT", "CANCEL"
    };

    public String getBtnReceiptCancel()
    {
        return BTN_RECEIPT_CANCEL[languageType.ordinal()];
    }

    private static final String[] BTN_RECEIPT_DISCOUNT =
    {
        "RABATT", "DISCOUNT"
    };

    public String getBtnReceiptDiscount()
    {
        return BTN_RECEIPT_DISCOUNT[languageType.ordinal()];
    }

    private static final String[] BTN_RECEIPT_PARK =
    {
        "PARKERA", "PARK"
    };

    public String getBtnReceiptPark()
    {
        return BTN_RECEIPT_PARK[languageType.ordinal()];
    }

    public String getBtnReceiptSplit()
    {
        return BTN_RECEIPT_SPLIT[languageType.ordinal()];
    }
    ;

    private static final String[] BTN_RECEIPT_SPLIT =
    {
        "DELA NOTA", "SPLIT BILL"
    };

    private static final String[] BTN_RECEIPT_BILL =
    {
        "NOTA", "BILL"
    };

    private static final String[] BTN_OPEN_ALTPAYMENT =
    {
        "ALT. BETALNING", "ALT. PAYMENT"
    };

    private static final String[] BTN_OPEN_ARTICLES =
    {
        "ARTIKLAR", "ARTICLES"
    };

    private static final String[] BTN_OPEN_CASHBOX =
    {
        "KASSALÅDA", "CASHBOX"
    };

    private static final String[] BTN_OPEN_CUSTOMERS =
    {
        "KUNDER", "CUSTOMERS"
    };

    private static final String[] BTN_OPEN_EDIT =
    {
        "REDIGERA", "EDIT"
    };

    private static final String[] BTN_OPEN_LOGOUT =
    {
        "LOGGA UT", "LOGOUT"
    };

    private static final String[] BTN_OPEN_REFUND =
    {
        "ÅTERKÖP", "REPURCHASE"
    };

    private static final String[] BTN_OPEN_SCAN =
    {
        "SCANNA", "SCAN"
    };

    private static final String[] BTN_OPEN_SPECIAL =
    {
        "SPECIAL", "SPECIAL"
    };

    public void changeLanguage(LanguageType languageType)
    {
        this.languageType = languageType;
    }

    public String getBtnReceiptBill()
    {
        return BTN_RECEIPT_BILL[languageType.ordinal()];
    }

    public String getBtnOpenAltPayment()
    {
        return BTN_OPEN_ALTPAYMENT[languageType.ordinal()];
    }

    public String getbtnArticles()
    {
        return BTN_OPEN_ARTICLES[languageType.ordinal()];
    }

    public String getBtnOpenCASHBOX()
    {
        return BTN_OPEN_CASHBOX[languageType.ordinal()];
    }

    public String getBtnOpenCustomers()
    {
        return BTN_OPEN_CUSTOMERS[languageType.ordinal()];
    }

    public String getBtnOpenEdit()
    {
        return BTN_OPEN_EDIT[languageType.ordinal()];
    }

    public String getBtnOpenLogout()
    {
        return BTN_OPEN_LOGOUT[languageType.ordinal()];
    }

    public String getBtnOpenRefund()
    {
        return BTN_OPEN_REFUND[languageType.ordinal()];
    }

    public String getBtnOpenScan()
    {
        return BTN_OPEN_SCAN[languageType.ordinal()];
    }

    public String getBtnOpenSpecial()
    {
        return BTN_OPEN_SPECIAL[languageType.ordinal()];
    }
}
