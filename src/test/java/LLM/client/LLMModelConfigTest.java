package LLM.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class LLMModelConfigTest {

    @Test
    public void testFullConstructorStoresValuesCorrectly() {
        LLMModelConfig config = new LLMModelConfig(
                "qwen3:27b",
                8192,
                256,
                0.2,
                123,
                10,
                0.9,
                "30m",
                true
        );

        assertEquals("qwen3:27b", config.getModelName());
        assertEquals(8192, config.getNumCtx());
        assertEquals(256, config.getNumPredict());
        assertEquals(0.2, config.getTemperature(), 0.0001);
        assertEquals(123, config.getSeed());
        assertEquals(10, config.getTopK());
        assertEquals(0.9, config.getTopP(), 0.0001);
        assertEquals("30m", config.getKeepAlive());
        assertTrue(config.isStreamEnabled());
    }

    @Test
    public void testConstructorWithoutSeedUsesDefaultSeed() {
        LLMModelConfig config = new LLMModelConfig(
                "gemma3:1b",
                4096,
                128,
                0.0,
                1,
                1.0,
                "30m",
                true
        );

        assertEquals(LLMModelConfig.DEFAULT_SEED, config.getSeed());
    }

    @Test
    public void testCreateDefaultUsesExpectedValues() {
        LLMModelConfig config =
                LLMModelConfig.createDefault("qwen3:27b");

        assertEquals("qwen3:27b", config.getModelName());
        assertEquals(4096, config.getNumCtx());
        assertEquals(128, config.getNumPredict());
        assertEquals(LLMModelConfig.DEFAULT_TEMPERATURE,
                config.getTemperature(), 0.0001);
        assertEquals(LLMModelConfig.DEFAULT_SEED,
                config.getSeed());
        assertEquals(LLMModelConfig.DEFAULT_TOP_K,
                config.getTopK());
        assertEquals(LLMModelConfig.DEFAULT_TOP_P,
                config.getTopP(), 0.0001);
        assertEquals("30m", config.getKeepAlive());
        assertTrue(config.isStreamEnabled());
    }

    @Test
    public void testCreateFastUsesExpectedPredictionLimit() {
        LLMModelConfig config =
                LLMModelConfig.createFast("qwen3:27b");

        assertEquals(4096, config.getNumCtx());
        assertEquals(80, config.getNumPredict());
        assertTrue(config.isStreamEnabled());
        assertEquals(LLMModelConfig.DEFAULT_TEMPERATURE,
                config.getTemperature(), 0.0001);
        assertEquals(LLMModelConfig.DEFAULT_SEED,
                config.getSeed());
    }

    @Test
    public void testCreateNonStreamedDisablesStreaming() {
        LLMModelConfig config =
                LLMModelConfig.createNonStreamed("qwen3:27b");

        assertEquals(4096, config.getNumCtx());
        assertEquals(128, config.getNumPredict());
        assertFalse(config.isStreamEnabled());
    }

    @Test
    public void testCreateStreamedEnablesStreaming() {
        LLMModelConfig config =
                LLMModelConfig.createStreamed("qwen3:27b");

        assertEquals(4096, config.getNumCtx());
        assertEquals(128, config.getNumPredict());
        assertTrue(config.isStreamEnabled());
    }

    @Test
    public void testCreateExperimentConfigMatchesDefaultConfiguration() {
        LLMModelConfig experimentConfig =
                LLMModelConfig.createExperimentConfig(
                        "qwen3:27b",
                        8192,
                        256,
                        true
                );

        LLMModelConfig defaultConfig =
                LLMModelConfig.createDefault(
                        "qwen3:27b",
                        8192,
                        256,
                        true
                );

        assertEquals(defaultConfig.getModelName(),
                experimentConfig.getModelName());
        assertEquals(defaultConfig.getNumCtx(),
                experimentConfig.getNumCtx());
        assertEquals(defaultConfig.getNumPredict(),
                experimentConfig.getNumPredict());
        assertEquals(defaultConfig.getTemperature(),
                experimentConfig.getTemperature(), 0.0001);
        assertEquals(defaultConfig.getSeed(),
                experimentConfig.getSeed());
        assertEquals(defaultConfig.getTopK(),
                experimentConfig.getTopK());
        assertEquals(defaultConfig.getTopP(),
                experimentConfig.getTopP(), 0.0001);
        assertEquals(defaultConfig.getKeepAlive(),
                experimentConfig.getKeepAlive());
        assertEquals(defaultConfig.isStreamEnabled(),
                experimentConfig.isStreamEnabled());
    }

    @Test
    public void testCreateCustomUsesExpectedConfiguration() {
        LLMModelConfig config =
                LLMModelConfig.createCustom(
                        "qwen3:27b",
                        8192,
                        256,
                        false
                );

        assertEquals("qwen3:27b", config.getModelName());
        assertEquals(8192, config.getNumCtx());
        assertEquals(256, config.getNumPredict());
        assertFalse(config.isStreamEnabled());
        assertEquals(LLMModelConfig.DEFAULT_TEMPERATURE,
                config.getTemperature(), 0.0001);
        assertEquals(LLMModelConfig.DEFAULT_SEED,
                config.getSeed());
        assertEquals(LLMModelConfig.DEFAULT_TOP_K,
                config.getTopK());
        assertEquals(LLMModelConfig.DEFAULT_TOP_P,
                config.getTopP(), 0.0001);
    }

    @Test
    public void testModelNameIsTrimmed() {
        LLMModelConfig config = LLMModelConfig.createDefault(
                "  qwen3:27b  "
        );

        assertEquals("qwen3:27b", config.getModelName());
    }

    @Test
    public void testKeepAliveIsTrimmed() {
        LLMModelConfig config = new LLMModelConfig(
                "qwen3:27b",
                4096,
                128,
                0.0,
                42,
                1,
                1.0,
                " 30m ",
                true
        );

        assertEquals("30m", config.getKeepAlive());
    }

    @Test
    public void testSafeFileNameReplacesSpecialCharacters() {
        LLMModelConfig config =
                LLMModelConfig.createDefault(
                        "qwen3:27b/test model"
                );

        assertEquals(
                "qwen3_27b_test_model",
                config.getSafeFileName()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsEmptyModelName() {
        LLMModelConfig.createDefault("   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsNonPositiveContextSize() {
        new LLMModelConfig(
                "qwen3:27b",
                0,
                128,
                0.0,
                42,
                1,
                1.0,
                "30m",
                true
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsNonPositivePredictionLimit() {
        new LLMModelConfig(
                "qwen3:27b",
                4096,
                0,
                0.0,
                42,
                1,
                1.0,
                "30m",
                true
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsNegativeTemperature() {
        new LLMModelConfig(
                "qwen3:27b",
                4096,
                128,
                -0.1,
                42,
                1,
                1.0,
                "30m",
                true
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsInvalidTopK() {
        new LLMModelConfig(
                "qwen3:27b",
                4096,
                128,
                0.0,
                42,
                0,
                1.0,
                "30m",
                true
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRejectsInvalidTopP() {
        new LLMModelConfig(
                "qwen3:27b",
                4096,
                128,
                0.0,
                42,
                1,
                1.1,
                "30m",
                true
        );
    }
}