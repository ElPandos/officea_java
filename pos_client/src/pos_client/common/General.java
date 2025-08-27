/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.common;

import pos_client.ResourcesHandler;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

/**
 *
 * @author Laptop
 */
public class General
{

    private static final String decimalFormatPattern = "###,##0.00";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final DecimalFormat decimalFormat = new DecimalFormat(decimalFormatPattern);

    private static General instance = null;

    protected General()
    {
        // Exists only to defeat instantiation.
    }

    public static General getInstance()
    {
        if (instance == null)
        {
            instance = new General();
        }
        return instance;
    }

    public final String newLine = System.getProperty("line.separator");
    public final String delimiterDot = "\\.";
    public final String delimiterComma = "\\,";
    public final String delimiterSemicolon = "\\:";

    public final int INVALID_VALUE = -9999;

    public final Color CONNECTED = Color.LIGHTGREEN;
    public final Color DISCONNECTED = Color.RED;

    public double str2Dbl(String data, int decimal)
    {
        return decimal(Double.parseDouble(data.replace(',', '.')), decimal, true);
    }

    public float str2Float(String data, int decimal)
    {
        return decimal((float) Float.parseFloat(data.replace(',', '.')), decimal, true);
    }

    public int str2Int(String data)
    {
        return Integer.parseInt(data);
    }

    public String int2Str(int data)
    {
        return Integer.toString(data);
    }

    public String float2Str(float data, int decimal)
    {
        float value = decimal(data, decimal, true);
        return General.decimalFormat.format(value);
    }

    public String float2StrDB(float data, int decimal)
    {
        float value = decimal(data, decimal, true);
        return Float.toString(value);
    }

    public String time()
    {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(TIME_FORMAT);

        return ft.format(dNow);
    }

    public String date()
    {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);

        return ft.format(dNow);
    }
    
    public String totalDateTime()
    {
        return date() + " " + time();
    }

    public void fitToParent(AnchorPane anch, TabPane tab)
    {
        anch.setLeftAnchor(tab, 0d);
        anch.setRightAnchor(tab, 0d);
        anch.setBottomAnchor(tab, 0d);
        anch.setTopAnchor(tab, 0d);
    }

    public void fitToParent(AnchorPane anch1, AnchorPane anch2)
    {
        anch1.setLeftAnchor(anch2, 0d);
        anch1.setRightAnchor(anch2, 0d);
        anch1.setBottomAnchor(anch2, 0d);
        anch1.setTopAnchor(anch2, 0d);
    }

    public void fitToParent(AnchorPane anch1, FlowPane flowPane)
    {
        anch1.setLeftAnchor(flowPane, 0d);
        anch1.setRightAnchor(flowPane, 0d);
        anch1.setBottomAnchor(flowPane, 0d);
        anch1.setTopAnchor(flowPane, 0d);
    }

    public float decimal(float value, int dec, boolean round)
    {
        int decimalValue = (int) Math.pow((double) 10, (double) dec);
        float valueDecimal = value * decimalValue;
        if (round)
        {
            valueDecimal = Math.round(valueDecimal);
        }

        int valueInt = (int) valueDecimal;
        float valueFloat = (float) valueInt / decimalValue;

        return valueFloat;

    }

    public double decimal(double value, int dec, boolean round)
    {
        int decimalValue = (int) Math.pow((double) 10, (double) dec);
        double valueDecimal = value * decimalValue;
        if (round)
        {
            valueDecimal = Math.round(valueDecimal);
        }

        int valueInt = (int) valueDecimal;
        double valueDouble = (float) valueInt / decimalValue;

        return valueDouble;
    }

    public void updateColumn(TableView table)
    {
        ((TableColumn) table.getColumns().get(0)).setVisible(false);
        ((TableColumn) table.getColumns().get(0)).setVisible(true);
    }

    public void updateTable(TableView table)
    {
        table.setVisible(false);
        table.setVisible(true);
    }

    public Image loadPicture(String path)
    {
        Image image;

        if (path.contains("file:"))
        {
            image = new Image(path);
        } else
        {
            image = new Image(ResourcesHandler.getInstance().getImageFolder() + path);
        }

        return image;
    }

    public boolean isFloat(Object floatValue)
    {
        try
        {
            Float.parseFloat(floatValue.toString());
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public void gaussianBlur(boolean activate, Parent parent)
    {
        GaussianBlur blurEffect;;
        if (activate)
        {
            blurEffect = new GaussianBlur(0.0);
            parent.setEffect(blurEffect);

            Timeline timeline = new Timeline();
            KeyValue kv = new KeyValue(blurEffect.radiusProperty(), 15.0);
            KeyFrame kf = new KeyFrame(Duration.millis(UI_ANIMATION_TIME_MSEC), kv);
            timeline.getKeyFrames().add(kf);
            timeline.play();
        } else
        {
            blurEffect = new GaussianBlur(0);
        }

        parent.setEffect(blurEffect);
    }
    public static final int UI_ANIMATION_TIME_MSEC = 600;
    public static final Duration UI_ANIMATION_TIME = Duration.millis(UI_ANIMATION_TIME_MSEC);
}
