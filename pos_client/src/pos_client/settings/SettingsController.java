package pos_client.settings;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

public class SettingsController implements Initializable
{

    // For nested fxml
    public SettingsArticleController settingsArticleController;
    public SettingsCategoriesController settingsCategoriesController;
    public SettingsCurrencyController settingsCurrencyController;
    public SettingsDatabaseDefaultsController settingsDatabaseDefaultsController;
    public SettingsEmployeesController settingsEmployeesController;
    public SettingsIngredienceController settingsIngredienceController;
    public SettingsIntegrationCashBoxController settingsIntegrationCashBoxController;
    public SettingsIntegrationControlUnitController settingsIntegrationControlUnitController;
    public SettingsIntegrationController settingsIntegrationController;
    public SettingsOtherController settingsOtherController;
    public SettingsReceiptExtrasController settingsReceiptExtrasController;
    public SettingsReportsController settingsReportsController;
    public SettingsSpecialController settingsSpecialController;
    public SettingsUserController settingsUserController;

    @FXML
    private Tab tabEmployees;
    @FXML
    private Tab tabArticles;
    @FXML
    private Tab tabUsers;
    @FXML
    private Tab tabCategories;
    @FXML
    private Tab tabIngredients;
    @FXML
    private Tab tabSpecials;
    @FXML
    private Tab tabReportsPrinters;
    @FXML
    private Tab tabReceiptInfo;
    @FXML
    private Tab tabCurrency;
    @FXML
    private Tab tabOther;
    @FXML
    private Tab tabDatabase;
    @FXML
    private Tab tabUnitIntegration;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

        //tabCategories.disableProperty().setValue(true);
        //tabSpecials.disableProperty().setValue(true);
        tabCurrency.disableProperty().setValue(true);
    }

    @FXML
    private void OnActivated(Event event)
    {

        if (tabEmployees != null && tabEmployees.isSelected())
        {

            if (settingsEmployeesController != null)
            {
                settingsEmployeesController.refresh();
            }
        }

        if (tabUsers != null && tabUsers.isSelected())
        {

            if (settingsUserController != null)
            {
                settingsUserController.refresh();
            }
        }

        if (tabCategories != null && tabCategories.isSelected())
        {

            if (settingsCategoriesController != null)
            {
                settingsCategoriesController.refresh(SettingsCategoriesController.TypeCatergory.ALL);
            }
        }

        if (tabIngredients != null && tabIngredients.isSelected())
        {

            if (settingsIngredienceController != null)
            {
                settingsIngredienceController.refresh();
            }
        }

        if (tabArticles != null && tabArticles.isSelected())
        {

            if (settingsArticleController != null)
            {
                settingsArticleController.refresh();
            }
        }

        if (tabSpecials != null && tabSpecials.isSelected())
        {

            if (settingsSpecialController != null)
            {
                settingsSpecialController.refresh();
            }
        }

        if (tabArticles != null && tabArticles.isSelected())
        {

            if (settingsArticleController != null)
            {
                settingsArticleController.refresh();
            }
        }

        if (tabReportsPrinters != null && tabReportsPrinters.isSelected())
        {

            if (settingsReportsController != null)
            {
                settingsReportsController.refresh();
            }
        }

        if (tabReceiptInfo != null && tabReceiptInfo.isSelected())
        {

            if (settingsReceiptExtrasController != null)
            {
                settingsReceiptExtrasController.refresh();
            }
        }
        if (tabCurrency != null && tabCurrency.isSelected())
        {
            if (settingsCurrencyController != null)
            {
                settingsCurrencyController.refresh();
            }
        }

        if (tabOther != null && tabOther.isSelected())
        {

            if (settingsOtherController != null)
            {
                settingsOtherController.refresh();
            }
        }

        if (tabDatabase != null && tabDatabase.isSelected())
        {

            if (settingsDatabaseDefaultsController != null)
            {
                settingsDatabaseDefaultsController.refresh();
            }
        }

        if (tabUnitIntegration != null && tabUnitIntegration.isSelected())
        {

            // No tables to update
        }

    }
}
