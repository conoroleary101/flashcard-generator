package flashcards;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlashcardWriter {

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmm");

    public Path writeToFile(List<Flashcard> flashcards) throws IOException{
        if(flashcards == null || flashcards.isEmpty()){
            throw new IllegalArgumentException("Flashcards list cannot be null or empty");
        }

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String filename = "flashcards-" + timestamp + ".txt";
        Path outputPath = Path.of(filename);

        StringBuilder content = new StringBuilder();
        content.append("=== Generated Flashcards ===\n");
        content.append("Created: ").append(LocalDateTime.now()).append("\n\n");

        int i = 1;
        for(Flashcard card : flashcards){
            content.append("--- Flashcard ").append(i).append(" ---\n");
            content.append(card.toString()).append("\n\n");
            i++;
        }

        Files.writeString(outputPath, content.toString());
        return outputPath;
    }
}
