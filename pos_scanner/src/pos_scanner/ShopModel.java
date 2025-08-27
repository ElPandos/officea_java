/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner;

/**
 *
 * @author Server
 */
public class ShopModel extends Model
{

    private String name;
    private String adress;
    private String postal;
    private String city;
    private String interal_ip;
    private String external_ip;
    private String mac;
    private String prefix;

    public ShopModel(
            String name,
            String adress,
            String postal,
            String city,
            String interal_ip,
            String external_ip,
            String mac,
            String prefix
    )
    {
        this.name = name;
        this.adress = adress;
        this.postal = postal;
        this.city = city;
        this.interal_ip = interal_ip;
        this.external_ip = external_ip;
        this.mac = mac;
        this.prefix = prefix;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAdress()
    {
        return this.adress;
    }

    public void setAdress(String adress)
    {
        this.adress = adress;
    }

    public String getPostal()
    {
        return this.postal;
    }

    public void setPostal(String postal)
    {
        this.postal = postal;
    }

    public String getCity()
    {
        return this.city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getInternalIP()
    {
        return this.interal_ip;
    }

    public void setInternalIP(String interal_ip)
    {
        this.interal_ip = interal_ip;
    }

    public String getExternalIP()
    {
        return this.external_ip;
    }

    public void setExternalIP(String external_ip)
    {
        this.external_ip = external_ip;
    }

    public String getMac()
    {
        return this.mac;
    }

    public void setMac(String mac)
    {
        this.mac = mac;
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

}
