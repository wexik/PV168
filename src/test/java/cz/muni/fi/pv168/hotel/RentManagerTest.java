package cz.muni.fi.pv168.hotel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * @author Masha Shevchenko
 *         Date: 02.04.14
 */
public class RentManagerTest {

    private RentManagerImpl rentManager;
    private RoomManager roomManager;
    private PersonManager personManager;

    private final LocalDate startDay = LocalDate.of(2014,4,1);
    private final LocalDate expectedEnd = LocalDate.of(2014,4,5);
    private final LocalDateTime realEnd = LocalDateTime.of(2014,4,6,22,30);

    private EmbeddedDatabase dataSource;

    @Before
    public void setUp() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        dataSource = builder.setType(EmbeddedDatabaseType.HSQL).addScript("init.sql").build();
        rentManager = new RentManagerImpl(dataSource);
        roomManager = new RoomManagerImpl(dataSource);
        personManager = new PersonManagerImpl(dataSource);
        rentManager.setPersonManager(personManager);
        rentManager.setRoomManager(roomManager);
    }

    @After
    public void tearDown() {
        dataSource.shutdown();
    }

    @Test
    public void testCreateRent() {
        Person person = PersonCreationUtil.createPerson("Name", "666", "address");
        personManager.createPerson(person);

        Room room = new Room(null, 2, BigDecimal.TEN);
        roomManager.createRoom(room);

        Rent rent = new Rent(null, BigDecimal.TEN, room, person, startDay, expectedEnd, realEnd, 1);
        rentManager.createRent(rent);

        assertEquals(rent, rentManager.findRentById(rent.getId()));
    }

    @Test
    public void testUpdateRent() {
        Person person = PersonCreationUtil.createPerson("Name", "666", "address");
        personManager.createPerson(person);

        Room room = new Room(null, 2, BigDecimal.TEN);
        roomManager.createRoom(room);

        Rent rent = new Rent(null, BigDecimal.TEN, room, person, startDay, expectedEnd, realEnd, 1);
        rentManager.createRent(rent);

        rent.setCountOfGuestsInRoom(2);
        rent.setExpectedEndDay(expectedEnd);
        rent.setPrice(BigDecimal.ONE);
        rent.setRealEndDay(realEnd);
        rent.setStartDay(startDay);

        rentManager.updateRent(rent);

        Rent foundRent = rentManager.findRentById(rent.getId());
        assertEquals(2, foundRent.getCountOfGuestsInRoom());
        assertEquals(expectedEnd, foundRent.getExpectedEndDay());
        assertEquals(realEnd, foundRent.getRealEndDay());
        assertEquals(startDay, foundRent.getStartDay());
        assertEquals(BigDecimal.ONE, foundRent.getPrice());

    }

    @Test
    public void testFindByIdRent() {
        Person person = PersonCreationUtil.createPerson("Name", "666", "address");
        personManager.createPerson(person);

        Room room = new Room(null, 2, BigDecimal.TEN);
        roomManager.createRoom(room);

        Rent rent = new Rent(null, BigDecimal.TEN, room, person, startDay, expectedEnd, realEnd, 1);
        rentManager.createRent(rent);

        assertNotNull(rentManager.findRentById(rent.getId()));
    }

    @Test
    public void testDeleteRent() {
        Person person = PersonCreationUtil.createPerson("Name", "666", "address");
        personManager.createPerson(person);

        Room room = new Room(null, 2, BigDecimal.TEN);
        roomManager.createRoom(room);

        Rent rent = new Rent(null, BigDecimal.TEN, room, person, startDay, expectedEnd, realEnd, 1);
        rentManager.createRent(rent);

        rentManager.deleteRent(rent);

        assertNull(rentManager.findRentById(rent.getId()));
    }
}
