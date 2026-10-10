package LLM.experiments;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PromptSizeStatsTest {

    @Test
    public void fromPromptShouldCalculateBasicStats() {
        PromptSizeStats result =
                PromptSizeStats.fromPrompt("Hello world");

        assertEquals(11, result.getCharacterCount());
        assertEquals(11, result.getUtf8ByteCount());
        assertEquals(3L, result.getEstimatedTokensByCharacters());
        assertEquals(3L, result.getEstimatedTokensByBytes());
    }

    @Test
    public void fromPromptShouldHandleEmptyPrompt() {
        PromptSizeStats result =
                PromptSizeStats.fromPrompt("");

        assertEquals(0, result.getCharacterCount());
        assertEquals(0, result.getUtf8ByteCount());
        assertEquals(0L, result.getEstimatedTokensByCharacters());
        assertEquals(0L, result.getEstimatedTokensByBytes());
    }

    @Test
    public void fromPromptShouldHandleNullPrompt() {
        PromptSizeStats result =
                PromptSizeStats.fromPrompt(null);

        assertEquals(0, result.getCharacterCount());
        assertEquals(0, result.getUtf8ByteCount());
        assertEquals(0L, result.getEstimatedTokensByCharacters());
        assertEquals(0L, result.getEstimatedTokensByBytes());
    }

    @Test
    public void fromPromptShouldCalculateUtf8BytesCorrectly() {
        PromptSizeStats result =
                PromptSizeStats.fromPrompt("αβγ");

        assertEquals(3, result.getCharacterCount());
        assertEquals(6, result.getUtf8ByteCount());
        assertEquals(1L, result.getEstimatedTokensByCharacters());
        assertEquals(2L, result.getEstimatedTokensByBytes());
    }

    @Test
    public void fromPromptShouldRoundTokenEstimatesUp() {
        PromptSizeStats result =
                PromptSizeStats.fromPrompt("12345");

        assertEquals(5, result.getCharacterCount());
        assertEquals(5, result.getUtf8ByteCount());
        assertEquals(2L, result.getEstimatedTokensByCharacters());
        assertEquals(2L, result.getEstimatedTokensByBytes());
    }

    @Test
    public void constructorShouldClampNegativeValuesToZero() {
        PromptSizeStats result =
                new PromptSizeStats(
                        -10,
                        -20,
                        -30L,
                        -40L
                );

        assertEquals(0, result.getCharacterCount());
        assertEquals(0, result.getUtf8ByteCount());
        assertEquals(0L, result.getEstimatedTokensByCharacters());
        assertEquals(0L, result.getEstimatedTokensByBytes());
    }
}