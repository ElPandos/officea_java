/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner;

/**
 *
 * @author eit-asn
 */
public class ScanModel extends Model
{

    private String card;
    private String date;
    private String time;
    private String location;

    private int scans = 0;

    public ScanModel(
            String card,
            String date,
            String time,
            String location
    )
    {
        this.card = card;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public String getCard()
    {
        return this.card;
    }

    public void setCard(String card)
    {
        this.card = card;
    }

    public String getDate()
    {
        return this.date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return this.time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getLocation()
    {
        return this.location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

}
