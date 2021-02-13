import model.CalculateResult;
import model.DownloadResult;

import java.util.Calendar;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Calendar start = Calendar.getInstance();

		int finishCounter = 0;

		ExecutorService executorDownload = Executors.newFixedThreadPool(10);
		ExecutorService executorCalculate = Executors.newFixedThreadPool(20);
		CompletionService<DownloadResult> completionDownloadService =
				new ExecutorCompletionService<>(executorDownload);
		CompletionService<CalculateResult> completionCalculateService =
				new ExecutorCompletionService<>(executorCalculate);

		IntStream.range(0, 1000).forEach(i -> completionDownloadService.submit(new Download(i)));

		int received = 0;
		boolean isDownloadFinished = false;

		while (finishCounter < 1000) {
			if (received < 1000) {
				Future<DownloadResult> downloadResultFuture = completionDownloadService.poll();
				if (downloadResultFuture != null) {
					downloadResultFuture.cancel(true);
					received++;
					completionCalculateService.submit(new Calculate(downloadResultFuture.get()));
				}
			} else if (!isDownloadFinished) {
				isDownloadFinished = true;
				executorDownload.shutdown();
			}
			Future<CalculateResult> calculateResultFuture = completionCalculateService.poll();
			if (calculateResultFuture != null) {
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