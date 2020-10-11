package notification;

public interface NotifyService<T> {
    void attach(Observer<T> observer);
    void detach(Observer<T> observer);

    void notify(T result);
}
