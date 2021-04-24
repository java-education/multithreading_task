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

    public CalculateResult calculate() {
        Random r = ThreadLocalRandom.current();
        CalculateResult result = new CalculateResult();
        result.id = downloadResult.id;
        while (true) {
            int check = r.nextInt(100000);
            if (downloadResult.check(check)) {
                result.found = true;
                return result;
            }
        }
    }

    @Override
    public CalculateResult call() {
        return calculate();
    }

}
