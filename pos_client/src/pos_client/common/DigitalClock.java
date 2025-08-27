package pos_client.common;

import java.time.LocalTime;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.format.DateTimeFormatter;
import javafx.scene.text.Text;

public class DigitalClock extends Text
{

    private static DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public DigitalClock()
    {
        bindToTime();
    }

    private void bindToTime()
    {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
                event -> setText(LocalTime.now().format(SHORT_TIME_FORMATTER))),
                new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
