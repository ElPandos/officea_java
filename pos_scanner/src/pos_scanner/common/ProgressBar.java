
package pos_scanner.common;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

public class ProgressBar
{

    float progressValue = 0;

    private Task worker = null;

    private ProgressIndicator piProgress;

    public ProgressBar(ProgressIndicator piProgress)
    {
        this.piProgress = piProgress;
        
        worker = createWorker();
    }

    public void start()
    {
        initProgressbar();
        
        progressValue = 0;
        piProgress.setProgress(progressValue);

        if (worker == null)
        {
            worker = createWorker();
        }

        piProgress.progressProperty().unbind();
        piProgress.progressProperty().bind(worker.progressProperty());

        worker.messageProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                System.out.println(newValue);
            }
        });

        new Thread(worker).start();
    }

    private void stop()
    {
        worker.cancel(true);
        piProgress.progressProperty().unbind();
        progressValue = 0;
    }

    public void setProgress(float progressValue)
    {
        this.progressValue = progressValue;
    }

    public Task createWorker()
    {
        return new Task()
        {
            @Override
            protected Object call() throws Exception
            {
                while (true)
                {
                    Thread.sleep(10);
                    piProgress.setProgress(progressValue);
                }
            }
        };
    }

    private void initProgressbar()
    {
        piProgress.progressProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number newValue)
            {
                // If progress is 100% then show Text
                if (newValue.doubleValue() >= 1)
                {
                    // Apply CSS so you can lookup the text
                    piProgress.applyCss();
                    Text text = (Text) piProgress.lookup(".text.percentage");
                    // This text replaces "Done"
                    text.setText("Foo");
                }
            }
        });
        
        setProgress(1);
    }
}
