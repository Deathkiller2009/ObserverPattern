package ru.deathkiller2009;

import ru.deathkiller2009.entity.Operation;
import ru.deathkiller2009.entity.Person;
import ru.deathkiller2009.observer.Observer;
import ru.deathkiller2009.observer.Subject;
import ru.deathkiller2009.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDao implements Subject {
    private static final List<Observer> observers = new ArrayList<>();
    private static final String SELECT_ALL = """
            SELECT id, name
            FROM person
            """;

    private static final String SELECT_BY_ID = SELECT_ALL + """
            WHERE id = ?
            """;

    private static final String UPDATE = """
            UPDATE person
            SET id = ?,
            name = ?
            WHERE id = ?
            """;

    private static final String DELETE = """
            DELETE
            FROM person
            WHERE id = ?
            """;

    private static final String INSERT = """
            INSERT INTO person (name)
            VALUES (?)
            """;
    public PersonDao(){

    }
    public static PersonDao getInstance(){
        return null;
    }

    public List<Person> findAll() {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Person> people = new ArrayList<>();
            while (resultSet.next()) {
                people.add(getPerson(resultSet));
            }
            return people;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Person> findById(Integer id){
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Person client = null;
            if (resultSet.next()){
                client = getPerson(resultSet);
            }
            return Optional.ofNullable(client);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(Person person) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setInt(1, person.getId());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setInt(3, person.getId());
            preparedStatement.executeUpdate();
            notifyObservers(person, Operation.UPDATE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete(Integer id) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, id);
            Person person = findById(id).get();
            preparedStatement.executeUpdate();
            notifyObservers(person, Operation.DELETE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Integer save(Person person) {
        try (Connection connection = ConnectionManager.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                int id = generatedKeys.getInt("id");
                person.setId(id);
                notifyObservers(person, Operation.INSERT);
                return id;
            }
            else throw new RuntimeException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Person getPerson(ResultSet resultSet) throws SQLException {

        return new Person(resultSet.getInt("id"), resultSet.getString("name"));

    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Person person, Operation operation) {
        for (Observer observer : observers) {
            observer.update(person, operation);
        }
    }
}

