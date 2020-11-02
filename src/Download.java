import model.DownloadResult;

import java.util.Random;
import java.util.concurrent.Callable;

public class Download implements Callable<DownloadResult> {
    private final int id;

    public Download(int id) {
        this.id = id;
    }

    public DownloadResult downloadNext() throws InterruptedException {
        Random r = new Random();
        Thread.sleep(r.nextInt(50)); //download process
        return new DownloadResult(id, r.nextInt(2000000));
    }

    @Override
    public DownloadResult call() throws Exception {
        return downloadNext();
    }
}
