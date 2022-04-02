package com.kuhleuski;

import com.kuhleuski.dao.PersonDao;
import com.kuhleuski.entity.Person;

public class DaoRunner {
    public static void main(String[] args) {

        //saveTest();
        //saveTest();
        //deleteTest(3);
        //updateTest();
        //findAllTest();
    }

    /**
     * SELECT * FROM person
     */
    private static void findAllTest(){
        var persons = PersonDao.getInstance().findAll();
        System.out.println(persons);
    }

    /**
     * UPDATE person
     * SET name = 'VASYA';
     */
    private static void updateTest(){
        var personDao = PersonDao.getInstance();
        var maybePerson = personDao.findById(6);
        System.out.println(maybePerson);

        maybePerson.ifPresent(person -> {
            person.setName("VASYA");
            personDao.update(person);
        });
    }

    /**
     * INSERT INTO person
     * VALUES (name, surname);
     */
    private static void saveTest() {
        var personDao = PersonDao.getInstance();
        var person = new Person();
        person.setName("TestTest");
        person.setSurname("Peetrov");

        var savedPerson = personDao.save(person);
        System.out.println(savedPerson);
    }

    /**
     * удаляет строку из таблицы по индексу
     */
    private static void deleteTest(int idx) {
        var personDao = PersonDao.getInstance();
        var deleteResult = personDao.delete(idx);
        System.out.println(deleteResult);
    }
}
