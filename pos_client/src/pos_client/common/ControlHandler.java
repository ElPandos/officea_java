/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.common;

import pos_client.fxml.edit.EditController;
import pos_client.fxml.keyboard.large.KeyboardController;
import pos_client.fxml.main.MainController;
import pos_client.fxml.mainButtonsRight.MainButtonsRightController;
import pos_client.fxml.mainCenter.MainCenterController;
import pos_client.fxml.mainInfoPane.MainInfoPaneController;
import pos_client.fxml.mainPaneLeft.MainPaneLeftController;
import pos_client.fxml.payment.PaymentController;
import pos_client.fxml.special.SpecialController;
import pos_client.fxml.work.WorkController;

/**
 *
 * @author Server
 */
public class ControlHandler
{

    private MainController mainController = null;
    private MainButtonsRightController mainButtonsRightController = null;
    private MainInfoPaneController mainInfoPaneController = null;
    private MainCenterController mainCenterController = null;
    private MainPaneLeftController mainPaneLeftController = null;
    private WorkController workController = null;
    private PaymentController paymentController = null;
    private EditController editController = null;
    private SpecialController specialController = null;
    private KeyboardController keyboardController = null;

    public ControlHandler()
    {
    }

    public void setMainController(MainController mainController)
    {
        this.mainController = mainController;
    }

    public MainController getMainController()
    {
        return this.mainController;
    }

    public void setMainButtonsRightController(MainButtonsRightController mainButtonsRightController)
    {
        this.mainButtonsRightController = mainButtonsRightController;
    }

    public MainButtonsRightController getMainButtonsRightController()
    {
        return this.mainButtonsRightController;
    }

    public void setMainInfoPaneController(MainInfoPaneController mainInfoPaneController)
    {
        this.mainInfoPaneController = mainInfoPaneController;
    }

    public MainInfoPaneController getMainInfoPaneController()
    {
        return this.mainInfoPaneController;
    }

    public void setMainCenterController(MainCenterController mainCenterController)
    {
        this.mainCenterController = mainCenterController;
    }

    public MainCenterController getMainCenterController()
    {
        return this.mainCenterController;
    }

    public void setMainPaneLeftController(MainPaneLeftController mainPaneLeftController)
    {
        this.mainPaneLeftController = mainPaneLeftController;
    }

    public MainPaneLeftController getMainPaneLeftController()
    {
        return this.mainPaneLeftController;
    }

    public void setWorkController(WorkController workController)
    {
        this.workController = workController;
    }

    public WorkController getWorkController()
    {
        return this.workController;
    }

    public void setPaymentController(PaymentController paymentController)
    {
        this.paymentController = paymentController;
    }

    public PaymentController getPaymentController()
    {
        return this.paymentController;
    }

    public void setEditController(EditController editController)
    {
        this.editController = editController;
    }

    public EditController getEditController()
    {
        return this.editController;
    }

    public void setSpecialController(SpecialController specialController)
    {
        this.specialController = specialController;
    }

    public SpecialController getSpecialController()
    {
        return this.specialController;
    }

    public void setKeyboardController(KeyboardController keyboardController)
    {
        this.keyboardController = keyboardController;
    }

    public KeyboardController getKeyboardController()
    {
        return this.keyboardController;
    }

    public void refreshAllGUI()
    {
        mainController.refresh();
        mainButtonsRightController.refresh();
        mainInfoPaneController.refresh();
        mainCenterController.refresh();
        mainPaneLeftController.refresh();
        workController.refresh();
        paymentController.refresh();
        editController.refresh();
        specialController.refresh();
        keyboardController.refresh();
    }

    public void refreshAllCSS()
    {
        mainController.refreshCSS();
        mainButtonsRightController.refreshCSS();
        mainInfoPaneController.refreshCSS();
        mainCenterController.refreshCSS();
        mainPaneLeftController.refreshCSS();
        workController.refreshCSS();
        paymentController.refreshCSS();
        editController.refreshCSS();
        specialController.refreshCSS();
        keyboardController.refreshCSS();
    }

}
