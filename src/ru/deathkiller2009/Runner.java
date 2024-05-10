package ru.deathkiller2009;

import ru.deathkiller2009.entity.Person;

public class Runner {
    public static void main(String[] args) {
        PersonDao personDao = new PersonDao();

        DatabaseLog databaseLog = new DatabaseLog();
        ConsoleLog consoleLog = new ConsoleLog();
        TextFileLog textFileLog = new TextFileLog();

        personDao.registerObserver(databaseLog);
        personDao.registerObserver(consoleLog);
        personDao.registerObserver(textFileLog);

        personDao.save(new Person("Lalo"));
        personDao.save(new Person("Mona"));
        personDao.save(new Person("Darius"));
        personDao.save(new Person("Kayle"));
//        Person person = personDao.findById(2).get();
//        person.setName("Anna");
//        personDao.update(person);

    }
}
