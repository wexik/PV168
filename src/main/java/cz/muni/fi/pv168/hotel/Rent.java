package cz.muni.fi.pv168.hotel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Created by wExiik on 5.3.2014.
 */
public class Rent {

    private Long id;
    private BigDecimal price;
    private Room room;
    private Person person;
    private LocalDate startDay;
    private LocalDate expectedEndDay;
    private LocalDateTime realEndDay;
    private int countOfGuestsInRoom;

    public Rent() {
    }

    public Rent(Long id, BigDecimal price, Room room, Person person, LocalDate startDay, LocalDate expectedEndDay, LocalDateTime realEndDay, int countOfGuestsInRoom) {
        this.id = id;
        this.price = price;
        this.room = room;
        this.person = person;
        this.startDay = startDay;
        this.expectedEndDay = expectedEndDay;
        this.realEndDay = realEndDay;
        this.countOfGuestsInRoom = countOfGuestsInRoom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }

    public LocalDate getExpectedEndDay() {
        return expectedEndDay;
    }

    public void setExpectedEndDay(LocalDate expectedEndDay) {
        this.expectedEndDay = expectedEndDay;
    }

    public LocalDateTime getRealEndDay() {
        return realEndDay;
    }

    public void setRealEndDay(LocalDateTime realEndDay) {
        this.realEndDay = realEndDay;
    }

    public int getCountOfGuestsInRoom() {
        return countOfGuestsInRoom;
    }

    public void setCountOfGuestsInRoom(int countOfGuestsInRoom) {
        this.countOfGuestsInRoom = countOfGuestsInRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rent rent = (Rent) o;

        if (countOfGuestsInRoom != rent.countOfGuestsInRoom) return false;
        if (expectedEndDay != null ? !expectedEndDay.equals(rent.expectedEndDay) : rent.expectedEndDay != null)
            return false;
        if (id != null ? !id.equals(rent.id) : rent.id != null) return false;
        if (person != null ? !person.equals(rent.person) : rent.person != null) return false;
        if (price != null ? !price.equals(rent.price) : rent.price != null) return false;
        if (realEndDay != null ? !realEndDay.equals(rent.realEndDay) : rent.realEndDay != null) return false;
        if (room != null ? !room.equals(rent.room) : rent.room != null) return false;
        if (startDay != null ? !startDay.equals(rent.startDay) : rent.startDay != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        result = 31 * result + (person != null ? person.hashCode() : 0);
        result = 31 * result + (startDay != null ? startDay.hashCode() : 0);
        result = 31 * result + (expectedEndDay != null ? expectedEndDay.hashCode() : 0);
        result = 31 * result + (realEndDay != null ? realEndDay.hashCode() : 0);
        result = 31 * result + countOfGuestsInRoom;
        return result;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", price=" + price +
                ", room=" + room +
                ", person=" + person +
                ", startDay=" + startDay +
                ", expectedEndDay=" + expectedEndDay +
                ", realEndDay=" + realEndDay +
                ", countOfGuestsInRoom=" + countOfGuestsInRoom +
                '}';
    }
}
