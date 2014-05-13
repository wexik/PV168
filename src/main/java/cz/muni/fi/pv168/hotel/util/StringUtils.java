package cz.muni.fi.pv168.hotel.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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

    public static boolean isDate(String str) {
        try {
            SimpleDateFormat sdf = getSimpleDateFormat();
            sdf.parse(str);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }

    public static LocalDate parseDate(String str) {
        try {
            return LocalDate.parse(str);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Failed to parse date " + str);
        }
    }

    public static int getDayDifference(LocalDate date1, LocalDate date2) {

        return (int) ChronoUnit.DAYS.between(date1, date2);
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
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
