import model.DownloadResult;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class Download implements Callable<DownloadResult> {
    private final int id;

    public Download(int id) {
        this.id = id;
    }

    public DownloadResult downloadNext(int id) throws InterruptedException {
        Random r = ThreadLocalRandom.current();
        Thread.sleep(r.nextInt(50)); //download process
        return new DownloadResult(id, r.nextInt(100000));
    }

    @Override
    public DownloadResult call() throws Exception {
        return downloadNext(id);
    }

}
