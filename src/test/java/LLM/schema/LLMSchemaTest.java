package LLM.schema;

import org.junit.Test;

import static org.junit.Assert.*;

public class LLMSchemaTest {

    @Test
    public void testCompleteCubeSchemaStructure() {

        CubeSchema cubeSchema = new CubeSchema();

        cubeSchema.setDataSourceType("RDBMS");
        cubeSchema.setDbcIniPath(
                "InputFiles/adult/dbc.ini"
        );
        cubeSchema.setCubeName("adult_cube");
        cubeSchema.setCubeDataSource("adult");

        DimensionSchema dimension =
                new DimensionSchema();

        dimension.setName("age_dim");
        dimension.setDataSource("age");
        dimension.setDimensionType("categorical");

        LevelSchema level =
                new LevelSchema();

        level.setLevelName("lvl0");
        level.setId("level0");
        level.setDescription("Age level");

        LevelAttributeSchema attribute =
                new LevelAttributeSchema(
                        "age",
                        "Number",
                        "age"
                );

        level.addAttribute(attribute);
        dimension.addLevel(level);
        dimension.addHierarchyLevel("lvl0");

        MeasureSchema measure =
                new MeasureSchema(
                        "hrs",
                        "adult.hours_per_week"
                );

        CubeReference reference =
                new CubeReference(
                        "age_dim",
                        "adult.age"
                );

        cubeSchema.addDimension(dimension);
        cubeSchema.addMeasure(measure);
        cubeSchema.addReference(reference);

        assertEquals(
                "RDBMS",
                cubeSchema.getDataSourceType()
        );

        assertEquals(
                "InputFiles/adult/dbc.ini",
                cubeSchema.getDbcIniPath()
        );

        assertEquals(
                "adult_cube",
                cubeSchema.getCubeName()
        );

        assertEquals(
                "adult",
                cubeSchema.getCubeDataSource()
        );

        assertEquals(
                1,
                cubeSchema.getDimensions().size()
        );

        assertEquals(
                1,
                cubeSchema.getMeasures().size()
        );

        assertEquals(
                1,
                cubeSchema.getReferences().size()
        );

        DimensionSchema extractedDimension =
                cubeSchema.getDimensions().get(0);

        assertEquals(
                "age_dim",
                extractedDimension.getName()
        );

        assertEquals(
                "age",
                extractedDimension.getDataSource()
        );

        assertEquals(
                "categorical",
                extractedDimension.getDimensionType()
        );

        assertEquals(
                1,
                extractedDimension.getLevels().size()
        );

        assertEquals(
                1,
                extractedDimension.getHierarchy().size()
        );

        assertEquals(
                "lvl0",
                extractedDimension.getHierarchy().get(0)
        );

        LevelSchema extractedLevel =
                extractedDimension
                        .getLevels()
                        .get(0);

        assertEquals(
                "lvl0",
                extractedLevel.getLevelName()
        );

        assertEquals(
                "level0",
                extractedLevel.getId()
        );

        assertEquals(
                "Age level",
                extractedLevel.getDescription()
        );

        assertEquals(
                1,
                extractedLevel.getAttributes().size()
        );

        LevelAttributeSchema extractedAttribute =
                extractedLevel
                        .getAttributes()
                        .get(0);

        assertEquals(
                "age",
                extractedAttribute.getName()
        );

        assertEquals(
                "Number",
                extractedAttribute.getType()
        );

        assertEquals(
                "age",
                extractedAttribute.getDataSource()
        );

        MeasureSchema extractedMeasure =
                cubeSchema
                        .getMeasures()
                        .get(0);

        assertEquals(
                "hrs",
                extractedMeasure.getName()
        );

        assertEquals(
                "adult.hours_per_week",
                extractedMeasure.getSource()
        );

        CubeReference extractedReference =
                cubeSchema
                        .getReferences()
                        .get(0);

        assertEquals(
                "age_dim",
                extractedReference.getDimensionName()
        );

        assertEquals(
                "adult.age",
                extractedReference.getCubeField()
        );
    }
}