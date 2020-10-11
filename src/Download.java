import model.DownloadResult;
import notification.NotifyService;

import java.util.Random;
import java.util.concurrent.Callable;

public class Download implements Callable<DownloadResult> {

    private final int id;
    private final NotifyService<DownloadResult> notifyService;

    public Download(int id, NotifyService<DownloadResult> notifyService) {
        this.id = id;
        this.notifyService = notifyService;
    }

    public DownloadResult downloadNext() throws InterruptedException {
        Random r = new Random();
        Thread.sleep(r.nextInt(50)); //download process
        DownloadResult result = new DownloadResult(id, r.nextInt(100000));
        notifyService.notify(result);
        return result;

    }

    @Override
    public DownloadResult call() throws Exception {
        return downloadNext();
    }
}
