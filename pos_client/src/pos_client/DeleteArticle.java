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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import pos_client.common.Core;
import pos_client.common.DialogHandler;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class DeleteArticle extends TableCell<ArticleModel, Boolean>
{

    final Button addButton = new Button("C");

    // Pads and centers the add button in the cell.
    final StackPane paddedButton = new StackPane();

    // Cecords the y pos of the last button press so that the add person dialog can be shown next to the cell.
    final DoubleProperty buttonY = new SimpleDoubleProperty();

    /**
     * AddButtonCell constructor
     *
     * @param stage the stage in which the table is placed.
     * @param table the table to which a new button can be added.
     */
    public DeleteArticle()
    {
        addButton.setMinWidth(40);
        addButton.setMaxWidth(40);
        addButton.setMaxHeight(40);
        addButton.setMinHeight(40);
        addButton.setId("deletearticle");

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

                ObservableList<SaleModel> sales = user.getCurrentReceipt().getAllSales();
                ArticleModel article = sales.get(selectedIndex).getArticle();

                String title = "Ta bort vara";
                String msg = "Vill du ta bort vara: " + article.getName() + " " + article.getSale().getPriceTotalIncmStr() + " kr?";
                String detail = "Bortagna artiklar kommer sparas undan...";
                Scene parentScene = ((Node) actionEvent.getSource()).getScene();

                try
                {
                    DialogHandler dialogHandler = new DialogHandler();
                    if (dialogHandler.question(parentScene, title, msg, detail))
                    {
                        if (user.getCurrentReceipt().removeSale(sales.get(selectedIndex)))
                        {
                            Core.getInstance().getLog().log("Lyckades byta SaleType på varan", Log.LogLevel.NORMAL);
                        }
                    }
                } catch (IOException ex)
                {
                    Core.getInstance().getLog().log("Det blev nått fel i dialogen: " + ex.toString(), Log.LogLevel.NORMAL);
                } catch (InterruptedException ex)
                {
                    Core.getInstance().getLog().log("Det blev nått fel i dialogen: " + ex.toString(), Log.LogLevel.NORMAL);
                }

                Core.getInstance().getControlHandler().getMainPaneLeftController().refresh();
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
