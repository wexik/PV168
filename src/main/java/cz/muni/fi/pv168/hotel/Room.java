package cz.muni.fi.pv168.hotel;

import java.math.BigDecimal;

/**
 *Rooms entity class
 */
public class Room {

    private Long id;
    private int capacity;
    private BigDecimal pricePerDay;

    public Room() {
    }

    public Room(Long id, int capacity, BigDecimal pricePerDay) {
        this.id = id;
        this.capacity = capacity;
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
        int result = id.intValue();
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
