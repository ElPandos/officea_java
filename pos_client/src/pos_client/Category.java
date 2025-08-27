package pos_client;

public class Category
{

    int id;
    String name;
    int order;

    public Category()
    {
    }

    public Category(
            int id,
            String name,
            int order)
    {

        this.id = id;
        this.name = name;
        this.order = order;
    }

    public void setId(int id)
    {

        this.id = id;
    }

    public Integer getId()
    {

        return this.id;
    }

    public void setName(String name)
    {

        this.name = name;
    }

    public String getName()
    {

        return this.name;
    }

    public void setOrder(int order)
    {

        this.order = order;
    }

    public Integer getOrder()
    {

        return this.order;
    }
}
