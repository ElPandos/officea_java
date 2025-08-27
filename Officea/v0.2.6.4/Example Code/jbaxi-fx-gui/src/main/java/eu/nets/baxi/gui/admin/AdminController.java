package eu.nets.baxi.gui.admin;

import eu.nets.baxi.gui.controller.Controller;
import eu.nets.baxi.gui.controller.DialogResult;
import eu.nets.baxi.gui.json.JSONMessage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController extends Controller implements Initializable {
	@FXML private ToggleGroup adminToggle;

	@FXML private TextField operIDTxt;
	@FXML private TextField adminCodeTxt;

	@FXML private RadioButton reconRadio;
	@FXML private RadioButton readyRadio;
	@FXML private RadioButton cancelRadio;
	@FXML private RadioButton wrongRadio;
	@FXML private RadioButton annulRadio;
	@FXML private RadioButton balanceRadio;
	@FXML private RadioButton xReportRadio;
	@FXML private RadioButton zReportRadio;
	@FXML private RadioButton sendOfflineRadio;
	@FXML private RadioButton turnoverReportRadio;
	@FXML private RadioButton printEOTRadio;
	@FXML private RadioButton testCommsRadio;
	@FXML private RadioButton finishedRadio;
	@FXML private RadioButton lFReceiptRadio;
	@FXML private RadioButton lFResultRadio;
	@FXML private RadioButton softwareDownloadRadio;
	@FXML private RadioButton datasetDownloadRadio;

	@FXML private TextArea optionalDataTxt;

	@FXML
	protected void okBtnClicked(ActionEvent event) {
		setDialogResult(DialogResult.OK);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	protected void cancelBtnClicked(ActionEvent event) {
		setDialogResult(DialogResult.Cancel);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	protected void multiterminalBtnClicked() {
		optionalDataTxt.setText(JSONMessage.getMerch(JSONMessage.TYPE_ADMIN));
	}

	@FXML
	protected void txnRefNrBtn() {
		optionalDataTxt.setText(JSONMessage.getTxnRef(JSONMessage.TYPE_ADMIN));
	}

	@FXML
	protected void allDataBtnClicked() {
		optionalDataTxt.setText(JSONMessage.getAllData(JSONMessage.TYPE_ADMIN));
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		reconRadio.setUserData("3130");
		readyRadio.setUserData("3131");
		cancelRadio.setUserData("3132");
		wrongRadio.setUserData("3133");
		annulRadio.setUserData("3134");
		balanceRadio.setUserData("3135");
		xReportRadio.setUserData("3136");
		zReportRadio.setUserData("3137");
		sendOfflineRadio.setUserData("3138");
		turnoverReportRadio.setUserData("3139");
		printEOTRadio.setUserData("313A");
		testCommsRadio.setUserData("3135");
		finishedRadio.setUserData("313B");
		lFReceiptRadio.setUserData("313C");
		lFResultRadio.setUserData("313D");
		softwareDownloadRadio.setUserData("313E");
		datasetDownloadRadio.setUserData("313F");

		adminToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if(new_toggle == null) {
					adminCodeTxt.setText("");
				}else{
					adminCodeTxt.setText(adminToggle.getSelectedToggle().getUserData().toString());
				}
			}
		});
	}

	public String getOperID() {
		return operIDTxt.getText();
	}

	public String getAdmCode(){
		return adminCodeTxt.getText();
	}

	public String getOptionalData(){
		return optionalDataTxt.getText();
	}
}