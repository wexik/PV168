package cz.muni.fi.pv168.hotel.gui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author mashka
 */
public final class ResourceBundleProvider {

    private static ResourceBundle resourceBundle = null;

    public static String getMessage(String key) {
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle("messages");
        }

        return resourceBundle.getString(key);
    }

    public static String getMessage(String key, Object ... params) {
        String message = getMessage(key);
        return MessageFormat.format(message, params);
    }

}
