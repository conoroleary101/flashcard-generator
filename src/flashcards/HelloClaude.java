package flashcards;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

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

        // Step 7: Check for HTTP errors
        if (response.statusCode() != 200) {
            System.err.println("API call failed with status " + response.statusCode());
            System.err.println(response.body());
            return;
        }

        // Step 8: Parse the outer API response to extract Claude's text
        Gson gson = new Gson();
        JsonObject apiResponse = gson.fromJson(response.body(), JsonObject.class);
        JsonArray content = apiResponse.getAsJsonArray("content");
        String claudeText = content.get(0).getAsJsonObject().get("text").getAsString();

        // Step 9: Clean up markdown code fences if present
        String cleaned = claudeText
                .replace("```json", "")
                .replace("```", "")
                .trim();

        // Step 10: Parse the inner JSON into our FlashcardResponse object
        FlashcardResponse flashcardResponse = gson.fromJson(cleaned, FlashcardResponse.class);

        // Step 11: Print the flashcards cleanly
        System.out.println("=== Generated Flashcards ===\n");
        int i = 1;
        for (Flashcard card : flashcardResponse.getFlashcards()) {
            System.out.println("--- Flashcard " + i + " ---");
            System.out.println(card);
            System.out.println();
            i++;
        }

    }

}
