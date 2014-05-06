package cz.muni.fi.pv168.hotel.util;

import java.util.List;

/**
 * @author kurocenko
 */
public final class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    public static boolean isEmptyOrNull(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static String printDelimited(List<String> messages, String delimiter) {
        StringBuilder sb = new StringBuilder();

        String d = "";

        for (String message : messages) {
            sb.append(d);
            sb.append(message);
            d = delimiter;
        }

        return sb.toString();

    }
}
