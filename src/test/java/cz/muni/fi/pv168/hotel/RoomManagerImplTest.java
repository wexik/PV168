package cz.muni.fi.pv168.hotel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by wExiik on 5.3.2014.
 */
public class RoomManagerImplTest {

    private RoomManagerImpl manager;



    private EmbeddedDatabase dataSource;

    @Before
    public void setUp() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        dataSource = builder.setType(EmbeddedDatabaseType.HSQL).addScript("testdb-init.sql").build();
        manager = new RoomManagerImpl(dataSource);
    }

    @After
    public void tearDown() {
        dataSource.shutdown();
    }

    @Test
    public void testCreateRoom() throws RoomException {
        Room room = new Room(null, 4, new BigDecimal("70.100"));

        manager.createRoom(room);

        Long roomId = room.getId();
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

        Room room = new Room(1L, 4, new BigDecimal("30.10"));
        try {
            manager.createRoom(room);
            fail("bad ID");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(null, -1, new BigDecimal("30.1"));
        try {
            manager.createRoom(room);
            fail("bad capacity");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(null, 0, new BigDecimal("30.1"));
        try {
            manager.createRoom(room);
            fail("bad capacity");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(null, 4, null);
        try {
            manager.createRoom(room);
            fail("bad price");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(null, 4, new BigDecimal("30.2"));
        manager.createRoom(room);
        Room newRoom = manager.findRoomById(room.getId());
        assertNotNull(newRoom);
    }

    @Test
    public void testDeleteRoom() throws RoomException {
        Room room1 = new Room(null, 4, new BigDecimal("40.3"));
        Room room2 = new Room(null, 6, new BigDecimal("50.4"));

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

        Room room = new Room(-1L, 4, new BigDecimal("40.3"));
        try {
            manager.updateRoom(room);
        } catch (IllegalArgumentException e) {

        }

        room = new Room(1L, -1, new BigDecimal("30.1"));
        try {
            manager.createRoom(room);
            fail("bad capacity");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(1L, 0, new BigDecimal("30.1"));
        try {
            manager.createRoom(room);
            fail("bad capacity");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(1L, 4, null);
        try {
            manager.createRoom(room);
            fail("bad price");
        } catch (IllegalArgumentException e) {

        }

        room = new Room(null, 10, new BigDecimal("30.200"));
        manager.createRoom(room);

        room = new Room(room.getId(), 12, new BigDecimal("40.300"));
        manager.updateRoom(room);
        Room newRoom = manager.findRoomById(room.getId());
        assertEquals(room, newRoom);

    }

    @Test
    public void testFindRoomById() throws Exception {
        Room room = new Room(null, 10, new BigDecimal("30.200"));
        manager.createRoom(room);

        assertEquals(room, manager.findRoomById(room.getId()));
        assertNull(manager.findRoomById(Long.MAX_VALUE));
    }


}