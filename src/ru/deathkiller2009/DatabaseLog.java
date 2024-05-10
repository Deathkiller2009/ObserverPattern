package ru.deathkiller2009;

import ru.deathkiller2009.entity.Operation;
import ru.deathkiller2009.entity.Person;
import ru.deathkiller2009.observer.Observer;
import ru.deathkiller2009.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DatabaseLog implements Observer {
    private static final String LOG = """
            INSERT INTO log (operation, time_of_change, changed_person_id, changed_person_name)
            VALUES (?, ?, ?, ?)
            """;

    private void logChange(Person person, Operation operation){
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(LOG)) {
            preparedStatement.setString(1, operation.toString());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(3, person.getId());
            preparedStatement.setString(4, person.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Person person, Operation operation) {
        logChange(person, operation);
    }
}
