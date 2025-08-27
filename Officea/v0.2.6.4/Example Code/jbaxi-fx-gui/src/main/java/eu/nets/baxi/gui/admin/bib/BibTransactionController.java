package eu.nets.baxi.gui.admin.bib;

import eu.nets.baxi.gui.controller.Controller;
import eu.nets.baxi.gui.controller.DialogResult;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class BibTransactionController extends Controller implements Initializable {
	private String transactionData;

	private enum TransType{
		Deposit,
		Deposit2,
		GiroDeposit,
		CurrencyDeposit,
		Withdrawal,
		CashWithdrawalCreditcard,
		CurrencyWithdrawal,
		GiroWithdrawal,
		CashCheck,
		BankCheck,
		Reversal,
		Saldo
	}

	private TransType transtype;

	@FXML private ToggleGroup bibTransToggle;

	@FXML private RadioButton depositRadio;
	@FXML private RadioButton deposit2Radio;
	@FXML private RadioButton giroDepositRadio;
	@FXML private RadioButton currencyDepositRadio;
	@FXML private RadioButton cashWithdrawalRadio;
	@FXML private RadioButton withdrawalRadio;
	@FXML private RadioButton currencyWithdrawalRadio;
	@FXML private RadioButton giroWithdrawalRadio;
	@FXML private RadioButton saldoRadio;
	@FXML private RadioButton cashCheckRadio;
	@FXML private RadioButton bankCheckRadio;
	@FXML private RadioButton reversalRadio;

	@FXML private TextField operIDText;
	@FXML private TextField amountText;
	@FXML private TextField sequenceText;
	@FXML private TextField accountText;
	@FXML private TextField buntText;
	@FXML private TextField kidText;
	@FXML private TextField prefixText;
	@FXML private TextField formText;
	@FXML private TextField commissionText;
	@FXML private TextField issueText;
	@FXML private TextField validText;
	@FXML private TextField personText;

	@FXML private TextField directText;

	@FXML private CheckBox withCardCheckBox;

	@FXML
	protected void okBtnClicked(ActionEvent event) {
		if(!directText.getText().isEmpty()){
			transactionData = directText.getText();
		}else{
			SetTransactionData(transtype);
		}
		setDialogResult(DialogResult.OK);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	protected void cancelBtnClicked(ActionEvent event) {
		setDialogResult(DialogResult.Cancel);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	protected void withCardCheckBoxChanged() {
		updateWithCard(transtype);
	}


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		transactionData = null;
		setTransactionType(TransType.Deposit);
		depositRadio.setSelected(true);

		depositRadio.setUserData(TransType.Deposit);
		deposit2Radio.setUserData(TransType.Deposit2);
		giroDepositRadio.setUserData(TransType.GiroDeposit);
		currencyDepositRadio.setUserData(TransType.CurrencyDeposit);
		cashWithdrawalRadio.setUserData(TransType.CashWithdrawalCreditcard);
		withdrawalRadio.setUserData(TransType.Withdrawal);
		currencyWithdrawalRadio.setUserData(TransType.CurrencyWithdrawal);
		giroWithdrawalRadio.setUserData(TransType.GiroWithdrawal);
		saldoRadio.setUserData(TransType.Saldo);
		cashCheckRadio.setUserData(TransType.CashCheck);
		bankCheckRadio.setUserData(TransType.BankCheck);
		reversalRadio.setUserData(TransType.Reversal);

		bibTransToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (new_toggle == null) {
					clearEnabledTextBoxes();
				} else {
					setTransactionType((TransType) bibTransToggle.getSelectedToggle().getUserData());
				}
			}
		});
	}

	private void setTransactionType(TransType userData) {
		transtype = userData;
		clearEnabledTextBoxes();
		updateWithCard(userData);
		switch(userData){
		case Deposit:
		case Deposit2:
		case Withdrawal:
			operIDText.setDisable(false);
			personText.setDisable(false);
			accountText.setDisable(false);
			commissionText.setDisable(false);
			break;

		case GiroDeposit:
			operIDText.setDisable(false);
			accountText.setDisable(false);
			buntText.setDisable(false);
			formText.setDisable(false);
			kidText.setDisable(false);
			prefixText.setDisable(false);
			commissionText.setDisable(false);
			break;

		case CurrencyDeposit:
		case CurrencyWithdrawal:
			operIDText.setDisable(false);
			accountText.setDisable(false);
			commissionText.setDisable(false);
			break;

		case CashWithdrawalCreditcard:
			operIDText.setDisable(false);
			break;

		case GiroWithdrawal:
			operIDText.setDisable(false);
			formText.setDisable(false);
			buntText.setDisable(false);
			accountText.setDisable(false);
			issueText.setDisable(false);
			validText.setDisable(false);
			prefixText.setDisable(false);
			commissionText.setDisable(false);
			break;

		case CashCheck:
			operIDText.setDisable(false);
			accountText.setDisable(false);
			buntText.setDisable(false);
			formText.setDisable(false);
			prefixText.setDisable(false);
			commissionText.setDisable(false);
			break;

		case BankCheck:
			operIDText.setDisable(false);
			accountText.setDisable(false);
			buntText.setDisable(false);
			prefixText.setDisable(false);
			break;

		case Reversal:
			operIDText.setDisable(false);
			personText.setDisable(false);
			break;

		case Saldo:
			operIDText.setDisable(false);
			sequenceText.setDisable(false);
			break;
		}
	}

	private void updateWithCard(TransType userData) {
		switch (userData){
		case Deposit:
			if (withCardCheckBox.isSelected()){
				personText.setText("           ");
			}else{
				personText.setText("23046147987");
			}
			break;
		case Deposit2:
		case Saldo:
		case Withdrawal:
			if(withCardCheckBox.isSelected()){
				personText.setText("           ");
			}else{
				personText.setText("23046147987");
			}
			break;
		default:
			// Transaction type does not affect With Card - Do nothing.
			break;
		}
	}

	private void clearEnabledTextBoxes(){
		//This one is always editable
		amountText.setDisable(false);

		// Others are cleared
		operIDText.setDisable(true);
		sequenceText.setDisable(true);
		accountText.setDisable(true);
		buntText.setDisable(true);
		kidText.setDisable(true);
		prefixText.setDisable(true);
		formText.setDisable(true);
		commissionText.setDisable(true);
		issueText.setDisable(true);
		validText.setDisable(true);
		personText.setDisable(true);
	}

	public String getTransactionData() {
		return transactionData;
	}

	private void SetTransactionData(TransType transtype) {
		switch(transtype){

		case Deposit:
			transactionData = "4" + operIDText.getText() + personText.getText() + accountText.getText() + commissionText.getText();
			break;
		case Deposit2:
			transactionData = "D" + operIDText.getText() + personText.getText() + accountText.getText() + commissionText.getText();
			break;
		case GiroDeposit:
			if(kidText.getText().isEmpty()){
				transactionData = "1" + operIDText.getText() + accountText.getText() + buntText.getText() + formText.getText() +
						prefixText.getText() + commissionText.getText();
			}else{
				transactionData = "7" + operIDText.getText() + accountText.getText() + buntText.getText() + formText.getText() +
						kidText.getText() + prefixText.getText() + commissionText.getText();
			}
			break;
		case CurrencyDeposit:
			transactionData = "B" + operIDText.getText() + accountText.getText() +	"           " + "           " +
					"                         " + "    " + commissionText.getText();
			break;
		case Withdrawal:
			transactionData = "3" + operIDText.getText() + personText.getText() + accountText.getText() + commissionText.getText();
			break;
		case CashWithdrawalCreditcard:
			transactionData = "A" + operIDText.getText();
			break;
		case CurrencyWithdrawal:
			transactionData = "C" + operIDText.getText() + accountText.getText() + "           " + "           " +
					"                         " + "    " + commissionText.getText();
			break;
		case GiroWithdrawal:
			transactionData = "2" + operIDText.getText() + formText.getText() + buntText.getText() + accountText.getText() +
					issueText.getText() + validText.getText() +	"             " + prefixText.getText() +
					commissionText.getText();
			break;
		case CashCheck:
			transactionData = "8" + operIDText.getText() + accountText.getText() + buntText.getText() + formText.getText() +
					prefixText.getText()+ commissionText.getText();
			break;
		case BankCheck:
			transactionData = "@" + operIDText.getText() + accountText.getText() +
					buntText.getText()+ prefixText.getText();
			break;
		case Reversal:
			transactionData = "9" + operIDText.getText() + sequenceText.getText() + "  ";
			break;
		case Saldo:
			transactionData = "5" + operIDText.getText() + personText.getText();
			break;
		default:
			transactionData = ""; // Unknown type
			break;
		}
	}

	public int getAmount(){
		try{
			return Integer.parseInt(amountText.getText());
		}catch (NumberFormatException e){
			return 0;
		}
	}

}