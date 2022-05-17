import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Delayed;

public class clockTest
{

    public static void main(String[] args)
    {

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                int second, minute, hour;
                Calendar date = Calendar.getInstance();
                second = date.get(Calendar.SECOND);
                minute = date.get(Calendar.MINUTE);
                hour = date.get(Calendar.HOUR);
                System.out.println("Current time is  " + hour + " : " +
                        minute +" : " + second);
            }
        }, 1 * 5000, 1 * 5000);
    }
}
