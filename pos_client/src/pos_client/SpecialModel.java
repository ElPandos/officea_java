/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

public class SpecialModel
{

    private int id;
    private String name;
    private float price;
    private String description;
    private int category_id;

    public SpecialModel()
    {
    }

    public SpecialModel(int id, String name, float price, String description, int category_id)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category_id = category_id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }

    public float getPrice()
    {
        return price;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setCategory(int category_id)
    {
        this.category_id = category_id;
    }

    public int getCategoryId()
    {
        return this.category_id;
    }
}
