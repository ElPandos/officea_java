package eu.nets.baxi.gui.transferamount;

import eu.nets.baxi.gui.controller.Controller;
import eu.nets.baxi.gui.controller.DialogResult;
import eu.nets.baxi.gui.json.JSONMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TransferAmountController extends Controller implements Initializable{

	@FXML private ComboBox<String> type1Combo;
	@FXML private ComboBox<String> type2Combo;
	@FXML private ComboBox<String> type3Combo;
	@FXML private ComboBox<String> topUpCombo;
	@FXML private TextField amount1Txt;
	@FXML private TextField amount2Txt;
	@FXML private TextField amount3Txt;
	@FXML private TextField operIdTxt;
	@FXML private TextField hostDataTxt;
	@FXML private TextField paymentConditionTxt;
	@FXML private TextField authCodeTxt;
	@FXML private TextArea optionalDataTxtArea;
	@FXML private TextArea topUpTxtArea;

	// private methods
	private void updateTopupTexts(String input){
		topUpTxtArea.appendText(input);
		topUpTxtArea.appendText(" RS ");
	}

	// Host data methods
	@FXML
	protected void trumfDataClicked() {
		hostDataTxt.setText("INS123456789012345678901234567890123456");
	}

	// Article Data methods
	@FXML
	protected void addClicked() {
		String codeToAdd = topUpCombo.getValue();
		codeToAdd = codeToAdd.substring(codeToAdd.lastIndexOf('_') + 1);
		updateTopupTexts(codeToAdd);
	}

	@FXML
	protected void addSeparatorClicked() {
		topUpTxtArea.appendText(" RS ");
	}

	// Optional data methods
	@FXML
	protected void autoDCCBtnClicked() {
		optionalDataTxtArea.setText(JSONMessage.getAutoDCC(JSONMessage.TYPE_TA));
	}

	@FXML
	protected void multiterminalBtnClicked() {
		optionalDataTxtArea.setText(JSONMessage.getMerch(JSONMessage.TYPE_TA));
	}

	@FXML
	protected void allDataBtnClicked() {
		optionalDataTxtArea.setText(JSONMessage.getAllData(JSONMessage.TYPE_TA));
	}

	@FXML
	protected void txnRefNrBtnClicked() {
		optionalDataTxtArea.setText(JSONMessage.getTxnRef(JSONMessage.TYPE_TA));
	}

	// Main methods
	@FXML
	protected void okClicked(ActionEvent event) {
		setDialogResult(DialogResult.OK);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	protected void cancelClicked(ActionEvent event) {
		setDialogResult(DialogResult.Cancel);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		type1Combo.getSelectionModel().select(0);
		type2Combo.getSelectionModel().select(0);
		type3Combo.getSelectionModel().select(0);
		topUpCombo.getSelectionModel().select(0);
	}

	// Getters
	public int getAmount1(){
		try {
			return Integer.parseInt(amount1Txt.getText());
		}catch(NumberFormatException ex){
			return 0;
		}
	}

	public int getAmount2(){
		try {
			return Integer.parseInt(amount2Txt.getText());
		}catch(NumberFormatException ex){
			return 0;
		}
	}

	public int getAmount3(){
		try {
			return Integer.parseInt(amount3Txt.getText());
		}catch(NumberFormatException ex){
			return 0;
		}
	}

	public String getOperID(){
		return operIdTxt.getText();
	}

	public int getType1Code(){
		return Integer.parseInt(type1Combo.getValue().substring(0, 2), 16);
	}

	public int getType2Code(){
		return Integer.parseInt(type2Combo.getValue().substring(0,2),16);
	}

	public int getType3Code(){
		return Integer.parseInt(type3Combo.getValue().substring(0,2),16);
	}

	public String getHostData(){
		return hostDataTxt.getText();
	}

	public String getTopupDetails(){
		return topUpTxtArea.getText();
	}

	public String getPCC(){
		return paymentConditionTxt.getText();
	}

	public String getAuthCode(){
		return authCodeTxt.getText();
	}

	public String getOptionalData(){
		return optionalDataTxtArea.getText();
	}
}
