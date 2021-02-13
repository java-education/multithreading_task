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

//
//		IntStream.range(0, 1000).mapToObj(i -> new FutureTask(() -> new Download(i).downloadNext()));
//
//		List<Future<DownloadResult>> futuresList = new ArrayList<>();
		ExecutorService executorDownload = Executors.newFixedThreadPool(10);
		ExecutorService executorCalculate = Executors.newFixedThreadPool(10);
		CompletionService<DownloadResult> completionDownloadService =
				new ExecutorCompletionService<>(executorDownload);
		CompletionService<CalculateResult> completionCalculateService =
				new ExecutorCompletionService<>(executorCalculate);

		for (int i = 0; i < 1000; i++) {

//			int id = i;
//			FutureTask<DownloadResult> futureTask = new FutureTask<>(() -> new Download(id).downloadNext());
//
//			Callable<DownloadResult> task = new Download(i);
//			Future<DownloadResult> future = executor.submit(task);
//			futuresList.add(future);
			completionDownloadService.submit(new Download(i));

//			Download d = new Download(i);
//			DownloadResult downloadResult = d.downloadNext();
//
//			Calculate c = new Calculate(downloadResult);
//			CalculateResult calculateResult = c.calculate();
//
//			if (calculateResult.found) {
//				finishCounter++;
//			}
		}

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