import model.CalculateResult;
import model.DownloadResult;
import notification.NotifyService;
import notification.Observer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CalculationService implements Observer<DownloadResult>, TerminatableService {
    private final ExecutorService calculateService = Executors.newFixedThreadPool(8);
    private final NotifyService<CalculateResult> notifyService;

    public CalculationService(NotifyService<CalculateResult> notifyService) {
        this.notifyService = notifyService;
    }

    @Override
    public void update(DownloadResult result) {
        calculateService.submit(new Calculate(result, notifyService));
    }

    @Override
    public void awaitTermination() throws InterruptedException {
        calculateService.shutdown();
        calculateService.awaitTermination(100, TimeUnit.HOURS);
    }
}
