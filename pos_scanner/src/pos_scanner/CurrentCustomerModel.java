/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner;

import static pos_scanner.common.General.*;

public class CurrentCustomerModel
{
    private String date;
    private String time;

    public CurrentCustomerModel(
            String date,
            String time)
    {
        this.date = date;
        this.time = time;
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

    public String getDateSinceToday()
    {
         return getTimeSinceToday(date(), time(), date, time);
    }
}
