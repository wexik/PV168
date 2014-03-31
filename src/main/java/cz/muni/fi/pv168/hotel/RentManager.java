package cz.muni.fi.pv168.hotel;

import java.util.List;

/**
 * Created by wExiik on 9.3.2014.
 */
public interface RentManager {

    void createRent(Rent rent);

    void deleteRent(Rent rent);

    List<Rent> findAllRents();

    List<Rent> findRentsForPerson(Person person);

    List<Rent> findRentsForRoom(Room room);

    Rent findRentById(Long id);

    void updateRent(Rent rent);

}
