package eu.nets.baxi.gui.admin.bib;

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

public class BibAdminController extends Controller implements Initializable{
	
	@FXML private ToggleGroup bibToggle;
	@FXML private RadioButton startBibModeRadio;
	@FXML private RadioButton endBibModeRadio;
	@FXML private RadioButton cancelRadio;
	@FXML private RadioButton reconciliationRadio;
	@FXML private RadioButton newCustomerRadio;
	@FXML private RadioButton identifyCustomerRadio;
	@FXML private RadioButton endCustomerRadio;

	@FXML private TextField operIDTxt;
	@FXML private TextField adminCodeTxt;

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

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		startBibModeRadio.setUserData("3133");
		endBibModeRadio.setUserData("3134");
		cancelRadio.setUserData("3132");
		reconciliationRadio.setUserData("3130");
		newCustomerRadio.setUserData("3131");
		identifyCustomerRadio.setUserData("7831");
		endCustomerRadio.setUserData("3135");

		bibToggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (new_toggle == null) {
					adminCodeTxt.setText("");
				} else {
					adminCodeTxt.setText(bibToggle.getSelectedToggle().getUserData().toString());
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
}
