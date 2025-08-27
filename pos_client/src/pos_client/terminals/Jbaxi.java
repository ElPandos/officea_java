/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.terminals;

import eu.nets.baxi.client.AdministrationArgs;
import eu.nets.baxi.client.BaxiCtrlEventListener;
import eu.nets.baxi.client.BaxiCtrl;
import eu.nets.baxi.client.BarcodeReaderEventArgs;
import eu.nets.baxi.client.BaxiErrorEventArgs;
import eu.nets.baxi.client.DisplayTextEventArgs;
import eu.nets.baxi.client.JsonReceivedEventArgs;
import eu.nets.baxi.client.LastFinancialResultEventArgs;
import eu.nets.baxi.client.LocalModeEventArgs;
import eu.nets.baxi.client.PrintTextEventArgs;
import eu.nets.baxi.client.StdRspReceivedEventArgs;
import eu.nets.baxi.client.TLDReceivedEventArgs;
import eu.nets.baxi.client.TerminalReadyEventArgs;
import eu.nets.baxi.client.TransferAmountArgs;
import static java.lang.Math.round;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import pos_client.common.General;
import pos_client.common.Core;
import pos_client.db.dao.ReceiptTypeDAO;
import pos_client.fxml.payment.PaymentController;
import pos_client.windows.Log;

public class Jbaxi extends Terminal
{

    public static String NETS = "Nets";

    private BaxiCtrl baxiCtrl = null;

    public AdminType adminType = AdminType.None;

    private String terminalOutput = "";
    private String terminalOutputMain = "";
    private String terminalOutputCustomer = "";
    private String terminalOutputShop = "";

    public enum AdminType
    {
        None,
        Cancel,
        Zreport, // case 9
        Xreport, // case 9
        LatestFinancialTransactionReceipt,
        LatestFinancialTransactionResult,
        PrintStoreAndForwardList, // case 11 , Send Offline Transactions to HOST
        SendStoreAndForwardList,
        EmptyPrinterBuffer, // case 12
        Reconciliation, // case 12
        DatasetDownload, // case 13
        SoftwareDownload // case 14
    }

    public TransferType transferType = TransferType.NONE;

    public Status currentStatus = Status.None;

    public enum Status
    {
        Valid_ACC, // OK ACC updated 0
        Valid_NO_ACC, // OK no ACC updated 1
        Denied_NO_ACC, // OK no ACC updated 2
        Error, // ERROR 99
        None
    }

    public Jbaxi()
    {
        super();
        init();
    }

    private void init()
    {
        baxiCtrl = new BaxiCtrl();

        if (baxiCtrl != null)
        {
            baxiCtrl.addBaxiCtrlEventListener(baxiCtrlListener);
            name = NETS;
            version = baxiCtrl.getVersion();
        } else
        {
            Core.getInstance().getLog().log("Jbaxi is null", Log.LogLevel.CRITICAL);
        }
    }

    public String getTerminalOutput()
    {
        return terminalOutputMain;
    }

    public String getTerminalOutputCustomer()
    {
        return terminalOutputCustomer;
    }

    public String getTerminalOutputShop()
    {
        return terminalOutputShop;
    }

    private void terminalReady()
    {
        Core.getInstance().getLog().log("Terminal Ready event received!", Log.LogLevel.DESCRIPTIVE);
    }

    @Override
    public boolean isConnected()
    {
        return (baxiCtrl != null && baxiCtrl.isOpen());
    }

    public boolean connect()
    {
        if (baxiCtrl != null)
        {
            StartTime = System.currentTimeMillis();
            transferType = TransferType.CONNECT;
            
            return baxiCtrl.open() == 1;
        }
        
        return false;
    }

    @Override
    public boolean close()
    {
        return ((baxiCtrl != null) && (baxiCtrl.close() == 1));
    }

    public BaxiCtrlEventListener baxiCtrlListener = new BaxiCtrlEventListener()
    {
        @Override
        public void OnStdRspReceived(final StdRspReceivedEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    //stdRspReceived(args);
                }
            });
        }

        @Override
        public void OnPrintText(final PrintTextEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    printText(args);
                }
            });
        }

        @Override
        public void OnDisplayText(final DisplayTextEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    displayText(args);
                }
            });
        }

        @Override
        public void OnLocalMode(final LocalModeEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    localMode(args);
                }
            });
        }

        @Override
        public void OnTerminalReady(final TerminalReadyEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    terminalReady();
                }
            });
        }

        @Override
        public void OnTLDReceived(final TLDReceivedEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    //tldReceived(args);
                }
            });
        }

        @Override
        public void OnLastFinancialResult(final LastFinancialResultEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    lastFinancialResult(args);
                }
            });
        }

        @Override
        public void OnBaxiError(final BaxiErrorEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    baxiError(args);
                }
            });
        }

        @Override
        public void OnJsonReceived(final JsonReceivedEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    //jsonReceived(args);
                }
            });
        }

        @Override
        public void OnBarcodeReader(final BarcodeReaderEventArgs args)
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    //barcodeReader(args);
                }
            });
        }
    };

    @Override
    public boolean sendData(String operatorID, double costAmount, double purchaseAmount, double vatAmount, TransferType transferType)
    {
        return transferAmount(operatorID, costAmount, purchaseAmount, vatAmount, transferType);
    }

    public boolean transferAmount(String operatorID, double costAmount, double purchaseAmount, double vatAmount, TransferType transferType)
    {
        this.transferType = transferType;

        TransferAmountArgs taArgs = new TransferAmountArgs();

        double amount1 = 0.0;
        double amount2 = 0.0;
        switch (transferType)
        {

            case PURCHASE: // case 1 & case 2 & case 7 & case 8
                amount1 = costAmount;
                taArgs.setType1(0x30);
                break;

            case REFUND: // case 4
                amount1 = costAmount;
                taArgs.setType1(0x31);
                break;

            case REVERSAL: // case 5
                amount1 = costAmount;
                taArgs.setType1(0x32);
                break;

            case CASHBACK: // case 3
                amount1 = purchaseAmount;
                amount2 = costAmount;
                taArgs.setType1(0x33);
                break;

            case WITHDRAW:
                taArgs.setType1(0x39);
                break;

            case OFFLINE: // case 5
                amount1 = costAmount;
                taArgs.setType1(0x40);
                break;
        }

        int purchase1 = (int) round(amount1 * 100);
        int purchase2 = (int) round(amount2 * 100);

        int vat = (int) (vatAmount * 100);

        taArgs.setAmount1(purchase1); // Purchase, totalsuman*
        taArgs.setAmount2(purchase2); // cashback med summa*
        taArgs.setAmount3(vat);

        taArgs.setArticleDetails("");

        taArgs.setAuthCode("");
        if (transferType == TransferType.OFFLINE)
        {
            taArgs.setAuthCode("ABC");
        }

        taArgs.setHostData("");
        taArgs.setOperID("0000");
        taArgs.setPaymentConditionCode("");

        taArgs.setType2(0x30);
        taArgs.setType3(0x30);
        if (vat > 0)
        {
            taArgs.setType3(0x32);
        }

        boolean success = (baxiCtrl != null && baxiCtrl.transferAmount(taArgs) == 1);
        if (success)
        {
            Core.getInstance().getLog().log("TransferAmount - Skickat data till terminalen!", Log.LogLevel.DESCRIPTIVE);
        } else
        {
            Core.getInstance().getLog().log("TransferAmount - Fel i överföringen till terminalen!", Log.LogLevel.CRITICAL);
        }

        return success;
    }

    public void administration(AdminType adminType)
    {

        this.adminType = adminType;

        int choice = 0;
        switch (adminType)
        {

            case Cancel:
                choice = 0x313B;
                break;

            case Xreport:
                choice = 0x3136;
                break;

            case Zreport:
                choice = 0x3137;
                break;

            case LatestFinancialTransactionReceipt:
                choice = 0x313C;
                break;

            case LatestFinancialTransactionResult:
                choice = 0x313D;
                break;

            case PrintStoreAndForwardList:
                choice = 0x313A;
                break;

            case SendStoreAndForwardList:
                choice = 0x3138;
                break;

            case EmptyPrinterBuffer:
                choice = 0x3131;
                break;

            case Reconciliation:
                choice = 0x3130;
                break;

            case DatasetDownload:
                choice = 0x313F;
                break;

            case SoftwareDownload:
                choice = 0x313E;
                break;
        }

        if (baxiCtrl != null && choice > 0)
        {
            int returnValue = baxiCtrl.administration(new AdministrationArgs(choice, "0000"));

            if (returnValue == 1)
            {
                Core.getInstance().getLog().log("administration - Skickat data till terminalen!", Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("administration - Fel i överföringen till terminalen!", Log.LogLevel.CRITICAL);
            }
        }
    }

    public void ShowbaxiCtrlConfig()
    {

        if (baxiCtrl != null && baxiCtrl.isOpen())
        {
            Core.getInstance().getLog().log("-- baxiCtrl Configure --", Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("LogFilePath = " + baxiCtrl.getLogFilePath(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("LogFilePrefix = " + baxiCtrl.getLogFilePrefix(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("TraceLevel = " + baxiCtrl.getTraceLevel(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("BaudRate = " + baxiCtrl.getBaudRate(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("ComPort = " + baxiCtrl.getComPort(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("DeviceString = " + baxiCtrl.getDeviceString(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("HostPort = " + baxiCtrl.getHostPort(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("HostIpAddress = " + baxiCtrl.getHostIpAddress(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("VendorInfoExtended = " + baxiCtrl.getVendorInfoExtended(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("IndicateEotTrans = " + baxiCtrl.getIndicateEotTransaction(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("CutterSupport = " + baxiCtrl.getCutterSupport(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("PrinterWidth = " + baxiCtrl.getPrinterWidth(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("DisplayWidth = " + baxiCtrl.getDisplayWidth(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("PowerCycleCheck = " + baxiCtrl.getPowerCycleCheck(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("TidSupervision = " + baxiCtrl.getTidSupervision(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("AutoGetCustomerInfo = " + baxiCtrl.getAutoGetCustomerInfo(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("TerminalReady = " + baxiCtrl.getTerminalReady(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("UseDisplayTextID = " + baxiCtrl.getUseDisplayTextID(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("UseExtendedLocalMode = " + baxiCtrl.getUseExtendedLocalMode(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("UseSplitDisplayText = " + baxiCtrl.getUseSplitDisplayText(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("CardInfoAll = " + baxiCtrl.getCardInfoAllAsInt(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("SocketListener = " + baxiCtrl.getSocketListener(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("SocketListenerPort = " + baxiCtrl.getSocketListenerPort(), Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("LogAutodeletedays = " + baxiCtrl.getLogAutoDeleteDays(), Log.LogLevel.NORMAL);
        } else
        {
            Core.getInstance().getLog().log("baxiCtrl is not open!", Log.LogLevel.CRITICAL);
        }
    }

    public String getStatus()
    {

        String info = "Status";

        if (currentStatus != null)
        {
            switch (currentStatus)
            {

                case None:
                    info = "";
                    break;

                case Valid_ACC:
                    info = info + " - GODKÄND";
                    break;

                case Valid_NO_ACC:
                    info = info + " - GODKÄND (NO_ACC)";
                    break;

                case Denied_NO_ACC:
                    info = info + " - EJ GODKÄND";
                    break;

                case Error:
                    info = info + " - FEL";
                    break;

                default:
                    info = info + " - FEL no switch key";
                    break;
            }
        } else
        {
            info = info + "ERROR, currentStatus is null";
        }

        return info;
    }

    private void localMode(LocalModeEventArgs args)
    {
        EndTime = System.currentTimeMillis();

        if (args.getResult() == 99)
        {
            currentStatus = Status.Error;
        } else
        {
            currentStatus = Status.values()[args.getResult()];
        }

        switch (transferType)
        {
            case CONNECT:
                if (baxiCtrl.isOpen())
                {
                    Core.getInstance().getLog().log(getName() + " ( " + getVersion() + " )" + " is connected!" + " Opened in: " + (EndTime - StartTime) / 1000 + " sek", Log.LogLevel.NORMAL);
                } else
                {
                    Core.getInstance().getLog().log("Baxi was not able to open...", Log.LogLevel.CRITICAL);
                }
                break;

            case OFFLINE:
            case REFUND:
            case REVERSAL:
            case WITHDRAW:
            case CASHBACK:
            case PURCHASE:
                switch (currentStatus)
                {
                    case Valid_ACC:
                    case Valid_NO_ACC:
                        double paymentRemain = receipt.getPaymentRemain();
                        if (paymentRemain <= 0)
                        {
                            receipt.createReceipt(ReceiptTypeDAO.ReceiptType.NORMAL, PaymentController.PaymentType.CARD);
                        }
                        break;

                    case Error:
                    case Denied_NO_ACC:
                        receipt.createReceipt(ReceiptTypeDAO.ReceiptType.NORMAL, PaymentController.PaymentType.CARD);
                        break;
                }
                break;
        }

        switch (adminType)
        {

            case Cancel:
            case Zreport:
            case Xreport:
            case LatestFinancialTransactionReceipt:
            case LatestFinancialTransactionResult:
            case PrintStoreAndForwardList:
            case SendStoreAndForwardList:
            case Reconciliation:
            case DatasetDownload:
            case SoftwareDownload:
            case EmptyPrinterBuffer:
                receipt.createReceipt(ReceiptTypeDAO.ReceiptType.ADMIN, PaymentController.PaymentType.CARD);
                break;
        }

        terminalOutputCustomer = "";
        terminalOutputShop = "";
        terminalOutput = "";
        terminalOutputMain = "";

        adminType = AdminType.None;
        transferType = TransferType.NONE;
        currentStatus = Status.None;
    }

    private void printText(PrintTextEventArgs args) // Result
    {
        if (txtPaymentInfo != null)
        {
            txtPaymentInfo.clear();
        }

        terminalOutput = terminalOutput + General.getInstance().newLine + args.getPrintText().replaceAll(";", General.getInstance().newLine);
        terminalOutputMain = terminalOutput;

        String splitString = "---RIV AV HÄR---";
        if (terminalOutput.contains(splitString))
        {
            String[] splitted = terminalOutput.split(splitString);

            if (splitted.length == 2)
            {
                terminalOutputCustomer = splitted[0];
                terminalOutputShop = splitted[1];
            }

            terminalOutputCustomer = terminalOutputCustomer.replace("\r", "");
            terminalOutputShop = terminalOutputShop.replace("\r", "");
        }

        terminalOutputMain = terminalOutputMain.replace("\r", "");

        Core.getInstance().getLog().log(terminalOutput, Log.LogLevel.DESCRIPTIVE);

        if (txtPaymentInfo != null)
        {
            txtPaymentInfo.setText(txtPaymentInfo.getText() + General.getInstance().newLine + terminalOutput);
        }
    }

    private void displayText(DisplayTextEventArgs args) // Current status
    {
        if (txtPaymentInfo != null)
        {
            txtPaymentInfo.clear();
        }

        //Change \r to \r\n but still support Linux
        String text = args.getDisplayText();
        if (text != null)
        {
            if (!text.contains(General.getInstance().newLine))
            {
                text = text.replace("\r", "");
            }

            String info = "(" + args.getDisplaytextSourceID() + "," + args.getDisplaytextID() + ") ";
            Core.getInstance().getLog().log(info + text, Log.LogLevel.DESCRIPTIVE);

            if (txtPaymentInfo != null)
            {
                txtPaymentInfo.setText(txtPaymentInfo.getText() + General.getInstance().newLine + info + text);
            }

        } else
        {
            Core.getInstance().getLog().log("DisplayText är null", Log.LogLevel.CRITICAL);
        }
    }

    private void baxiError(BaxiErrorEventArgs args)
    {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Varning");
        alert.setHeaderText("Baxi information - Kortterminal ej inkopplad");
        alert.setContentText("Vänligen kontrollera att kortterminalen är påslagen och inkopplad korrekt");
        alert.showAndWait();
        Core.getInstance().getLog().log("Baxi ERROR received!", Log.LogLevel.CRITICAL);
    }

    private void lastFinancialResult(LastFinancialResultEventArgs args)
    {

        String output = General.getInstance().int2Str(transferType.ordinal()) + General.getInstance().newLine + args.getResultData().replaceAll(";", General.getInstance().newLine);

        Core.getInstance().getLog().log(output, Log.LogLevel.DESCRIPTIVE);

        if (txtPaymentInfo != null)
        {
            txtPaymentInfo.setText(output);
        }
    }

    @Override
    public List<Button> getTerminalFuncions()
    {

        List<Button> functionList = new ArrayList<>();

        Button btnZreport = new Button("Z-report");
        btnZreport.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.Zreport);
            }
        });

        Button btnXreport = new Button("X-report");
        btnXreport.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.Xreport);
            }
        });

        Button btnLFtransReceipt = new Button("Last financial trans.receipt");
        btnLFtransReceipt.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.LatestFinancialTransactionReceipt);
            }
        });

        Button btnLFtransResult = new Button("Last financial trans.result");
        btnLFtransResult.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.LatestFinancialTransactionResult);
            }
        });

        Button btnSendStoreAndForwardList = new Button("Send StoreAndForwardList");
        btnSendStoreAndForwardList.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.SendStoreAndForwardList);
            }
        });

        Button btnPrintStoreAndForwardList = new Button("Print StoreAndForwardList");
        btnPrintStoreAndForwardList.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.PrintStoreAndForwardList);
            }
        });

        Button btnEmptyPrintBuffer = new Button("EmptyPrintBuffer");
        btnEmptyPrintBuffer.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.EmptyPrinterBuffer); // case 12
            }
        });

        Button btnReconciliation = new Button("Reconciliation");
        btnReconciliation.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.Reconciliation); // case 12
            }
        });

        Button btnDatasetDownload = new Button("Dataset download");
        btnDatasetDownload.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.DatasetDownload); // case 13
            }
        });

        Button btnSoftwareDownload = new Button("Software download");
        btnSoftwareDownload.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                administration(AdminType.SoftwareDownload); // case 14
            }
        });

        Button btnSettings2Log = new Button("Nets Config");
        btnSettings2Log.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent e)
            {
                ShowbaxiCtrlConfig();
            }
        });

        functionList.add(btnZreport);
        functionList.add(btnXreport);
        functionList.add(btnLFtransReceipt);
        functionList.add(btnLFtransResult);
        functionList.add(btnSendStoreAndForwardList);
        functionList.add(btnPrintStoreAndForwardList);
        functionList.add(btnEmptyPrintBuffer);
        functionList.add(btnReconciliation);
        functionList.add(btnDatasetDownload);
        functionList.add(btnSoftwareDownload);
        functionList.add(btnSettings2Log);

        return functionList;
    }

}
