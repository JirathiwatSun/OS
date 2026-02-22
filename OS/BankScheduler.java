import java.util.Timer;
import java.util.TimerTask;

public class BankScheduler {

    private Timer timer;        // HEAP
    private boolean isRunning;  // HEAP (instance variable)

    public void startInterestTimer(BankAccount acc) {

        if (isRunning) {
            System.out.println("Scheduler already running.");
            return;
        }

        timer = new Timer();
        isRunning = true;

        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (acc instanceof SavingsAccount) {
                    ((SavingsAccount) acc).applyYearlyInterest();
                }

                stopTimer();
            }

        }, 3000); // 3 seconds delay
    }

    public void stopTimer() {

        if (timer != null) {
            timer.cancel();
            isRunning = false;
        }
    }
}