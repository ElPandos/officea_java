/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.settings;

/**
 *
 * @author Laptop
 */
public class Setting
{

    private int id;
    private String variable;
    private String value;
    private String unit;

    public Setting()
    {
    }

    public void setId(int id)
    {

        this.id = id;
    }

    public int getId()
    {

        return this.id;
    }

    public void setVariable(String variable)
    {

        this.variable = variable;
    }

    public String getVariable()
    {

        return this.variable;
    }

    public void setValue(String value)
    {

        this.value = value;
    }

    public String getValue()
    {

        return this.value;
    }

    public void setUnit(String unit)
    {

        this.unit = unit;
    }

    public String getUnit()
    {

        return this.unit;
    }
}
