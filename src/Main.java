import model.CalculateResult;

import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Download d = new Download();
        Calculate c = new Calculate();
        int finishCounter = 0;
        long start = Calendar.getInstance().getTimeInMillis();

        ExecutorService service = Executors.newFixedThreadPool(3000);

        Queue<Future<CalculateResult>> queue = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < 3000; i++) {
            DownloadCallable task = new DownloadCallable(d, i, queue, service, c);
            service.submit(task);
        }

        for (Future<CalculateResult> result : queue) {
            if (result.get().found) {
                finishCounter++;
            }
        }
        service.shutdown();

        long end = Calendar.getInstance().getTimeInMillis();

        System.out.println("Total success checks: " + finishCounter);
        System.out.println("Time: " + (end - start)  + " ms");
    }
}
