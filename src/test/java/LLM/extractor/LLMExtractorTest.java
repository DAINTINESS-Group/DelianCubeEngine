package LLM.extractor;

import LLM.schema.*;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;

import static org.junit.Assert.*;

public class LLMExtractorTest {

    private static final String SAMPLE_INI =
            "DATASOURCE TYPE: RDBMS WITH INI FILE: InputFiles/adult/dbc.ini;\n" +
                    "\n" +
                    "CREATE DIMENSION age_dim\n" +
                    "LIST OF LEVELS {\n" +
                    "   CREATE LEVEL lvl0 WITH ATTRIBUTES{\n" +
                    "       level0: Number DATASOURCE level0\n" +
                    "   } WITH ID: level0 AND DESCRIPTION: level0,\n" +
                    "   CREATE LEVEL lvl1 WITH ATTRIBUTES{\n" +
                    "       level1: Text DATASOURCE level1\n" +
                    "   } WITH ID: level1 AND DESCRIPTION: level1\n" +
                    "}\n" +
                    "HIERARCHY lvl0>lvl1\n" +
                    "DIMENSION_TYPE categorical\n" +
                    "DATASOURCE age;\n" +
                    "\n" +
                    "CREATE DIMENSION gender_dim\n" +
                    "LIST OF LEVELS {\n" +
                    "   CREATE LEVEL lvl0 WITH ATTRIBUTES{\n" +
                    "       level0: Text DATASOURCE level0\n" +
                    "   } WITH ID: level0 AND DESCRIPTION: level0,\n" +
                    "   CREATE LEVEL lvl1 WITH ATTRIBUTES{\n" +
                    "       level1: Text DATASOURCE level1\n" +
                    "   } WITH ID: level1 AND DESCRIPTION: level1\n" +
                    "}\n" +
                    "HIERARCHY lvl0>lvl1\n" +
                    "DATASOURCE gender;\n" +
                    "\n" +
                    "CREATE CUBE adult_cube\n" +
                    "DATASOURCE adult\n" +
                    "MEASURES hrs AT adult.hours_per_week\n" +
                    "REFERENCES DIMENSION age_dim AT adult.age,\n" +
                    "                     gender_dim AT adult.gender;";

    @Test
    public void testCubeIniSchemaExtractorFromContent() {
        CubeIniSchemaExtractor extractor = new CubeIniSchemaExtractor();

        CubeSchema schema = extractor.extractFromContent(SAMPLE_INI);

        assertEquals("RDBMS", schema.getDataSourceType());
        assertEquals("InputFiles/adult/dbc.ini", schema.getDbcIniPath());

        assertEquals("adult_cube", schema.getCubeName());
        assertEquals("adult", schema.getCubeDataSource());

        assertEquals(2, schema.getDimensions().size());
        assertEquals(1, schema.getMeasures().size());
        assertEquals(2, schema.getReferences().size());

        MeasureSchema measure = schema.getMeasures().get(0);
        assertEquals("hrs", measure.getName());
        assertEquals("adult.hours_per_week", measure.getSource());

        DimensionSchema ageDim = schema.getDimensions().get(0);
        assertEquals("age_dim", ageDim.getName());
        assertEquals("age", ageDim.getDataSource());
        assertEquals("categorical", ageDim.getDimensionType());
        assertEquals(2, ageDim.getLevels().size());
        assertEquals(2, ageDim.getHierarchy().size());
        assertEquals("lvl0", ageDim.getHierarchy().get(0));
        assertEquals("lvl1", ageDim.getHierarchy().get(1));

        LevelSchema firstLevel = ageDim.getLevels().get(0);
        assertEquals("lvl0", firstLevel.getLevelName());
        assertEquals("level0", firstLevel.getId());
        assertEquals("level0", firstLevel.getDescription());
        assertEquals(1, firstLevel.getAttributes().size());

        LevelAttributeSchema firstAttribute = firstLevel.getAttributes().get(0);
        assertEquals("level0", firstAttribute.getName());
        assertEquals("Number", firstAttribute.getType());
        assertEquals("level0", firstAttribute.getDataSource());

        DimensionSchema genderDim = schema.getDimensions().get(1);
        assertEquals("gender_dim", genderDim.getName());
        assertEquals("gender", genderDim.getDataSource());
        assertEquals("other", genderDim.getDimensionType());

        CubeReference firstReference = schema.getReferences().get(0);
        assertEquals("age_dim", firstReference.getDimensionName());
        assertEquals("adult.age", firstReference.getCubeField());

        CubeReference secondReference = schema.getReferences().get(1);
        assertEquals("gender_dim", secondReference.getDimensionName());
        assertEquals("adult.gender", secondReference.getCubeField());
    }

    @Test
    public void testCubeSchemaFileLoaderAndExtractorFromFile() throws Exception {
        File tempFile = File.createTempFile("adult-test", ".ini");

        FileWriter writer = new FileWriter(tempFile);
        writer.write(SAMPLE_INI);
        writer.close();

        CubeSchemaFileLoader loader = new CubeSchemaFileLoader();
        String loadedContent = loader.loadFileContent(tempFile.getAbsolutePath());

        assertNotNull(loadedContent);
        assertTrue(loadedContent.contains("CREATE CUBE adult_cube"));
        assertTrue(loadedContent.contains("CREATE DIMENSION age_dim"));

        CubeIniSchemaExtractor extractor = new CubeIniSchemaExtractor();
        CubeSchema schema = extractor.extractFromFile(tempFile.getAbsolutePath());

        assertEquals("adult_cube", schema.getCubeName());
        assertEquals(2, schema.getDimensions().size());
        assertEquals(1, schema.getMeasures().size());
        assertEquals(2, schema.getReferences().size());

        tempFile.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractorRejectsNullContent() {
        CubeIniSchemaExtractor extractor = new CubeIniSchemaExtractor();
        extractor.extractFromContent(null);
    }

    @Test
    public void testExtractorWithRealAdultIniFile() throws Exception {
        File iniFile = new File("InputFiles/adult/adult.ini");

        assertTrue(
                "adult.ini file should exist. Current working directory: "
                        + new File(".").getAbsolutePath(),
                iniFile.exists()
        );

        CubeIniSchemaExtractor extractor = new CubeIniSchemaExtractor();
        CubeSchema schema = extractor.extractFromFile(iniFile.getPath());

        assertNotNull(schema);

        assertEquals("RDBMS", schema.getDataSourceType());
        assertEquals("InputFiles/adult/dbc.ini", schema.getDbcIniPath());
        assertEquals("adult_cube", schema.getCubeName());
        assertEquals("adult", schema.getCubeDataSource());

        assertEquals(8, schema.getDimensions().size());
        assertEquals(1, schema.getMeasures().size());
        assertEquals(8, schema.getReferences().size());

        assertEquals("hrs", schema.getMeasures().get(0).getName());
        assertEquals("adult.hours_per_week", schema.getMeasures().get(0).getSource());

        DimensionSchema ageDimension = schema.getDimensions().get(0);
        assertEquals("age_dim", ageDimension.getName());
        assertEquals("age", ageDimension.getDataSource());
        assertEquals("other", ageDimension.getDimensionType());
        assertEquals(5, ageDimension.getLevels().size());
        assertEquals(5, ageDimension.getHierarchy().size());

        LevelSchema firstLevel = ageDimension.getLevels().get(0);
        assertEquals("lvl0", firstLevel.getLevelName());
        assertEquals("level0", firstLevel.getId());
        assertEquals("level0", firstLevel.getDescription());
        assertEquals(1, firstLevel.getAttributes().size());

        LevelAttributeSchema firstAttribute = firstLevel.getAttributes().get(0);
        assertEquals("level0", firstAttribute.getName());
        assertEquals("Number", firstAttribute.getType());
        assertEquals("level0", firstAttribute.getDataSource());

        CubeReference firstReference = schema.getReferences().get(0);
        assertEquals("age_dim", firstReference.getDimensionName());
        assertEquals("adult.age", firstReference.getCubeField());
    }
}