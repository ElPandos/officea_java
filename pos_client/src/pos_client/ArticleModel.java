/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import java.util.ArrayList;
import pos_client.common.General;
import javafx.collections.ObservableList;
import pos_client.db.dao.IngredienceDAO;
import pos_client.db.dao.VatDAO.VatType;

/**
 *
 * @author Laptop
 */
public class ArticleModel
{

    SaleModel sale = null;

    private int id;
    private int layoutIndex;
    private String name;
    private float purchase;
    private float priceIncm;
    private float priceExcm;
    private VatType vatType;
    private String barcode;
    private int articleCategoryId;
    private int articleTypeId;
    private String description;
    private String imageUrl;

    private ArrayList<Ingredience> ingrediences = new ArrayList<>();

    public ArticleModel()
    {
    }

    public ArticleModel(
            int id,
            int layoutIndex,
            String name,
            float purchase,
            float priceIncm,
            float priceExcm,
            VatType vatType,
            String barcode,
            int articleCategoryId,
            String description,
            String imageUrl,
            String ingrediencesStr,
            int typeId)
    {

        this.id = id;
        this.layoutIndex = layoutIndex;
        this.name = name;
        this.purchase = purchase;
        this.priceIncm = priceIncm;
        this.priceExcm = priceExcm;
        this.vatType = vatType;
        this.barcode = barcode;
        this.articleCategoryId = articleCategoryId;
        this.description = description;
        this.imageUrl = imageUrl;

        setIngrediences(ingrediencesStr);

        this.articleTypeId = typeId;

        sale = new SaleModel();
        sale.setArticle(this);
    }

    // Copy constructor
    public ArticleModel(ArticleModel copy)
    {
        this(copy.id,
                copy.layoutIndex,
                copy.name,
                copy.purchase,
                copy.priceIncm,
                copy.priceExcm,
                copy.vatType,
                copy.barcode,
                copy.articleCategoryId,
                copy.description,
                copy.imageUrl,
                copy.getIngrediencesStrDB(),
                copy.articleTypeId
        );
    }

    public void setSale(SaleModel sale)
    {
        this.sale = sale;
    }

    public SaleModel getSale()
    {
        return sale;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getArticleId()
    {
        return this.id;
    }

    public void setLayoutIndex(int index)
    {
        this.layoutIndex = index;
    }

    public int getLayoutIndex()
    {
        return this.layoutIndex;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getNameAndCategory()
    {
        return sale.getNameAndCategory();
    }

    public void setPurchase(float purchase)
    {
        this.purchase = purchase;
    }

    public float getPurchase()
    {
        return this.purchase;
    }

    public String getPurchaseStr()
    {
        return General.decimalFormat.format(getPurchase());
    }

    public void setPriceExcm(float price)
    {
        this.priceExcm = price;
    }

    public float getPriceExcm()
    {
        return this.priceExcm;
    }

    public String getPriceExcmStr()
    {
        return General.decimalFormat.format(getPriceExcm());
    }

    public void setPriceIncm(float price)
    {
        this.priceIncm = price;
    }

    public float getPriceIncm()
    {
        return this.priceIncm;
    }

    public String getPriceIncmStr()
    {
        return General.decimalFormat.format(getPriceIncm());
    }

    public void setVatType(VatType vatType)
    {
        this.vatType = vatType;
    }

    public VatType getVatType()
    {
        return this.vatType;
    }

    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }

    public String getBarcode()
    {
        return this.barcode;
    }

    public void setArticleCategoryId(int articleTabId)
    {
        this.articleCategoryId = articleTabId;
    }

    public int getArticleCategoryId()
    {
        return this.articleCategoryId;
    }

    public void setArticleTypeId(int articleTypeId)
    {
        this.articleTypeId = articleTypeId;
    }

    public int getArticleTypeId()
    {
        return this.articleTypeId;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl()
    {
        return this.imageUrl;
    }

    public void setIngrediences(String ingrediencesStr)
    {
        if (ingrediencesStr != null && !ingrediencesStr.isEmpty())
        {
            this.ingrediences.clear();

            IngredienceDAO ingredienceDAO = new IngredienceDAO();
            ObservableList<Ingredience> ingrediencesObj = ingredienceDAO.getIngrediences();

            String[] ingredienceList = ingrediencesStr.split(",");
            for (String ingredienceStr : ingredienceList)
            {
                for (Ingredience ingredienceObj : ingrediencesObj)
                {
                    if (General.getInstance().str2Int(ingredienceStr) == ingredienceObj.getId())
                    {
                        this.ingrediences.add(ingredienceObj);
                    }
                }
            }
        }
    }

    public void setIngrediences(ArrayList<Ingredience> ingrediences)
    {
        this.ingrediences = ingrediences;
    }

    public ArrayList<Ingredience> getIngrediences()
    {
        return ingrediences;
    }

    public String getIngrediencesStrDB()
    {
        String str = "";
        for (Ingredience ingredience : ingrediences)
        {
            str += General.getInstance().int2Str(ingredience.getId()) + ",";
        }

        return str;
    }

    public void addIngredience(Ingredience ingredience)
    {
        ingrediences.add(ingredience);
    }

    public boolean containsIngredience(Ingredience ingredience)
    {
        for (Ingredience ingredienceCurrent : ingrediences)
        {
            if (ingredience.getId() == ingredienceCurrent.getId())
            {
                return true;
            }
        }

        return false;
    }

    public boolean hasIngrediences()
    {
        return (ingrediences.size() > 0);
    }

}
