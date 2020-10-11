package notification.impl;

import notification.NotifyService;
import notification.Observer;

import java.util.ArrayList;
import java.util.List;

public class NotifyServiceImpl<T> implements NotifyService<T> {
    protected List<Observer<T>> observers = new ArrayList<>();

    public void attach(Observer<T> observer) {
        observers.add(observer);
    }

    public void detach(Observer<T> observer) {
        observers.remove(observer);
    }

    @Override
    public void notify(T result) {
        for (Observer<T> observer : observers) {
            observer.update(result);
        }
    }
}
