package pos_client.reporting;

public class ReceiptRow
{

    private String item;
    private String amount;
    private final String specials;
    private final String ingrediencesExtra;
    private final String ingrediencesExclude;

    public ReceiptRow(String item, String amount, String specials, String ingrediencesExtra, String ingrediencesExclude)
    {
        this.item = item;
        this.amount = amount;
        this.specials = specials;
        this.ingrediencesExtra = ingrediencesExtra;
        this.ingrediencesExclude = ingrediencesExclude;
    }

    public String getArticleItem()
    {
        item += "\n";

        if (!specials.isEmpty())
        {
            item += specials;
        }

        if (!ingrediencesExclude.isEmpty())
        {
            item += ingrediencesExclude;
        }

        if (!ingrediencesExtra.isEmpty())
        {
            item += ingrediencesExtra;
        }

        return item;
    }

    public String getItem()
    {
        return item;
    }

    public void setItem(String item)
    {
        this.item = item;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }
}
