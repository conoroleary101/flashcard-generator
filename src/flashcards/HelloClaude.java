package flashcards;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HelloClaude {

    private static final String FLASHCARD_PROMPT = """
        You are a study flashcard generator for university students.

        Given the lecture notes below, generate 5 high-quality flashcards.

        Requirements:
        - Each flashcard has a "question" and an "answer".
        - Questions should test understanding, not just recall.
        - Answers should be 1-2 sentences, clear and concise.
        - Do not include explanations, preamble, or closing text.

        Respond with ONLY a JSON object in this exact format:
        {
          "flashcards": [
            {"question": "...", "answer": "..."},
            {"question": "...", "answer": "..."}
          ]
        }

        Lecture notes:
        ---
        %s
        ---
        """;

    private static final String SAMPLE_NOTES = """
        A primary key is a column or set of columns in a database table \
        that uniquely identifies each row. It must be unique and cannot \
        be null. A foreign key is a column that references the primary \
        key of another table, creating a relationship between the two \
        tables. Foreign keys enforce referential integrity, meaning a \
        value in the foreign key column must exist in the referenced \
        primary key column.
        """;

    public static void main(String[] args) throws Exception {
        //Step 1: Read API key from environment variables
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("ERROR: ANTHROPIC_API_KEY environment variable is not set.");
            return;
        }

        // Step 2: Build the user prompt by inserting notes into the template
        String userPrompt = String.format(FLASHCARD_PROMPT, SAMPLE_NOTES);

        // Step 3: Escape the prompt for safe inclusion in JSON
        String escapedPrompt = userPrompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        // Step 4: Build JSON request body
        String requestBody = """
        {
          "model": "claude-sonnet-4-5",
          "max_tokens": 2000,
          "messages": [
            {"role": "user", "content": "%s"}
          ]
        }
        """.formatted(escapedPrompt);

        //Step 5: Build HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.anthropic.com/v1/messages"))
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        //Step 6: Send request and get response
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Step 7: Print what we got back
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response body");
        System.out.println(response.body());

    }

}
