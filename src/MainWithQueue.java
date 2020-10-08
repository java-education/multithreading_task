
import model.CalculateResult;
import model.DownloadResult;

import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.*;

public class MainWithQueue {

    private Queue<Future<DownloadResult>> downloadResultQueue = new LinkedBlockingQueue<>();
    private Queue<Future<CalculateResult>> calculateResultQueue = new LinkedBlockingQueue<>();

    private volatile boolean cycle = true;

    private static final int COUNT = 3_000;


    private void calculateResult() throws InterruptedException, ExecutionException {
        int finishCounter = 0;
        long start = Calendar.getInstance().getTimeInMillis();

        Thread[] threads = new Thread[]
                {new Thread(new Producer()), new Thread(new Consumer())};

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        for (Future<CalculateResult> result : calculateResultQueue) {
            if (result.get().found) {
                finishCounter++;
            }
        }

        long end = Calendar.getInstance().getTimeInMillis();

        System.out.println("Total success checks: " + finishCounter);
        System.out.println("Time: " + (end - start) + " ms");
    }


    class Producer implements Runnable {

        @Override
        public void run() {
            Download d = new Download();
            ExecutorService downloadService = Executors.newFixedThreadPool(3000);
            for (int i = 0; i < COUNT; i++) {
                DownloadCallable task = new DownloadCallable(d, i);
                downloadResultQueue.add(downloadService.submit(task));
            }
            cycle = false;
            downloadService.shutdown();
        }

    }

    class Consumer implements Runnable {

        @Override
        public void run() {
            Calculate c = new Calculate();
            Future<DownloadResult> downloadResultFuture;

            ExecutorService calculateService = Executors.newFixedThreadPool(100);

            while (cycle || downloadResultQueue.size() > 0) {
                if ((downloadResultFuture = downloadResultQueue.poll()) != null) {
                    try {
                        Future<CalculateResult> calculateResultFuture =
                                calculateService.submit(new CalculateCallable(c, downloadResultFuture.get()));
                        calculateResultQueue.add(calculateResultFuture);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            calculateService.shutdown();
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        MainWithQueue mainWithQueue = new MainWithQueue();
        mainWithQueue.calculateResult();
    }

}
