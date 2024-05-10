package ru.deathkiller2009;

import ru.deathkiller2009.entity.Operation;
import ru.deathkiller2009.entity.Person;
import ru.deathkiller2009.observer.Observer;

import java.time.LocalDateTime;

public class ConsoleLog implements Observer {
    private void logChange(Person person, Operation operation) {
        System.err.println("Operation: " + operation.toString() + " Time of change: " + LocalDateTime.now() + " Changed/Inserted id " + person.getId() + " Changed/Inserted name " + person.getName());
    }

    @Override
    public void update(Person person, Operation operation) {
        logChange(person, operation);
    }
}
