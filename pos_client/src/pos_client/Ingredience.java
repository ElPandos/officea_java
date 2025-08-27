
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import pos_client.common.General;

/**
 *
 * @author Laptop
 */
public class Ingredience
{

    private int id;
    private String name;
    private float price;
    private int category_id;

    public Ingredience()
    {

    }

    public Ingredience(int id, String name, float price, int category_id)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category_id = category_id;
    }

    // Copy constructor
    public Ingredience(Ingredience copy)
    {
        this(copy.id, copy.name, copy.price, copy.category_id);
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

    public void setPrice(float price)
    {
        this.price = price;
    }

    public float getPrice()
    {
        return this.price;
    }

    public String getPriceStr()
    {
        return General.getInstance().float2Str(getPrice(), 2);
    }

    public void setCategory(int category_id)
    {
        this.category_id = category_id;
    }

    public int getCategory()
    {
        return this.category_id;
    }
}
