package dk.dtu.SoftEngExamProjectG18.General;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

    protected static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    protected static SimpleDateFormat weekFormatter = new SimpleDateFormat("yyyy-ww");

    public static String formatDate(Date d) {
        return dateFormatter.format(d);
    }

    public static String formatWeek(Date d) {
        return weekFormatter.format(d);
    }

    public static Date parseDate(String str) throws ParseException {
        return dateFormatter.parse(str);
    }

    public static Date parseWeek(String str) throws ParseException {
        return weekFormatter.parse(str);
    }

    public static String toDatePattern() {
        return dateFormatter.toPattern();
    }

    public static String toWeekPattern() {
        return weekFormatter.toPattern();
    }

}
