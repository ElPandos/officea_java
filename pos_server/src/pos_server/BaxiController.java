/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_server;

import eu.nets.baxi.client.AdministrationArgs;
import eu.nets.baxi.client.BaxiCtrl;
import eu.nets.baxi.client.TransferAmountArgs;
import java.util.Date;

/**
 *
 * @author Laptop
 */
public class BaxiController {

    private BaxiCtrl Baxi;
   
    public BaxiController(BaxiCtrl Baxi) {
        this.Baxi = Baxi;
    }
    
    LastTrans LastTransaction; 
    class LastTrans {
        public Date Time;
        public int Amount;
        public TransferAmountArgs taArgs;
    };
    
    public LastTrans LatestTransaction() {
        return LastTransaction;
    }
    
    TransferType LastTransferType;
    class TransferType {
        public int Transaction = 0x30;
        public int ReversalLastTransation = 0x32;
        //public int TransaactionWithCashBack = 0x33;
        //public int CashWithdraw = 0x39;
    };

    public void LastFinancialResult() {
        AdministrationArgs args = new AdministrationArgs();
        args.OperID = "0000";
        args.AdmCode = 0x313D;
        Baxi.administration(args);
    }
    
    public void StoreNadForwardResult() {
        AdministrationArgs args = new AdministrationArgs();
        args.OperID = "0000";
        args.AdmCode = 0x3138;
        Baxi.administration(args);
    }
       
    public void TransferAmount(double rAmount, int type) {
        
        TransferAmountArgs taArgs = new TransferAmountArgs();
        
        if (LastTransferType == null)
            LastTransferType = new TransferType(); 
                
        switch (type) {
            case 2: // Payment
                taArgs.setType1(0x30);
                break;
                
            case 3: // Refund
                taArgs.setType1(0x32);
                break;
                
            case 4: // LastTransaction
                taArgs.setType1(0x33);
                break;
                
            case 5: // EndOfDayReport
                taArgs.setType1(0x30);
                break;
        }
        
        if (LastTransaction == null)
            LastTransaction = new LastTrans();
        
        int iAmount = (int)(rAmount*100);
        
        taArgs.setAmount1(iAmount);
        taArgs.setAmount2(0);
        taArgs.setAmount3(0);
        
        taArgs.setArticleDetails("");
        taArgs.setAuthCode("");
        taArgs.setHostData("");
        taArgs.setOperID("0000");
        taArgs.setPaymentConditionCode("");
        
        taArgs.setType2(0x30);
        taArgs.setType3(0x30);

        if (Baxi != null && Baxi.transferAmount(taArgs) == 1) {
            LastTransaction.Time = new Date();
            LastTransaction.Amount = iAmount;
            LastTransaction.taArgs = taArgs;
        }
    }

    public void EndOfDayReport() {

    }

    public void Connect() {
        if (Baxi != null) {
            Baxi.open();
        }
    }

    public void Close() {
        if (Baxi != null) {
            Baxi.close();
        }
    }
    
    public void ShowBaxiConfig(MainController mainC) {
        if (Baxi != null && Baxi.isOpen()) {
            mainC.Log("-- Baxi Configure --");
            mainC.Log("LogFilePath = " + Baxi.getLogFilePath());
            mainC.Log("LogFilePrefix = " + Baxi.getLogFilePrefix());
            mainC.Log("TraceLevel = " + Baxi.getTraceLevel());
            mainC.Log("BaudRate = " + Baxi.getBaudRate());
            mainC.Log("ComPort = " + Baxi.getComPort());
            mainC.Log("DeviceString = " + Baxi.getDeviceString());
            mainC.Log("HostPort = " + Baxi.getHostPort());
            mainC.Log("HostIpAddress = " + Baxi.getHostIpAddress());
            mainC.Log("VendorInfoExtended = " + Baxi.getVendorInfoExtended());
            mainC.Log("IndicateEotTrans = " + Baxi.getIndicateEotTransaction());
            mainC.Log("CutterSupport = " + Baxi.getCutterSupport());
            mainC.Log("PrinterWidth = " + Baxi.getPrinterWidth());
            mainC.Log("DisplayWidth = " + Baxi.getDisplayWidth());
            mainC.Log("PowerCycleCheck = " + Baxi.getPowerCycleCheck());
            mainC.Log("TidSupervision = " + Baxi.getTidSupervision());
            mainC.Log("AutoGetCustomerInfo = " + Baxi.getAutoGetCustomerInfo());
            mainC.Log("TerminalReady = " + Baxi.getTerminalReady());
            mainC.Log("UseDisplayTextID = " + Baxi.getUseDisplayTextID());
            mainC.Log("UseExtendedLocalMode = " + Baxi.getUseExtendedLocalMode());
            mainC.Log("UseSplitDisplayText = " + Baxi.getUseSplitDisplayText());
            mainC.Log("CardInfoAll = " + Baxi.getCardInfoAllAsInt());
            mainC.Log("SocketListener = " + Baxi.getSocketListener());
            mainC.Log("SocketListenerPort = " + Baxi.getSocketListenerPort());
            mainC.Log("LogAutodeletedays = " + Baxi.getLogAutoDeleteDays());
        } else {
            mainC.Log("Baxi is not open!");
        }
    }
}
