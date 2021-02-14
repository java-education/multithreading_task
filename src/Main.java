import model.CalculateResult;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

	public static void main(String[] args) {

		Calendar start = Calendar.getInstance();

		int TASK_NUMBER = 1000;

		ExecutorService executorDownload = Executors.newFixedThreadPool(10);
		ExecutorService executorCalculate = Executors.newFixedThreadPool(20);


		List<CompletableFuture<CalculateResult>> calculateResult = IntStream.range(0, TASK_NUMBER)
				.mapToObj(i -> CompletableFuture.supplyAsync(() -> new Download(i), executorDownload)
						.thenApplyAsync(Main::calculateResultFuture, executorCalculate))
				.collect(Collectors.toList());

		long count = calculateResult.stream().filter(Main::isFutureCalculated).count();

		executorDownload.shutdown();
		executorCalculate.shutdown();

		System.out.println("Total success checks: " + count);

		Calendar stop = Calendar.getInstance();

		System.out.println("Total time: " + (stop.getTimeInMillis() - start.getTimeInMillis()) + " ms");

	}

	public static CalculateResult calculateResultFuture(Download download) {
		return new Calculate((download.call())).call();
	}

	public static boolean isFutureCalculated(Future<CalculateResult> future) {
		try {
			return future.get().found;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}
}