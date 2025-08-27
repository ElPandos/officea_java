/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.fxml.mainCenter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pos_client.ResourcesHandler;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.fxml.edit.EditController;
import pos_client.fxml.mainCenter.MainCenterController.AnchType;
import pos_client.fxml.payment.PaymentController;
import pos_client.fxml.special.SpecialController;
import pos_client.fxml.work.WorkController;
import pos_client.windows.Log;

public class MainCenterController implements Initializable
{

    public PaymentController paymentController = null;
    public WorkController workController = null;
    public SpecialController specialController = null;
    public EditController editController = null;

    @FXML
    private AnchorPane center;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    }

    public void init()
    {
        toFront(AnchType.WORK);
    }

    public void refresh()
    {
        update();
    }

    private void update()
    {
        language();
    }

    private void language()
    {
        /*
        btnCancelReceipt.setText(Language.getInstance().getBtnReceiptCancel());
        btnDiscount.setText(Language.getInstance().getBtnReceiptDiscount());
        String str = Language.getInstance().getBtnReceiptPark();
        btnParkReceipt.setText(Language.getInstance().getBtnReceiptPark());
        btnSplitReceipt.setText(Language.getInstance().getBtnReceiptSplit());
         */
    }

    public Scene getScene()
    {
        return center.getScene();
    }

    public void refreshCSS()
    {
        Scene scene = getScene();
        Stage stage = (Stage) scene.getWindow();
        scene.getStylesheets().clear();

        File f = new File(ResourcesHandler.getInstance().getThemeUpdate());
        String path = "file:///" + f.getAbsolutePath().replace("\\", "/");

        scene.getStylesheets().add(path);
        stage.setScene(scene);
    }

    public enum AnchType
    {
        NONE,
        WORK,
        PAYMENT,
        SPECIAL,
        EDIT
    };

    private ArrayList<AnchType> sceneOrder = new ArrayList<>();

    private int getAnchTypeLayer(AnchType type)
    {
        int pos = 0;
        for (AnchType anch : sceneOrder)
        {
            if (anch == type)
            {
                break;
            } else
            {
                pos++;
            }
        }

        return pos;
    }

    public void loadPayment() throws IOException
    {
        if (Core.getInstance().getControlHandler().getPaymentController() == null)
        {
            FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlPayment());
            Parent root = (Parent) loader.load();

            Core.getInstance().getControlHandler().setPaymentController(loader.getController());

            General.getInstance().fitToParent(Core.getInstance().getControlHandler().getMainController().getAnchor(), Core.getInstance().getControlHandler().getPaymentController().getAnchor());
            Core.getInstance().getControlHandler().getMainController().getAnchor().getChildren().add(root);

            sceneOrder.add(AnchType.PAYMENT);
        }
    }

    public void loadWork() throws IOException
    {
        if (Core.getInstance().getControlHandler().getWorkController() == null)
        {
            FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlWork());
            Parent root = (Parent) loader.load();

            Core.getInstance().getControlHandler().setWorkController(loader.getController());
            Core.getInstance().getControlHandler().getWorkController().init();

            General.getInstance().fitToParent(Core.getInstance().getControlHandler().getMainController().getAnchor(), Core.getInstance().getControlHandler().getWorkController().getAnchor());
            Core.getInstance().getControlHandler().getMainController().getAnchor().getChildren().add(root);

            sceneOrder.add(AnchType.WORK);
        }
    }

    public void loadSpecial() throws IOException
    {
        if (Core.getInstance().getControlHandler().getSpecialController() == null)
        {
            FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlSpecial());
            Parent root = (Parent) loader.load();

            Core.getInstance().getControlHandler().setSpecialController(loader.getController());

            General.getInstance().fitToParent(Core.getInstance().getControlHandler().getMainController().getAnchor(), Core.getInstance().getControlHandler().getSpecialController().getAnchor());
            Core.getInstance().getControlHandler().getMainController().getAnchor().getChildren().add(root);

            sceneOrder.add(AnchType.SPECIAL);
        }
    }

    public void loadEdit() throws IOException
    {
        if (Core.getInstance().getControlHandler().getEditController() == null)
        {
            FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlEdit());
            Parent root = (Parent) loader.load();

            Core.getInstance().getControlHandler().setEditController(loader.getController());

            General.getInstance().fitToParent(Core.getInstance().getControlHandler().getMainController().getAnchor(), Core.getInstance().getControlHandler().getEditController().getAnchor());
            Core.getInstance().getControlHandler().getMainController().getAnchor().getChildren().add(root);

            sceneOrder.add(AnchType.EDIT);
        }
    }

    public void loadCenterAnchs()
    {
        // Denna ska bara köras en gång när den startar upp
        if (!sceneOrder.contains(AnchType.NONE))
        {
            sceneOrder.add(AnchType.NONE); // Ful-lösning för att få anchsen i rätt ordning
            try
            {
                // Måste vara i rätt ordning enligt enum AnchType
                loadPayment(); // 1
                loadSpecial(); // 2
                loadEdit(); // 3
                loadWork(); // 4 denna pga Work blir frontad först så AnchType ordningen stämmer inte annars
            } catch (IOException ex)
            {
                Core.getInstance().getLog().log("loadCenterAnchs - Lyckades inte ladda centrum GUIT", Log.LogLevel.CRITICAL);
            }

            init();
        }
    }

    public void toFront(AnchType child)
    {
        ObservableList<Node> anchList = Core.getInstance().getControlHandler().getMainController().getAnchor().getChildren();
        if (anchList.size() > child.ordinal())
        {
            switch (child)
            {
                case NONE:
                case WORK:
                    hideBottomButtons(false);
                    Core.getInstance().getControlHandler().getMainPaneLeftController().setDisableAnchor(false);
                    Core.getInstance().getControlHandler().getMainPaneLeftController().refresh();
                    break;
                case SPECIAL:
                case EDIT:
                    hideBottomButtons(false);
                    Core.getInstance().getControlHandler().getMainPaneLeftController().setDisableAnchor(true);
                    break;
                case PAYMENT:
                    hideBottomButtons(true);
                    Core.getInstance().getControlHandler().getMainPaneLeftController().setDisableAnchor(true);
                    Core.getInstance().getControlHandler().getPaymentController().refresh();
                    break;

            }

            int pos = getAnchTypeLayer(child);
            anchList.get(pos).toFront();

            AnchType copy = sceneOrder.get(pos);
            sceneOrder.remove(pos);
            sceneOrder.add(copy);
        }
    }

    private void hideBottomButtons(boolean choice)
    {
        AnchorPane anch = Core.getInstance().getControlHandler().getMainController().mainbottomrightanchor;
        if (choice)
        {
            anch.setMaxHeight(0);
            anch.setMinHeight(0);
        } else
        {
            anch.setMaxHeight(200);
            anch.setMinHeight(200);
        }
    }

}
