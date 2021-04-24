import model.CalculateResult;
import model.DownloadResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ResultService implements Observer<CalculateResult> {

    AtomicLong counter = new AtomicLong();

    @Override
    public void update(CalculateResult result) {
        if (result.found) {
            counter.incrementAndGet();
        }
    }

    public long getTotal() {
        return counter.get();
    }
}
