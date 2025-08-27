/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.common;

import pos_client.common.General;

/**
 *
 * @author Server
 */
public class InputHandler
{

    private String comma = ",";
    private String dot = ".";
    private int decimalNr = 2;

    public InputHandler()
    {

    }

    public String append(String input, String currentNumber)
    {
        return verifyInput(input, currentNumber);
    }

    public String delete(String currentNumber)
    {
        if (currentNumber.length() > 0)
        {
            return currentNumber.substring(0, currentNumber.length() - 1);
        }

        return "";
    }

    private enum decimalType
    {
        NONE,
        DEC_ONE,
        DEC_TWO
    };

    private String verifyInput(String input, String currentStr)
    {
        if (currentStr.contains(comma) || currentStr.contains(dot))
        {
            switch (checkDecimal(currentStr))
            {
                case NONE:
                    currentStr += input;
                    break;
                case DEC_ONE:
                    if ((input.compareTo(comma) != 0 && input.compareTo(dot) != 0 && input.compareTo("00") != 0))
                    {
                        currentStr += input;
                    }
                    break;
            }
        } else
        {
            currentStr += input;
        }

        return currentStr;
    }

    public decimalType checkDecimal(String sum)
    {
        String[] strExplodeDot = sum.split(General.getInstance().delimiterDot);
        String[] strExplodeComma = sum.split(General.getInstance().delimiterComma);

        if (strExplodeDot.length == 1 && strExplodeComma.length == 1)
        {
            return decimalType.NONE;
        }

        if ((strExplodeDot.length == 2 && strExplodeDot[1].length() == 1)
                || (strExplodeComma.length == 2 && strExplodeComma[1].length() == 1))
        {
            return decimalType.DEC_ONE;
        }

        return decimalType.DEC_TWO;
    }

}
