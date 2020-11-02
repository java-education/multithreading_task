import model.CalculateResult;
import model.DownloadResult;

import java.util.Random;
import java.util.concurrent.Callable;

public class Calculate implements Callable<CalculateResult> {

    private final DownloadResult downloadResult;

    public Calculate(DownloadResult downloadResult) {
        this.downloadResult = downloadResult;
    }


    public CalculateResult calculate() {
        Random r = new Random();
        CalculateResult result = new CalculateResult();
        result.id = downloadResult.id;

        int check;
        do {
            check = r.nextInt(2000000);
        } while (!downloadResult.check(check));

        result.found = true;

        return result;
    }

    @Override
    public CalculateResult call() throws Exception {
        return calculate();
    }
}
