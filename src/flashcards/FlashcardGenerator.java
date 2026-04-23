package flashcards;

import java.util.List;

public class FlashcardGenerator {

    public static void main(String[] args){

        //Step 1: Read API key from environment variables
        String apiKey = System.getenv("ANTHROPIC_API_KEY");
        if(apiKey == null || apiKey.isEmpty()) {
            System.err.println("ERROR: ANTHROPIC_API_KEY environment variable is not set");
            System.exit(1);
        }

        //Step 2: The notes to generate flashcards from (temporarily hardcoded, Part B replaces this file input)
        String notes = """
                Correlation measures the strength and direction of a linear relationship
                between two variables. The correlation coefficient, denoted r, ranges from
                negative one to positive one. A value near positive one indicates a strong
                positive relationship. Correlation does not imply causation: two variables
                may move together without one causing the other, due to coincidence, reverse
                causation, or a lurking variable that influences both.
                """;

        //Step 3: Orchestrate the three workers
        try{
            PromptBuilder promptBuilder = new PromptBuilder();
            ClaudeClient claudeClient = new ClaudeClient(apiKey);
            FlashcardParser parser = new FlashcardParser();

            String prompt = promptBuilder.buildFlashcardPrompt(notes);
            String apiResponse = claudeClient.sendMessage(prompt);
            List<Flashcard> flashcards = parser.parse(apiResponse);

            //Step 4: Print flashcards
            printFlashcards(flashcards);
        } catch (Exception e){
            System.err.println("ERROR: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printFlashcards(List<Flashcard> flashcards){
        System.out.println("=== Generated Flashcards ===\n");
        int i = 1;
        for (Flashcard card : flashcards){
            System.out.println("--- Flashcard " + i + " ---");
            System.out.println(card);
            System.out.println();
            i++;
        }
    }
}
