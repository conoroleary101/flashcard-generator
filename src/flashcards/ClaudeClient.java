package flashcards;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClaudeClient {

    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final String API_VERSION = "2023-06-01";
    private static final String MODEL = "claude-sonnet-4-5";
    private static final int MAX_TOKENS = 2000;

    private final String apiKey;
    private final HttpClient httpClient;

    public ClaudeClient(String apiKey){
        if(apiKey == null || apiKey.isEmpty()){
            throw new IllegalArgumentException("API key cannot be null or empty");
        }
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    public String sendMessage(String userPrompt) throws Exception {

        //Escape the prompt for safe JSON inclusion
        String escaped = userPrompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        //Build the JSON request body
        String requestBody = """
                {
                  "model": "%s",
                  "max_tokens": %d,
                  "messages": [
                    {"role": "user", "content": "%s"}
                  ]
                }
                """.formatted(MODEL,MAX_TOKENS,escaped);

        //Build HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("x-api-key", apiKey)
                .header("anthropic-version", API_VERSION)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        //Send and get response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        //Error check
        if(response.statusCode() != 200){
            throw new RuntimeException(
                    "API call failed with status " + response.statusCode() + ": " + response.body()
            );
        }

        return response.body();
    }
}
