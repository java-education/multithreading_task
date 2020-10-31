import model.CalculateResult;
import model.DownloadResult;

import java.util.Calendar;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Calendar start = Calendar.getInstance();

        AtomicInteger finishCounter = new AtomicInteger(0);
        for (int i = 0; i < 1000; i++) {
            final int index = i;
            CompletableFuture.runAsync(() -> {
                Download d = new Download(index);
                DownloadResult downloadResult = null;
                try {
                    downloadResult = d.downloadNext();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Calculate c = new Calculate(downloadResult);
                CalculateResult calculateResult = c.calculate();

                if (calculateResult.found) {
                    finishCounter.incrementAndGet();
                }
            });
        }

        System.out.println("Total success checks: " + finishCounter);

        Calendar stop = Calendar.getInstance();

        System.out.println("Total time: " + (stop.getTimeInMillis() - start.getTimeInMillis()) + " ms");

    }
}