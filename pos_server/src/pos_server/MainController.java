/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_server;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import eu.nets.baxi.client.*;
import eu.nets.baxi.client.BaxiCtrl;
import eu.nets.baxi.client.BaxiCtrlEventListener;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Laptop
 */
public class MainController implements Initializable {

    private final String newLine = System.getProperty("line.separator");

    Thread ComThread;

    private BaxiCtrl Baxi;
    
    private String FilemakerScript = "card_return";
    private String FilemakerName = "officeapos1";

    private BaxiController myBaxi;
    private General myGeneral;

    public boolean isOpen = false;

    private final String Status[] = {"Not Connected", "Connecting...", "Connected"};
    
    @FXML
    private TextField input_file;
    @FXML
    private TextField output_file;
    @FXML
    private AnchorPane Main;
    @FXML
    private Button Clear_Log;
    @FXML
    private Button Conf;
    @FXML
    private TextField status_file;
    @FXML
    private TextField source;

    @FXML
    private void CreateInput_handleButtonAction(ActionEvent event) {
        if (myGeneral.FileCreate(source.getText() + input_file.getText(), Communication_data.getText(), true)) {
            Log("Created file: " + source.getText() + input_file.getText() + " with data: " + Communication_data.getText());
        } else {
            Log("File EXISTS no need to created file: " + source.getText() + input_file.getText());
        }
    }

    @FXML
    private void CreateOutput_handleButtonAction(ActionEvent event) {
        if (myGeneral.FileCreate(source.getText() + output_file.getText(), Communication_data.getText(), true)) {
            Log("Created file: " + source.getText() + output_file.getText() + " with data: " + Communication_data.getText());
        } else {
            Log("File EXISTS no need to created file: " + source.getText() + output_file.getText());
        }
    }

    @FXML
    private void SaveInput_handleButtonAction(ActionEvent event) {
    
    
    }

    private enum StatusType {

        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }

    TransType CurrentType;

    private enum TransType {

        None,
        Connect,
        Payment, // 2
        Refund, // 3
        LastTransaction, // 4
        EndOfDayReport // 5
    }

    TransStatus CurrentStatus;

    private enum TransStatus {

        Valid_ACC,      // OK ACC updated 0
        Valid_NO_ACC,   // OK no ACC updated 1
        Denied_NO_ACC,  // OK no ACC updated 2
        Error           // ERROR 99
    }

    private double StartTime = 0, EndTime = 0;

    public BaxiCtrlEventListener BaxiListener = new BaxiCtrlEventListener() {
        @Override
        public void OnStdRspReceived(final StdRspReceivedEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //stdRspReceived(args);
                }
            });
        }

        @Override
        public void OnPrintText(final PrintTextEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    PrintText(args);
                }
            });
        }

        @Override
        public void OnDisplayText(final DisplayTextEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    DisplayText(args);
                }
            });
        }

        @Override
        public void OnLocalMode(final LocalModeEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    LocalMode(args);
                }
            });
        }

        @Override
        public void OnTerminalReady(final TerminalReadyEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    TerminalReady();
                }
            });
        }

        @Override
        public void OnTLDReceived(final TLDReceivedEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //tldReceived(args);
                }
            });
        }

        @Override
        public void OnLastFinancialResult(final LastFinancialResultEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    LastFinancialResult(args);
                }
            });
        }

        @Override
        public void OnBaxiError(final BaxiErrorEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    BaxiError(args);
                }
            });
        }

        @Override
        public void OnJsonReceived(final JsonReceivedEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //jsonReceived(args);
                }
            });
        }

        @Override
        public void OnBarcodeReader(final BarcodeReaderEventArgs args) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //barcodeReader(args);
                }
            });
        }
    };

    @FXML
    private Button Connect;
    @FXML
    private Button Disconnect;
    @FXML
    private Text StatusText;
    @FXML
    private TextArea PrintLogger;
    @FXML
    private Rectangle Connected_color;
    @FXML
    private ProgressBar Thread_progress;
    @FXML
    private TextField Communication_data;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        CurrentType = TransType.None;

        Baxi = new BaxiCtrl();
        Baxi.addBaxiCtrlEventListener(BaxiListener);

        //SetBaxiSettings();
        Disconnect.setDisable(true);
        StatusText.setText(Status[StatusType.DISCONNECTED.ordinal()]);

        myBaxi = new BaxiController(Baxi);
        myGeneral = new General();

        ComThread();
    }

    private void SetBaxiSettings() {

    }

    private void LocalMode(LocalModeEventArgs args) {

        EndTime = System.currentTimeMillis();

        switch (CurrentType) {
            case Connect:
                if (Baxi.isOpen()) {
                    Log("Baxi is opened in: " + (EndTime - StartTime) + " mSek");
                    Connected_color.setFill(Color.LIGHTGREEN);
                    Disconnect.setDisable(false);
                    Connect.setDisable(true);
                    StatusText.setText(Status[StatusType.CONNECTED.ordinal()]);
                } else {
                    Log("Baxi was not able to open...");
                    Connected_color.setFill(Color.RED);
                    Disconnect.setDisable(true);
                }
                break;

            case Payment:

                CurrentStatus = TransStatus.values()[args.getResult()];
                String preInfo = "Payment";
                switch (CurrentStatus) {

                    case Valid_ACC:
                        Log(preInfo + " - OK");
                        break;

                    case Valid_NO_ACC:
                        Log(preInfo + " - OK (NO_ACC)");
                        break;

                    case Denied_NO_ACC:
                        Log(preInfo + " - DENIED");
                        break;

                    case Error:
                        Log(preInfo + " - ERROR");
                        break;
                }
                break;

            case Refund:
                               
                if (myBaxi.LatestTransaction() != null)
                    Log("Refund transaction: " + myBaxi.LatestTransaction().Amount);
                else
                    Log("Refund transaction: NOT AVAILABLE");
                break;

            case LastTransaction:
                if (myBaxi.LatestTransaction() != null)
                    Log("Latest transaction: " + myBaxi.LatestTransaction().Amount);
                else
                    Log("Latest transaction: NOT AVAILABLE");
                break;

            case EndOfDayReport:

                break;
        }
    }

    private void TerminalReady() {
        Log("Terminal Ready event received");
    }

    private void PrintText(PrintTextEventArgs args) // Result
    {
        String PreStatus = myGeneral.Int2Str(CurrentType.ordinal()) + "\n" + myGeneral.Int2Str(CurrentStatus.ordinal()) + "\n";
        String Output = PreStatus + args.getPrintText().replaceAll(";", newLine);
        
        if (myGeneral.FileCreate(source.getText() + output_file.getText(), Output, false))
            Log("Created file: " + source.getText() + output_file.getText());
        else
            Log("File EXISTS no need to created file: " + source.getText() + output_file.getText());
    }

    private void DisplayText(DisplayTextEventArgs args) // Current status
    {
        //Change \r to \r\n but still support Linux
        String text = args.getDisplayText();
        if (text != null)
        {
            if (!text.contains(newLine))
                text = text.replace("\r", "");
            
            String info = "(" + args.getDisplaytextSourceID() + "," + args.getDisplaytextID() + ") ";
            Log(info + text);
            
            myGeneral.FileCreate(source.getText() + status_file.getText(), text + "\n", true);
        }
    }

    private void BaxiError(BaxiErrorEventArgs args) {
        Log("Baxi ERROR received!");
        StatusText.setText(Status[StatusType.DISCONNECTED.ordinal()]);
    }

    private void LastFinancialResult(LastFinancialResultEventArgs args) {
        
        String Output = myGeneral.Int2Str(CurrentType.ordinal()) + newLine + args.getResultData().replaceAll(";", newLine);
        Log(Output);
    }

    private void Reset() {
        if (myGeneral.FileExists(source.getText() + output_file.getText())) {
            myGeneral.FileDelete(source.getText() + output_file.getText());
            Log("RESET - " + source.getText() + output_file.getText() + " is deleted!");
        }

        if (myGeneral.FileExists(source.getText() + input_file.getText())) {
            myGeneral.FileDelete(source.getText() + input_file.getText());
            Log("RESET - " + source.getText() + input_file.getText() + " is deleted!");
        }
        
        if (myGeneral.FileExists(source.getText() + status_file.getText())) {
            myGeneral.FileDelete(source.getText() + status_file.getText());
            Log("RESET - " + source.getText() + status_file.getText() + " is deleted!");
        }
    }

    @FXML
    private void Connect_handleButtonAction(ActionEvent event) {

        Reset();

        Log("Trying to connect to terminal...");
        CurrentType = TransType.Connect;
        StartTime = System.currentTimeMillis();
        myBaxi.Connect();

        Connected_color.setFill(Color.YELLOW);
        StatusText.setText(Status[StatusType.CONNECTING.ordinal()]);
    }

    @FXML
    private void Disconnect_handleButtonAction(ActionEvent event) {
        myBaxi.Close();
        Log("Baxi is closed...");

        Connect.setDisable(false);
        Disconnect.setDisable(true);

        CurrentType = TransType.None;

        Connected_color.setFill(Color.RED);
        StatusText.setText(Status[StatusType.DISCONNECTED.ordinal()]);
    }

    public void Log(String sText) {
        PrintLogger.appendText(myGeneral.Time() + sText + newLine);
    }

    private void ComThread() {
        if (ComThread == null) {
            Log("Creating LOOP poll thread on: " + source.getText());

            Thread pollThread = new Thread() {
                public void run() {
                    try {

                        boolean Error = false;
                        double ProgressStatus = 0;
                        double ProgressSteps = 1;

                        while (true) {
                            Thread_progress.setProgress(ProgressStatus / 1000);

                            Thread.sleep(10);

                            if (Baxi != null && Baxi.isOpen() && !Error) {

                                // INPUT                            
                                if (myGeneral.FileExists(source.getText() + input_file.getText())) {
                                    
                                    //Thread.sleep(2000);
                                     
                                    String Transaction = myGeneral.ReadFile(source.getText() + input_file.getText());

                                    String[] ExplodedCommunication = Transaction.split(";");

                                    if (myGeneral.Str2Int(ExplodedCommunication[0]) >= TransType.Payment.ordinal()
                                            && myGeneral.Str2Int(ExplodedCommunication[0]) <= TransType.EndOfDayReport.ordinal()) {

                                        switch ((TransType.values()[myGeneral.Str2Int(ExplodedCommunication[0])])) {

                                            case Payment:
                                                CurrentType = TransType.Payment;
                                                myBaxi.TransferAmount(myGeneral.Str2Dbl(ExplodedCommunication[1]), TransType.Payment.ordinal());
                                                break;

                                            case Refund:
                                                CurrentType = TransType.Refund;
                                                 myBaxi.TransferAmount(myGeneral.Str2Dbl(ExplodedCommunication[1]), TransType.Refund.ordinal());
                                                break;

                                            case LastTransaction:
                                                CurrentType = TransType.LastTransaction;
                                                myBaxi.LastFinancialResult();

                                                break;

                                            case EndOfDayReport:
                                                CurrentType = TransType.EndOfDayReport;

                                                break;
                                        }
                                    } else {
                                        Log("ERROR in communication! " + ExplodedCommunication[0] + " is not a Valid token!");
                                    }

                                    if (myGeneral.FileDelete(source.getText() + input_file.getText())) {
                                        Log(source.getText() + input_file.getText() + " is deleted!");
                                    } else {
                                        Log("Delete operation failed.");
                                        Error = true;
                                    }
                                }
                            }

                            // OUTPUT
                            if (myGeneral.FileExists(source.getText() + output_file.getText())) {

                                try {
                                    String script = "fmp://$/" + FilemakerName + "?script=" + FilemakerScript;
                                    myGeneral.ExecuteFM(script);
                                    
                                    /*
                                    int iCurrent = 0;
                                    int iWaitMs = 10;
                                    int iWaitSteps = 500;
                                    */
                                    while (myGeneral.FileExists(source.getText() + output_file.getText()))
                                        Thread.sleep(10);
                                    
                                    if (myGeneral.FileExists(source.getText() + status_file.getText()))
                                    {
                                        if (myGeneral.FileDelete(source.getText() + status_file.getText()))
                                            Log("Deleted: " + source.getText() + status_file.getText());
                                        else
                                            Log("FAILED Deleting: " + source.getText() + status_file.getText());
                                    }
                                } catch (Exception e) {
                                    // ...
                                }
                            }
                            
                            ProgressStatus += ProgressSteps;

                            if (ProgressStatus / 1000 >= 1 || ProgressStatus / 1000 <= 0) {
                                ProgressSteps *= -1;
                            }
                        }
                        
                    } catch (InterruptedException v) {
                        Log(v.toString());
                    }
                }
            };            
            pollThread.start();
            
        } else {
            ComThread.interrupt();
        }
    }

    @FXML
    private void Clear_Log_handleButtonAction(ActionEvent event) {
        PrintLogger.clear();
    }

    @FXML
    private void Conf_handleButtonAction(ActionEvent event) {
        myBaxi.ShowBaxiConfig(this);
    }

}
