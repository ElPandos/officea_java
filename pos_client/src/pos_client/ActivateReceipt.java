/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import pos_client.common.Core;
import pos_client.common.DialogHandler;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.fxml.mainPaneLeft.MainPaneLeftController.TabType;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class ActivateReceipt extends TableCell<Receipt, Boolean>
{

    final Button addButton = new Button("Öppna");
    final StackPane paddedButton = new StackPane();
    final DoubleProperty buttonY = new SimpleDoubleProperty();

    public ActivateReceipt()
    {
        addButton.setMinWidth(60);
        addButton.setMaxWidth(60);
        addButton.setMaxHeight(40);
        addButton.setMinHeight(40);
        addButton.textAlignmentProperty().set(TextAlignment.CENTER);
        addButton.setId("activateparked");

        paddedButton.getChildren().add(addButton);

        addButton.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                buttonY.set(mouseEvent.getScreenY());
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                UserModel user = Core.getInstance().getLoginHandler().getUser();

                int selectedIndex = getTableRow().getIndex();

                ReceiptDAO receiptDAO = new ReceiptDAO();
                ObservableList<Receipt> receipts = receiptDAO.getReceipts(ReceiptType.PARKED);

                if (receipts.size() > 0)
                {
                    Receipt parkedReceipt = receipts.get(selectedIndex);
                    UserModel parkedUser = parkedReceipt.getUser();
                    if (parkedUser.getName().compareTo(user.getName()) != 0)
                    {
                        String title = "Switcha kvitto";
                        String msg = "Detta kvitto är skapat av: " + parkedUser.getName() + ". Vill du öppna det?";
                        String detail = "Byter ägare på kvittot till " + Core.getInstance().getLoginHandler().getUser().getName();

                        try
                        {
                            DialogHandler dialogHandler = new DialogHandler();
                            if (dialogHandler.question(((Node) actionEvent.getSource()).getScene(), title, msg, detail))
                            {
                                receiptDAO.updateReceiptUser(parkedReceipt, user);
                            }
                        } catch (IOException | InterruptedException ex)
                        {
                            Core.getInstance().getLog().log("ActivateReceipt - Det blev nått fel i dialogen: " + ex.toString(), Log.LogLevel.CRITICAL);
                        }
                    }

                    user.swapReceipt(parkedReceipt);

                    Core.getInstance().getControlHandler().getMainPaneLeftController().activateTab(TabType.ACTIVE);
                } else
                {
                    Core.getInstance().getLog().log("Hittade inga parkerade kvitton!", Log.LogLevel.CRITICAL);
                }
            }
        });
    }

    /**
     * Places an add button in the row only if the row is not empty.
     */
    @Override
    protected void updateItem(Boolean item, boolean empty)
    {
        super.updateItem(item, empty);
        if (!empty)
        {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);
        } else
        {
            setGraphic(null);
        }
    }

}
