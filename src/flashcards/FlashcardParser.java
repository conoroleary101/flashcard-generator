package flashcards;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class FlashcardParser {

    private final Gson gson = new Gson();

    public List<Flashcard> parse(String apiResponse) throws FlashcardException {
        if (apiResponse == null || apiResponse.isBlank()) {
            throw new FlashcardException("API response cannot be null or empty");
        }

        try {
            // Step 1: Extract the text Claude generated from the outer API response
            String claudeText = extractClaudeText(apiResponse);

            // Step 2: Clean up any markdown code fences
            String cleaned = cleanMarkdownFences(claudeText);

            // Step 3: Parse the inner JSON into our FlashcardResponse object
            FlashcardResponse response = gson.fromJson(cleaned, FlashcardResponse.class);

            if (response == null || response.getFlashcards() == null) {
                throw new FlashcardException("Failed to parse flashcards from response: " + cleaned);
            }

            return response.getFlashcards();

        } catch (com.google.gson.JsonSyntaxException e) {
            throw new FlashcardException("Invalid JSON in Claude response: " + e.getMessage(), e);
        }
    }

    private String extractClaudeText(String apiResponse){
        JsonObject root = gson.fromJson(apiResponse, JsonObject.class);
        JsonArray content = root.getAsJsonArray("content");
        return content.get(0).getAsJsonObject().get("text").getAsString();
    }

    private String cleanMarkdownFences(String text){
        return text
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}
