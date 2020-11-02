import model.CalculateResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Calendar start = Calendar.getInstance();

        int finishCounter = 0;

        ExecutorService service = Executors.newFixedThreadPool(8);
        List<Future<CalculateResult>> calcResults = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Callable<CalculateResult> bigTask = new BigTask(i);
            Future<CalculateResult> futureResult = service.submit(bigTask);
            calcResults.add(futureResult);
        }
        service.shutdown();

        for (Future<CalculateResult> result : calcResults) {
            if (result.get().found) {
                finishCounter++;
            }
        }

        System.out.println("Total success checks: " + finishCounter);

        Calendar stop = Calendar.getInstance();

        System.out.println("Total time: " + (stop.getTimeInMillis() - start.getTimeInMillis()) + " ms");

    }
}
