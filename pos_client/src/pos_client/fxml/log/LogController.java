/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.log;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Rectangle;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.windows.Log;
import pos_client.windows.Log.LogLevel;

/**
 * FXML Controller class
 *
 * @author Laptop
 */
public class LogController implements Initializable
{

    @FXML
    private TextArea logWindow;
    @FXML
    private Rectangle recTerminal;
    @FXML
    private Rectangle recDatabase;
    @FXML
    private ComboBox<String> cbxLogLevel;
    @FXML
    private CheckBox chkSpeed;
    @FXML
    private Rectangle recControlUnit;
    @FXML
    private Rectangle recCashBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }

    public TextArea getLogTextArea()
    {
        return logWindow;
    }

    public void setDatabaseStatus(boolean connected)
    {
        status(connected, recDatabase);
    }

    public void setTerminalStatus(boolean connected)
    {
        status(connected, recTerminal);
    }

    public void setControlUnitStatus(boolean connected)
    {
        status(connected, recControlUnit);
    }

    public void setCashBoxStatus(boolean connected)
    {
        status(connected, recCashBox);
    }

    private void status(boolean connected, Rectangle rect)
    {
        if (connected)
        {
            rect.setFill(General.getInstance().CONNECTED);
        } else
        {
            rect.setFill(General.getInstance().DISCONNECTED);
        }
    }

    public boolean isSpeedChecked()
    {
        return chkSpeed.isSelected();
    }

    public void loadLogLevel()
    {
        for (LogLevel level : Log.LogLevel.values())
        {
            String lvlStr = Core.getInstance().getLog().logLevel2String(level);
            cbxLogLevel.getItems().add(lvlStr);
        }

        cbxLogLevel.setValue("Critical");
    }

    public void setLogLevel(LogLevel level)
    {
        cbxLogLevel.setValue(Core.getInstance().getLog().logLevel2String(level));
    }

    public LogLevel getLogLevel()
    {

        return Core.getInstance().getLog().string2LogLevel(cbxLogLevel.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void cmdClear(ActionEvent event)
    {

        logWindow.clear();
    }

    @FXML
    private void OnLoglevelChange(ActionEvent event)
    {

        //String selected = cbxLogLevel.getSelectionModel().getSelectedItem();
        //Core.getInstance().getLog().setLogLevel(Core.getInstance().getLog().string2LogLevel(selected));
    }

}
