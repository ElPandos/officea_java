/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import pos_client.common.DefinedVariables;
import pos_client.db.dao.SettingsDAO;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsOtherController implements Initializable
{

    @FXML
    private CheckBox chkStartFullscreen;
    @FXML
    private TextField txtDiscount1;
    @FXML
    private TextField txtDiscount2;
    @FXML
    private TextField txtDiscount3;
    @FXML
    private TextField txtDiscount4;
    @FXML
    private Button btnActivateFullscreen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        populate();

        btnActivateFullscreen.setDisable(true);
    }

    public void refresh()
    {

        clear();
        populate();
    }

    private void clear()
    {

        txtDiscount1.clear();
        txtDiscount2.clear();
        txtDiscount3.clear();
        txtDiscount4.clear();
    }

    private void populate()
    {

        loadDiscountSettings();
    }

    private void loadDiscountSettings()
    {

        SettingsDAO settingsDAO = new SettingsDAO();

        txtDiscount1.setText(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_DISCOUNT_1));
        txtDiscount2.setText(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_DISCOUNT_2));
        txtDiscount3.setText(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_DISCOUNT_3));
        txtDiscount4.setText(settingsDAO.getSetting(DefinedVariables.getInstance().SETTING_DISCOUNT_4));
    }

    @FXML
    private void OnFullscreenActivate(ActionEvent event)
    {

    }

    @FXML
    private void OnDiscountSave(ActionEvent event)
    {

        ArrayList<String> discountButtonsName = new ArrayList();

        discountButtonsName.add(DefinedVariables.getInstance().SETTING_DISCOUNT_1);
        discountButtonsName.add(DefinedVariables.getInstance().SETTING_DISCOUNT_2);
        discountButtonsName.add(DefinedVariables.getInstance().SETTING_DISCOUNT_3);
        discountButtonsName.add(DefinedVariables.getInstance().SETTING_DISCOUNT_4);

        ArrayList<String> discountTextfield = new ArrayList<>();

        discountTextfield.add(txtDiscount1.getText());
        discountTextfield.add(txtDiscount2.getText());
        discountTextfield.add(txtDiscount3.getText());
        discountTextfield.add(txtDiscount4.getText());

        SettingsDAO settingsDAO = new SettingsDAO();
        for (int i = 0; i < discountButtonsName.size(); i++)
        {
            settingsDAO.updateSettingValue(discountButtonsName.get(i), discountTextfield.get(i));
        }
    }
}
