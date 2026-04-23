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
        Correlation measures the strength and direction of a linear relationship
        between two variables. The correlation coefficient, denoted r, ranges from
        negative one to positive one. A value near positive one indicates a strong
        positive relationship where both variables increase together. A value near
        negative one indicates a strong negative relationship where one variable
        increases as the other decreases. A value near zero indicates little to no
        linear relationship. The coefficient of determination, R squared, represents
        the proportion of variation in the dependent variable that can be explained
        by the independent variable.

        A common mistake is confusing correlation with causation. Correlation does
        not imply causation. Two variables may move together without one causing
        the other. This can happen due to coincidence, reverse causation where
        the effect actually causes the supposed cause, or due to a lurking variable.
        A lurking variable is a third variable that influences both of the observed
        variables, creating an apparent relationship where none directly exists.
        A confounding variable is similar but specifically obscures the true
        relationship between the variables being studied.

        Scatter plots are the primary visual tool for examining relationships.
        A scatter plot displays paired data points on two axes. The overall
        pattern reveals the direction, form, and strength of the relationship.
        Outliers, unusual points far from the main cluster, can heavily influence
        the calculated correlation coefficient and should always be investigated.
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
