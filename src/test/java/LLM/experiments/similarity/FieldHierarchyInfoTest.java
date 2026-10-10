package LLM.experiments.similarity;

import org.junit.Test;

import static org.junit.Assert.*;

public class FieldHierarchyInfoTest {

    @Test
    public void testSameDimension() {
        FieldHierarchyInfo first =
                createInfo(
                        "date_dim.the_year",
                        "date_dim",
                        "date_dim",
                        "the_year",
                        0,
                        3
                );

        FieldHierarchyInfo second =
                createInfo(
                        "date_dim.year_quarter",
                        "date_dim",
                        "date_dim",
                        "year_quarter",
                        1,
                        3
                );

        assertTrue(
                first.belongsToSameDimension(second)
        );
    }

    @Test
    public void testDifferentDimensions() {
        FieldHierarchyInfo first =
                createInfo(
                        "date_dim.the_year",
                        "date_dim",
                        "date_dim",
                        "the_year",
                        0,
                        3
                );

        FieldHierarchyInfo second =
                createInfo(
                        "product_dim.product_category",
                        "product_dim",
                        "product_dim",
                        "product_category",
                        0,
                        3
                );

        assertFalse(
                first.belongsToSameDimension(second)
        );
    }

    @Test
    public void testSameHierarchy() {
        FieldHierarchyInfo first =
                createInfo(
                        "date_dim.the_year",
                        "date_dim",
                        "date_dim",
                        "the_year",
                        0,
                        3
                );

        FieldHierarchyInfo second =
                createInfo(
                        "date_dim.year_quarter",
                        "date_dim",
                        "date_dim",
                        "year_quarter",
                        1,
                        3
                );

        assertTrue(
                first.belongsToSameHierarchy(second)
        );
    }

    @Test
    public void testDifferentHierarchies() {
        FieldHierarchyInfo first =
                createInfo(
                        "date_dim.the_year",
                        "date_dim",
                        "date_hierarchy",
                        "the_year",
                        0,
                        3
                );

        FieldHierarchyInfo second =
                createInfo(
                        "date_dim.year_quarter",
                        "date_dim",
                        "other_hierarchy",
                        "year_quarter",
                        1,
                        3
                );

        assertFalse(
                first.belongsToSameHierarchy(second)
        );
    }

    @Test
    public void testSameLevel() {
        FieldHierarchyInfo first =
                createInfo(
                        "date_dim.year_quarter",
                        "date_dim",
                        "date_dim",
                        "year_quarter",
                        1,
                        3
                );

        FieldHierarchyInfo second =
                createInfo(
                        "date_dim.quarter",
                        "date_dim",
                        "date_dim",
                        "year_quarter",
                        1,
                        3
                );

        assertTrue(
                first.belongsToSameLevel(second)
        );
    }

    @Test
    public void testDifferentLevels() {
        FieldHierarchyInfo first =
                createInfo(
                        "date_dim.the_year",
                        "date_dim",
                        "date_dim",
                        "the_year",
                        0,
                        3
                );

        FieldHierarchyInfo second =
                createInfo(
                        "date_dim.year_quarter",
                        "date_dim",
                        "date_dim",
                        "year_quarter",
                        1,
                        3
                );

        assertFalse(
                first.belongsToSameLevel(second)
        );
    }

    @Test
    public void testLevelDistance() {
        FieldHierarchyInfo year =
                createInfo(
                        "date_dim.the_year",
                        "date_dim",
                        "date_dim",
                        "the_year",
                        0,
                        3
                );

        FieldHierarchyInfo month =
                createInfo(
                        "date_dim.year_and_month",
                        "date_dim",
                        "date_dim",
                        "year_and_month",
                        2,
                        3
                );

        assertEquals(
                2,
                year.getLevelDistance(month)
        );

        assertEquals(
                2,
                month.getLevelDistance(year)
        );
    }

    @Test
    public void testLevelDistanceForDifferentHierarchies() {
        FieldHierarchyInfo first =
                createInfo(
                        "date_dim.the_year",
                        "date_dim",
                        "date_hierarchy",
                        "the_year",
                        0,
                        3
                );

        FieldHierarchyInfo second =
                createInfo(
                        "date_dim.year_quarter",
                        "date_dim",
                        "other_hierarchy",
                        "year_quarter",
                        1,
                        3
                );

        assertEquals(
                -1,
                first.getLevelDistance(second)
        );
    }

    @Test
    public void testUnknownHierarchyPosition() {
        FieldHierarchyInfo info =
                createInfo(
                        "date_dim.the_year",
                        "date_dim",
                        "date_dim",
                        "the_year",
                        -1,
                        3
                );

        assertFalse(
                info.hasKnownHierarchyPosition()
        );
    }

    @Test
    public void testComparisonsAreCaseInsensitive() {
        FieldHierarchyInfo first =
                createInfo(
                        "date_dim.the_year",
                        "DATE_DIM",
                        "DATE_DIM",
                        "THE_YEAR",
                        0,
                        3
                );

        FieldHierarchyInfo second =
                createInfo(
                        "date_dim.year_quarter",
                        "date_dim",
                        "date_dim",
                        "year_quarter",
                        1,
                        3
                );

        assertTrue(
                first.belongsToSameDimension(second)
        );

        assertTrue(
                first.belongsToSameHierarchy(second)
        );
    }

    @Test
    public void testNullOtherReturnsFalseOrMinusOne() {
        FieldHierarchyInfo info =
                createInfo(
                        "date_dim.the_year",
                        "date_dim",
                        "date_dim",
                        "the_year",
                        0,
                        3
                );

        assertFalse(
                info.belongsToSameDimension(null)
        );

        assertFalse(
                info.belongsToSameHierarchy(null)
        );

        assertFalse(
                info.belongsToSameLevel(null)
        );

        assertEquals(
                -1,
                info.getLevelDistance(null)
        );
    }

    private FieldHierarchyInfo createInfo(
            String fieldName,
            String dimensionName,
            String hierarchyName,
            String levelName,
            int levelPosition,
            int hierarchySize
    ) {
        return new FieldHierarchyInfo(
                fieldName,
                dimensionName,
                hierarchyName,
                levelName,
                levelPosition,
                hierarchySize
        );
    }
}