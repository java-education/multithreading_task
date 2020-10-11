import model.DownloadResult;
import notification.NotifyService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class DownloadService implements TerminatableService, Runnable {
    private final ExecutorService service = Executors.newFixedThreadPool(3000);
    private final NotifyService<DownloadResult> notifyService;
    private final int tasksCount;

    public DownloadService(NotifyService<DownloadResult> notifyService, int tasksCount) {
        this.notifyService = notifyService;
        this.tasksCount = tasksCount;
    }

    public void run() {
        for (int i = 0; i < tasksCount; i++) {
            Download task = new Download(i, notifyService);
            service.submit(task);
        }
        service.shutdown();
    }

    public void awaitTermination() throws InterruptedException {
        service.shutdown();
        service.awaitTermination(100, TimeUnit.HOURS);
    }

}
