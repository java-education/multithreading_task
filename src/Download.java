import model.DownloadResult;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class Download implements Callable<DownloadResult> {
    private final int id;
    private final NotificationManager<DownloadResult> notificationManager;

    public Download(int id, NotificationManager<DownloadResult> notificationManager) {
        this.id = id;
        this.notificationManager = notificationManager;
    }

    public DownloadResult downloadNext(int id) throws InterruptedException {
        Random r = ThreadLocalRandom.current();
        Thread.sleep(r.nextInt(50)); //download process
        DownloadResult result = new DownloadResult(id, r.nextInt(100000));
        notificationManager.notify(result);

        return result;
    }

    @Override
    public DownloadResult call() throws Exception {
        return downloadNext(id);
    }



}
