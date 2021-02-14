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
		List<Future<DownloadResult>> futures = new ArrayList<>(1000);
		List<Future<CalculateResult>> futuresCalc = new ArrayList<>(1000);

//		Thread createDownloadTasks = new Thread(() -> {
//			IntStream.range(0, 1000).forEach(i -> executorDownload.submit(new Download(i)));
//			executorDownload.shutdown();
//		});
		for (int i = 0; i < 1000; i++) {
			futures.add(executorDownload.submit(new Download(i)));
		}
		executorDownload.shutdown();
		for (Future<DownloadResult> f : futures) {
			futuresCalc.add(executorCalculate.submit(new Calculate(f.get())));
		}
		executorCalculate.shutdown();
		for (Future<CalculateResult> f : futuresCalc) {
			if (f.get().found) {
				finishCounter++;
			}
		}


		System.out.println("Total success checks: " + finishCounter);

		Calendar stop = Calendar.getInstance();

		System.out.println("Total time: " + (stop.getTimeInMillis() - start.getTimeInMillis()) + " ms");

	}
}