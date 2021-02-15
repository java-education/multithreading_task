import model.DownloadResult;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class Download implements Callable<DownloadResult> {
    private final int id;

    public Download(int id) {
        this.id = id;
    }

    @Override
    public DownloadResult call() {
        Random r = ThreadLocalRandom.current();
        try {
            Thread.sleep(r.nextInt(50)); //download process
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new DownloadResult(id, r.nextInt(2000000));
    }
}
