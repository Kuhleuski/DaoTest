package com.kuhleuski.dao;

import com.kuhleuski.entity.Person;
import com.kuhleuski.exception.DaoException;
import com.kuhleuski.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDao {

    private static final PersonDao INSTANSE = new PersonDao();
    private static final String DELETE_SQL = """
            DELETE FROM person
            WHERE id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO person (name, surname)
            VALUES (?,?);
                           
            """;

    private static final String UPDATE_SQL = """
            UPDATE person
            SET name = ?,
            surname = ?
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id,
                name,
                surname
            FROM person
                        
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private PersonDao() {
    }

    /**
     * SELECT всех строк в таблице и всех полей
     * */
    public List<Person> findAll() {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            var resultSet = preparedStatement.executeQuery();
            List<Person> persons = new ArrayList<>();
            while (resultSet.next()) {
                persons.add(buildPerson(resultSet));
            }
            return persons;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    /**
     * Находит строку в таблице по Айдишнику
     */
    public Optional<Person> findById(int id) {
        try (var connection = ConnectionManager.open();
             var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            prepareStatement.setInt(1, id);
            Person person = null;
            var resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                person = new Person(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname")
                );
            }
            return Optional.ofNullable(person);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    /**
     * Обновляет/вставляет в строку данные, но перечислить нужно все поля
     */
    public void update(Person person) {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getSurname());
            preparedStatement.setInt(3, person.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    /**
     * Добавляет новую строку в таблицу person
     */
    public Person save(Person person) {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getSurname());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                person.setId(generatedKeys.getInt(1));
            }
            return person;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    /**
     * Удаляет строку из таблицы person
     */
    public boolean delete(int id) {
        try (var connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public static PersonDao getInstance() {
        return INSTANSE;
    }

    /**
     * метод который поставляет готовый резалтСет в ArrayList persons в метод findAll
     * */
    private Person buildPerson(ResultSet resultSet) throws SQLException {
        return new Person(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("surname")
        );
    }
}

