package cz.muni.fi.pv168.hotel.util;

import java.math.BigDecimal;
import java.math.MathContext;
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

    public static boolean isDecimal(String str) {
        try {
            new BigDecimal(str, new MathContext(2));
        } catch (NumberFormatException e) {
            return false;
        } catch (ArithmeticException e) {
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
