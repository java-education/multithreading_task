import model.CalculateResult;
import model.DownloadResult;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DownloadCallable implements Runnable {
    private final Download download;
    private final int id;
    private final Queue<Future<CalculateResult>> queue;
    private final ExecutorService calculateService;
    private final Calculate c;

    public DownloadCallable(Download download, int id, Queue<Future<CalculateResult>> queue, ExecutorService calculateService, Calculate c) {
        this.download = download;
        this.id = id;
        this.queue = queue;
        this.calculateService = calculateService;
        this.c = c;
    }

    @Override
    public void run() {
        try {
            Future<CalculateResult> calcFuture = calculateService.submit(new CalculateCallable(c, download.downloadNext(id)));
            queue.add(calcFuture);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
