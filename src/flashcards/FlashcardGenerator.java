package flashcards;

import java.nio.file.Path;
import java.util.List;

public class FlashcardGenerator {

    public static void main(String[] args) {

        // Step 1: Validate command-line arguments
        if (args.length == 0) {
            System.err.println("ERROR: No notes file provided.");
            System.err.println("Usage: java flashcards.FlashcardGenerator <path-to-notes-file>");
            System.exit(1);
        }
        String notesFilePath = args[0];

        // Step 2: Read API key from environment
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("ERROR: ANTHROPIC_API_KEY environment variable is not set");
            System.exit(1);
        }

        // Step 3: Orchestrate the workers
        try {
            NotesReader notesReader = new NotesReader();
            PromptBuilder promptBuilder = new PromptBuilder();
            ClaudeClient claudeClient = new ClaudeClient(apiKey);
            FlashcardParser parser = new FlashcardParser();
            FlashcardWriter writer = new FlashcardWriter();

            System.out.println("Reading notes from: " + notesFilePath);
            String notes = notesReader.readFromFile(notesFilePath);

            System.out.println("Generating flashcards...");
            String prompt = promptBuilder.buildFlashcardPrompt(notes);
            String apiResponse = claudeClient.sendMessage(prompt);
            List<Flashcard> flashcards = parser.parse(apiResponse);

            // Step 4: Print to console
            printFlashcards(flashcards);

            // Step 5: Save to file
            Path savedTo = writer.writeToFile(flashcards);
            System.out.println("Saved to: " + savedTo.toAbsolutePath());

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printFlashcards(List<Flashcard> flashcards) {
        System.out.println("\n=== Generated Flashcards ===\n");
        int i = 1;
        for (Flashcard card : flashcards) {
            System.out.println("--- Flashcard " + i + " ---");
            System.out.println(card);
            System.out.println();
            i++;
        }
    }
}