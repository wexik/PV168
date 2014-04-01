package cz.muni.fi.pv168.hotel;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * @author Masha Shevchenko
 *         Date: 02.04.14
 */
public class RentManagerTest {

    private RentManager rentManager;
    private RoomManager roomManager;
    private PersonManager personManager;

    @Before
    public void setUp() {
        rentManager = new RentManagerImpl(Main.getDateSource());
        roomManager = new RoomManagerImpl(Main.getDateSource());
        personManager = new PersonManagerImpl(Main.getDateSource());
    }

    @Test
    public void testCreateRent() {
        Person person = PersonCreationUtil.createPerson("Name", "666", "address");
        person.setId(1L);

        Room room = new Room(2, 2, BigDecimal.TEN);

        Rent rent = new Rent(null, BigDecimal.TEN, room, person, LocalDate.MIN, LocalDate.MIN, LocalDateTime.MIN, 1);
        rentManager.createRent(rent);

        assertEquals(rent, rentManager.findRentById(rent.getId()));
    }

    @Test
    public void testUpdateRent() {
        Person person = PersonCreationUtil.createPerson("Name", "666", "address");
        person.setId(1L);

        Room room = new Room(2, 2, BigDecimal.TEN);

        Rent rent = new Rent(null, BigDecimal.TEN, room, person, LocalDate.MIN, LocalDate.MIN, LocalDateTime.MIN, 1);
        rentManager.createRent(rent);

        rent.setCountOfGuestsInRoom(2);
        rent.setExpectedEndDay(LocalDate.MAX);
        rent.setPrice(BigDecimal.ONE);
        rent.setRealEndDay(LocalDateTime.MAX);
        rent.setStartDay(LocalDate.MAX);

        rentManager.updateRent(rent);

        Rent foundRent = rentManager.findRentById(rent.getId());
        assertEquals(2, foundRent.getCountOfGuestsInRoom());
        assertEquals(LocalDate.MAX, foundRent.getExpectedEndDay());
        assertEquals(LocalDateTime.MAX, foundRent.getRealEndDay());
        assertEquals(LocalDate.MAX, foundRent.getStartDay());
        assertEquals(BigDecimal.ONE, foundRent.getPrice());

    }

    @Test
    public void testFindByIdRent() {
        Person person = PersonCreationUtil.createPerson("Name", "666", "address");
        person.setId(1L);

        Room room = new Room(2, 2, BigDecimal.TEN);

        Rent rent = new Rent(null, BigDecimal.TEN, room, person, LocalDate.MIN, LocalDate.MIN, LocalDateTime.MIN, 1);
        rentManager.createRent(rent);

        assertNotNull(rentManager.findRentById(rent.getId()));
    }

    @Test
    public void testDeleteRent() {
        Person person = PersonCreationUtil.createPerson("Name", "666", "address");
        person.setId(1L);

        Room room = new Room(2, 2, BigDecimal.TEN);

        Rent rent = new Rent(null, BigDecimal.TEN, room, person, LocalDate.MIN, LocalDate.MIN, LocalDateTime.MIN, 1);
        rentManager.createRent(rent);

        rentManager.deleteRent(rent);

        assertNull(rentManager.findRentById(rent.getId()));
    }
}
