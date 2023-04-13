package assess.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DatesHandlerTest {
    @Test
    public void decrementDays() {
        String date = "01/03/1998";
        String dateLevel = "day";
        int times = 20;
        String expectedDate = "1998-02-09";

        List<String> dates = DatesHandler.decrementDate(date, dateLevel,  times);
        String actualDate = dates.get(dates.size() - 1);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void decrementMonths() {
        String date = "06/1998";
        String dateLevel = "month";
        int times = 5;
        String expectedDate = "1998-01";

        List<String> dates = DatesHandler.decrementDate(date, dateLevel,  times);
        String actualDate = dates.get(dates.size() - 1);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void decrementYears() {
        String date = "1997";
        String dateLevel = "year";
        int times = 10;
        String expectedDate = "1987";

        List<String> dates = DatesHandler.decrementDate(date, dateLevel,  times);
        String actualDate = dates.get(dates.size() - 1);
        assertEquals(expectedDate, actualDate);
    }
}
