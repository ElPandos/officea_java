/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner.common;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class General
{

    public final String newLine = System.getProperty("line.separator");

    public static final String dot = "\\.";
    public static final String comma = "\\,";
    public static final String semicolon = "\\:";

    public static final int INVALID_VALUE = -9999;

    public static final Color CONNECTED = Color.LIGHTGREEN;
    public static final Color DISCONNECTED = Color.RED;

    public General()
    {

    }

    //==========================================================================
    //== TIME and DATE
    //==========================================================================
    
    public enum DayName
    {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    };
    
    public enum MonthName
    {
        JANUARY,
        FEBRUARY,
        MARCH,
        APRIL,
        MAY,
        JUNE,
        JULY,
        AUGUST,
        SEPTEMBER,
        OCTOBER,
        NOVEMBER,
        DECEMBER
    };
            
    public static final String YEAR_FORMAT = "yyyy";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static String time()
    {
        return time(new Date());
    }

    public static String time(Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat(TIME_FORMAT);
        return ft.format(date);
    }

    public static String date()
    {
        return date(new Date());
    }

    public static String date(Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);
        return ft.format(date);
    }

    public static String totalDateTime()
    {
        return date() + " " + time();
    }

    public static String year()
    {
        SimpleDateFormat ft = new SimpleDateFormat(YEAR_FORMAT);
        return ft.format(new Date());
    }

    public static String getTimeSinceToday(String dateNow, String timeNow, String dateThen, String timeThen)
    {
        String timeDiff = "";

        try
        {
            SimpleDateFormat ft_d = new SimpleDateFormat(DATE_FORMAT);
            SimpleDateFormat ft_t = new SimpleDateFormat(TIME_FORMAT);

            Date date_now = ft_d.parse(dateNow);
            Date time_now = ft_t.parse(timeNow);

            long sum_now = date_now.getTime() + time_now.getTime();

            Date date_then = ft_d.parse(dateThen);
            Date time_then = ft_t.parse(timeThen);

            long sum_then = date_then.getTime() + time_then.getTime();

            long diff = sum_now - sum_then;

            //long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            List<String> clock = new ArrayList<>();
            if (diffDays > 0)
            {
                clock.add(Long.toString(diffDays) + " Dagar");
            }

            if (diffHours > 0)
            {
                clock.add(Long.toString(diffHours) + " Timmar");
            }

            if (clock.size() < 2)
            {
                clock.add(Long.toString(diffMinutes) + " Minuter");
            }

            for (int i = 0; i < clock.size(); i++)
            {
                timeDiff += clock.get(i);
                timeDiff += (i < clock.size() - 1 ? ", " : "");
            }

        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return timeDiff;
    }

    private final static String MONTHS[] =
    {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    //==========================================================================
    //== OTHER
    //==========================================================================
    public static void executeURL(String type)
    {
        if (Desktop.isDesktopSupported())
        {
            try
            {
                Desktop.getDesktop().browse(new URI(type));
            } catch (IOException ex)
            {
                //Core.getInstance().getLog().log("executeFM - " + ex.toString(), Log.LogLevel.CRITICAL);
            } catch (URISyntaxException ex)
            {
                //Core.getInstance().getLog().log("executeFM - " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        }
    }

    private static final int UI_ANIMATION_TIME_MSEC = 600;
    private static final Duration UI_ANIMATION_TIME = Duration.millis(UI_ANIMATION_TIME_MSEC);

    public static void gaussianBlur(boolean activate, Parent parent)
    {
        GaussianBlur blurEffect;
        if (activate)
        {
            blurEffect = new GaussianBlur(0.0);
            parent.setEffect(blurEffect);

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(blurEffect.radiusProperty(), 15.0);
            KeyFrame kf = new KeyFrame(UI_ANIMATION_TIME, kv);
            timeline.getKeyFrames().add(kf);
            timeline.play();
        } else
        {
            blurEffect = new GaussianBlur(0);
        }

        parent.setEffect(blurEffect);
    }

    //==========================================================================
    //== JavaFX help
    //==========================================================================
    public static void fitToParent(AnchorPane anch, TabPane tab)
    {
        anch.setLeftAnchor(tab, 0d);
        anch.setRightAnchor(tab, 0d);
        anch.setBottomAnchor(tab, 0d);
        anch.setTopAnchor(tab, 0d);
    }

    public static void fitToParent(AnchorPane anch1, AnchorPane anch2)
    {
        anch1.setLeftAnchor(anch2, 0d);
        anch1.setRightAnchor(anch2, 0d);
        anch1.setBottomAnchor(anch2, 0d);
        anch1.setTopAnchor(anch2, 0d);
    }

    public static void fitToParent(AnchorPane anch1, FlowPane flowPane)
    {
        anch1.setLeftAnchor(flowPane, 0d);
        anch1.setRightAnchor(flowPane, 0d);
        anch1.setBottomAnchor(flowPane, 0d);
        anch1.setTopAnchor(flowPane, 0d);
    }

    public static void updateColumn(TableView table)
    {
        ((TableColumn) table.getColumns().get(0)).setVisible(false);
        ((TableColumn) table.getColumns().get(0)).setVisible(true);
    }

    public static void updateTable(TableView table)
    {
        table.setVisible(false);
        table.setVisible(true);
    }

    //==========================================================================
    //== JavaFX help
    //==========================================================================
    public static final String FILE_PREFIX = "file:";

    public static Image loadPicture(String path)
    {
        Image image = null;

        if (path.contains(FILE_PREFIX))
        {
            image = new Image(path);
        } else
        {
            image = new Image(path /*ResourcesHandler.getInstance().getImageFolder() + path*/);
        }

        return image;
    }

}
