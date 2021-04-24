import model.DownloadResult;

import java.util.HashSet;
import java.util.Set;

public class NotificationManagerImpl<T> implements NotificationManager<T> {

    Set<Observer<T>> observers = new HashSet<>();

    @Override
    public void subscribe(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer<T> observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(T result) {
        for(Observer<T> observer : observers) {
            observer.update(result);
        }
    }
}
