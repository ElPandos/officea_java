/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.windows;

import javafx.collections.ObservableList;
import pos_client.ArticleModel;
import pos_client.ResourcesHandler;
import pos_client.fxml.screens.CustomerController;

/**
 *
 * @author Server
 */
public class Customer extends Screen
{

    CustomerController ctrl = null;

    public Customer()
    {

    }

    public void load()
    {
        loadScreen(ResourcesHandler.getInstance().getFxmlScreenCustomer(), ResourcesHandler.getInstance().getThemeMain());
        changeScreen(1);

        stage.setAlwaysOnTop(true);

        ctrl = loader.getController();
    }

    public void setReceiptData(ObservableList<ArticleModel> receiptData)
    {
        ctrl.setCustomerReceiptData(receiptData);
    }

}
