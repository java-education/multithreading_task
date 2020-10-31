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
                            .supplyAsync(() -> TaskRunner.downloadTask(index))
                            .thenApplyAsync(downloadResult -> TaskRunner.calculateTask(finishCounter, downloadResult))
            );
        }
        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();
        System.out.println("Total success checks: " + finishCounter);
        Calendar stop = Calendar.getInstance();
        System.out.println("Total time: " + (stop.getTimeInMillis() - start.getTimeInMillis()) + " ms");
    }
}