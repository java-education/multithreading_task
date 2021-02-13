import model.CalculateResult;
import model.DownloadResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Calendar start = Calendar.getInstance();

		int finishCounter = 0;

		ExecutorService executorDownload = Executors.newFixedThreadPool(10);
		ExecutorService executorCalculate = Executors.newFixedThreadPool(20);
		//ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
		CompletionService<DownloadResult> completionDownloadService =
				new ExecutorCompletionService<>(executorDownload);
		CompletionService<CalculateResult> completionCalculateService =
				new ExecutorCompletionService<>(executorCalculate);

		IntStream.range(0, 1000).forEach(i -> completionDownloadService.submit(new Download(i)));

		int received = 0;

		while (finishCounter < 1000) {
			if (received < 1000) {
				Future<DownloadResult> downloadResultFuture = completionDownloadService.take();
				if (downloadResultFuture.isDone()) {
					downloadResultFuture.cancel(true);
					received++;
					completionCalculateService.submit(new Calculate(downloadResultFuture.get()));
				}
			} else {
				executorDownload.shutdown();
			}
			Future<CalculateResult> calculateResultFuture = completionCalculateService.take();
			if (calculateResultFuture.isDone()) {
				if (calculateResultFuture.get().found) {
					calculateResultFuture.cancel(true);
					finishCounter++;
				}
			}
		}

		executorCalculate.shutdown();
		System.out.println("Total success checks: " + finishCounter);

		Calendar stop = Calendar.getInstance();

		System.out.println("Total time: " + (stop.getTimeInMillis() - start.getTimeInMillis()) + " ms");

	}
}