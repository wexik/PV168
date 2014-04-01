package cz.muni.fi.pv168.hotel;

import java.util.List;

/**
 * Created by wExiik on 5.3.2014.
 */
public interface PersonManager {

    void createPerson(Person person);

    void deletePerson(Person person);

    List<Person> findAllPeople();

    Person findPersonById(Long id);

    void updatePerson(Person person);

}
