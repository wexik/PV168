package cz.muni.fi.pv168.hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wExiik on 5.3.2014.
 */
public class PersonManagerImpl extends NamedParameterJdbcTemplate implements PersonManager {

    Logger logger = LoggerFactory.getLogger(PersonManagerImpl.class);

    public static final String ATTR_PERSON_NAME = "PERSON_NAME";
    public static final String ATTR_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String ATTR_ADDRESS = "ADDRESS";
    public static final String ATTR_ID = "ID";
    public static final String TABLE_NAME = "PERSONS";

    private DataSource dataSource;

    public PersonManagerImpl(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void createPerson(Person person) {

        logger.info("Creating new person {}", person);

        if (person == null) {
            throw new IllegalArgumentException("Person is null");
        }
        if (person.getId() != null) {
            throw new IllegalArgumentException("Person ID is not null");
        }

        Map<String, Object> params = new HashMap<>();
        params.put(ATTR_PERSON_NAME, person.getName());
        params.put(ATTR_PHONE_NUMBER, person.getPhoneNumber());
        params.put(ATTR_ADDRESS, person.getAddress());

        SimpleJdbcInsert simpleInsert =
                new SimpleJdbcInsert(dataSource)
                        .withTableName(TABLE_NAME)
                        .usingColumns(ATTR_PERSON_NAME, ATTR_PHONE_NUMBER, ATTR_ADDRESS)
                        .usingGeneratedKeyColumns(ATTR_ID);
        Number id = simpleInsert.executeAndReturnKey(params);

        person.setId(id.longValue());
    }

    @Override
    public void deletePerson(Person person) {

        logger.info("Removing person {}", person);

        if (person == null) {
            throw new IllegalArgumentException("Person is null");
        }
        if (person.getId() == null) {
            throw new IllegalArgumentException("Person ID is null");
        }

        int affectedRows = getJdbcOperations().update("DELETE FROM " + TABLE_NAME + " WHERE " + ATTR_ID + "=?", person.getId());
        if (affectedRows != 1) {
            logger.error("Delete statement affected {} rows, expected 1, while removing person {}", affectedRows, person);
            throw new PersistenceException("Delete statement affected " + affectedRows + " rows, expected 1, while removing person " + person.toString());
        }
    }

    @Override
    public List<Person> findAllPeople() {

        logger.info("Retrieving all people");

        List<Map<String, Object>> maps = getJdbcOperations().queryForList(
                "SELECT " + ATTR_ID + ", " + ATTR_PERSON_NAME + ", " + ATTR_PHONE_NUMBER + ", " + ATTR_ADDRESS +
                " FROM " + TABLE_NAME
        );

        return getPeopleFromMap(maps);
    }

    private List<Person> getPeopleFromMap(List<Map<String, Object>> maps) {
        List<Person> result = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Person person = new Person();
            person.setId((Long)map.get(ATTR_ID));
            person.setName((String)map.get(ATTR_PERSON_NAME));
            person.setPhoneNumber((String)map.get(ATTR_PHONE_NUMBER));
            person.setAddress((String)map.get(ATTR_ADDRESS));

            result.add(person);
        }

        return result;
    }


    @Override
    public Person findPersonById(Long id) {

        logger.info("Searching for person with id {}", id);

        if (id == null) {
            throw new IllegalArgumentException("ID is null");
        }

        Person person = null;
        try {
            person = getJdbcOperations().queryForObject(
                    "SELECT " + ATTR_ID + ", " + ATTR_PERSON_NAME + ", " + ATTR_PHONE_NUMBER + ", " + ATTR_ADDRESS +
                            " FROM " + TABLE_NAME + " WHERE " + ATTR_ID + "=?", new PersonMapper(), id
            );
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No person with ID " + id + " was found", e);
        }

        return person;
    }

    @Override
    public void updatePerson(Person person) {

        logger.info("Updating person {}", person);

        if (person == null) {
            throw new IllegalArgumentException("Person is null");
        }
        if (person.getId() == null) {
            throw new IllegalArgumentException("Person ID is null");
        }

        int affectedRows = getJdbcOperations().update(
                "UPDATE " + TABLE_NAME + " SET " + ATTR_PERSON_NAME + "=?, " + ATTR_PHONE_NUMBER + "=?, " + ATTR_ADDRESS + "=? WHERE " + ATTR_ID + "=?",
                person.getName(), person.getPhoneNumber(), person.getAddress(), person.getId());

        if (affectedRows != 1) {
            logger.error("Update statement affected {} rows, expected 1, while updating person {}", affectedRows, person);
            throw new PersistenceException("Update statement affected " + affectedRows + " rows, expected 1, while updating person " + person.toString());
        }
    }

    private static class PersonMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.setId(rs.getLong(ATTR_ID));
            person.setName(rs.getString(ATTR_PERSON_NAME));
            person.setPhoneNumber(rs.getString(ATTR_PHONE_NUMBER));
            person.setAddress(rs.getString(ATTR_ADDRESS));

            return person;
        }
    }
}
