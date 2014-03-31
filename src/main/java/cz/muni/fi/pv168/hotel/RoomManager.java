package cz.muni.fi.pv168.hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by wExiik on 5.3.2014.
 */
public interface RoomManager {

    void createRoom(Room room) throws RoomException;

    void deleteRoom(Room room) throws RoomException;

    List<Room> findAllRooms() throws RoomException;

    Room findRoomById(int i) throws RoomException;

    void updateRoom(Room room) throws RoomException;

}
