import model.CalculateResult;
import model.DownloadResult;
import notification.NotifyService;
import notification.impl.NotifyServiceImpl;

import java.util.Calendar;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int tasksCount = 30000;

        long start = Calendar.getInstance().getTimeInMillis();

        NotifyService<CalculateResult> calculateNotifyService = new NotifyServiceImpl<>();
        NotifyService<DownloadResult> downloadNotifyService = new NotifyServiceImpl<>();

        CalculationService calculationService = new CalculationService(calculateNotifyService);
        downloadNotifyService.attach(calculationService);

        //todo run statistic reporter

        DownloadService downloadService = new DownloadService(downloadNotifyService, tasksCount);

        downloadService.run();

        downloadService.awaitTermination();
        calculationService.awaitTermination();

        long end = Calendar.getInstance().getTimeInMillis();

        System.out.println("Total success checks: " );  //todo receive and print results

        System.out.println("Time: " + (end - start)  + " ms");
    }

}

