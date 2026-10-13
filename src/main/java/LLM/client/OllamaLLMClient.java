package LLM.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;

public class OllamaLLMClient {

    private final String endpoint;
    private final String apiKey;

    public OllamaLLMClient(
            String endpoint,
            String apiKey
    ) {
        this.endpoint = safeString(endpoint);
        this.apiKey = safeString(apiKey);
    }

    public LLMCallResult generate(
            LLMModelConfig modelConfig,
            String prompt
    ) {
        long startNs = System.nanoTime();

        HttpURLConnection connection = null;

        try {

            URL url =
                    new URL(endpoint);

            connection =
                    (HttpURLConnection)
                            url.openConnection();

            connection.setRequestMethod(
                    "POST"
            );

            connection.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            if (modelConfig.isStreamEnabled()) {

                connection.setRequestProperty(
                        "Accept",
                        "application/x-ndjson"
                );
            }

            if (!apiKey.isEmpty()) {

                connection.setRequestProperty(
                        "X-API-Key",
                        apiKey
                );
            }

            connection.setConnectTimeout(
                    60000
            );

            /*
             * 0 means infinite Java read timeout.
             * Server/proxy timeouts may still apply.
             */
            connection.setReadTimeout(
                    180000
            );

            connection.setDoOutput(
                    true
            );

            String jsonPayload =
                    buildJsonPayload(
                            modelConfig,
                            prompt
                    );

            OutputStream outputStream =
                    connection.getOutputStream();

            byte[] input =
                    jsonPayload.getBytes(
                            StandardCharsets.UTF_8
                    );

            outputStream.write(
                    input,
                    0,
                    input.length
            );

            outputStream.flush();
            outputStream.close();

            int responseCode =
                    connection.getResponseCode();

            InputStream responseStream;

            if (responseCode >= 200
                    && responseCode < 300) {

                responseStream =
                        connection.getInputStream();

            } else {

                responseStream =
                        connection.getErrorStream();

                String rawErrorResponse =
                        readStream(
                                responseStream
                        );

                long responseTimeMs =
                        elapsedMs(
                                startNs
                        );

                return LLMCallResult.failure(
                        responseTimeMs,
                        "HTTP_ERROR_" + responseCode,
                        "LLM request failed with HTTP status: "
                                + responseCode,
                        rawErrorResponse
                );
            }

            if (modelConfig.isStreamEnabled()) {

                return readStreamedResponse(
                        responseStream,
                        startNs
                );
            }

            return readNonStreamedResponse(
                    responseStream,
                    startNs
            );

        } catch (Exception e) {

            long responseTimeMs =
                    elapsedMs(
                            startNs
                    );

            return LLMCallResult.failure(
                    responseTimeMs,
                    "CLIENT_EXCEPTION",
                    e.getClass().getSimpleName()
                            + ": "
                            + safeString(
                            e.getMessage()
                    ),
                    ""
            );

        } finally {

            if (connection != null) {

                connection.disconnect();
            }
        }
    }

    /*
     * ==========================================================
     * NON-STREAMED RESPONSE
     * ==========================================================
     */

    private LLMCallResult readNonStreamedResponse(
            InputStream responseStream,
            long startNs
    ) throws Exception {

        String rawResponse =
                readStream(
                        responseStream
                );

        long responseTimeMs =
                elapsedMs(
                        startNs
                );

        String answer =
                extractStringField(
                        rawResponse,
                        "response"
                );

        long promptEvalCount =
                extractLongField(
                        rawResponse,
                        "prompt_eval_count"
                );

        long promptEvalCachedCount =
                extractLongField(
                        rawResponse,
                        "prompt_eval_cached_count"
                );

        long evalCount =
                extractLongField(
                        rawResponse,
                        "eval_count"
                );

        long totalDuration =
                extractLongField(
                        rawResponse,
                        "total_duration"
                );

        long loadDuration =
                extractLongField(
                        rawResponse,
                        "load_duration"
                );

        long promptEvalDuration =
                extractLongField(
                        rawResponse,
                        "prompt_eval_duration"
                );

        long evalDuration =
                extractLongField(
                        rawResponse,
                        "eval_duration"
                );

        return LLMCallResult.success(
                answer,
                rawResponse,
                responseTimeMs,
                promptEvalCount,
                promptEvalCachedCount,
                evalCount,
                totalDuration,
                loadDuration,
                promptEvalDuration,
                evalDuration
        );
    }

    /*
     * ==========================================================
     * STREAMED RESPONSE
     * ==========================================================
     */

    private LLMCallResult readStreamedResponse(
            InputStream responseStream,
            long startNs
    ) throws Exception {

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                responseStream,
                                StandardCharsets.UTF_8
                        )
                );

        StringBuilder rawNdjson =
                new StringBuilder();

        StringBuilder answerBuilder =
                new StringBuilder();

        String finalJsonLine = "";

        String line;

        while ((line = reader.readLine())
                != null) {

            String trimmedLine =
                    line.trim();

            if (trimmedLine.isEmpty()) {
                continue;
            }

            rawNdjson
                    .append(trimmedLine)
                    .append("\n");

            finalJsonLine =
                    trimmedLine;

            String responseChunk =
                    extractStringField(
                            trimmedLine,
                            "response"
                    );

            if (responseChunk != null
                    && !responseChunk.isEmpty()) {

                answerBuilder.append(
                        responseChunk
                );
            }
        }

        reader.close();

        long responseTimeMs =
                elapsedMs(
                        startNs
                );

        /*
         * Ollama places usage/performance statistics
         * in the final streamed JSON object.
         */

        long promptEvalCount =
                extractLongField(
                        finalJsonLine,
                        "prompt_eval_count"
                );

        long promptEvalCachedCount =
                extractLongField(
                        finalJsonLine,
                        "prompt_eval_cached_count"
                );

        long evalCount =
                extractLongField(
                        finalJsonLine,
                        "eval_count"
                );

        long totalDuration =
                extractLongField(
                        finalJsonLine,
                        "total_duration"
                );

        long loadDuration =
                extractLongField(
                        finalJsonLine,
                        "load_duration"
                );

        long promptEvalDuration =
                extractLongField(
                        finalJsonLine,
                        "prompt_eval_duration"
                );

        long evalDuration =
                extractLongField(
                        finalJsonLine,
                        "eval_duration"
                );

        return LLMCallResult.success(
                answerBuilder.toString(),
                rawNdjson.toString(),
                responseTimeMs,
                promptEvalCount,
                promptEvalCachedCount,
                evalCount,
                totalDuration,
                loadDuration,
                promptEvalDuration,
                evalDuration
        );
    }

    /*
     * ==========================================================
     * REQUEST PAYLOAD
     * ==========================================================
     */

    private String buildJsonPayload(
            LLMModelConfig modelConfig,
            String prompt
    ) {

        StringBuilder json =
                new StringBuilder();

        json.append("{");


        json.append("\"model\":\"")
                .append(
                        escapeJson(
                                modelConfig.getModelName()
                        )
                )
                .append("\",");


        json.append("\"prompt\":\"")
                .append(
                        escapeJson(
                                prompt
                        )
                )
                .append("\",");


        json.append("\"stream\":")
                .append(
                        modelConfig.isStreamEnabled()
                )
                .append(",");

        if (modelConfig.getModelName().startsWith("qwen3")
                || modelConfig.getModelName().startsWith("gemma4")) {

            json.append("\"think\":false,");
        }

        if (modelConfig
                .getModelName()
                .startsWith("gpt-oss")) {

            json.append(
                    "\"think\":\"low\","
            );
        }

        if (modelConfig.getKeepAlive() != null
                && !modelConfig
                .getKeepAlive()
                .trim()
                .isEmpty()) {

            json.append(
                            "\"keep_alive\":\""
                    )
                    .append(
                            escapeJson(
                                    modelConfig.getKeepAlive()
                            )
                    )
                    .append("\",");
        }

        json.append(
                "\"options\":{"
        );


        json.append(
                        "\"num_ctx\":"
                )
                .append(
                        modelConfig.getNumCtx()
                )
                .append(",");


        json.append(
                        "\"num_predict\":"
                )
                .append(
                        modelConfig.getNumPredict()
                )
                .append(",");

        json.append(
                        "\"temperature\":"
                )
                .append(
                        modelConfig.getTemperature()
                )
                .append(",");


        json.append(
                        "\"seed\":"
                )
                .append(
                        modelConfig.getSeed()
                )
                .append(",");


        json.append(
                        "\"top_k\":"
                )
                .append(
                        modelConfig.getTopK()
                )
                .append(",");

        json.append(
                        "\"top_p\":"
                )
                .append(
                        modelConfig.getTopP()
                );

        json.append("}");

        json.append("}");

        return json.toString();
    }

    private String readStream(
            InputStream inputStream
    ) throws Exception {

        if (inputStream == null) {
            return "";
        }

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                inputStream,
                                StandardCharsets.UTF_8
                        )
                );

        StringBuilder response =
                new StringBuilder();

        String line;

        while ((line = reader.readLine())
                != null) {

            response
                    .append(line)
                    .append("\n");
        }

        reader.close();

        return response.toString();
    }


    private static String extractStringField(
            String jsonResponse,
            String fieldName
    ) {

        if (jsonResponse == null
                || fieldName == null) {

            return "";
        }

        String key =
                "\""
                        + fieldName
                        + "\":\"";

        int start =
                jsonResponse.indexOf(
                        key
                );

        if (start == -1) {
            return "";
        }

        start =
                start
                        + key.length();

        StringBuilder extracted =
                new StringBuilder();

        boolean escaping =
                false;

        for (int i = start;
             i < jsonResponse.length();
             i++) {

            char c =
                    jsonResponse.charAt(i);

            if (escaping) {

                switch (c) {

                    case 'n':
                        extracted.append('\n');
                        break;

                    case 'r':
                        extracted.append('\r');
                        break;

                    case 't':
                        extracted.append('\t');
                        break;

                    case '"':
                        extracted.append('"');
                        break;

                    case '\\':
                        extracted.append('\\');
                        break;

                    case 'b':
                        extracted.append('\b');
                        break;

                    case 'f':
                        extracted.append('\f');
                        break;

                    default:
                        extracted.append(c);
                        break;
                }

                escaping = false;

            } else {

                if (c == '\\') {

                    escaping = true;

                } else if (c == '"') {

                    break;

                } else {

                    extracted.append(c);
                }
            }
        }

        return extracted.toString();
    }

    private static long extractLongField(
            String jsonResponse,
            String fieldName
    ) {

        if (jsonResponse == null
                || fieldName == null) {

            return -1L;
        }

        String key =
                "\""
                        + fieldName
                        + "\":";

        int start =
                jsonResponse.indexOf(
                        key
                );

        if (start == -1) {
            return -1L;
        }

        start =
                start
                        + key.length();

        while (start < jsonResponse.length()
                && Character.isWhitespace(
                jsonResponse.charAt(start)
        )) {

            start++;
        }

        StringBuilder number =
                new StringBuilder();

        if (start < jsonResponse.length()
                && jsonResponse.charAt(start) == '-') {

            number.append('-');
            start++;
        }

        while (start < jsonResponse.length()
                && Character.isDigit(
                jsonResponse.charAt(start)
        )) {

            number.append(
                    jsonResponse.charAt(start)
            );

            start++;
        }

        if (number.length() == 0
                || "-".equals(
                number.toString()
        )) {

            return -1L;
        }

        try {

            return Long.parseLong(
                    number.toString()
            );

        } catch (NumberFormatException e) {

            return -1L;
        }
    }

    private static String escapeJson(
            String value
    ) {

        if (value == null) {
            return "";
        }

        StringBuilder escaped =
                new StringBuilder();

        for (int i = 0;
             i < value.length();
             i++) {

            char c =
                    value.charAt(i);

            switch (c) {

                case '"':
                    escaped.append("\\\"");
                    break;

                case '\\':
                    escaped.append("\\\\");
                    break;

                case '\b':
                    escaped.append("\\b");
                    break;

                case '\f':
                    escaped.append("\\f");
                    break;

                case '\n':
                    escaped.append("\\n");
                    break;

                case '\r':
                    escaped.append("\\r");
                    break;

                case '\t':
                    escaped.append("\\t");
                    break;

                default:
                    escaped.append(c);
                    break;
            }
        }

        return escaped.toString();
    }

    private static long elapsedMs(
            long startNs
    ) {

        long elapsedNs =
                System.nanoTime()
                        - startNs;

        return elapsedNs
                / 1000000L;
    }

    private static String safeString(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }
}