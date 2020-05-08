package dk.dtu.SoftEngExamProjectG18.General;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {

    protected static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    protected static SimpleDateFormat weekFormatter = new SimpleDateFormat("YYYY-ww");

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static void assertStartEndValid(Date start, Date end) throws IllegalArgumentException {
        if (start == null || end == null) {
            return;
        }

        if (start.compareTo(end) > 0) {
            String output = String.format(
                "The given start week, %s, is after the given end week, %s.",
                Dates.formatWeek(start),
                Dates.formatWeek(end)
            );
            throw new IllegalArgumentException(output);
        }

        assert start == null || end == null || start.compareTo(end) < 0 : "Precondition of assertStartEndValid";
        assert true : "Postcondition of assertStartEndValid";
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static String formatDate(Date d) throws IllegalArgumentException {
        Assertions.assertOrThrow(
            () -> new IllegalArgumentException("Date cannot be null."),
            d != null
        );

        return dateFormatter.format(d);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static String formatWeek(Date d) throws IllegalArgumentException {
        Assertions.assertOrThrow(
            () -> new IllegalArgumentException("Week cannot be null."),
            d != null
        );

        return weekFormatter.format(d);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static Date parseDate(String str) throws ParseException {
        return dateFormatter.parse(str);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static Date parseWeek(String str) throws ParseException {
        return weekFormatter.parse(str);
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static String toDatePattern() {
        return dateFormatter.toPattern();
    }

    /**
     * @author Lasse Lund-Egmose (s194568)
     */
    public static String toWeekPattern() {
        return weekFormatter.toPattern();
    }

}
