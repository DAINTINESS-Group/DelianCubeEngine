package assess.utils;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class handles anything regarding dates in the assess operator.
 */
public class DatesHandler {
    private final Set<String> levels = new HashSet<>();

    public DatesHandler(List<String> levels) {
        this.levels.addAll(levels);
    }

    /**
     * SQL is set up to read dates as "YY-MM-DD", while ANLTR reads "DD/MM/YY" <br>
     * Note that this probably introduces an additional execution load
     *
     * @param date The date in ANTLR Format
     * @return The date in SQL Format
     */
    public static String formatDates(String date) {
        List<String> dates = Arrays.asList(date.split("/"));
        if (dates.size() > 1) {
            Collections.reverse(dates);
            StringJoiner stringJoiner = new StringJoiner("-");
            dates.forEach(stringJoiner::add);
            return stringJoiner.toString();
        }
        return date;
    }

    /**
     * Identifies the key as a date level or not
     *
     * @param key the predicate we are interested in
     * @return True if the key is contained in date dimension
     */
    public boolean keyIsDate(String key) {
        return levels.contains(key);
    }

    /**
     * Collects the dates from the current date going back based on aggregation
     * level
     *
     * @param date      The full date as a String
     * @param dateLevel The aggregation level
     * @param times     The times the date gets decremented
     * @return A list of dates from date times back, but including date
     * @throws RuntimeException if the date level can not be defined
     */
    public static List<String> decrementDate(String date, String dateLevel, int times) {
        DateDecrementer decrementOperator = new DateDecrementer(dateLevel, date);
        return Stream
                .iterate(decrementOperator.startingDate, decrementOperator.decrementer)
                .limit(times)
                .map(decrementOperator.stringFormatter)
                .collect(Collectors.toList());
    }

    private static class DateDecrementer {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        UnaryOperator<LocalDate> decrementer;
        LocalDate startingDate;
        Function<LocalDate, String> stringFormatter;

        DateDecrementer(String dateLevel, String date) {
            switch (dateLevel) {
                case "day":
                    decrementByDay(date);
                    return;
                case "month":
                    decrementByMonth(date);
                    return;
                case "year":
                    decrementByYear(date);
                    return;
                default:
                    throw new RuntimeException("Couldn't identify date level");
            }
        }

        private void decrementByDay(String date) {
            decrementer = d -> d.minusDays(1);
            startingDate = LocalDate.parse(date, formatter).minusDays(1);
            stringFormatter = LocalDate::toString;
        }

        private void decrementByMonth(String date) {
            decrementer = d -> d.minusMonths(1);
            startingDate = LocalDate.parse("01/" + date, formatter).minusMonths(1);
            stringFormatter = d -> d.toString().substring(0, 7);
        }

        private void decrementByYear(String date) {
            decrementer = d -> d.minusYears(1);
            startingDate = LocalDate.parse("01/01/" + date, formatter).minusYears(1);
            stringFormatter = d -> d.toString().substring(0, 4);
        }
    }
}
