package eu.nets.baxi.gui.properties;

import eu.nets.baxi.client.BaxiCtrl;
import eu.nets.baxi.gui.controller.Controller;
import eu.nets.baxi.gui.controller.DialogResult;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class PropertyController extends Controller implements Initializable{

	private Properties currentProperty;
	private String newProperty;
	private BaxiCtrl baxi;

	@FXML private ToggleGroup propertiesToggle;

	@FXML private RadioButton logFilePrefixRadio;
	@FXML private RadioButton logFilePathRadio;
	@FXML private RadioButton traceLevelRadio;
	@FXML private RadioButton serialDriverRadio;
	@FXML private RadioButton baudRateRadio;
	@FXML private RadioButton comPortRadio;
	@FXML private RadioButton deviceStringRadio;
	@FXML private RadioButton powerCycleCheckRadio;
	@FXML private RadioButton tidSupervisionRadio;
	@FXML private RadioButton hostPortRadio;
	@FXML private RadioButton hostIpAddressRadio;
	@FXML private RadioButton vendorInfoExtendedRadio;
	@FXML private RadioButton cutterSupportRadio;
	@FXML private RadioButton useSplitDisplayTextRadio;
	@FXML private RadioButton indicateEotTransactionRadio;
	@FXML private RadioButton autoGetCustomerInfoRadio;
	@FXML private RadioButton terminalReadyRadio;
	@FXML private RadioButton useDisplayTextIDRadio;
	@FXML private RadioButton socketListenerPortRadio;
	@FXML private RadioButton useExtendedLocalModeRadio;
	@FXML private RadioButton cardInfoAllRadio;
	@FXML private RadioButton logAutoDeleteDaysRadio;
	@FXML private RadioButton socketListenerRadio;

	@FXML private TextField setPropertyTxt;

	// Button methods
	@FXML
	protected void okBtnClicked(ActionEvent event) {
		newProperty = setPropertyTxt.getText();
		setDialogResult(DialogResult.OK);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	protected void cancelBtnClicked(ActionEvent event) {
		setDialogResult(DialogResult.Cancel);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		logFilePrefixRadio.setUserData(Properties.LogFilePrefix);
		logFilePathRadio.setUserData(Properties.LogFilePath);
		traceLevelRadio.setUserData(Properties.TraceLevel);
		serialDriverRadio.setUserData(Properties.SerialDriver);
		baudRateRadio.setUserData(Properties.BaudRate);
		comPortRadio.setUserData(Properties.ComPort);
		deviceStringRadio.setUserData(Properties.DeviceString);
		powerCycleCheckRadio.setUserData(Properties.PowerCycleCheck);
		tidSupervisionRadio.setUserData(Properties.TidSupervision);
		hostPortRadio.setUserData(Properties.HostPort);
		hostIpAddressRadio.setUserData(Properties.HostIpAddress);
		vendorInfoExtendedRadio.setUserData(Properties.VendorInfoExtended);
		cutterSupportRadio.setUserData(Properties.CutterSupport);
		useSplitDisplayTextRadio.setUserData(Properties.UseSplitDisplayText);
		indicateEotTransactionRadio.setUserData(Properties.IndicateEotTransaction);
		autoGetCustomerInfoRadio.setUserData(Properties.AutoGetCustomerInfo);
		terminalReadyRadio.setUserData(Properties.TerminalReady);
		useDisplayTextIDRadio.setUserData(Properties.UseDisplayTextID);
		socketListenerPortRadio.setUserData(Properties.SocketListenerPort);
		useExtendedLocalModeRadio.setUserData(Properties.UseExtendedLocalMode);
		cardInfoAllRadio.setUserData(Properties.CardInfoAll);
		logAutoDeleteDaysRadio.setUserData(Properties.LogAutoDeleteDays);
		socketListenerRadio.setUserData(Properties.SocketListener);

		propertiesToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (new_toggle == null) {
					currentProperty = Properties.None;
					setPropertyTxt.setText("");
				} else {
					currentProperty = ((Properties) propertiesToggle.getSelectedToggle().getUserData());
					if(baxi != null) {
						switch (currentProperty) {
						case None:
							setPropertyTxt.setText("");
							break;
						case LogFilePrefix:
							setPropertyTxt.setText(baxi.getLogFilePrefix());
							break;
						case LogFilePath:
							setPropertyTxt.setText(baxi.getLogFilePath());
							break;
						case TraceLevel:
							setPropertyTxt.setText(String.valueOf(baxi.getTraceLevel()));
							break;
						case BaudRate:
							setPropertyTxt.setText(String.valueOf(baxi.getBaudRate()));
							break;
						case ComPort:
							setPropertyTxt.setText(String.valueOf(baxi.getComPort()));
							break;
						case HostPort:
							setPropertyTxt.setText(String.valueOf(baxi.getHostPort()));
							break;
						case HostIpAddress:
							setPropertyTxt.setText(baxi.getHostIpAddress());
							break;
						case VendorInfoExtended:
							setPropertyTxt.setText(baxi.getVendorInfoExtended());
							break;
						case IndicateEotTransaction:
							setPropertyTxt.setText(String.valueOf(baxi.getIndicateEotTransaction()));
							break;
						case CutterSupport:
							setPropertyTxt.setText(String.valueOf(baxi.getCutterSupport()));
							break;
						case PrinterWidth:
							setPropertyTxt.setText(String.valueOf(baxi.getPrinterWidth()));
							break;
						case DisplayWidth:
							setPropertyTxt.setText(String.valueOf(baxi.getDisplayWidth()));
							break;
						case PowerCycleCheck:
							setPropertyTxt.setText(String.valueOf(baxi.getPowerCycleCheck()));
							break;
						case TidSupervision:
							setPropertyTxt.setText(String.valueOf(baxi.getTidSupervision()));
							break;
						case AutoGetCustomerInfo:
							setPropertyTxt.setText(String.valueOf(baxi.getAutoGetCustomerInfo()));
							break;
						case TerminalReady:
							setPropertyTxt.setText(String.valueOf(baxi.getTerminalReady()));
							break;
						case UseDisplayTextID:
							setPropertyTxt.setText(String.valueOf(baxi.getUseDisplayTextID()));
							break;
						case UseExtendedLocalMode:
							setPropertyTxt.setText(String.valueOf(baxi.getUseExtendedLocalMode()));
							break;
						case UseSplitDisplayText:
							setPropertyTxt.setText(String.valueOf(baxi.getUseDisplayTextID()));
							break;
						case SerialDriver:
							setPropertyTxt.setText(baxi.getSerialDriver());
							break;
						case DeviceString:
							setPropertyTxt.setText(baxi.getDeviceString());
							break;
						case SocketListenerPort:
							setPropertyTxt.setText(String.valueOf(baxi.getSocketListenerPort()));
							break;
						case CardInfoAll:
							setPropertyTxt.setText(String.valueOf(baxi.getCardInfoAll()));
							break;
						case LogAutoDeleteDays:
							setPropertyTxt.setText(String.valueOf(baxi.getLogAutoDeleteDays()));
							break;
						case SocketListener:
							setPropertyTxt.setText(String.valueOf(baxi.getSocketListener()));
							break;
						default:
							setPropertyTxt.setText("");
							break;
						}
					}
				}
			}
		});
	}

	public String getNewProperty(){
		return newProperty;
	}

	public Properties getCurrentProperty(){
		return currentProperty;
	}

	public void initData(BaxiCtrl baxi) {
		this.baxi = baxi;
	}
}