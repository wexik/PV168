package cz.muni.fi.pv168.hotel;

import java.math.BigDecimal;

/**
 *Rooms entity class
 */
public class Room {

    private Long id;
    private int capacity;
    private int number;
    private BigDecimal pricePerDay;

    public Room() {
    }

    public Room(Long id, int capacity, int number, BigDecimal pricePerDay) {
        this.id = id;
        this.capacity = capacity;
        this.number = number;
        this.pricePerDay = pricePerDay;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (capacity != room.capacity) return false;
        if (number != room.number) return false;
        if (id != null ? !id.equals(room.id) : room.id != null) return false;
        if (pricePerDay != null ? !pricePerDay.equals(room.pricePerDay) : room.pricePerDay != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + capacity;
        result = 31 * result + number;
        result = 31 * result + (pricePerDay != null ? pricePerDay.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", number=" + number +
                ", pricePerDay=" + pricePerDay +
                '}';
    }
}
