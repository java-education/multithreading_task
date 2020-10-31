import model.CalculateResult;
import model.DownloadResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        Calendar start = Calendar.getInstance();
        List<CompletableFuture<AtomicInteger>> completableFutures = new ArrayList<>();
        AtomicInteger finishCounter = new AtomicInteger(0);
        for (int i = 0; i < 3000; i++) {
            final int index = i;
            completableFutures.add(
                    CompletableFuture
                            .supplyAsync(() -> downloadTask(index))
                            .thenApplyAsync(downloadResult -> calculateTask(finishCounter, downloadResult))
            );
        }
        completableFutures.forEach(CompletableFuture::join);
        System.out.println("Total success checks: " + finishCounter);
        Calendar stop = Calendar.getInstance();
        System.out.println("Total time: " + (stop.getTimeInMillis() - start.getTimeInMillis()) + " ms");
    }

    private static DownloadResult downloadTask(int index) {
        Download d = new Download(index);
        DownloadResult downloadResult = null;
        try {
            downloadResult = d.downloadNext();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return downloadResult;
    }

    private static AtomicInteger calculateTask(AtomicInteger finishCounter, DownloadResult downloadResult) {
        Calculate c = new Calculate(downloadResult);
        CalculateResult calculateResult = c.calculate();

        if (calculateResult.found) {
            finishCounter.incrementAndGet();
        }
        return finishCounter;
    }
}