import model.CalculateResult;
import model.DownloadResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int finishCounter = 0;
        long start = System.currentTimeMillis();

        List<Future<DownloadResult>> downloadResults = new ArrayList<>();

        ExecutorService service = Executors.newFixedThreadPool(3000);
        for (int i = 0; i < 3000; i++) {
            Download task = new Download(i);
            Future<DownloadResult> futureResult = service.submit(task);
            downloadResults.add(futureResult);
        }
        service.shutdown();

        ExecutorService calculateService = Executors.newFixedThreadPool(8);
        List<Future<CalculateResult>> calcResults = new ArrayList<>();

        for (Future<DownloadResult> futureResult : downloadResults) {
            Calculate task = new Calculate(futureResult.get());
            calcResults.add(calculateService.submit(task));
        }

        calculateService.shutdown();

        for (Future<CalculateResult> result : calcResults) {
            if (result.get().found) {
                finishCounter++;
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("Total success checks: " + finishCounter);
        System.out.println("Time: " + (end - start)  + " ms");
    }
}
