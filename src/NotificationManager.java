import model.DownloadResult;

public interface NotificationManager<T> {
    void subscribe(Observer<T> observer);
    void unsubscribe(Observer<T> observer);
    void notify(T result);
}
