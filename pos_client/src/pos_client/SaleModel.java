/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;
import java.util.Map;
import javafx.collections.ObservableList;
import static jdk.nashorn.internal.objects.NativeString.toUpperCase;
import pos_client.common.General;
import pos_client.db.dao.ArticleCategoryDAO;
import pos_client.db.dao.IngredienceDAO;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.SalesDAO;
import pos_client.db.dao.SpecialDAO;
import pos_client.db.dao.VatDAO;

public class SaleModel
{

    VatDAO vatDAO = new VatDAO();

    public static enum SaleType
    {
        NONE,
        NORMAL,
        REMOVED
    }

    private SaleType type = SaleType.NORMAL;

    Date createDate;
    Time createTime;

    private int saleId;
    private int receiptId;

    private UserModel user = null;
    private ArticleModel article = null;

    float amount = 0;
    protected float discountPercent = 0;

    private ArrayList<Ingredience> ingrediencesExtra = new ArrayList();
    private ArrayList<Ingredience> ingrediencesExclude = new ArrayList();
    private ArrayList<SpecialModel> specials = new ArrayList();

    public SaleModel()
    {
    }

    public SaleModel(int saleId, int receiptId, UserModel user, ArticleModel article)
    {
        this.saleId = saleId;
        this.receiptId = receiptId;
        this.user = user;
        this.article = article;

        article.setSale(this);
    }

    public void setSaleId(int saleId)
    {
        this.saleId = saleId;
    }
    
    public int getSaleId()
    {
        return this.saleId;
    }

    public void setSaleType(SaleType type)
    {
        this.type = type;
    }

    public void setCreateTime(Time time)
    {
        this.createTime = time;
    }

    public void setCreateDate(Date date)
    {
        this.createDate = date;
    }

    public void setAmount(float amount)
    {
        this.amount = amount;
    }

    public void setReceiptId(int receiptId)
    {
        this.receiptId = receiptId;
    }
    
    public int getReceiptId()
    {
        return this.receiptId;
    }

    public Receipt getReceipt()
    {
        ReceiptDAO receiptDAO = new ReceiptDAO();
        return receiptDAO.getReceipt(receiptId);
    }

    public String getNameAndCategory()
    {
        ArticleCategoryDAO articleCategoryDAO = new ArticleCategoryDAO();
        Map<Integer, Category> articleCategories = articleCategoryDAO.getArticleCategories();

        String categoryName = articleCategories.get(article.getArticleCategoryId()).getName();
        String articleName = article.getName();
        String priceIncm = General.getInstance().float2Str(getPriceIncm(true), 2); // lägger till priset på samma rad som artikelnamnet

        String output = categoryName + "\n" + articleName + " " + priceIncm + "\n";

        String specials = getSpecialStr();
        if (!specials.isEmpty())
        {
            output += specials;
        }

        String excluded = getIngredienceExcludeStr();
        if (!excluded.isEmpty())
        {
            output += excluded;
        }

        String extra = getIngredienceExtraStr();
        if (!extra.isEmpty())
        {
            output += extra;
        }

        return output;
    }

    public void setArticle(ArticleModel article)
    {
        this.article = article;
        
        article.setSale(this);        
    }

    public ArticleModel getArticle()
    {
        return this.article;
    }
    
    public void setUser(UserModel user)
    {
       this.user = user;
    }

    public UserModel getUser()
    {
        return this.user;
    }

    public boolean changeSaleType(SaleType changeToType)
    {
        SalesDAO salesDAO = new SalesDAO();
        return salesDAO.changeSaleType(changeToType, getReceipt(), saleId);
    }

    public void setDiscount(float discount)
    {
        this.discountPercent = discount;
    }

    public float getDiscount()
    {
        return this.discountPercent;
    }

    public float getPriceIncm(boolean withDiscount)
    {
        float discountValue = 1;
        if (withDiscount)
        {
            discountValue = (1 - discountPercent);
        }

        return (discountValue * article.getPriceIncm());
    }

    public String getPriceIncmStr()
    {
        return General.decimalFormat.format(getPriceIncm(true));
    }

    public float getPriceTotalIncm(boolean withDiscount)
    {
        float discountValue = 1;
        if (withDiscount)
        {
            discountValue = (1 - discountPercent);
        }

        return discountValue * (getPriceIncm(false) + getIngredienceExtraIncm() + getSpecialsIncm());
    }

    public String getPriceTotalIncmStr()
    {
        return General.decimalFormat.format(getPriceTotalIncm(true));
    }

    public float getPriceExcm(boolean withDiscount)
    {
        float discountValue = 1;
        if (withDiscount)
        {
            discountValue = (1 - discountPercent);
        }

        return (discountValue * article.getPriceExcm());
    }

    public String getPriceExcmStr()
    {
        return General.decimalFormat.format(getPriceExcm(true));
    }

    public float getPriceTotalExcm(boolean withDiscount)
    {
        float discountValue = 1;
        if (withDiscount)
        {
            discountValue = (1 - discountPercent);
        }

        return discountValue * (article.getPriceExcm() + ((getIngredienceExtraIncm() + getSpecialsIncm()) / (1 + vatDAO.getVat(article.getVatType()))));
    }

    public String getPriceTotalExcmStr()
    {
        return General.decimalFormat.format(getPriceTotalExcm(true));
    }

    public double getVat()
    {
        return (getPriceIncm(true) - getPriceExcm(true));
    }

    public void addIngredienceExclude(Ingredience ingredience)
    {
        ingrediencesExclude.add(ingredience);
    }

    public ArrayList<Ingredience> getIngrediencesExcluded()
    {
        return ingrediencesExclude;
    }

    public void removeIngredienceExclude(Ingredience ingredience)
    {
        int ack = 0;
        for (Ingredience ingredienceObj : ingrediencesExclude)
        {
            if (ingredience.getId() == ingredienceObj.getId())
            {
                break;
            } else
            {
                ack++;
            }
        }

        if (ack <= ingrediencesExclude.size())
        {
            ingrediencesExclude.remove(ack);
        }
    }

    public boolean containsIngredienceExcluded(Ingredience ingredience)
    {
        for (Ingredience ingredienceCurrent : ingrediencesExclude)
        {
            if (ingredience.getId() == ingredienceCurrent.getId())
            {
                return true;
            }
        }

        return false;
    }

    public String getIngredienceExcludeStr()
    {
        String str = "";
        for (Ingredience ingredience : ingrediencesExclude)
        {
            str += "   - " + ingredience.getName() + "\n";
        }

        return str;
    }

    public String getIngredienceExcludedStrDB()
    {
        String str = "";
        for (Ingredience ingredience : ingrediencesExclude)
        {
            str += General.getInstance().int2Str(ingredience.getId()) + ",";
        }

        return str;
    }

    public void setIngrediencesExcluded(String ingrediencesExcludedStr)
    {
        if (!ingrediencesExcludedStr.isEmpty())
        {
            this.ingrediencesExclude.clear();

            IngredienceDAO ingredienceDAO = new IngredienceDAO();
            ObservableList<Ingredience> ingrediencesObj = ingredienceDAO.getIngrediences();

            String[] ingredienceList = ingrediencesExcludedStr.split(",");
            for (String ingredienceStr : ingredienceList)
            {
                for (Ingredience ingredienceObj : ingrediencesObj)
                {
                    if (General.getInstance().str2Int(ingredienceStr) == ingredienceObj.getId())
                    {
                        this.ingrediencesExclude.add(ingredienceObj);
                    }
                }
            }
        }
    }

    public void clearIngredienceExtra()
    {
        ingrediencesExtra.clear();
    }

    public void addIngredienceExtra(Ingredience ingredience)
    {
        ingrediencesExtra.add(ingredience);
    }

    public void removeIngredienceExtra(Ingredience ingredience)
    {
        int ack = 0;
        for (Ingredience ingredienceExtra : ingrediencesExtra)
        {
            if (ingredience.getId() == ingredienceExtra.getId())
            {
                break;
            } else
            {
                ack++;
            }
        }

        if (ack <= ingrediencesExtra.size())
        {
            ingrediencesExtra.remove(ack);
        }
    }

    public boolean containsIngredienceExtra(Ingredience ingredience)
    {
        for (Ingredience ingredienceExtra : ingrediencesExtra)
        {
            if (ingredience.getId() == ingredienceExtra.getId())
            {
                return true;
            }
        }

        return false;
    }

    public String getIngredienceExtraStr()
    {
        String str = "";
        for (Ingredience ingredience : ingrediencesExtra)
        {
            str += "   + " + ingredience.getName() + " " + General.getInstance().float2Str(ingredience.getPrice(), 2) + "\n";

        }

        return str;
    }

    public String getIngredienceExtraStrDB()
    {
        String str = "";
        for (Ingredience ingredience : ingrediencesExtra)
        {
            str += General.getInstance().int2Str(ingredience.getId()) + ",";
        }

        return str;
    }

    public void setIngrediencesExtra(String ingrediencesExtraStr)
    {
        if (!ingrediencesExtraStr.isEmpty())
        {
            clearIngredienceExtra();

            IngredienceDAO ingredienceDAO = new IngredienceDAO();
            ObservableList<Ingredience> ingrediencesObj = ingredienceDAO.getIngrediences();

            String[] ingredienceList = ingrediencesExtraStr.split(",");
            for (String ingredienceStr : ingredienceList)
            {
                for (Ingredience ingredienceObj : ingrediencesObj)
                {
                    if (General.getInstance().str2Int(ingredienceStr) == ingredienceObj.getId())
                    {
                        this.ingrediencesExtra.add(ingredienceObj);
                    }
                }
            }
        }
    }

    public float getIngredienceExtraIncm()
    {
        float total = 0;
        for (Ingredience ingredience : ingrediencesExtra)
        {
            total += ingredience.getPrice();
        }

        return total;
    }

    public void clearSpecial()
    {
        specials.clear();
    }

    public void addSpecial(SpecialModel special)
    {
        specials.add(special);
    }

    public ArrayList<SpecialModel> getSpecials()
    {
        return specials;
    }

    public void removeSpecial(SpecialModel special)
    {
        int ack = 0;
        for (SpecialModel specialCurrent : specials)
        {
            if (specialCurrent.getId() == special.getId())
            {
                break;
            } else
            {
                ack++;
            }
        }

        if (ack <= specials.size())
        {
            specials.remove(ack);
        }
    }

    public boolean containsSpecial(SpecialModel special)
    {
        for (SpecialModel specialCurrent : specials)
        {
            if (special.getId() == specialCurrent.getId())
            {
                return true;
            }
        }

        return false;
    }

    public float getSpecialsIncm()
    {
        float total = 0;
        for (SpecialModel special : specials)
        {
            total += special.getPrice();
        }

        return total;
    }

    public String getSpecialStr()
    {
        String str = "";
        for (SpecialModel special : specials)
        {
            str += "   • " + toUpperCase(special.getName()) + " " + ((special.getPrice() > 0) ? General.getInstance().float2Str(special.getPrice(), 2) : "") + "\n";
        }

        return str;
    }

    public String getSpecialsStrDB()
    {
        String str = "";
        for (SpecialModel special : specials)
        {
            str += General.getInstance().int2Str(special.getId()) + ",";
        }

        return str;
    }

    public void setSpecials(String specialsStr)
    {
        if (!specialsStr.isEmpty())
        {
            this.specials.clear();

            SpecialDAO specialDAO = new SpecialDAO();
            ObservableList<SpecialModel> specials = specialDAO.getSpecials();

            String[] specialsList = specialsStr.split(",");
            for (String specialStr : specialsList)
            {
                for (SpecialModel specialObj : specials)
                {
                    if (General.getInstance().str2Int(specialStr) == specialObj.getId())
                    {
                        this.specials.add(specialObj);
                    }
                }
            }
        }
    }
}
