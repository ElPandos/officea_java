
package pos_scanner.common;

import java.text.DecimalFormat;
import static pos_scanner.common.General.*;

public class Converter
{

    private static final String decimalFormatPattern = "###,##0.00";
    public static final DecimalFormat decimalFormat = new DecimalFormat(decimalFormatPattern);

    public Converter()
    {
    }

    public static double str2Dbl(String data, int decimal)
    {
        return decimal(Double.parseDouble(data.replace(comma, dot)), decimal, true);
    }

    public static float str2Float(String data, int decimal)
    {
        return decimal((float) Float.parseFloat(data.replace(comma, dot)), decimal, true);
    }

    public static int str2Int(String data)
    {
        return Integer.parseInt(data);
    }

    public static String int2Str(int data)
    {
        return Integer.toString(data);
    }

    public static String float2Str(float data, int decimal)
    {
        float value = decimal(data, decimal, true);
        return decimalFormat.format(value);
    }

    public static String float2StrDB(float data, int decimal)
    {
        float value = decimal(data, decimal, true);
        return Float.toString(value);
    }

    public static float decimal(float value, int dec, boolean round)
    {
        int decimalValue = (int) Math.pow((double) 10, (double) dec);
        float valueDecimal = value * decimalValue;
        if (round)
        {
            valueDecimal = Math.round(valueDecimal);
        }

        int valueInt = (int) valueDecimal;
        float valueFloat = (float) valueInt / decimalValue;

        return valueFloat;
    }

    public static double decimal(double value, int dec, boolean round)
    {
        int decimalValue = (int) Math.pow((double) 10, (double) dec);
        double valueDecimal = value * decimalValue;
        if (round)
        {
            valueDecimal = Math.round(valueDecimal);
        }

        int valueInt = (int) valueDecimal;
        double valueDouble = (float) valueInt / decimalValue;

        return valueDouble;
    }

    public boolean isFloat(Object floatValue)
    {
        try
        {
            Float.parseFloat(floatValue.toString());
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
}
