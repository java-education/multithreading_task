import model.DownloadResult;

public interface Observer<T> {
    void update(T result);
}
