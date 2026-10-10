package LLM.experiments.similarity;

import LLM.schema.CubeSchema;
import LLM.schema.DimensionSchema;
import LLM.schema.LevelAttributeSchema;
import LLM.schema.LevelSchema;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class HierarchyMetadataIndexTest {

    @Test
    public void testFieldsAreIndexedCorrectly() {
        CubeSchema schema = createSchema();

        HierarchyMetadataIndex index =
                new HierarchyMetadataIndex(schema);

        assertTrue(
                index.containsField("date_dim.the_year")
        );

        assertTrue(
                index.containsField("date_dim.year_quarter")
        );

        assertTrue(
                index.containsField("date_dim.year_and_month")
        );

        assertEquals(
                3,
                index.size()
        );
    }

    @Test
    public void testFieldInfoContainsCorrectMetadata() {
        CubeSchema schema = createSchema();

        HierarchyMetadataIndex index =
                new HierarchyMetadataIndex(schema);

        FieldHierarchyInfo info =
                index.getFieldInfo(
                        "date_dim.year_quarter"
                );

        assertNotNull(info);

        assertEquals(
                "date_dim.year_quarter",
                info.getFieldName()
        );

        assertEquals(
                "date_dim",
                info.getDimensionName()
        );

        assertEquals(
                "date_dim",
                info.getHierarchyName()
        );

        assertEquals(
                "year_quarter",
                info.getLevelName()
        );

        assertEquals(
                1,
                info.getLevelPosition()
        );

        assertEquals(
                3,
                info.getHierarchySize()
        );
    }

    @Test
    public void testFieldLookupPreservesCaseSensitivity() {
        CubeSchema schema = createSchema();

        HierarchyMetadataIndex index =
                new HierarchyMetadataIndex(schema);

        assertNotNull(
                index.getFieldInfo(
                        "date_dim.year_quarter"
                )
        );

        assertNull(
                index.getFieldInfo(
                        "DATE_DIM.YEAR_QUARTER"
                )
        );

        assertNull(
                index.getFieldInfo(
                        "Date_Dim.Year_Quarter"
                )
        );
    }

    @Test
    public void testFieldLookupIgnoresWhitespace() {
        CubeSchema schema = createSchema();

        HierarchyMetadataIndex index =
                new HierarchyMetadataIndex(schema);

        assertNotNull(
                index.getFieldInfo(
                        "  date_dim.year_quarter  "
                )
        );
    }

    @Test
    public void testUnknownFieldReturnsNull() {
        CubeSchema schema = createSchema();

        HierarchyMetadataIndex index =
                new HierarchyMetadataIndex(schema);

        assertNull(
                index.getFieldInfo(
                        "date_dim.unknown_field"
                )
        );

        assertFalse(
                index.containsField(
                        "date_dim.unknown_field"
                )
        );
    }

    @Test
    public void testHierarchyPositionsAreCorrect() {
        CubeSchema schema = createSchema();

        HierarchyMetadataIndex index =
                new HierarchyMetadataIndex(schema);

        FieldHierarchyInfo year =
                index.getFieldInfo(
                        "date_dim.the_year"
                );

        FieldHierarchyInfo quarter =
                index.getFieldInfo(
                        "date_dim.year_quarter"
                );

        FieldHierarchyInfo month =
                index.getFieldInfo(
                        "date_dim.year_and_month"
                );

        assertEquals(
                0,
                year.getLevelPosition()
        );

        assertEquals(
                1,
                quarter.getLevelPosition()
        );

        assertEquals(
                2,
                month.getLevelPosition()
        );

        assertEquals(
                3,
                year.getHierarchySize()
        );

        assertEquals(
                3,
                quarter.getHierarchySize()
        );

        assertEquals(
                3,
                month.getHierarchySize()
        );
    }

    @Test
    public void testNullSchemaCreatesEmptyIndex() {
        HierarchyMetadataIndex index =
                new HierarchyMetadataIndex(null);

        assertEquals(
                0,
                index.size()
        );

        assertFalse(
                index.containsField(
                        "date_dim.the_year"
                )
        );
    }

    @Test
    public void testNullAndEmptyFieldNamesAreIgnored() {
        CubeSchema schema = new CubeSchema();

        DimensionSchema dimension =
                new DimensionSchema("date_dim");

        LevelSchema validLevel =
                new LevelSchema();

        validLevel.setLevelName(
                "the_year"
        );

        validLevel.addAttribute(
                new LevelAttributeSchema(
                        "the_year",
                        "String",
                        "date_dim.the_year"
                )
        );

        LevelSchema invalidLevel =
                new LevelSchema();

        invalidLevel.setLevelName("");

        invalidLevel.addAttribute(
                new LevelAttributeSchema(
                        "",
                        "String",
                        ""
                )
        );

        dimension.addLevel(validLevel);
        dimension.addLevel(invalidLevel);

        dimension.setHierarchy(
                Arrays.asList(
                        "the_year"
                )
        );

        schema.addDimension(dimension);

        HierarchyMetadataIndex index =
                new HierarchyMetadataIndex(schema);

        assertEquals(
                1,
                index.size()
        );

        assertTrue(
                index.containsField(
                        "date_dim.the_year"
                )
        );
    }

    private CubeSchema createSchema() {
        CubeSchema schema =
                new CubeSchema();

        DimensionSchema dateDimension =
                new DimensionSchema("date_dim");

        LevelSchema yearLevel =
                new LevelSchema();

        yearLevel.setLevelName(
                "the_year"
        );

        yearLevel.addAttribute(
                new LevelAttributeSchema(
                        "the_year",
                        "String",
                        "date_dim.the_year"
                )
        );

        LevelSchema quarterLevel =
                new LevelSchema();

        quarterLevel.setLevelName(
                "year_quarter"
        );

        quarterLevel.addAttribute(
                new LevelAttributeSchema(
                        "year_quarter",
                        "String",
                        "date_dim.year_quarter"
                )
        );

        LevelSchema monthLevel =
                new LevelSchema();

        monthLevel.setLevelName(
                "year_and_month"
        );

        monthLevel.addAttribute(
                new LevelAttributeSchema(
                        "year_and_month",
                        "String",
                        "date_dim.year_and_month"
                )
        );

        dateDimension.addLevel(
                yearLevel
        );

        dateDimension.addLevel(
                quarterLevel
        );

        dateDimension.addLevel(
                monthLevel
        );

        dateDimension.setHierarchy(
                Arrays.asList(
                        "the_year",
                        "year_quarter",
                        "year_and_month"
                )
        );

        schema.addDimension(
                dateDimension
        );

        return schema;
    }
}