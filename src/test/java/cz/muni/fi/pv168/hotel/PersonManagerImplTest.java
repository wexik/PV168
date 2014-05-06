package cz.muni.fi.pv168.hotel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Masha Shevchenko
 *         Date: 02.04.14
 */
public class PersonManagerImplTest {

    private PersonManager personManager;

    private EmbeddedDatabase dataSource;

    @Before
    public void setUp() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        dataSource = builder.setType(EmbeddedDatabaseType.HSQL).addScript("testdb-init.sql").build();
        personManager = new PersonManagerImpl(dataSource);
    }

    @After
    public void tearDown() {
        dataSource.shutdown();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullPerson() {
        personManager.createPerson(null);
    }

    @Test
    public void testCreatePersonNotNullId() {
        Person person = PersonCreationUtil.createPerson("Ja", "666", "Praha");
        person.setId(1L);
        try {
            personManager.createPerson(person);
            fail();
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    @Test
    public void testCreate() {
        Person person = PersonCreationUtil.createPerson("ja", "666", "Praha");

        personManager.createPerson(person);
        assertNotNull(person.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullPerson() {
        personManager.updatePerson(null);
    }

    @Test
    public void testUpdatePersonNullId() {
        Person person = PersonCreationUtil.createPerson("Ja", "666", "Praha");
        try {
            personManager.updatePerson(person);
            fail();
        } catch (IllegalArgumentException e) {
            // OK
        }
    }


    @Test
    public void testUpdate() {
        Person person = PersonCreationUtil.createPerson("ja", "666", "Praha");

        personManager.createPerson(person);

        person.setName("ON");
        person.setPhoneNumber("999");
        person.setAddress("Brno");
        personManager.updatePerson(person);

        Person foundPerson = personManager.findPersonById(person.getId());
        assertEquals("ON", foundPerson.getName());
        assertEquals("999", foundPerson.getPhoneNumber());
        assertEquals("Brno", foundPerson.getAddress());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNullId() {
        personManager.deletePerson(null);
    }

    @Test
    public void testDelete() {
        Person person = PersonCreationUtil.createPerson("ja", "666", "Praha");

        personManager.createPerson(person);

        personManager.deletePerson(person);

       assertNull(personManager.findPersonById(person.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindPersonByIdNull() {
        personManager.findPersonById(null);
    }

    @Test
    public void testFindPersonById() {
        Person person = PersonCreationUtil.createPerson("ja", "666", "Praha");

        personManager.createPerson(person);

        Person foundPerson = personManager.findPersonById(person.getId());
        assertEquals("ja", foundPerson.getName());
        assertEquals("666", foundPerson.getPhoneNumber());
        assertEquals("Praha", foundPerson.getAddress());
    }

    @Test
    public void testFindAllPersons() {
        Person person1 = PersonCreationUtil.createPerson("ja", "666", "Praha");

        personManager.createPerson(person1);

        Person person2 = PersonCreationUtil.createPerson("On", "999", "Brno");

        personManager.createPerson(person2);

        assertEquals(2, personManager.findAllPeople().size());
    }
}
