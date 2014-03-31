package cz.muni.fi.pv168.hotel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 *Rooms entity class
 */
public class Room {

    private int id;
    private int capacity;
    private BigDecimal pricePerDay;

    public Room() {
    }

    public Room(int id, int capacity, BigDecimal pricePerDay) {
        this.id = id;
        this.capacity = capacity;
        this.pricePerDay = pricePerDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (capacity != room.capacity) return false;
        if (id != room.id) return false;
        if (!pricePerDay.equals(room.pricePerDay)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + capacity;
        result = 31 * result + pricePerDay.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", pricePerDay=" + pricePerDay +
                '}';
    }


}
