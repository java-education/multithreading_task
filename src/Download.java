import model.DownloadResult;

import java.util.Random;

public class Download {
    private static final ThreadLocal<Random> r = ThreadLocal.withInitial(() -> new Random());

    public DownloadResult downloadNext(int id) throws InterruptedException {
        Thread.sleep(r.get().nextInt(50)); //download process
        return new DownloadResult(id, r.get().nextInt(100000));
    }
}
