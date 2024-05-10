package ru.deathkiller2009;

import ru.deathkiller2009.entity.Operation;
import ru.deathkiller2009.entity.Person;
import ru.deathkiller2009.observer.Observer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class TextFileLog implements Observer {
    private void logChange(Person person, Operation operation) {
        String log = "Operation: " + operation.toString() + " Time of change: " + LocalDateTime.now() + " Changed/Inserted id " + person.getId() + " Changed/Inserted name " + person.getName() + "\n";
        try {
            Files.write(Path.of("resources", "log.txt"), log.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Person person, Operation operation) {
        logChange(person, operation);
    }
}
