import model.CalculateResult;
import model.DownloadResult;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        long finishCounter = 0;
        long start = System.currentTimeMillis();

        NotificationManager<CalculateResult> notificationManagerCalculate = new NotificationManagerImpl<>();

        NotificationManager<DownloadResult> notificationManagerDowload = new NotificationManagerImpl<>();
        CalculateService calculateService = new CalculateService(notificationManagerCalculate);
        notificationManagerDowload.subscribe(calculateService);

        ResultService resultService = new ResultService();
        notificationManagerCalculate.subscribe(resultService);

        runDownloads(notificationManagerDowload);

        calculateService.awaitAndTerminate();
        finishCounter = resultService.getTotal();

        long end = System.currentTimeMillis();

        System.out.println("Total success checks: " + finishCounter);
        System.out.println("Time: " + (end - start)  + " ms");

    }

    private static void runDownloads(NotificationManager<DownloadResult> notificationManagerDowload) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(3000);

        for (int i = 0; i < 3000; i++) {
            Download task = new Download(i, notificationManagerDowload);
            service.submit(task);
        }
        service.shutdown();

        service.awaitTermination(1, TimeUnit.HOURS);
    }
}
