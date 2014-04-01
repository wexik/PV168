package cz.muni.fi.pv168.hotel;

/**
 * @author Masha Shevchenko
 *         Date: 02.04.14
 */
public class PersistenceException extends RuntimeException {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
