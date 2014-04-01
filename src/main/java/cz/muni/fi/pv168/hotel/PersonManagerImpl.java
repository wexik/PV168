package cz.muni.fi.pv168.hotel;

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

    public static final String ATTR_PERSON_NAME = "person_name";
    public static final String ATTR_PHONE_NUMBER = "phone_number";
    public static final String ATTR_ADDRESS = "address";
    public static final String ATTR_ID = "id";
    private DataSource dataSource;

    public PersonManagerImpl(DataSource dataSource) {
        super(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void createPerson(Person person) {
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
                        .withTableName("person")
                        .usingColumns(ATTR_PERSON_NAME, ATTR_PHONE_NUMBER, ATTR_ADDRESS)
                        .usingGeneratedKeyColumns(ATTR_ID);
        Number id = simpleInsert.executeAndReturnKey(params);

        person.setId(id.longValue());
    }

    @Override
    public void deletePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person is null");
        }
        if (person.getId() == null) {
            throw new IllegalArgumentException("Person ID is null");
        }

        int affectedRows = getJdbcOperations().update("DELETE FROM person WHERE id=?", person.getId());
        if (affectedRows != 1) {
            throw new PersistenceException("Deleted rows " + affectedRows);
        }
    }

    @Override
    public List<Person> findAllPeople() {
        List<Map<String, Object>> maps = getJdbcOperations().queryForList("SELECT id, person_name, phone_number, address FROM person");
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
        if (id == null) {
            throw new IllegalArgumentException("ID is null");
        }

        return getJdbcOperations().queryForObject("SELECT id, person_name, phone_number, address FROM person WHERE id=?", new PersonMapper(), id);
    }

    @Override
    public void updatePerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person is null");
        }
        if (person.getId() == null) {
            throw new IllegalArgumentException("Person ID is null");
        }

        Map<String, Object> params = new HashMap<>();
        params.put(ATTR_ID, person.getId());
        params.put(ATTR_PERSON_NAME, person.getName());
        params.put(ATTR_PHONE_NUMBER, person.getPhoneNumber());
        params.put(ATTR_ADDRESS, person.getAddress());

        int affectedRows = getJdbcOperations().update("UPDATE person SET person_name=:person_name, phone_number=:phone_number, address=:address WHERE id=:id", params);
        if (affectedRows != 1) {
            throw new PersistenceException("Update rows " + affectedRows);
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
