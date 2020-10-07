import model.CalculateResult;
import model.DownloadResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Download d = new Download();
        Calculate c = new Calculate();
        int finishCounter = 0;
        long start = Calendar.getInstance().getTimeInMillis();

        ExecutorService downloadService = Executors.newFixedThreadPool(30);
        ExecutorService calcService = Executors.newFixedThreadPool(30);
        BlockingDeque<Future<CalculateResult>> queue = new LinkedBlockingDeque<>();

        for (int i = 0; i < 3000; i++) {
            DownloadCallable task = new DownloadCallable(d, i, queue, calcService, c);
            downloadService.submit(task);
        }
        for (Future<CalculateResult> calcResult : queue) {
            if (calcResult.get().found) {
                finishCounter++;
            }
        }
        downloadService.shutdown();
        calcService.shutdown();

        long end = Calendar.getInstance().getTimeInMillis();

        System.out.println("Total success checks: " + finishCounter);
        System.out.println("Time: " + (end - start)  + " ms");
    }
}
