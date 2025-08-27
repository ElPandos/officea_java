/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.db.dao.ArticleCategoryDAO;
import pos_client.db.dao.ArticleDAO;
import pos_client.db.dao.IngredienceDAO;
import pos_client.db.dao.IngredientsCategoryDAO;
import pos_client.db.dao.SalesDAO;
import pos_client.db.dao.SpecialCategoryDAO;
import pos_client.db.dao.SpecialDAO;
import pos_client.windows.Log;

/**
 *
 * @author Server
 */
public class CategoryHandler
{

    public enum CategoryType
    {
        ARTICLE,
        EDIT,
        SPECIAL
    }

    public CategoryHandler()
    {

    }

    private void populateCategories(int CategoryId, Tab categoryTab, Map<Integer, FlowPane> flowMap, CategoryType categoryType)
    {
        flowMap.clear();

        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(10, 10, 10, 10));
        flow.setVgap(5);
        flow.setHgap(5);
        flow.setPrefWrapLength(400);
        flow.setAlignment(Pos.TOP_LEFT);

        for (int i=CategoryType.ARTICLE.ordinal(); i<CategoryType.values().length; i++)
        {
            flowMap.put(categoryType.ordinal(), flow);
        }

        categoryTab.setContent(flowMap.get(categoryType.ordinal()));

        Core.getInstance().getLog().log("Laddar innehåll i " + typeName(categoryType) + " kategori...", Log.LogLevel.NORMAL);

        ArticleModel article = null;
        if (Core.getInstance().getControlHandler().getMainPaneLeftController().getSelectedArticle() != null)
        {
            article = Core.getInstance().getControlHandler().getMainPaneLeftController().getSelectedArticle();
        }

        switch (categoryType)
        {
            case ARTICLE:
                loadArticle(CategoryId, flowMap.get(categoryType.ordinal()));
                break;
            case EDIT:
                loadEdit(CategoryId, flowMap.get(categoryType.ordinal()), article);
                break;
            case SPECIAL:
                loadSpecials(CategoryId, flowMap.get(categoryType.ordinal()), article);
                break;
        };
    }

    private void loadArticle(int categoryId, FlowPane flowPane)
    {
        ArticleDAO articleDAO = new ArticleDAO();
        ObservableList<ArticleModel> articles = articleDAO.getArticles(categoryId);

        for (ArticleModel article : articles)
        {
            ImageView image = new ImageView(General.getInstance().loadPicture(article.getImageUrl()));
            image.setFitWidth(60);
            image.setPreserveRatio(true);
            image.setSmooth(true);
            image.setCache(true);

            Button button = new Button(article.getName() + "\n" + article.getPriceIncmStr() + " kr", image);
            button.setId("article_button");

            button.setContentDisplay(ContentDisplay.TOP);

            button.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent e)
                {
                    if (article.getSale() != null)
                    {
                        article.getSale().clearIngredienceExtra();
                        article.getSale().clearSpecial();
                    }

                    Core.getInstance().getControlHandler().getWorkController().addArticle(article);
                }
            });

            flowPane.getChildren().add(button);
        }
    }

    private void loadEdit(int categoryId, FlowPane flowPane, ArticleModel article)
    {
        IngredienceDAO ingredienceDAO = new IngredienceDAO();
        ObservableList<Ingredience> ingredients = ingredienceDAO.getIngrediences(categoryId);

        for (Ingredience ingredience : ingredients)
        {
            CheckBox box = new CheckBox();
            Label name = new Label();

            if (article == null)
            {
                Core.getInstance().getLog().log("Ingen vald Artikel att hämta", Log.LogLevel.CRITICAL);
                return;
            }

            if (article.containsIngredience(ingredience))
            {
                box.setSelected(true);
            }

            if (article.getSale().containsIngredienceExtra(ingredience))
            {
                box.setSelected(true);
            }

            if (article.getSale().containsIngredienceExcluded(ingredience))
            {
                box.setSelected(false);
            }

            name.setText(ingredience.getName());
            box.setUserData(ingredience);

            GridPane grid = new GridPane();
            grid.setPrefWidth(200);
            grid.add(box, 0, 0);
            grid.add(name, 1, 0);

            box.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent e)
                {
                    Ingredience ingredience = (Ingredience) box.getUserData();

                    if (box.isSelected())
                    {
                        if (article.containsIngredience(ingredience))
                        {
                            article.getSale().removeIngredienceExclude(ingredience);
                        } else
                        {
                            article.getSale().addIngredienceExtra(ingredience);
                        }
                    } else if (article.containsIngredience(ingredience))
                    {
                        article.getSale().addIngredienceExclude(ingredience);
                    } else
                    {
                        article.getSale().removeIngredienceExtra(ingredience);
                    }

                    SalesDAO salesDAO = new SalesDAO();
                    salesDAO.update(article.getSale());
                }
            });

            flowPane.getChildren().add(grid);
        }
    }

    private void loadSpecials(int categoryId, FlowPane flowPane, ArticleModel article)
    {
        SpecialDAO specialDAO = new SpecialDAO();
        ObservableList<SpecialModel> specials = specialDAO.getSpecials(categoryId);

        for (SpecialModel special : specials)
        {
            CheckBox box = new CheckBox();
            Label name = new Label();

            if (article == null)
            {
                Core.getInstance().getLog().log("Ingen vald Artikel att hämta", Log.LogLevel.CRITICAL);
                return;
            }

            if (article.getSale().containsSpecial(special))
            {
                box.setSelected(true);
            }

            name.setText(special.getName());
            box.setUserData(special);

            GridPane grid = new GridPane();
            grid.setPrefWidth(200);
            grid.add(box, 0, 0);
            grid.add(name, 1, 0);

            box.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent e)
                {
                    SpecialModel special = (SpecialModel) box.getUserData();

                    if (box.isSelected())
                    {
                        article.getSale().addSpecial(special);
                    } else
                    {
                        article.getSale().removeSpecial(special);
                    }

                    SalesDAO salesDAO = new SalesDAO();
                    salesDAO.update(article.getSale());
                }
            });

            flowPane.getChildren().add(grid);
        }
    }

    public void createCategories(TabPane tabPane, AnchorPane anchMain, Map<Integer, FlowPane> flowMap, CategoryType type)
    {
        tabPane.getTabs().clear();

        tabPane.setId("article_tabpane");
        //tabPane.getStyleClass().add("article_tabpane");

        if (anchMain.getChildren().contains(tabPane))
        {
            anchMain.getChildren().remove(tabPane);
            return; // Denna för att inte generera om allt hela tiden
        }

        Core.getInstance().getLog().log("Skapar upp alla " + typeName(type) + " kategorier...", Log.LogLevel.NORMAL);

        anchMain.getChildren().add(tabPane);

        Map<Integer, Category> categories = new HashMap<>();

        switch (type)
        {
            case ARTICLE:
                ArticleCategoryDAO articleCategoryDAO = new ArticleCategoryDAO();
                categories = articleCategoryDAO.getArticleCategories();
                break;
            case EDIT:
                IngredientsCategoryDAO ingredientsCategoryDAO = new IngredientsCategoryDAO();
                categories = ingredientsCategoryDAO.getIngredientsCategories();
                break;
            case SPECIAL:
                SpecialCategoryDAO specialCategoryDAO = new SpecialCategoryDAO();
                categories = specialCategoryDAO.getSpecialCategories();
                break;
            default:
                return;
        }

        for (Map.Entry<Integer, Category> entry : categories.entrySet())
        {
            Tab categoryTab = new Tab();

            categoryTab.setClosable(false);
            categoryTab.setText(entry.getValue().getName());
            General.getInstance().fitToParent(anchMain, tabPane);

            populateCategories(entry.getKey(), categoryTab, flowMap, type);
            tabPane.getTabs().add(categoryTab);
        }
    }

    private String typeName(CategoryType type)
    {
        switch (type)
        {
            case ARTICLE:
                return "artiklar";
            case EDIT:
                return "ingredienser";
            case SPECIAL:
                return "special";
        }

        return "ERROR";
    }

}
