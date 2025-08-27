/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import pos_client.common.General;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import pos_client.db.dao.SettingsDAO;

/**
 *
 * @author Laptop
 */
public class DiscountArticle extends TableCell<ArticleModel, Boolean>
{

    private Receipt currentReceipt;
    private TableView table;
    private Button payButton;

    private ToggleButton addDiscountButton_1 = new ToggleButton();
    private ToggleButton addDiscountButton_2 = new ToggleButton();
    private ToggleButton addDiscountButton_3 = new ToggleButton();
    private ToggleButton addDiscountButton_4 = new ToggleButton();

    private StackPane paddedPane = new StackPane();
    private GridPane gridPane = new GridPane();

    DiscountArticle(TableView table, Receipt receipt, ToggleGroup group, Button payButton)
    {

        this.payButton = payButton;
        this.table = table;
        this.currentReceipt = receipt;

        addDiscountButton_1.setMinSize(40, 40);
        addDiscountButton_1.setMaxSize(40, 40);
        addDiscountButton_2.setMinSize(40, 40);
        addDiscountButton_2.setMaxSize(40, 40);
        addDiscountButton_3.setMinSize(40, 40);
        addDiscountButton_3.setMaxSize(40, 40);
        addDiscountButton_4.setMinSize(40, 40);
        addDiscountButton_4.setMaxSize(40, 40);

        addDiscountButton_1.setSelected(true);
        addDiscountButton_1.setToggleGroup(group);
        addDiscountButton_2.setToggleGroup(group);
        addDiscountButton_3.setToggleGroup(group);
        addDiscountButton_4.setToggleGroup(group);

        SettingsDAO settingsDAO = new SettingsDAO();

        addDiscountButton_1.setText(settingsDAO.getSetting("Discount-1"));
        addDiscountButton_2.setText(settingsDAO.getSetting("Discount-2"));
        addDiscountButton_3.setText(settingsDAO.getSetting("Discount-3"));
        addDiscountButton_4.setText(settingsDAO.getSetting("Discount-4"));

        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.setMaxHeight(Double.MAX_VALUE);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(Insets.EMPTY);

        gridPane.add(addDiscountButton_1, 0, 0);
        gridPane.add(addDiscountButton_2, 1, 0);
        gridPane.add(addDiscountButton_3, 2, 0);
        gridPane.add(addDiscountButton_4, 3, 0);

        paddedPane.setMaxWidth(Double.MAX_VALUE);
        paddedPane.setMaxHeight(Double.MAX_VALUE);
        paddedPane.setPadding(Insets.EMPTY);
        paddedPane.getChildren().add(gridPane);

        addDiscountButton_1.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                int selectedIndex = getTableRow().getIndex();
                currentReceipt.getAllSales().get(selectedIndex).getArticle().getSale().setDiscount((float) General.getInstance().str2Int(settingsDAO.getSetting("Discount-1")) / 100);
                updateButton(selectedIndex);
            }
        });

        addDiscountButton_2.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                int selectedIndex = getTableRow().getIndex();
                currentReceipt.getAllSales().get(selectedIndex).getArticle().getSale().setDiscount((float) General.getInstance().str2Int(settingsDAO.getSetting("Discount-2")) / 100);
                updateButton(selectedIndex);
            }
        });

        addDiscountButton_3.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                int selectedIndex = getTableRow().getIndex();
                currentReceipt.getAllSales().get(selectedIndex).getArticle().getSale().setDiscount((float) General.getInstance().str2Int(settingsDAO.getSetting("Discount-3")) / 100);
                updateButton(selectedIndex);
            }
        });

        addDiscountButton_4.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                int selectedIndex = getTableRow().getIndex();
                currentReceipt.getAllSales().get(selectedIndex).getArticle().getSale().setDiscount((float) General.getInstance().str2Int(settingsDAO.getSetting("Discount-4")) / 100);
                updateButton(selectedIndex);
            }
        });
    }

    /**
     * places an add button in the row only if the row is not empty.
     */
    @Override
    protected void updateItem(Boolean item, boolean empty)
    {
        super.updateItem(item, empty);
        if (!empty)
        {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedPane);
        } else
        {
            setGraphic(null);
        }
    }

    private void updateTable()
    {
        ((TableColumn) table.getColumns().get(0)).setVisible(false);
        ((TableColumn) table.getColumns().get(0)).setVisible(true);
    }

    private void updateButton(int index)
    {
        payButton.setText(currentReceipt.updatePayment());
        updateTable();
        table.getSelectionModel().select(index);
    }
}
