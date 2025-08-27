package eu.nets.baxi.gui.tld;

import eu.nets.baxi.gui.controller.Controller;
import eu.nets.baxi.gui.controller.DialogResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import java.io.UnsupportedEncodingException;

public class SendTLDController extends Controller{

	private static final byte[] us = {0x1F};
	private static final byte[] rs = {0x1E};

	@FXML private TextField tldFieldTxt;
	@FXML private TextField typeTxt;

	// Ordinary buttons
	@FXML
	protected void clearBtnClicked() {
		tldFieldTxt.setText("");
		typeTxt.setText("");
	}
	
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
	
	// Buttons in TerminalInfo Tab
	@FXML
	protected void ituTestInfoBtnClicked() {
		tldFieldTxt.appendText("1000 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void ituStatusInfoBtnClicked() {
		tldFieldTxt.appendText("1001 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void ituPhysicalSerialNumberBtnClicked() {
		tldFieldTxt.appendText("1010 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void 	ituProtocolVersionNumberBtnClicked() {
		tldFieldTxt.appendText("1011 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void 	ituVersionBtnClicked() {
		tldFieldTxt.appendText("1004 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void 	ituSWVersionBtnClicked() {
		tldFieldTxt.appendText("1005 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void ituTerminalIDBtnClicked() {
		tldFieldTxt.appendText("1002 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
		
	@FXML
	protected void ituSiteIDBtnClicked() {
		tldFieldTxt.appendText("1003 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void ituPSPConnectionStatusBtnClicked() {
		tldFieldTxt.appendText("1012 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void ituLanguageCodeSetBtnClicked() {
		tldFieldTxt.appendText("1014 US 0002 US en RS ");
		typeTxt.setText("CMD");
	}
	
	@FXML
	protected void ituServiceTextIDSetBtnClicked() {
		tldFieldTxt.appendText("1015 US 0000 US RS ");
		typeTxt.setText("CMD");
	}
	
	@FXML
	protected void ituTimeStampPowerOn1BtnClicked() {
		tldFieldTxt.appendText("1006 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void ituTimeStampPowerOn2BtnClicked() {
		tldFieldTxt.appendText("1007 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void ituTimeStampPowerOff1BtnClicked() {
		tldFieldTxt.appendText("1008 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	@FXML
	protected void ituTimeStampPowerOff2BtnClicked() {
		tldFieldTxt.appendText("1009 US 0000 US RS ");
		typeTxt.setText("INFO");
	}
	
	// Buttons in VasIdInfo Tab
	@FXML
	protected void vasBtnClicked() {
		tldFieldTxt.appendText("2000 US 0003 US 001 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void pspCommandGetBtnClicked() {
		tldFieldTxt.appendText("2002 US 0003 US 001 RS");
		typeTxt.setText("REQ");
	}

	@FXML
	protected void customerIDBtnClicked() {
		tldFieldTxt.appendText("2001 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void informationField1BtnClicked() {
		tldFieldTxt.appendText("2006 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void cardValidationBtnClicked() {
		tldFieldTxt.appendText("2015 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void pspVASIDBtnClicked() {
		tldFieldTxt.appendText("2008 US 0003 US 001 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void pspCommandGenerateSignatureTokenBtnClicked() {
		tldFieldTxt.appendText("2008 US 0003 US 004 RS " + "2002 US 0003 US 003 RS " + "2011 US 0001 US 1 RS " +
				"2012 US 0001 US 0 RS " + "2013 US 0005 US 12500 RS " + "2006 US 0005 US 12345 RS " + "2007 US 0005 US 12345 RS ");
		typeTxt.setText("REQ");
	}
	
	// Buttons in Customer Dialog tab
	@FXML
	protected void customerDialogueHeaderBtnClicked() {
		tldFieldTxt.appendText("2100 US 0003 US 001 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void textIDBtnClicked() {
		tldFieldTxt.appendText("2101 US 0003 US 002 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void keyboardInputFormatBtnClicked() {
		tldFieldTxt.appendText("2102 US 0001 US 1 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void cardInputFormatBtnClicked() {
		tldFieldTxt.appendText("2103 US 0001 US 2 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void keyboardMinCharBtnClicked() {
		tldFieldTxt.appendText("2104 US 0003 US 008 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void keyboardMaxCharBtnClicked() {
		tldFieldTxt.appendText("2105 US 0003 US 008 RS ");
		typeTxt.setText("REQ");
	}
	
	// Buttons in Card Info tab
	@FXML
	protected void getCardInfoHeaderBtnClicked() {
		tldFieldTxt.appendText("3010 US 0003 US 001 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void iccGroupIDBtnClicked() {
		tldFieldTxt.appendText("3000 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void panBtnClicked() {
		tldFieldTxt.appendText("3001 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void issuerIDBtnClicked() {
		tldFieldTxt.appendText("3002 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void countryCodeBtnClicked() {
		tldFieldTxt.appendText("3003 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void cardRestrictionsBtnClicked() {
		tldFieldTxt.appendText("3004 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void cardFeeBtnClicked() {
		tldFieldTxt.appendText("3005 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void track2BtnClicked() {
		tldFieldTxt.appendText("3009 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void track3BtnClicked() {
		tldFieldTxt.appendText("3013 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void tccBtnClicked() {
		tldFieldTxt.appendText("3011 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void bankAgentBtnClear() {
		tldFieldTxt.appendText("3012 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void chipLoyaltyBtnClicked() {
		tldFieldTxt.appendText("3014 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void allTagsBtnClicked() {
		tldFieldTxt.appendText("3999 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	// Buttons in Digital Assets tab
	@FXML
	protected void digitalAssetHeaderBtnClicked() {
		tldFieldTxt.appendText("2200 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void digitalAssetTypeBtnClicked() {
		tldFieldTxt.appendText("2203 US 0003 US 998 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void digitalAssetActionBtnClicked() {
		tldFieldTxt.appendText("2204 US 0003 US 001 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void apacsTypeBtnClicked() {
		tldFieldTxt.appendText("2202 US 0001 US 0 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void apacsEncryptionDescriptorBtnClicked() {
		tldFieldTxt.appendText("2201 US 0001 US 0 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void digitalAssetDataRAToDAMBtnClicked() {
		tldFieldTxt.appendText("2207 US 0007 US 0010808 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void promptConsumerCardBtnClicked() {
		tldFieldTxt.appendText("2205 US 0001 US 1 RS ");
		typeTxt.setText("REQ");
	}
	
	@FXML
	protected void promptConsumerKBDBtnClicked() {
		tldFieldTxt.appendText("5000 US 0000 US RS ");
		typeTxt.setText("REQ");
	}
	
	// Buttons for ECR Command tab
	@FXML
	protected void ecrCmdEnveloperBtnClicked() {
		tldFieldTxt.appendText("5000 US 0000 US RS ");
		typeTxt.setText("CMD");
	}
	
	@FXML
	protected void setPinPadBacklightBtnClicked() {
		tldFieldTxt.appendText("5001 US 0001 US 1 RS ");
		typeTxt.setText("CMD");
	}
	
	@FXML
	protected void setCardReaderBacklightBtnClicked() {
		tldFieldTxt.appendText("5002 US 0001 US 1 RS ");
		typeTxt.setText("CMD");
	}
	
	@FXML
	protected void setPinShieldBacklightBtnClicked() {
		tldFieldTxt.appendText("5003 US 0001 US 1 RS ");
		typeTxt.setText("CMD");
	}

	public String getTLDType(){
		return typeTxt.getText();
	}

	public byte[] getTLDField() throws UnsupportedEncodingException{
		String tldFieldString = tldFieldTxt.getText();
		String usStr = new String(us, "UTF-8");
		String rsStr = new String(rs, "UTF-8");

		tldFieldString = tldFieldString.replace(" US", usStr);
		tldFieldString = tldFieldString.replace(" RS", rsStr);
		tldFieldString = tldFieldString.replace(" ", "");
		return tldFieldString.getBytes("UTF-8");
	}
}