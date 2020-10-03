import model.DownloadResult;

import java.util.concurrent.Callable;

public class DownloadCallable implements Callable<DownloadResult> {

    private final Download download;
    private final int id;

    public DownloadCallable(Download download, int id) {
        this.download = download;
        this.id = id;
    }

    @Override
    public DownloadResult call() throws Exception {
        return download.downloadNext(id);
    }
}
