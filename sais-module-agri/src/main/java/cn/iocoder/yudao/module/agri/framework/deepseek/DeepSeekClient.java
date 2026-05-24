package cn.iocoder.yudao.module.agri.framework.deepseek;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.time.Duration;

@Component
@ConditionalOnProperty(prefix = "yudao.agri.deepseek", name = "enabled", havingValue = "true")
@Slf4j
public class DeepSeekClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Resource
    private DeepSeekProperties properties;

    private OkHttpClient httpClient;

    public boolean isConfigured() {
        return hasText(properties.getApiKey());
    }

    public String complete(String prompt) throws IOException {
        if (!isConfigured()) {
            throw new IllegalStateException("DeepSeek API key is not configured.");
        }

        OkHttpClient client = getHttpClient();
        String body = buildRequestBody(prompt);
        Request request = new Request.Builder()
                .url(normalizeBaseUrl(properties.getBaseUrl()) + "/chat/completions")
                .addHeader("Authorization", "Bearer " + properties.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(body, JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            String responseText = responseBody != null ? responseBody.string() : "";
            if (!response.isSuccessful()) {
                throw new IOException("DeepSeek API error " + response.code() + ": " + responseText);
            }
            JsonNode root = OBJECT_MAPPER.readTree(responseText);
            return root.path("choices").path(0).path("message").path("content").asText("");
        }
    }

    private String buildRequestBody(String prompt) throws IOException {
        ObjectNode root = OBJECT_MAPPER.createObjectNode();
        root.put("model", properties.getModel());
        root.put("temperature", 0);
        root.put("max_tokens", 512);
        ObjectNode thinking = root.putObject("thinking");
        thinking.put("type", "disabled");

        ArrayNode messages = root.putArray("messages");
        ObjectNode user = messages.addObject();
        user.put("role", "user");
        user.put("content", prompt);

        return OBJECT_MAPPER.writeValueAsString(root);
    }

    private OkHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(Duration.ofMillis(properties.getTimeoutMs()))
                    .readTimeout(Duration.ofMillis(properties.getTimeoutMs()))
                    .writeTimeout(Duration.ofMillis(properties.getTimeoutMs()))
                    .callTimeout(Duration.ofMillis(properties.getTimeoutMs()))
                    .build();
            log.info("[DeepSeek] Client initialized: baseUrl={}, model={}",
                    properties.getBaseUrl(), properties.getModel());
        }
        return httpClient;
    }

    private static String normalizeBaseUrl(String baseUrl) {
        String value = hasText(baseUrl) ? baseUrl.trim() : "https://api.deepseek.com";
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

}
