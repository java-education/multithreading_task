import model.CalculateResult;
import model.DownloadResult;

import java.util.concurrent.atomic.AtomicInteger;

public class TaskRunner {

    public static DownloadResult downloadTask(int index) {
        Download d = new Download(index);
        DownloadResult downloadResult = null;
        try {
            downloadResult = d.downloadNext();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return downloadResult;
    }

    public static AtomicInteger calculateTask(AtomicInteger finishCounter, DownloadResult downloadResult) {
        Calculate c = new Calculate(downloadResult);
        CalculateResult calculateResult = c.calculate();

        if (calculateResult.found) {
            finishCounter.incrementAndGet();
        }
        return finishCounter;
    }
}
