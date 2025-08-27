package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import pos_client.common.Core;
import static pos_client.common.DefinedVariables.*;
import pos_client.windows.Log;

/**
 *
 * @author Ola Adolfsson
 */
public class ReceiptTypeDAO extends DAO
{

    private static final String TABLE_RECEIPTS_TYPE_ID = TABLE_RECEIPTS_TYPE + "_id";
    private static final String COLUMN_RECEIPT_NO = "receiptno";

    public ReceiptTypeDAO()
    {

    }

    public enum ReceiptType
    {
        ONGOING,
        PARKED,
        CANCELED,
        NORMAL,
        PRACTICE,
        COPY,
        REFUND,
        PROFO,
        ADMIN
    }

    public String receiptType2Str(ReceiptType type)
    {
        String ret = "ERROR";
        switch (type)
        {
            case ADMIN:
                ret = "ADMIN";
                break;
            case ONGOING:
                ret = "ONGOING";
                break;
            case PARKED:
                ret = "PARKED";
                break;
            case CANCELED:
                ret = "CANCELED";
                break;
            case NORMAL:
                ret = "NORMAL";
                break;
            case PRACTICE:
                ret = "PRACTICE";
                break;
            case COPY:
                ret = "COPY";
                break;
            case REFUND:
                ret = "REFUND";
                break;
            case PROFO:
                ret = "PROFO";
                break;
        }

        return ret;
    }

    public ReceiptType str2receiptType(String type)
    {
        if (type.compareTo("ADMIN") != 0)
        {
            return ReceiptType.ADMIN;
        }

        if (type.compareTo("ONGOING") != 0)
        {
            return ReceiptType.ONGOING;
        }

        if (type.compareTo("PARKED") != 0)
        {
            return ReceiptType.PARKED;
        }

        if (type.compareTo("CANCELED") != 0)
        {
            return ReceiptType.CANCELED;
        }

        if (type.compareTo("NORMAL") != 0)
        {
            return ReceiptType.NORMAL;
        }

        if (type.compareTo("PRACTICE") != 0)
        {
            return ReceiptType.PRACTICE;
        }

        if (type.compareTo("COPY") != 0)
        {
            return ReceiptType.COPY;
        }

        if (type.compareTo("REFUND") != 0)
        {
            return ReceiptType.REFUND;
        }

        if (type.compareTo("PROFO") != 0)
        {
            return ReceiptType.PROFO;
        }

        return ReceiptType.ONGOING;
    }

    public int updateReceiptNo(ReceiptType receiptType, int newReceiptNo)
    {
        int receiptTypeId = receiptTypeToDb(receiptType);

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar kvittotyp: " + Integer.toString(receiptTypeId) + " med nytt nummer " + newReceiptNo, Log.LogLevel.NORMAL);
            String query = "UPDATE " + TABLE_RECEIPTS_TYPE
                    + " SET " + COLUMN_RECEIPT_NO + "=" + newReceiptNo
                    + " WHERE " + TABLE_RECEIPTS_TYPE_ID + "=" + receiptTypeId;

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Kvittotyp " + receiptType.toString() + " uppdaterat med kvittonr " + newReceiptNo, Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("Lyckades inte uppdatera kvittotyp " + receiptType.toString() + " med kvittonr " + newReceiptNo, Log.LogLevel.CRITICAL);
            }

        } else
        {
            Core.getInstance().getLog().log("updateCurrentReceiptNo() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return newReceiptNo;

    }

    public int fetchCurrentReceiptNo(ReceiptType receiptType)
    {
        int receiptTypeId = receiptTypeToDb(receiptType);
        int currentReceiptNo = -1;

        String query = "SELECT " + COLUMN_RECEIPT_NO + "  FROM " + TABLE_RECEIPTS_TYPE + " " + " WHERE " + TABLE_RECEIPTS_TYPE_ID + "=" + receiptTypeId;

        Core.getInstance().getLog().log("Hämtar kvitto nr", Log.LogLevel.DESCRIPTIVE);

        if (db.query(query, false))
        {
            ResultSet resultSet = db.getResult();
            try
            {
                if (resultSet.next())
                {
                    currentReceiptNo = resultSet.getInt(COLUMN_RECEIPT_NO);
                }

            } catch (SQLException ex)
            {
                Core.getInstance().getLog().log("fetchCurrentReceiptNo() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        }
        return currentReceiptNo;
    }

    public static int receiptTypeToDb(ReceiptType type)
    {
        return (type.ordinal());
    }

    public static String receiptTypeNametoDb(ReceiptType type)
    {
        return (type.name());
    }

    public static ReceiptType receiptTypeFromDb(int type)
    {
        return (ReceiptType.values()[type]);
    }
}
