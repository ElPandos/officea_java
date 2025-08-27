/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.scene.text.Text;
import pos_client.common.Core;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class Time
{

    private Thread thread = null;
    private int updateInterval = 1000;
    private static Time instance = null;

    private String time;
    private String date;

    private boolean running = false;

    private ArrayList<Text> txtFieldsToUpdateTime = new ArrayList();
    private ArrayList<Text> txtFieldsToUpdateDate = new ArrayList();

    public Time()
    {
        CreateThread();
    }

    private void CreateThread()
    {
        if (thread == null)
        {
            thread = new Thread()
            {
                public void run()
                {
                    try
                    {
                        while (running)
                        {
                            setDate();
                            setTime();
                            Thread.sleep(updateInterval);
                        }
                    } catch (InterruptedException v)
                    {
                        Core.getInstance().getLog().log("Time - Tråden fungerade ej: " + v.toString(), Log.LogLevel.CRITICAL);
                    }

                }
            };
        }
    }

    public void setUpdateInterval(int msek)
    {
        updateInterval = msek;
    }

    public void setTime()
    {
        time = LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public void setDate()
    {
        date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void start()
    {
        running = true;
        thread.start();
    }

    public void stop()
    {
        running = false;
        thread = null;
    }

}
