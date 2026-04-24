package flashcards;

public class FlashcardException extends Exception {

    public FlashcardException(String message) {
        super(message);
    }

    public FlashcardException(String message, Throwable cause) {
        super(message, cause);
    }
}