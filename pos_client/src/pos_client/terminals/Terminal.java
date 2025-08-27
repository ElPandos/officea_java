/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.terminals;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import pos_client.Receipt;
import pos_client.common.Core;

public class Terminal
{

    public enum TransferType
    {
        NONE,
        CONNECT,
        PURCHASE,
        REFUND,
        REVERSAL,
        CASHBACK,
        WITHDRAW,
        OFFLINE
    }

    public double StartTime = 0, EndTime = 0;

    protected String name = "";
    protected String version = "";

    protected TextArea txtPaymentInfo;

    protected Receipt receipt = null;

    public Terminal()
    {
    }
    
    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setTextArea(TextArea text)
    {
        txtPaymentInfo = text;
    }

    void setReceipt(Receipt receipt)
    {
        this.receipt = receipt;
    }

    public List<Button> getTerminalFuncions()
    {
        return null;
    }

     // Terminal overrides this function
    public boolean isConnected()
    {
        return false;
    }

     // Terminal overrides this function
    public boolean close()
    {
        return false;
    }

    // Terminal overrides this function
    public boolean sendData(String operatorID, double costAmount, double purchaseAmount, double vatAmount, TransferType transferType)
    {
        return false;
    }
}
