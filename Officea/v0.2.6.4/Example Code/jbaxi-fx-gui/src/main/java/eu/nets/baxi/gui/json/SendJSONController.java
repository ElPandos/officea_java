package eu.nets.baxi.gui.json;

import eu.nets.baxi.gui.controller.Controller;
import eu.nets.baxi.gui.controller.DialogResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

public class SendJSONController extends Controller{

	@FXML private TextArea sendJSONText;

	@FXML
	protected void cancelBtnClicked(ActionEvent event) {
		setDialogResult(DialogResult.Cancel);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	protected void sendBtnClicked(ActionEvent event) {
		setDialogResult(DialogResult.OK);
		((Node)(event.getSource())).getScene().getWindow().hide();
	}

    @FXML
    protected void cardinfoBtnClicked(){
    	sendJSONText.setText(JSONMessage.getMUALLTAGSref(JSONMessage.TYPE_CI,true));
    }
        
	public String getJSONData(){
		return sendJSONText.getText();
	}
	
}
