package LLM.experiments;

import java.nio.charset.StandardCharsets;

public class PromptSizeStats {

    private final int characterCount;
    private final int utf8ByteCount;
    private final long estimatedTokensByCharacters;
    private final long estimatedTokensByBytes;

    public PromptSizeStats(
            int characterCount,
            int utf8ByteCount,
            long estimatedTokensByCharacters,
            long estimatedTokensByBytes
    ) {
        this.characterCount = Math.max(0, characterCount);
        this.utf8ByteCount = Math.max(0, utf8ByteCount);
        this.estimatedTokensByCharacters =
                Math.max(0L, estimatedTokensByCharacters);

        this.estimatedTokensByBytes =
                Math.max(0L, estimatedTokensByBytes);
    }

    public static PromptSizeStats fromPrompt(
            String prompt
    ) {
        if (prompt == null) {
            prompt = "";
        }

        int characterCount =
                prompt.length();

        int utf8ByteCount =
                prompt
                        .getBytes(StandardCharsets.UTF_8)
                        .length;

        /*
         * Rough token estimates.
         *
         * These are useful before the LLM request,
         * but they are NOT the authoritative token count.
         *
         * The real token count is obtained later from
         * Ollama's prompt_eval_count.
         */
        long estimatedTokensByCharacters =
                (long) Math.ceil(
                        characterCount / 4.0
                );

        long estimatedTokensByBytes =
                (long) Math.ceil(
                        utf8ByteCount / 4.0
                );

        return new PromptSizeStats(
                characterCount,
                utf8ByteCount,
                estimatedTokensByCharacters,
                estimatedTokensByBytes
        );
    }

    public int getCharacterCount() {
        return characterCount;
    }

    public int getUtf8ByteCount() {
        return utf8ByteCount;
    }

    public long getEstimatedTokensByCharacters() {
        return estimatedTokensByCharacters;
    }

    public long getEstimatedTokensByBytes() {
        return estimatedTokensByBytes;
    }

    @Override
    public String toString() {
        return "PromptSizeStats{" +
                "characterCount=" + characterCount +
                ", utf8ByteCount=" + utf8ByteCount +
                ", estimatedTokensByCharacters="
                + estimatedTokensByCharacters +
                ", estimatedTokensByBytes="
                + estimatedTokensByBytes +
                '}';
    }
}