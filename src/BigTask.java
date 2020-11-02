import model.CalculateResult;
import model.DownloadResult;

import java.util.concurrent.Callable;

public class BigTask implements Callable<CalculateResult> {
    private final int id;
    private DownloadResult downloadResult;
    private CalculateResult calculateResult;

    public BigTask(int id) {
        this.id = id;
    }

    @Override
    public CalculateResult call() throws Exception {
        downloadResult = new Download(id).downloadNext();
        calculateResult = new Calculate(downloadResult).calculate();
        return calculateResult;
    }
}
