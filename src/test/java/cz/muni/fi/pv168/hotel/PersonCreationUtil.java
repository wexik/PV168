package cz.muni.fi.pv168.hotel;

/**
 * @author Masha Shevchenko
 *         Date: 02.04.14
 */
public final class PersonCreationUtil {

    private PersonCreationUtil() {
        throw new AssertionError();
    }

    public static Person createPerson(String name, String phone, String address) {
        Person person = new Person();
        person.setName(name);
        person.setPhoneNumber(phone);
        person.setAddress(address);

        return person;
    }
}
