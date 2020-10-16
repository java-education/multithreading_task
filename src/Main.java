import model.CalculateResult;
import model.DownloadResult;
import notification.NotifyService;
import notification.Observer;
import notification.impl.NotifyServiceImpl;

import java.util.Calendar;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int tasksCount = 30_000;

        long start = Calendar.getInstance().getTimeInMillis();

        NotifyService<CalculateResult> calculateNotifyService = new NotifyServiceImpl<>();
        NotifyService<DownloadResult> downloadNotifyService = new NotifyServiceImpl<>();

        CounterService counterService = new CounterService();
        calculateNotifyService.attach(counterService);

        CalculationService calculationService = new CalculationService(calculateNotifyService);
        downloadNotifyService.attach(calculationService);

        // run statistic reporter
        StatisticReporterService statisticReporterService = new StatisticReporterService(tasksCount);

        downloadNotifyService.attach(statisticReporterService);
        calculateNotifyService.attach(statisticReporterService);

        statisticReporterService.start();

        DownloadService downloadService = new DownloadService(downloadNotifyService, tasksCount);

        downloadService.run();

        downloadService.awaitTermination();
        calculationService.awaitTermination();
        statisticReporterService.stop();

        long end = Calendar.getInstance().getTimeInMillis();

        System.out.println("Total success checks: " + counterService.getSuccessChecks());

        System.out.println("Time: " + (end - start)  + " ms");
    }

}

