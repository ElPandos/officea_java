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
public class PrinterModel
{

    public enum PrinterType
    {
        NONE,
        REPORT,
        RECEIPT,
        BONG1,
        BONG2,
        BONG3,
        BONG4
    }

    String name = "ERROR";
    PrinterType type = PrinterType.NONE;

    public PrinterModel(String name, PrinterType type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public PrinterType getType()
    {
        return type;
    }

    public void setType(PrinterType type)
    {
        this.type = type;
    }
}
