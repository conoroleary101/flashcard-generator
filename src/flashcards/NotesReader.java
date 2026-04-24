package flashcards;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NotesReader {

    public String readFromFile(String filePath) throws IOException{
        if(filePath == null || filePath.isBlank()){
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        Path path = Path.of(filePath);

        if(!Files.exists(path)){
            throw new IOException("Notes file not found " + filePath);
        }

        if(!Files.isRegularFile(path)){
            throw new IOException("Path is not a file: " + filePath);
        }

        String content = Files.readString(path);

        if(content.isBlank()){
            throw new IOException("Notes file is empty: " + filePath);
        }

        return content;
    }
}
