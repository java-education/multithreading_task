import model.CalculateResult;
import model.DownloadResult;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class Calculate implements Callable<CalculateResult> {

	private final DownloadResult downloadResult;

	public Calculate(DownloadResult downloadResult) {
		this.downloadResult = downloadResult;
	}

	@Override
	public CalculateResult call() {
		Random r = ThreadLocalRandom.current();
		CalculateResult result = new CalculateResult();
		result.id = downloadResult.id;

		int check;
		do {
			check = r.nextInt(2000000);
		} while (!downloadResult.check(check));

		result.found = true;
		result.data = check;

		return result;
	}
}
