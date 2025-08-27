/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.communication;

import pos_client.Receipt;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.common.General;
import pos_client.communication.comHandler.ComType;
import pos_client.db.dao.ProfoDAO;
import pos_client.db.dao.ReceiptTypeDAO;
import pos_client.db.dao.SettingsDAO;
import pos_client.db.dao.VatDAO;
import pos_client.windows.Log;

/**
 *
 * @author Server
 */
public class ControlUnit extends Com
{

    SettingsDAO settingsDAO = new SettingsDAO();

    private ComType type = ComType.CONTROLUNIT;
    private String name = DefinedVariables.getInstance().SETTING_COM_CONTROLUNIT;

    public ControlUnit()
    {

    }

    public boolean loadStatus()
    {
        String type = settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_COM_CONTROLUNIT_USE);
        String connect = settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_COM_CONTROLUNIT_CONNECT);

        if (connect.compareTo(DefinedVariables.TRUE) == 0)
        {
            return connect();
        }

        return false;
    }

    public boolean connect()
    {
        if (open(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_PORT)))
        {
            int baud = General.getInstance().str2Int(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_BAUDRATE));
            int dataBits = General.getInstance().str2Int(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_DATABITS));
            int stopBit = General.getInstance().str2Int(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_STOPBIT));
            int parity = General.getInstance().str2Int(settingsDAO.getSetting(name + DefinedVariables.getInstance().SETTING_COM_PARITY));

            return params(baud, dataBits, stopBit, parity);

        } else
        {
            Core.getInstance().getLog().log("connect - Port is not created or opened", Log.LogLevel.CRITICAL);
        }

        return false;
    }

    public String controlUnitData(ReceiptTypeDAO.ReceiptType type, Receipt receipt)
    {
        String dateString = receipt.getCreatedDate() + " " + receipt.getCreatedTime();
        dateString = parseDateTime(dateString);

        VatDAO vatDAO = new VatDAO();

        int nr = General.getInstance().INVALID_VALUE;
        String stringType = "";
        float valueOne = 0;
        float valueTwo = 0;
        int refundFactor = 1;

        switch (type)
        {
            case REFUND:
                stringType = "normal";
                valueOne = receipt.getSumTotalIncm();
                nr = receipt.getNr();
                refundFactor = -1;
                break;
            case NORMAL:
                stringType = "normal";
                valueTwo = receipt.getSumTotalIncm();
                nr = receipt.getNr();
                break;
            case PROFO:
                stringType = "profo";
                ProfoDAO profoDAO = new ProfoDAO();
                nr = profoDAO.getProfoNr();
                valueTwo = receipt.getSumTotalIncm();
                profoDAO.increaseProfoNr(nr);
                break;
            case COPY:
                stringType = "kopia";
                nr = receipt.getNr();
                valueTwo = receipt.getSumTotalIncm();
                break;
            default:
                return "";
        }

        float sumVat1 = receipt.getSumTotalVat(VatDAO.VatType.VAT1);
        float sumVat2 = receipt.getSumTotalVat(VatDAO.VatType.VAT2);
        float sumVat3 = receipt.getSumTotalVat(VatDAO.VatType.VAT3);
        float sumVat4 = receipt.getSumTotalVat(VatDAO.VatType.VAT4);

        String data = "kd "
                + dateString + " "
                + receipt.getOrgNo() + " "
                + receipt.getCashRegisterNr() + " "
                + General.getInstance().int2Str(nr) + " "
                + stringType + " "
                + General.decimalFormat.format(valueOne) + " "
                + General.decimalFormat.format(valueTwo) + " "
                + General.decimalFormat.format(vatDAO.getVat(VatDAO.VatType.VAT1)) + ";" + General.decimalFormat.format(sumVat1 > 0 ? refundFactor * sumVat1 : sumVat1) + " "
                + General.decimalFormat.format(vatDAO.getVat(VatDAO.VatType.VAT2)) + ";" + General.decimalFormat.format(sumVat2 > 0 ? refundFactor * sumVat2 : sumVat2) + " "
                + General.decimalFormat.format(vatDAO.getVat(VatDAO.VatType.VAT3)) + ";" + General.decimalFormat.format(sumVat3 > 0 ? refundFactor * sumVat3 : sumVat3) + " "
                + General.decimalFormat.format(vatDAO.getVat(VatDAO.VatType.VAT4)) + ";" + General.decimalFormat.format(sumVat4 > 0 ? refundFactor * sumVat4 : sumVat4) + " "
                + "0000";

        return data;
    }

    public String parseControlNr(String data)
    {
        String ret = "Parse ERROR: ";

        if (data != null)
        {
            String[] splitted = data.split(" ");
            if (splitted.length > 0 && splitted[0].compareTo("0") == 0)
            {
                ret = splitted[1];
            }

            ret += splitted[0];
        }

        return ret;
    }

    public String getControlUnitNr(String writeData)
    {
        if (writeData.isEmpty())
        {
            return "";
        }

        Core.getInstance().getComHandler().write(comHandler.ComType.CONTROLUNIT, writeData);
        return parseControlNr(Core.getInstance().getComHandler().read(comHandler.ComType.CONTROLUNIT));
    }

    public String parseDateTime(String data)
    {
        data = data.replace("-", "");

        String modifiedData = "";
        String[] strExplodeDot = data.split(General.getInstance().delimiterSemicolon);
        for (int i = 0; i < strExplodeDot.length - 1; i++)
        {
            modifiedData += strExplodeDot[i];
        }

        data = modifiedData.replace(" ", "");

        return data;
    }

}
