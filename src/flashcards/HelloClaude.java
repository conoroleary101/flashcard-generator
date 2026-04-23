package flashcards;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelloClaude {
    public static void main(String[] args) throws Exception {

        //Step 1: Read API key from environment variables
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("ERROR: ANTHROPIC_API_KEY environment variable is not set.");
            return;
        }

        //Step 2: Build JSON request body
        String requestBody = """
                {
                "model": "claude-sonnet-4-5",
                "max_tokens": 200,
                "messages": [
                {"role": "user", "content": "Say hello in one sentence."}
                    ]
                }
                """;

        //Step 3: Build HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.anthropic.com/v1/messages"))
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        //Step 4: Send request and get response
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Step 5: Print what we got back
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response body");
        System.out.println(response.body());

    }
}
