import model.CalculateResult;
import model.DownloadResult;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CalculateService implements Observer<DownloadResult> {

    ExecutorService executorService = Executors.newFixedThreadPool(8);

    private final NotificationManager<CalculateResult> notificationManager;

    public CalculateService(NotificationManager<CalculateResult> notificationManager) {
        this.notificationManager = notificationManager;
    }

    @Override
    public void update(DownloadResult result) {
        Calculate task = new Calculate(result, notificationManager);
        executorService.submit(task);
    }

    public void awaitAndTerminate() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
    }


}
