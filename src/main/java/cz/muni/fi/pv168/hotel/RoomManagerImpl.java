package cz.muni.fi.pv168.hotel;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by wExiik on 5.3.2014.
 */
public class RoomManagerImpl implements RoomManager {

    //final static Logger log = LoggerFactory.getLogger(RoomManagerImpl.class);

    private final JdbcTemplate jdbc;
    private RowMapper<Room> roomMapper = (rs, rowNum) ->
            new Room(rs.getLong("id"), rs.getInt("capacity"), rs.getInt("number"), rs.getBigDecimal("pricePerDay"));

    public RoomManagerImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }


    @Override
    public void deleteRoom(Room room) throws RoomException {
        //log.debug("deleteRoom({})", room);
        try{
            jdbc.update("DELETE FROM rooms WHERE id=?", room.getId());
        } catch (Exception e){
            //log.error
        }

    }

    @Override
    public void updateRoom(Room room) throws RoomException {
        //log.debug("updateRoom({})", room);
        if (room == null) {
            //log.error
            throw new IllegalArgumentException("Object room is null!");
        }

        if(room.getPricePerDay() == null){
            //log.error
            throw new IllegalArgumentException("price is null");
        }

        if(room.getCapacity() <= 0){
            //log.error
            throw new IllegalArgumentException("bad capacity");
        }

        try{
            jdbc.update("UPDATE rooms set capacity=?, pricePerDay=?, number=? WHERE id=?",
                    room.getCapacity(), room.getPricePerDay(), room.getNumber(), room.getId());
        } catch (Exception e){
            //log.error
        }

    }


    @Override
    public List<Room> findAllRooms() throws RoomException {
        //log.debug("findAllRooms()");
        return jdbc.query("SELECT * FROM rooms", roomMapper);
    }

    @Override
    public Room findRoomById(Long id) throws RoomException {
        //log.debug("getRoomByID({})", i);
        try {
            return jdbc.queryForObject("SELECT id, capacity, number, pricePerDay FROM rooms WHERE id=?", roomMapper, id);
        } catch (Exception e) {
            //log.error
            return null;
        }

    }

    @Override
    public void createRoom(Room room) throws RoomException, IllegalArgumentException{
        if (room == null) {
            //log.error("Object room is null");
            throw new IllegalArgumentException("Object room is null");
        }

        if (room.getId() != null) {
            //log.error("bad id");
            throw new IllegalArgumentException("bad argument id");
        }

        if (room.getCapacity() <= 0) {
            //log.error("bad capacity");
            throw new IllegalArgumentException("bad argument capacity");
        }

        if (room.getPricePerDay() == null) {
            //log.error("bad pricePerDay");
            throw new IllegalArgumentException("bad argument pricePerDay");
        }

        //log.debug("createRoom({})", room);
        SimpleJdbcInsert insertRoom = new SimpleJdbcInsert(jdbc)
                .withTableName("rooms").usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("capacity", room.getCapacity())
                .addValue("number", room.getNumber())
                .addValue("pricePerDay", room.getPricePerDay());

        Number id = insertRoom.executeAndReturnKey(parameters);
        room.setId(id.longValue());
    }
}
