package ru.deathkiller2009.observer;

import ru.deathkiller2009.entity.Operation;
import ru.deathkiller2009.entity.Person;

public interface Observer {
    void update(Person person, Operation operation);
}
