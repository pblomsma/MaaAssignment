package nl.uu.cs.MaaAssignment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duame on 12/05/2017.
 */
public abstract class ASubject {

    private List<IObserver> observers = new ArrayList<>();

    public void attach(IObserver observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for (IObserver observer : observers) {
            observer.update();
        }
    }
}
