package notification;

public interface Observer<T> {
    void update(T result);
}
