package cz.muni.fi.pv168.hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by wExiik on 9.3.2014.
 */
public class RentManagerImpl implements RentManager {

    final static Logger log = LoggerFactory.getLogger(RentManagerImpl.class);
    private JdbcTemplate jdbc;
    private RoomManager roomManager;
    private PersonManager personManager;
    private RowMapper<Rent> rentRowMapper = (rs, rowNum) -> {
        try {
            return new Rent(rs.getLong("id"), rs.getBigDecimal("price"), roomManager.findRoomById(rs.getLong("roomId")),
                    personManager.findPersonById(rs.getLong("personId")),
                        rs.getDate("startDay").toLocalDate(), rs.getDate("expectedEndDay").toLocalDate(),
                            rs.getTimestamp("realEndDay").toLocalDateTime(), rs.getInt("countOGuestsInRoom"));
        } catch (RoomException e) {
            log.error("cannot find room", e);
        }
        return null;
    };

    public RentManagerImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public void setRoomManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }

    @Override
    public List<Rent> findRentsForPerson(Person person) {
        return jdbc.query("SELECT * FROM rents WHERE personId=?",
                (rs, rowNum) -> {
                    Long roomId = rs.getLong("roomId");
                    Room room = null;
                    try {
                        room = roomManager.findRoomById(roomId);
                    } catch (RoomException e) {
                        log.error("cannot find room", e);
                    }
                    LocalDate startDay = rs.getDate("startDay").toLocalDate();
                    LocalDate expcectedEndDay = rs.getDate("expectedEndDay").toLocalDate();
                    Timestamp ts = rs.getTimestamp("realeEndDay");
                    LocalDateTime realEndDay = ts == null ? null : ts.toLocalDateTime();
                    BigDecimal price = rs.getBigDecimal("price");
                    int countOfGuestsInRoom = rs.getInt("countOGuestsInRoom");
                    return new Rent(rs.getLong("id"), price, room, person, startDay, expcectedEndDay, realEndDay, countOfGuestsInRoom);
                },
                person.getId());

    }

    @Override
    public void createRent(Rent rent) {
            SimpleJdbcInsert insertRent = new SimpleJdbcInsert(jdbc).withTableName("rents").usingGeneratedKeyColumns("id");
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("roomId", rent.getRoom().getId())
                        .addValue("personId", rent.getPerson().getId())
                            .addValue("startday", toSQLDate(rent.getStartDay()))
                                .addValue("expectedendday", toSQLDate(rent.getExpectedEndDay()))
                                    .addValue("realendday", toSQLTimestamp(rent.getRealEndDay()))
                                        .addValue("price", rent.getPrice())
                                            .addValue("countOGuestsInRoom", rent.getCountOfGuestsInRoom());
            Number id = insertRent.executeAndReturnKey(parameters);
            rent.setId(id.longValue());
        }


    @Override
    public void deleteRent(Rent rent) {
        jdbc.update("DELETE FROM rents WHERE id=?", rent.getId());
    }

    @Override
    public List<Rent> findAllRents() {
        return jdbc.query("select * from rents", rentRowMapper);
    }

    @Override
    public List<Rent> findRentsForRoom(Room room) {
        return jdbc.query("select * from rents where roomId =?", rentRowMapper, room.getId());
    }

    @Override
    public Rent findRentById(Long id) {
        Rent rent = null;
        try {
            rent = jdbc.queryForObject("SELECT * FROM rents WHERE id=?", rentRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            // LOG exception, do nothing to return null
        }
        return rent;
    }

    @Override
    public void updateRent(Rent rent) {
        jdbc.update("UPDATE rents set roomId=?, personId=?, startDay=?, expectedEndDay=?, realEndDay=?," +
                        "price=?, countOGuestsInRoom=? WHERE id=?",
                            rent.getRoom().getId(), rent.getPerson().getId(), toSQLDate(rent.getStartDay()),
                                toSQLDate(rent.getExpectedEndDay()), toSQLTimestamp(rent.getRealEndDay()),
                                    rent.getPrice(), rent.getCountOfGuestsInRoom(), rent.getId());
    }

    private Date toSQLDate(LocalDate localDate) {
        if (localDate == null) return null;
        return new Date(ZonedDateTime.of(localDate.atStartOfDay(), ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    private Timestamp toSQLTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return new Timestamp(ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}
