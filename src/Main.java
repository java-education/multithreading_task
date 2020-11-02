import model.CalculateResult;
import model.DownloadResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Calendar start = Calendar.getInstance();

        int finishCounter = 0;

        List<Future<DownloadResult>> downloadResults = new ArrayList<>();
        ExecutorService downloadService = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 1000; i++) {
            Callable<DownloadResult> downloadTask = new Download(i);
            Future<DownloadResult> dResult = downloadService.submit(downloadTask);
            downloadResults.add(dResult);
        }
        downloadService.shutdown();

        List<Future<CalculateResult>> calculateResults = new ArrayList<>();
        ExecutorService calculateService = Executors.newFixedThreadPool(8);
        for (Future<DownloadResult> dResult : downloadResults) {
            Callable<CalculateResult> calculateTask = new Calculate(dResult.get());
            Future<CalculateResult> cResult = calculateService.submit(calculateTask);
            calculateResults.add(cResult);
        }
        calculateService.shutdown();

        for (Future<CalculateResult> cResult : calculateResults) {
            if (cResult.get().found) {
                finishCounter++;
            }
        }

        System.out.println("Total success checks: " + finishCounter);

        Calendar stop = Calendar.getInstance();

        System.out.println("Total time: " + (stop.getTimeInMillis() - start.getTimeInMillis()) + " ms");

    }
}
