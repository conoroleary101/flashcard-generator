package flashcards;

public class Flashcard {
    private String question;
    private String answer;

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "Q: " + question + "\nA: " + answer;
    }
}
