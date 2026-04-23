package flashcards;

public class PromptBuilder {

    private static final String FLASHCARD_TEMPLATE = """
            You are a study flashcard generator for university students.
            
            Given the lecture notes below, generate 5 high-quality flashcards.
            
            Requirements:
            - Each flashcard has a "question" and an "answer"
            - Questions should test understanding, not just recall.
            - Answers should be 1-2 sentences, clear and concise.
            - Do not include explanations, preamble, or closing text.
            - Do not wrap the JSON in markdown code fences or backticks.
            
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

    public String buildFlashcardPrompt(String notes){
        if(notes == null || notes.isBlank()){
            throw new IllegalArgumentException("Notes cannot be null or empty");
        }
        return FLASHCARD_TEMPLATE.formatted(notes);
    }
}
