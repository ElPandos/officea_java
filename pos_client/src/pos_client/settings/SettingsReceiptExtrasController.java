/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import pos_client.db.dao.BoutiqueDAO;

/**
 * FXML Controller class
 *
 * @author eit-asn
 */
public class SettingsReceiptExtrasController implements Initializable
{

    @FXML
    private TextField txtOrgNo;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAddress1;
    @FXML
    private TextField txtAddress2;
    @FXML
    private TextField txtPostal;
    @FXML
    private TextField txtCity;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtWebsite;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        populate();
    }

    public void refresh()
    {
        clear();
        populate();
    }

    private void clear()
    {
        txtOrgNo.clear();
        txtName.clear();
        txtAddress1.clear();
        txtAddress2.clear();
        txtPostal.clear();
        txtCity.clear();
        txtPhone.clear();
        txtWebsite.clear();
    }

    private void populate()
    {
        BoutiqueDAO boutiqueDAO = new BoutiqueDAO();

        txtOrgNo.setText(boutiqueDAO.getOrgNumber());
        txtName.setText(boutiqueDAO.getCompanyName());
        txtAddress1.setText(boutiqueDAO.getCompanyAdress1());
        txtAddress2.setText(boutiqueDAO.getCompanyAdress2());
        txtPostal.setText(boutiqueDAO.getCompanyPostal());
        txtCity.setText(boutiqueDAO.getCompanyCity());
        txtPhone.setText(boutiqueDAO.getPhoneNumber());
        txtWebsite.setText(boutiqueDAO.getCompanyWebsite());
    }

    @FXML
    private void saveboutique(ActionEvent event)
    {
        String org_no = txtOrgNo.getText();
        String name = txtName.getText();
        String address1 = txtAddress1.getText();
        String address2 = txtAddress2.getText();
        String postal = txtPostal.getText();
        String city = txtCity.getText();
        String phone = txtPhone.getText();
        String website = txtWebsite.getText();

        BoutiqueDAO boutiquDAO = new BoutiqueDAO();

        boutiquDAO.add(org_no, name, address1, address2, postal, city, phone, website);

    }

    @FXML
    private void setboutiqevalues(ActionEvent event)
    {
        populate();
    }

}
