package assess.utils;

import cubemanager.cubebase.Level;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.*;
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
     * TODO: Perhaps by using the LocalDate object we can achieve a better solution
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        switch (dateLevel) {
            case "day":
                return Stream.iterate(
                                LocalDate.parse(date, formatter).minusDays(1),
                                d -> d.minusDays(1))
                        .limit(times)
                        .map(LocalDate::toString)
                        .collect(Collectors.toList());
            case "month":
                // The trick with this is that we have to set a default day
                // when the day is missing. Otherwise, it doesn't get parsed
                return Stream.iterate(
                                LocalDate.parse("01/" + date, formatter).minusMonths(1),
                                d -> d.minusMonths(1))
                        .limit(times)
                        .map(d-> d.toString().substring(0, 7))
                        .collect(Collectors.toList());
            case "year":
                return Stream.iterate(
                                LocalDate.parse("01/01/" + date, formatter).minusYears(1),
                                d -> d.minusYears(1))
                        .limit(times)
                        .map(d-> d.toString().substring(0, 4))
                        .collect(Collectors.toList());
            default:
                throw new RuntimeException("Couldn't identify date level");
        }
    }
}
