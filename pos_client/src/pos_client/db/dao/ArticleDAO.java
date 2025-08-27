package pos_client.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pos_client.ArticleModel;
import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import pos_client.windows.Log;

public class ArticleDAO extends DAO
{

    public ArticleDAO()
    {

    }

    public ObservableList<ArticleModel> getArticles(int categoryId)
    {
        ObservableList<ArticleModel> articlesWithCategory = FXCollections.observableArrayList();

        ObservableList<ArticleModel> articles = getArticles();
        for (ArticleModel article : articles)
        {
            if (article.getArticleCategoryId() == categoryId)
            {
                articlesWithCategory.add(article);
            }
        }

        return articlesWithCategory;
    }

    public ArticleModel getArticle(int id)
    {
        ObservableList<ArticleModel> articles = getArticles();
        for (ArticleModel article : articles)
        {
            if (article.getArticleId() == id)
            {
                return article;
            }
        }

        return null;
    }

    public ObservableList<ArticleModel> getArticles()
    {
        ObservableList<ArticleModel> articles = FXCollections.observableArrayList();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Laddar artiklar i inställningar", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet resultSet = db.getResult();
                    while (resultSet.next())
                    {
                        ArticleModel article = new ArticleModel(
                                resultSet.getInt(DefinedVariables.getInstance().TABLE_ARTICLES + "_id"),
                                resultSet.getInt("layoutIndex"),
                                resultSet.getString("name"),
                                resultSet.getFloat("purchase"),
                                resultSet.getFloat("price_incm"),
                                resultSet.getFloat("price_excm"),
                                VatDAO.VatType.values()[resultSet.getInt(DefinedVariables.getInstance().TABLE_VAT + "_id")],
                                resultSet.getString("barcode"),
                                resultSet.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + "_id"),
                                resultSet.getString("description"),
                                resultSet.getString("image_url"),
                                resultSet.getString(DefinedVariables.getInstance().TABLE_INGREDIENCES),
                                resultSet.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_TYPE + "_id")
                        );

                        articles.add(article);
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getArticles() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getArticles() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getArticles() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return articles;
    }

    public boolean add(ArticleModel article)
    {
        boolean result = false;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Lägger till vald artikel i db...", Log.LogLevel.DESCRIPTIVE);

            String query = "INSERT INTO " + DefinedVariables.getInstance().TABLE_ARTICLES
                    + "("
                    + "layoutIndex, "
                    + "name, "
                    + "purchase, "
                    + "price_incm, "
                    + "price_excm, "
                    + "barcode, "
                    + "description,"
                    + "image_url,"
                    + "ingrediences,"
                    + DefinedVariables.getInstance().TABLE_VAT + "_id,"
                    + DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + "_id,"
                    + DefinedVariables.getInstance().TABLE_ARTICLES_TYPE + "_id"
                    + ") "
                    + "VALUES("
                    + "'" + article.getLayoutIndex() + "', "
                    + "'" + article.getName() + "', "
                    + "'" + article.getPurchase() + "', "
                    + "'" + article.getSale().getPriceIncm(false) + "', "
                    + "'" + article.getSale().getPriceExcm(false) + "', "
                    + "'" + article.getBarcode() + "', "
                    + "'" + article.getDescription() + "', "
                    + "'" + article.getImageUrl() + "', "
                    + "'" + article.getIngrediencesStrDB() + "', "
                    + "'" + article.getVatType().ordinal() + "', "
                    + "'" + article.getArticleCategoryId() + "', "
                    + "'" + article.getArticleTypeId() + "');";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Sparade artikel: " + article.getName(), Log.LogLevel.DESCRIPTIVE);
                result = true;
            } else
            {
                Core.getInstance().getLog().log("add - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("add - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return result;
    }

    public boolean remove(int id)
    {
        if (db.isConnected())
        {
            return db.deleteRow(DefinedVariables.getInstance().TABLE_ARTICLES, id);
        } else
        {
            Core.getInstance().getLog().log("remove() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
        return false;
    }

    public boolean update(ArticleModel article)
    {
        boolean result = true;

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Uppdaterar vald artikel " + article.getArticleId() + " i db...", Log.LogLevel.DESCRIPTIVE);

            String query = "UPDATE " + DefinedVariables.getInstance().TABLE_ARTICLES
                    + " SET layoutIndex = '" + article.getLayoutIndex()
                    + "', name = '" + article.getName()
                    + "', purchase = '" + article.getPurchase()
                    + "', price_incm = '" + article.getSale().getPriceIncm(false)
                    + "', price_excm = '" + article.getSale().getPriceExcm(false)
                    + "'," + DefinedVariables.getInstance().TABLE_VAT + "_id = '" + article.getVatType().ordinal()
                    + "', barcode = '" + article.getBarcode() + "'"
                    + "," + DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + "_id = " + article.getArticleCategoryId()
                    + ", description = '" + article.getDescription()
                    + "', image_url = '" + article.getImageUrl()
                    + "', ingrediences = '" + article.getIngrediencesStrDB()
                    + "'," + DefinedVariables.getInstance().TABLE_ARTICLES_TYPE + "_id = '" + article.getArticleTypeId()
                    + "' WHERE " + DefinedVariables.getInstance().TABLE_ARTICLES + "_id = '" + article.getArticleId() + "'";

            if (db.query(query, true))
            {
                Core.getInstance().getLog().log("Updaterade artikel: " + article.getName() + " med id: " + article.getArticleId(), Log.LogLevel.DESCRIPTIVE);
            } else
            {
                Core.getInstance().getLog().log("update() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
                result = false;
            }
        } else
        {
            Core.getInstance().getLog().log("update() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
            result = false;
        }

        return result;
    }

    /*
    public ObservableList<ArticleCategoryModel> getArticleCategories() {

        ObservableList<ArticleCategoryModel> articleTabs = FXCollections.observableArrayList();

        if (db.isConnected()) {

            Core.getInstance().getLog().log("Hämtar alla artikel tabbar", 3);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY;

            if (db.query(query, false)) {
                try {
                    ResultSet setType = db.getResult();
                    while (setType.next()) {
                        int id = setType.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + "_id");
                        int order = setType.getInt("tab_article_order");
                        String name = setType.getString("name");

                        ArticleCategoryModel articleTabModel = new ArticleCategoryModel(id, order, name);
                        articleTabs.add(articleTabModel);
                    }
                } catch (SQLException ex) {
                    Core.getInstance().getLog().log("getArticleTabs() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else {
                Core.getInstance().getLog().log("getArticleTabs() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else {
            Core.getInstance().getLog().log("getArticleTabs() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return articleTabs;
    }*/
    public Map<Integer, String> getArticleMoms()
    {
        Map<Integer, String> articleMoms = new HashMap<>();

        if (db.isConnected())
        {

            Core.getInstance().getLog().log("Hämtar artikel moms", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_VAT;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_VAT + "_id");
                        String value = set.getString("value");

                        articleMoms.put(id, value);
                    }
                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getArticleMoms() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getArticleMoms() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getArticleMoms() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return articleMoms;
    }

    public Map<Integer, String> getArticleType()
    {
        Map<Integer, String> articleTypes = new HashMap<>();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar artikel typer", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_TYPE;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_TYPE + "_id");
                        String value = set.getString("type");

                        articleTypes.put(id, value);
                    }

                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getArticleType() - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getArticleType() - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getArticleType() - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return articleTypes;
    }

    public Map<Integer, String> getArticleCategories()
    {
        Map<Integer, String> articleTabs = new HashMap<>();

        if (db.isConnected())
        {
            Core.getInstance().getLog().log("Hämtar artikel kategorier", Log.LogLevel.DESCRIPTIVE);

            String query = "SELECT * FROM " + DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY;

            if (db.query(query, false))
            {
                try
                {
                    ResultSet set = db.getResult();
                    while (set.next())
                    {
                        int id = set.getInt(DefinedVariables.getInstance().TABLE_ARTICLES_CATEGORY + "_id");
                        String name = set.getString("name");
                        int order = set.getInt("orderNr"); // TODO

                        articleTabs.put(id, name);
                    }

                } catch (SQLException ex)
                {
                    Core.getInstance().getLog().log("getArticleCategories - Avvikelse i ResulstatSet : " + ex.toString(), Log.LogLevel.CRITICAL);
                }
            } else
            {
                Core.getInstance().getLog().log("getArticleCategories - Lyckades inte att ställa fråga... : " + query, Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("getArticleCategories - Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }

        return articleTabs;
    }
}
