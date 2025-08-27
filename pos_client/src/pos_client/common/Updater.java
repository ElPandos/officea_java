/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.common;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 *
 * @author Server
 */
public class Updater
{

    Timeline timeline = null;
    int milliSek = 1000;

    public void Updater()
    {
        init();
    }

    private void init()
    {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(milliSek),
                ae -> update()));

        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void setUpdateSpeed(int milliSek)
    {
        this.milliSek = milliSek;
    }

    public void start()
    {
        if (timeline == null)
        {
            init();
        }

        timeline.play();
    }

    public void stop()
    {
        if (timeline != null)
        {
            timeline.stop();
        }
    }

    public void update()
    {
        // denna ska ärvas för update
    }

}
