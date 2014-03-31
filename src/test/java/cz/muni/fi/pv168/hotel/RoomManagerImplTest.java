package cz.muni.fi.pv168.hotel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by wExiik on 5.3.2014.
 */
public class RoomManagerImplTest {

    private RoomManagerImpl manager;



    @Before
    public void setUp() throws Exception {
        manager = new RoomManagerImpl(Main.getDateSource());
    }

    @Test
    public void testCreateRoom() throws RoomException {
        Room room = new Room(123, 4, new BigDecimal("70.100"));

        manager.createRoom(room);

        int roomId = room.getId();
        assertTrue(roomId != 0);
        Room newRoom = manager.findRoomById(roomId);
        assertEquals(room, newRoom);
    }

    @Test
    public void testCreateRoomWithWrongArguments() throws RoomException {
        try {
            manager.createRoom(null);
            fail("createRoom(null)");
        } catch (IllegalArgumentException e) {
            //OK
        }

        Room room = new Room(-1, 4, new BigDecimal("30.10"));
        try {
            manager.createRoom(room);
            fail("bad ID");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(123, -1, new BigDecimal("30.1"));
        try {
            manager.createRoom(room);
            fail("bad capacity");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(123, 0, new BigDecimal("30.1"));
        try {
            manager.createRoom(room);
            fail("bad capacity");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(123, 4, null);
        try {
            manager.createRoom(room);
            fail("bad price");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(1, 4, new BigDecimal("30.2"));
        manager.createRoom(room);
        Room newRoom = manager.findRoomById(room.getId());
        assertNotNull(newRoom);
    }

    @Test
    public void testDeleteRoom() throws RoomException {
        Room room1 = new Room(34, 4, new BigDecimal("40.3"));
        Room room2 = new Room(57, 6, new BigDecimal("50.4"));

        manager.createRoom(room1);
        manager.createRoom(room2);

        assertNotNull(manager.findRoomById(room1.getId()));
        assertNotNull(manager.findRoomById(room2.getId()));

        manager.deleteRoom(room1);

        assertNull(manager.findRoomById(room1.getId()));
        assertNotNull(manager.findRoomById(room2.getId()));

    }

    @Test
    public void testUpdateRoomWithWrongArguments() throws Exception {
        try {
            manager.updateRoom(null);
            fail();
        } catch (IllegalArgumentException e){

        }

        Room room = new Room(-1, 4, new BigDecimal("40.3"));
        try {
            manager.updateRoom(room);
        } catch (IllegalArgumentException e) {

        }

        room = new Room(123, -1, new BigDecimal("30.1"));
        try {
            manager.createRoom(room);
            fail("bad capacity");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(123, 0, new BigDecimal("30.1"));
        try {
            manager.createRoom(room);
            fail("bad capacity");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(123, 4, null);
        try {
            manager.createRoom(room);
            fail("bad price");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(55, 10, new BigDecimal("30.200"));
        manager.updateRoom(room);
        Room newRoom = manager.findRoomById(room.getId());
        assertEquals(room, newRoom);

    }

    @Test
    public void testFindRoomById() throws Exception {
        Room room = new Room(55, 10, new BigDecimal("30.200"));
        manager.createRoom(room);

        assertEquals(room, manager.findRoomById(room.getId()));
        assertNull(manager.findRoomById(90000000));
    }


}