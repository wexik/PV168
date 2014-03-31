package cz.muni.fi.pv168.hotel;

import java.util.List;

/**
 * Created by wExiik on 5.3.2014.
 */
public interface PersonManager {

    void createPerson(Person person) throws PersonException;

    void deletePerson(Person person) throws PersonException;

    List<Person> findAllPersons() throws PersonException;

    Person findPersonById(int id) throws PersonException;

    void updatePerson(Person person) throws PersonException;

}
