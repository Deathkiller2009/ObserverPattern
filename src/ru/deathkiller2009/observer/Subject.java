package ru.deathkiller2009.observer;

import ru.deathkiller2009.entity.Operation;
import ru.deathkiller2009.entity.Person;

public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Person person, Operation operation);
}
