import model.DownloadResult;

import java.util.Random;
import java.util.concurrent.Callable;

public class Download implements Callable<DownloadResult> {
    private final int id;

    public Download(int id) {
        this.id = id;
    }

    public DownloadResult downloadNext() {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(50)); //download process
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new DownloadResult(id, r.nextInt(2000000));
    }

    @Override
    public DownloadResult call() {
        Random r = new Random();
        try {
            Thread.sleep(r.nextInt(50)); //download process
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new DownloadResult(id, r.nextInt(2000000));
    }
}
