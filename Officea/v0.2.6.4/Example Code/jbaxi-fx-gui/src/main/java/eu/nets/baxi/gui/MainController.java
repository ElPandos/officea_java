package eu.nets.baxi.gui;

import eu.nets.baxi.client.*;
import eu.nets.baxi.client.ef.BaxiEF;
import eu.nets.baxi.client.ef.BaxiEFEventListener;
import eu.nets.baxi.client.ef.CardInfoAllArgs;
import eu.nets.baxi.gui.admin.AdminController;
import eu.nets.baxi.gui.admin.bib.BibAdminController;
import eu.nets.baxi.gui.admin.bib.BibTransactionController;
import eu.nets.baxi.gui.controller.Controller;
import eu.nets.baxi.gui.controller.DialogResult;
import eu.nets.baxi.gui.json.SendJSONController;
import eu.nets.baxi.gui.properties.Properties;
import eu.nets.baxi.gui.properties.PropertyController;
import eu.nets.baxi.gui.style.HandleResources;
import eu.nets.baxi.gui.tld.SendTLDController;
import eu.nets.baxi.gui.tld.TLDCommand;
import eu.nets.baxi.gui.transferamount.TransferAmountController;
import eu.nets.baxi.util.Conversions;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class MainController implements Initializable{
	private String operID = "0000";

	private String newLine = System.getProperty("line.separator");
	private BaxiCtrl baxi;

	private Semaphore openSem = null;
	private int openCloseCounter;

	private boolean stopwatchTimerShouldStart;

	private TransferAmountArgs lastTransferAmountArgs;
	
	private double startOperation = 0,finishOperation = 0;

	public BaxiCtrlEventListener baxiListener = new BaxiCtrlEventListener() {
		@Override public void OnStdRspReceived(final StdRspReceivedEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					stdRspReceived(args);
				}
			});
		}

		@Override public void OnPrintText(final PrintTextEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					printText(args);
				}
			});
		}

		@Override public void OnDisplayText(final DisplayTextEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					displayText(args);
				}
			});
		}

		@Override public void OnLocalMode(final LocalModeEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					localMode(args);
				}
			});
		}

		@Override public void OnTerminalReady(final TerminalReadyEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					terminalReady();
				}
			});
		}

		@Override public void OnTLDReceived(final TLDReceivedEventArgs args){
			Platform.runLater(new Runnable() {
				@Override public void run() {
					tldReceived(args);
				}
			});
		}

		@Override public void OnLastFinancialResult(final LastFinancialResultEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					lastFinancialResult(args);
				}
			});
		}

		@Override public void OnBaxiError(final BaxiErrorEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					baxiError(args);
				}
			});
		}

		@Override public void OnJsonReceived(final JsonReceivedEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					jsonReceived(args);
				}
			});
		}

		@Override public void OnBarcodeReader(final BarcodeReaderEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					barcodeReader(args);
				}
			});
		}
	};

	BaxiEFEventListener efListener = new BaxiEFEventListener() {
		@Override public void OnStdRspReceived(final StdRspReceivedEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					stdRspReceived(args);
				}
			});
		}

		@Override public void OnPrintText(final PrintTextEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					printText(args);
				}
			});
		}

		@Override public void OnDisplayText(final DisplayTextEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					displayText(args);
				}
			});
		}

		@Override public void OnLocalMode(final LocalModeEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					localMode(args);
				}
			});
		}

		@Override public void OnTerminalReady(final TerminalReadyEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					terminalReady();
				}
			});
		}

		@Override public void OnTLDReceived(final TLDReceivedEventArgs args){
			Platform.runLater(new Runnable() {
				@Override public void run() {
					tldReceived(args);
				}
			});
		}

		@Override public void OnLastFinancialResult(final LastFinancialResultEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					lastFinancialResult(args);
				}
			});
		}

		@Override public void OnBaxiError(final BaxiErrorEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					baxiError(args);
				}
			});
		}

		@Override public void OnJsonReceived(final JsonReceivedEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					jsonReceived(args);
				}
			});
		}

		@Override public void OnBarcodeReader(final BarcodeReaderEventArgs args) {
			Platform.runLater(new Runnable() {
				@Override public void run() {
					barcodeReader(args);
				}
			});
		}

		@Override public void OnCardInfoAll(final CardInfoAllArgs args){
			Platform.runLater(new Runnable() {
				@Override public void run() {
					cardInfoAll(args);
				}
			});
		}
	};



	// Main area controls
	@FXML private TextField originTxt;
	@FXML private TextField cardDataTxt;
	@FXML private TextArea displayTxtArea;
	@FXML private TextArea printTxtArea;
	@FXML private TextArea localModeTxtArea;
	@FXML private TextField baxiEFIssuerIDSetTxt;
	@FXML private Button readIssuerIdSetBtn;
	@FXML private Label baxiEFIssuerIDSetlbl;

	// Basic tab controls
	@FXML private TextField resultTxt;
	@FXML private TextField methodRejectTxt;
	@FXML private TextField errorCodeTxt;
	@FXML private Rectangle terminalReadyRec;

	// Development tab controls
	@FXML private CheckBox gciAfterCardCheckBox;
	@FXML private CheckBox sendTAAfterCardCheckBox;
	@FXML private TextField delay1Txt;
	@FXML private TextField delay2Txt;

	// Listener methods
	private void stdRspReceived(StdRspReceivedEventArgs args) {
		localModeTxtArea.appendText("AddStdRspData = " + args.getResponse() + newLine);
	}

	private void printText(PrintTextEventArgs args) {
		printTxtArea.appendText(args.getPrintText());
	}

	private void displayText(DisplayTextEventArgs args) {
		//Change \r to \r\n but still support Linux
		String text = args.getDisplayText();
		if(text != null){
			if (!text.contains(newLine)){
				text = text.replace("\r", newLine);
			}
			displayTxtArea.appendText("(" + args.getDisplaytextSourceID()+ "," + args.getDisplaytextID() + ") " + text);
		}
	}

	private void localMode(LocalModeEventArgs args) {
		finishOperation = System.currentTimeMillis();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		double operationTimeValue = (finishOperation - startOperation)/1000;
		DecimalFormat df = new DecimalFormat("###.###");
		
		String result = "AddLMResult()" + newLine;
		result += "Total operation time: " + df.format(operationTimeValue) +"s at: " + dateFormat.format(new Date()) + newLine;
		result += "LocalModeResultData: " + args.getResultData() + newLine;
		result += "Result: " +  + args.getResult() + newLine;
		result += "   AccumulatorUpdate: "  +  args.getAccumulatorUpdate() + newLine;
		result += "   IssuerId: " + args.getIssuerId() + newLine;
		result += "   CardData: " + args.getTruncatedPan() + newLine;
		result += "   Timestamp: " + args.getTimestamp() + newLine;
		result += "   VerificationMethod: " + args.getVerificationMethod() + newLine;
		result += "   SessionNumber: " + args.getSessionNumber() + newLine;
		result += "   StanAuth: " + args.getStanAuth() + newLine ;
		result += "   SequenceNumber: " + args.getSequenceNumber() + newLine;
		result += "   TotalAmount: " + args.getTotalAmount() + newLine;
		result += "   RejectionSource: " + args.getRejectionSource() + newLine;
		result += "   RejectionReason: " + args.getRejectionReason() + newLine;
		result += "   TipAmount: " + args.getTipAmount() + newLine;
		result += "   SurchargeAmount: " + args.getSurchargeAmount() + newLine;
		result += "   terminalID: " + args.getTerminalID() + newLine;
		result += "   acquirerMerchantID: " + args.getAcquirerMerchantID() + newLine;
		result += "   cardIssuerName: " + args.getCardIssuerName() + newLine;
		result += "   responseCode: " + args.getResponseCode() + newLine;
		result += "   TCC: " + args.getTCC() + newLine;
		result += "   AID: " + args.getAID() + newLine;
		result += "   TVR: " + args.getTVR() + newLine;
		result += "   TSI: " + args.getTSI() + newLine;
		result += "   ATC: " + args.getATC() + newLine;
		result += "   AED: " + args.getAED() + newLine;
		result += "   IAC: " + args.getIAC() + newLine;
		result += "   OrganisationNumber: " + args.getOrganisationNumber() + newLine;
		result += "   BankAgent : " + args.getBankAgent() + newLine;
		result += "   EncryptedPAN : " + args.getEncryptedPAN() + newLine;
		result += "   AccountType : " + args.getAccountType() + newLine;
		result += "   OptionalData : " + args.getOptionalData() + newLine;
		localModeTxtArea.appendText(result);
		openCloseNewTry();
	}

	private void terminalReady() {
		terminalReadyRec.setFill(Color.GREEN);
		localModeTxtArea.appendText("Terminal Ready event received" + newLine);
		openCloseNewTry();
	}

	private String getTld(byte[] tldData){
		String tld;
		try {
			tld = new String(tldData, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			tld = "Encoding not supported: \n" + e.getMessage();
		}
		return tld;
	}
	
	private String getTld(String command){
		String tld;
		try {
			tld = getTld(TLDCommand.getBytes(command));
		} catch (UnsupportedEncodingException e) {
			tld = "Encoding not supported: \n" + e.getMessage();
		}
		return tld;
	}
	
	private String getTldReadable(byte[] tldData){
		String tld = getTld(tldData);
		byte[] us = {0x1F};
		byte[] rs = {0x1E};
		try {
			String usStr = new String(us, "UTF-8");
	        String rsStr = new String(rs, "UTF-8");
	        tld = tld.replace(usStr, " ").replace(rsStr, newLine);
		} catch (UnsupportedEncodingException e1) {
			localModeTxtArea.appendText("Failed to perform getTldReadable: " + e1.getMessage());
		}
		return tld;
	}
	
	private void tldReceived(TLDReceivedEventArgs args) {
		String tldDataString = getTld(args.TldData);
		char[] usChar = {(char)0x1e,(char)0x1f,(char)0x20};
		String [] receivedTldSubString = tldDataString.split(usChar[0]+ "|" + usChar[1] + "|" + usChar[2]);

		localModeTxtArea.appendText("Tld received: " + getTldReadable(args.TldData) + newLine);

		if(gciAfterCardCheckBox.isSelected() && receivedTldSubString[1].equals("1013") && receivedTldSubString[2].equals("0001") && receivedTldSubString[3].equals("1")){
			TLDCommand[] commands = { TLDCommand.VASCustomerInfo, TLDCommand.PspCommand, TLDCommand.CustomerId, TLDCommand.InformationField1,
					TLDCommand.CardValidation, TLDCommand.PspVasIdCustomerInfo };
			byte[] tldBytes = new byte[0];
			try {
				tldBytes = TLDCommand.getCommandListAsBytes(commands);
			} catch (UnsupportedEncodingException e) {
				localModeTxtArea.appendText("Unsupported encoding when doing GCI in OnTLDReceived" + newLine);
			}

			SendTldArgs newArgs = new SendTldArgs();
			newArgs.setTldType("REQ");
			newArgs.setTldField(tldBytes);

			int delayGCIAfterCardInMS;
			try{
				delayGCIAfterCardInMS =Integer.parseInt(delay1Txt.getText());
			}catch(NumberFormatException ex){
				delayGCIAfterCardInMS = 0;
			}

			if (delayGCIAfterCardInMS > 0){
				try {
					Thread.sleep(delayGCIAfterCardInMS);
				} catch (InterruptedException e) {
					localModeTxtArea.appendText("Delay for GCI After card interrupted prematurely" + newLine);
				}
			}

			baxi.sendTLD(newArgs);
		}else if (sendTAAfterCardCheckBox.isSelected() && receivedTldSubString[1].equals("2000")){
			int delayTAAfterCardInMS;
			try{
				delayTAAfterCardInMS =Integer.parseInt(delay2Txt.getText());
			}catch(NumberFormatException ex){
				delayTAAfterCardInMS = 0;
			}

			if (delayTAAfterCardInMS > 0){
				try {
					Thread.sleep(delayTAAfterCardInMS);
				} catch (InterruptedException e) {
					localModeTxtArea.appendText("Delay for TA After card interrupted prematurely" + newLine);
				}
			}
			int result = baxi.transferAmount(lastTransferAmountArgs);
			resultTxt.setText(String.valueOf(result));
		}

		if(stopwatchTimerShouldStart){
			stopwatchTimerShouldStart = false;
			try {
				Thread.sleep(103000);
			} catch (InterruptedException e) {
				localModeTxtArea.appendText("Stopwatch timer interrupted too early");
			}

			TransferAmountArgs taArgs = new TransferAmountArgs();
			taArgs.setAmount1(2800);
			taArgs.setAmount2(0);
			taArgs.setAmount3(0);
			taArgs.setArticleDetails("");
			taArgs.setAuthCode("");
			taArgs.setHostData("");
			taArgs.setOperID("0000");
			taArgs.setPaymentConditionCode("");
			taArgs.setType1(0x30);
			taArgs.setType2(0x30);
			taArgs.setType3(0x30);

			baxi.transferAmount(taArgs);
		}
	}

	private void lastFinancialResult(LastFinancialResultEventArgs evt) {
		String result = "AddLastFinancialResult()" + newLine;
		result += "LocalModeResultData: " + evt.getResultData() + newLine;
		result += "Result: " +  + evt.getResult() + newLine;
		result += "   AccumulatorUpdate: "  +  evt.getAccumulatorUpdate() + newLine;
		result += "   IssuerId: " + evt.getIssuerId() + newLine;
		result += "   CardData: " + evt.getTruncatedPan() + newLine;
		result += "   Timestamp: " + evt.getTimestamp() + newLine;
		result += "   VerificationMethod: " + evt.getVerificationMethod() + newLine;
		result += "   SessionNumber: " + evt.getSessionNumber() + newLine;
		result += "   StanAuth: " + evt.getStanAuth() + newLine ;
		result += "   SequenceNumber: " + evt.getSequenceNumber() + newLine;
		result += "   TotalAmount: " + evt.getTotalAmount() + newLine;
		result += "   RejectionSource: " + evt.getRejectionSource() + newLine;
		result += "   RejectionReason: " + evt.getRejectionReason() + newLine;
		result += "   TipAmount: " + evt.getTipAmount() + newLine;
		result += "   SurchargeAmount: " + evt.getSurchargeAmount() + newLine;
		result += "   terminalID: " + evt.getTerminalID() + newLine;
		result += "   acquirerMerchantID: " + evt.getAcquirerMerchantID() + newLine;
		result += "   cardIssuerName: " + evt.getCardIssuerName() + newLine;
		result += "   responseCode: " + evt.getResponseCode() + newLine;
		result += "   TCC: " + evt.getTCC() + newLine;
		result += "   AID: " + evt.getAID() + newLine;
		result += "   TVR: " + evt.getTVR() + newLine;
		result += "   TSI: " + evt.getTSI() + newLine;
		result += "   ATC: " + evt.getATC() + newLine;
		result += "   AED: " + evt.getAED() + newLine;
		result += "   IAC: " + evt.getIAC() + newLine;
		result += "   OrganisationNumber: " + evt.getOrganisationNumber() + newLine;
		result += "   BankAgent : " + evt.getBankAgent() + newLine;
		result += "   EncryptedPAN : " + evt.getEncryptedPAN() + newLine;
		result += "   AccountType : " + evt.getAccountType() + newLine;
		localModeTxtArea.appendText(result);
	}

	private void baxiError(BaxiErrorEventArgs args) {
		errorCodeTxt.setText(String.valueOf(args.getErrorCode()));
		localModeTxtArea.appendText("Error : " + args.getErrorCode() + " " + args.getErrorString() + newLine);
	}

	private void jsonReceived(JsonReceivedEventArgs args) {
		localModeTxtArea.appendText("Json: " + args.getJsonData() + newLine);
	}

	private void barcodeReader(BarcodeReaderEventArgs args){
		localModeTxtArea.appendText("BarcodeData: " + args.getBarcodeText() + newLine);
	}

	private void cardInfoAll(CardInfoAllArgs args) {
		String str = "";

		str += "CardInfoAll() " + newLine;
		str += "VAS: " + args.getVAS() + newLine;
		str += "Customer id: " + args.getCustomerId() + newLine;
		str += "Psp Command: " + args.getPspCommand() + newLine;
		str += "Status Code: " + args.getStatusCode() + newLine;
		str += "Information Field 1: " + args.getInformationField1() + newLine;
		str += "Information Field 2: " + args.getInformationField2() + newLine;
		str += "Psp Vas ID: " + args.getPspVasId() + newLine;
		str += "Card Validation: " + args.getCardValidation() + newLine;
		str += "ICC: " + args.getICCGroupId() + newLine;
		str += "PAN: " + args.getPAN() + newLine;
		str += "Issuer ID: " + args.getIssuerId() + newLine;
		str += "Country Code: " + args.getCountryCode() + newLine;
		str += "Card Restrictions: " + args.getCardRestrictions() + newLine;
		str += "Card Fee: " + args.getCardFee() + newLine;
		str += "Track2: " + args.getTrack2() + newLine;
		str += "TCC: " + args.getTCC() + newLine;
		str += "Bank Agent: " + args.getBankAgent() + newLine;

		localModeTxtArea.appendText(str);
	}

	// Menu clicked methods
	@FXML
	protected void setPropertyClicked() {
		PropertyController propertyController = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(HandleResources.getFxmlSetProperty()));
			Stage stage = new Stage();
			stage.setTitle("Set Properties");
			stage.setScene(new Scene((Pane) (loader.load())));
			HandleResources.LoadStylesheet(stage.getScene());
			propertyController = loader.getController();
			propertyController.initData(baxi);
			stage.initModality(Modality.WINDOW_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			localModeTxtArea.appendText("Couldn't load new window due to: " + e.getMessage() + newLine);
		}

		if(propertyController != null) {
			if (propertyController.getDialogResult() == DialogResult.OK) {
				Properties newProperty = propertyController.getCurrentProperty();
				String newValue = propertyController.getNewProperty();

				switch(newProperty){
				case None:
					// Do nothing
					break;
				case LogFilePrefix:
					baxi.setLogFilePrefix(newValue);
					break;
				case LogFilePath:
					baxi.setLogFilePath(newValue);
					break;
				case TraceLevel:
					try {
						baxi.setTraceLevel(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case BaudRate:
					try {
						baxi.setBaudRate(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case ComPort:
					try {
						baxi.setComPort(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case HostPort:
					try {
						baxi.setHostPort(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case HostIpAddress:
					baxi.setHostIpAddress(newValue);
					break;
				case VendorInfoExtended:
					baxi.setVendorInfoExtended(newValue);
					break;
				case IndicateEotTransaction:
					try {
						baxi.setIndicateEotTransaction(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case CutterSupport:
					try {
						baxi.setCutterSupport(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case PrinterWidth:
					try {
						baxi.setPrinterWidth(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case DisplayWidth:
					try {
						baxi.setDisplayWidth(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case PowerCycleCheck:
					try {
						baxi.setPowerCycleCheck(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case TidSupervision:
					try {
						baxi.setTidSupervision(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case AutoGetCustomerInfo:
					try {
						baxi.setAutoGetCustomerInfo(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case TerminalReady:
					try {
						baxi.setTerminalReady(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case UseDisplayTextID:
					try {
						baxi.setUseDisplayTextID(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case UseExtendedLocalMode:
					try {
						baxi.setUseExtendedLocalMode(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case UseSplitDisplayText:
					try {
						baxi.setUseSplitDisplayText(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case SerialDriver:
					baxi.setSerialDriver(newValue);
					break;
				case DeviceString:
					baxi.setDeviceString(newValue);
					break;
				case SocketListenerPort:
					try {
						baxi.setSocketListenerPort(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case CardInfoAll:
					try {
						baxi.setCardInfoAll(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case LogAutoDeleteDays:
					try {
						baxi.setLogAutoDeleteDays(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				case SocketListener:
					try {
						baxi.setSocketListener(Integer.parseInt(newValue));
					}catch(NumberFormatException e){
						localModeTxtArea.appendText("That is not a valid setting!" +newLine);
					}
					break;
				default:
					localModeTxtArea.appendText("No such property exists!" + newLine);
					break;
				}
			}
		}
	}

	@FXML
	protected void setDefaultMenuClicked() {
		baxi.setLogFilePrefix("baxilog");
		baxi.setHostIpAddress("91.102.24.111");
		baxi.setVendorInfoExtended("BBS;Retail;02.13.01;57807343803;");
		baxi.setBaudRate(9600);
		baxi.setHostPort(9670);
		baxi.setTraceLevel(4);
		baxi.setComPort(1);
		baxi.setDeviceString("SAGEM MONETEL USB Telium");
		baxi.setIndicateEotTransaction(1);
		baxi.setCutterSupport(0);
		baxi.setPrinterWidth(24);
		baxi.setDisplayWidth(20);
		baxi.setPowerCycleCheck(0);
		baxi.setTidSupervision(0);
		baxi.setAutoGetCustomerInfo(0);
		baxi.setTerminalReady(0);
		baxi.setUseDisplayTextID(1);
		baxi.setUseExtendedLocalMode(1);
		baxi.setSerialDriver("Nets");
		baxi.setUseSplitDisplayText(0);
		baxi.setCardInfoAll(0);
		baxi.setSocketListenerPort(6001);
		baxi.setLogAutoDeleteDays(0);
		baxi.setSocketListener(0);

		if(Conversions.isRunningLinux() || Conversions.isRunningAndroid()){
			baxi.setLogFilePath("/var/log/baxi/logfiles/");
		}else{
			baxi.setLogFilePath("C:\\baxi\\logfiles");
		}
	}

	@FXML
	protected void readWPropertiesMenuClicked() {
		String str;

		printTxtArea.clear();

		str = "LogFilePath            =" + baxi.getLogFilePath() + newLine;
		str += "LogFilePrefix         =" + baxi.getLogFilePrefix() + newLine;
		str += "TraceLevel            =" + baxi.getTraceLevel() + newLine;
		str += "BaudRate              =" + baxi.getBaudRate() + newLine;
		str += "ComPort               =" + baxi.getComPort() + newLine;
		str += "DeviceString          =" + baxi.getDeviceString() + newLine;
		str += "HostPort              =" + baxi.getHostPort() + newLine;
		str += "HostIpAddress         =" + baxi.getHostIpAddress() + newLine;
		str += "VendorInfoExtended    =" + baxi.getVendorInfoExtended()	+ newLine;
		str += "IndicateEotTrans      =" + baxi.getIndicateEotTransaction()	+ newLine;
		str += "CutterSupport         =" + baxi.getCutterSupport() + newLine;
		str += "PrinterWidth          =" + baxi.getPrinterWidth() + newLine;
		str += "DisplayWidth          =" + baxi.getDisplayWidth() + newLine;
		str += "PowerCycleCheck       =" + baxi.getPowerCycleCheck() + newLine;
		str += "TidSupervision        =" + baxi.getTidSupervision() + newLine;
		str += "AutoGetCustomerInfo   =" + baxi.getAutoGetCustomerInfo()+ newLine;
		str += "TerminalReady         =" + baxi.getTerminalReady() + newLine;
		str += "UseDisplayTextID      =" + baxi.getUseDisplayTextID() + newLine;
		str += "UseExtendedLocalMode  =" + baxi.getUseExtendedLocalMode() + newLine;
		str += "UseSplitDisplayText   =" + baxi.getUseSplitDisplayText() + newLine;
		str += "CardInfoAll           =" + baxi.getCardInfoAllAsInt() + newLine;
		str += "SocketListener        =" + baxi.getSocketListener() + newLine;
		str += "SocketListenerPort    =" + baxi.getSocketListenerPort() + newLine;
		str += "LogAutodeletedays     =" + baxi.getLogAutoDeleteDays() + newLine;

		printTxtArea.appendText(str);
	}

	@FXML
	protected void readRPropertiesMenuClicked() {
		String str = "";
		byte[] terminalVersionBuf;
		String terminalVersionStr = "";
		byte[] TLD_TAG_SWC_VERSION = new byte[] { 0x31, 0x30, 0x30, 0x35 };

		if (baxi.getTerminalDeviceData_TLD() != null) {
			terminalVersionBuf = baxi.getTLDTag(TLD_TAG_SWC_VERSION, baxi.getTerminalDeviceData_TLD());

			if (terminalVersionBuf != null) {
				try {
					terminalVersionStr = "Terminal version "+ new String(terminalVersionBuf, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					localModeTxtArea.appendText("Could not read terminalVersion due to " + ex.getMessage());
				}
			}
		}
		printTxtArea.clear();
		str += "Version             =" + baxi.getVersion() + newLine;
		str += "Sofie version       =" + baxi.getTermType() + newLine;
		str += "MethodRejectCode    =" + baxi.getMethodRejectCode() + newLine;
		str += "MethodRejectInfo    =" + baxi.getMethodRejectInfo() + newLine;
		str += "TerminalID          =" + baxi.getTerminalID() + newLine;
		str += "TerminalSerialNr.   =" + baxi.getTerminalSerialNumber() + newLine;
		str += "Terminal Version    =" + terminalVersionStr + newLine;
		str += "TerminalDeviceData_TLD =" + getTldReadable(baxi.getTerminalDeviceData_TLD()) + newLine;

		printTxtArea.appendText(str);
	}

	// Main area button methods
	@FXML
	protected void clearDisplayClicked() {
		displayTxtArea.clear();
	}

	@FXML
	protected void clearPrintClicked() {
		printTxtArea.clear();
	}

	@FXML
	protected void localModeClearClicked() {
		localModeTxtArea.clear();
	}

	@FXML
	protected void transferCardDataClicked() {
		if(baxi instanceof BaxiEF){
			((BaxiEF)baxi).transferCardData(new TransferCardDataArgs(Integer.parseInt(originTxt.getText(), 16), cardDataTxt.getText()));
		}else{
			baxi.transferCardData(new TransferCardDataArgs(Integer.parseInt(originTxt.getText(),16),cardDataTxt.getText()));
		}

	}

	@FXML
	protected void readIssuerIdSetBtnClicked(){
		Set<Integer> issuerIDSet = new HashSet<>();
		String issuerIDText =  baxiEFIssuerIDSetTxt.getText();

		if(!issuerIDText.isEmpty()){
			String[] issuerIds = issuerIDText.split(";");

			for(String id:issuerIds){
				try{
					issuerIDSet.add(Integer.parseInt(id));
				}catch(NumberFormatException e){
					localModeTxtArea.appendText("\n That Issuerid is not a number! \n");
				}
			}

			((BaxiEF)baxi).setCGIIssuerIDSet(issuerIDSet);
		}else{
			((BaxiEF)baxi).clearCGIIssuerIDSet();
		}
	}

	// Basic tab button methods
	@FXML
	protected void openClicked() {
		clearUIs();
		int result = baxi.open();
		resultTxt.setText(String.valueOf(result));
	}

	@FXML
	protected void closeClicked() {
		clearUIs();
		int result = baxi.close();
		resultTxt.setText(String.valueOf(result));
	}

	@FXML
	protected void administrationClicked(ActionEvent event) {
		if(baxi.isOpen()) {
			Scene mainScene = ((Node)event.getSource()).getScene();
			AdminController adminController = (AdminController) spawnModalWindow("Administration", HandleResources.getFxmlAdmin(), mainScene.getWindow());

			if(adminController != null) {
				if (adminController.getDialogResult() == DialogResult.OK) {
					clearUIs();
					
					if (adminController.getAdmCode().isEmpty() || adminController.getOperID().isEmpty()) {
						localModeTxtArea.appendText("Neither OperID or Admin Code can be empty!" + newLine);
					} else {
						operID = adminController.getOperID();
						AdministrationArgs args = new AdministrationArgs();
						args.AdmCode = Integer.parseInt(adminController.getAdmCode(), 16);
						args.OperID = operID;
						args.OptionalData = adminController.getOptionalData();
						startOperation = System.currentTimeMillis();
						baxi.administration(args);
					}
				}
			}
		}else{
			localModeTxtArea.appendText("Baxi is not opened. Open it before running admin." + newLine);
		}
	}

	@FXML
	protected void transferAmountClicked(ActionEvent event) {
		if(baxi.isOpen()) {
			Scene mainScene = ((Node)event.getSource()).getScene();
			TransferAmountController transferAmountController = (TransferAmountController) spawnModalWindow("TransferAmount", HandleResources.getFxmlTransferAmount(), mainScene.getWindow());
			
			if(transferAmountController != null) {
				if (transferAmountController.getDialogResult() == DialogResult.OK) {
					clearUIs();
					
					TransferAmountArgs args = new TransferAmountArgs();
					args.setOperID(transferAmountController.getOperID());
					args.setType1(transferAmountController.getType1Code());
					args.setAmount1(transferAmountController.getAmount1());
					args.setType2(transferAmountController.getType2Code());
					args.setAmount2(transferAmountController.getAmount2());
					args.setType3(transferAmountController.getType3Code());
					args.setAmount3(transferAmountController.getAmount3());
					args.setHostData(transferAmountController.getHostData());
					args.setArticleDetails(getTld(transferAmountController.getTopupDetails()));
					args.setPaymentConditionCode(transferAmountController.getPCC());
					args.setAuthCode(transferAmountController.getAuthCode());
					args.setOptionalData(transferAmountController.getOptionalData());

					lastTransferAmountArgs = args;
					startOperation = System.currentTimeMillis();
					int result = baxi.transferAmount(args);
					resultTxt.setText(String.valueOf(result));
				}
			}
		}else{
			localModeTxtArea.appendText("Baxi is not opened. Open it before running transferamount." + newLine);
		}
	}

	@FXML
	protected void bibAdministrationClicked(ActionEvent event) {
		if(baxi.isOpen()) {
			Scene mainScene = ((Node)event.getSource()).getScene();
			BibAdminController bibAdminController = (BibAdminController) spawnModalWindow("BiB Administration", HandleResources.getFxmlBibAdmin(), mainScene.getWindow());
			
			if(bibAdminController != null) {
				if (bibAdminController.getDialogResult() == DialogResult.OK) {
					clearUIs();
					
					if (bibAdminController.getAdmCode().isEmpty() || bibAdminController.getOperID().isEmpty()) {
						localModeTxtArea.appendText("Neither OperID or Admin Code can be empty!" + newLine);
					} else {
						operID = bibAdminController.getOperID();
						BiBAdministrationArgs args = new BiBAdministrationArgs(Integer.parseInt(bibAdminController.getAdmCode(), 16), operID);
						startOperation = System.currentTimeMillis();
						baxi.biBAdministration(args);
					}
				}
			}
		}else{
			localModeTxtArea.appendText("Baxi is not opened. Open it before running BibAdmin." + newLine);
		}
	}

	@FXML
	protected void bibTransactionClicked(ActionEvent event) {
		if(baxi.isOpen()) {
			clearUIs();
			Scene mainScene = ((Node)event.getSource()).getScene();
			BibTransactionController bibTransactionController = (BibTransactionController) spawnModalWindow("BiB Transaction", HandleResources.getFxmlBibTransaction(), mainScene.getWindow());

			if(bibTransactionController != null) {
				if (bibTransactionController.getDialogResult() == DialogResult.OK) {
					startOperation = System.currentTimeMillis();
					baxi.biBTransaction(new BiBTransactionArgs(bibTransactionController.getAmount(), bibTransactionController.getTransactionData()));
				}
			}
		}else{
			localModeTxtArea.appendText("Baxi is not opened. Open it before running BibTransaction." + newLine);
		}
	}

	@FXML
	protected void admCancelClicked() {
		int result = baxi.administration(new AdministrationArgs(0x3132, operID));
		resultTxt.setText(String.valueOf(result));
		if(result != 1) {
			methodRejectTxt.setText(baxi.getMethodRejectInfo());
			errorCodeTxt.setText(String.valueOf(baxi.getMethodRejectCode()));
		}
	}

	@FXML
	protected void sendTLDClicked(ActionEvent event) {
		if(baxi.isOpen()) {
			clearUIs();
			Scene mainScene = ((Node)event.getSource()).getScene();
			SendTLDController sendTLDController = (SendTLDController) spawnModalWindow("Send TLD", HandleResources.getFxmlSendTld(), mainScene.getWindow());
			
			if(sendTLDController != null) {
				if (sendTLDController.getDialogResult() == DialogResult.OK) {
					try {
						SendTldArgs args = new SendTldArgs(sendTLDController.getTLDType(),sendTLDController.getTLDField());
						baxi.sendTLD(args);
					} catch (UnsupportedEncodingException e) {
						localModeTxtArea.appendText("Could not send TLD, encoding not supported." + newLine);
					}
				}
			}
		}else{
			localModeTxtArea.appendText("Baxi is not opened. Open it before running SendTLD." + newLine);
		}
	}

	@FXML
	protected void admReadyClicked() {
		int result = baxi.administration(new AdministrationArgs(0x3131, operID));
		resultTxt.setText(String.valueOf(result));
		if(result != 1) {
			methodRejectTxt.setText(baxi.getMethodRejectInfo());
			errorCodeTxt.setText(String.valueOf(baxi.getMethodRejectCode()));
		}
	}

	@FXML
	protected void getMethodRejectClicked() {
		methodRejectTxt.setText(baxi.getMethodRejectInfo());
	}
	
	@FXML
	protected void sendJSONBtnClicked(ActionEvent event) {
		if(baxi.isOpen()){
			clearUIs();
			Scene mainScene = ((Node)event.getSource()).getScene();
			SendJSONController sendJSONController = (SendJSONController) spawnModalWindow("Send JSON", HandleResources.getFxmlSendJson(), mainScene.getWindow());
			
			if(sendJSONController != null) {
				if (sendJSONController.getDialogResult() == DialogResult.OK) {
					SendJsonArgs args = new SendJsonArgs(sendJSONController.getJSONData());
					baxi.sendJson(args);
				}
			}
		}else{
			localModeTxtArea.appendText("Baxi is not opened. Open it before running SendJSON." + newLine);
		}
	}

	// Development tab button methods
	@FXML
	protected void killBaxiClicked() {
		baxi = null;
		System.gc();
	}

	@FXML
	protected void disposeBaxiClicked() {
		baxi.dispose();
	}

	@FXML
	protected void newBaxiClicked() {
		baxi = new BaxiCtrl();
		baxi.addBaxiCtrlEventListener(baxiListener);
		readIssuerIdSetBtn.setDisable(true);
		baxiEFIssuerIDSetlbl.setDisable(true);
		baxiEFIssuerIDSetTxt.setDisable(true);
	}

	@FXML
	protected void createSemaphoreClicked() {
		if(openSem == null){
			openSem = new Semaphore(1,false);
		}else{
			openSem.release();
		}
	}

	@FXML
	protected void retainSemaphoreClicked() {
		if(openSem != null){
			openSem.acquireUninterruptibly();
		}
	}

	@FXML
	protected void releaseSemaphoreClicked() {
		if(openSem != null){
			openSem.release();
		}
	}

	@FXML
	protected void closeThenReleaseClicked() {
		baxi.close();
		openSem.release();
	}

	@FXML
	protected void openAfterGetClicked() {
		if(openSem != null ){
			openSem.acquireUninterruptibly();
			int result = baxi.open();
			resultTxt.setText(String.valueOf(result));
			if(result == 1){
				localModeTxtArea.appendText("Baxi.Open after close with semaphore went OK!!" + newLine);
			}
			else{
				localModeTxtArea.appendText("Baxi.Open after close with semaphore FAILED!!" + newLine);
			}
		}
		else{
			localModeTxtArea.appendText("cannot perform, openSem is NULL!");
		}
	}

	@FXML
	protected void openOpenClicked() {
		int result;
		clearUIs();
		result = baxi.open();
		if( result == 0 ){
			localModeTxtArea.appendText(baxi.getMethodRejectCode() + " ");
			localModeTxtArea.appendText(baxi.getMethodRejectInfo() + newLine);
			localModeTxtArea.appendText("Admin...");
			result = baxi.biBAdministration(new BiBAdministrationArgs(0x3131, "123456"));
			localModeTxtArea.appendText("result: " + result + newLine);
			if(result == 0){
				localModeTxtArea.appendText(baxi.getMethodRejectCode() + " ");
				localModeTxtArea.appendText(baxi.getMethodRejectInfo() + newLine);
			}
		}
	}

	@FXML
	protected void tldTaTimeoutClicked() {
		int result;
		// string request = "3002 US 0000 US RS";
		SendTldArgs args = new SendTldArgs();
		args.setTldType("REQ");
		args.setTldField(new byte[] {0x33, 0x30, 0x30, 0x32, 0x1f, 0x30, 0x30, 0x30, 0x30, 0x1f, 0x1e});
		result = baxi.sendTLD(args);
		if (result == 1){
			stopwatchTimerShouldStart = true;
		}
	}

	@FXML
	protected void openCloseClicked() {
		openCloseCounter = 0;
		openCloseTest();
	}

	// Init methods
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		openCloseCounter = -1;
	}

	// Other private methods
	private void clearUIs(){
		terminalReadyRec.setFill(Color.RED);
		displayTxtArea.clear();
		printTxtArea.clear();
		localModeTxtArea.clear();
		resultTxt.clear();
		methodRejectTxt.clear();
		errorCodeTxt.clear();
	}

	private void openCloseTest(){
		int result = 1;

		if(openCloseCounter < 100){
			openCloseCounter++;
			resultTxt.setText(String.valueOf(openCloseCounter));

			if(openCloseCounter != 0 ){
				result = baxi.close();
			}

			if(result == 1) {
				baxi.open();
			}
		}else{
			openCloseCounter = -1;
		}
	}

	private void openCloseNewTry(){
		if(baxi.getTerminalReady() == 1 && openCloseCounter > -1){
			openCloseTest();
		}
	}

	private Controller spawnModalWindow(String title, String path, Window owner){
		Controller controller = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			Stage stage = new Stage();
			stage.setTitle(title);
			Scene scene = new Scene((Pane) (loader.load()));
			HandleResources.LoadStylesheet(scene);
			stage.setScene(scene);
			controller = loader.getController();
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(owner);
			stage.showAndWait();
		} catch (IOException e) {
			localModeTxtArea.appendText("Couldn't load new window due to: " + e.getMessage() + newLine);
		}
		return controller;
	}

	public void initData(BaxiCtrl baxi) {
		this.baxi = baxi;
		if(baxi instanceof BaxiEF){
			((BaxiEF) baxi).addBaxiEFListener(efListener);
		}else{
			baxi.addBaxiCtrlEventListener(baxiListener);
			readIssuerIdSetBtn.setDisable(true);
			baxiEFIssuerIDSetlbl.setDisable(true);
			baxiEFIssuerIDSetTxt.setDisable(true);
		}
	}
}