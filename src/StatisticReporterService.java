import model.CalculateResult;
import model.DownloadResult;
import notification.Observer;

public class StatisticReporterService<T> implements Observer<T>, Runnable {

    private final int tasksCount;
    private volatile int downloadsCount;
    private volatile int calculatesCount;

    public StatisticReporterService(int tasksCount) {
        this.tasksCount = tasksCount;
    }

    @Override
    public synchronized void update(T result) {
        if (result instanceof DownloadResult) {
            downloadsCount++;
        } else if (result instanceof CalculateResult) {
            calculatesCount++;
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println("загружено " + downloadsCount + " из " + tasksCount
                        + ", подсчитано " + calculatesCount + " из " + downloadsCount);
            } catch (InterruptedException e) {
                break;
            }
        }

    }

    public void start() {
        Thread statisticReporterServiceThread = new Thread(this);
        statisticReporterServiceThread.setDaemon(true);
        statisticReporterServiceThread.start();
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }
}
