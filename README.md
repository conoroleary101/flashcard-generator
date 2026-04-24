# Flashcard Generator

A Java command-line tool that uses the Claude API to generate study flashcards from any text file of lecture notes.

## About

This is a learning project that I'm building as a 2nd year Software Development student at MTU Cork.
The goal is to improve my skills with LLM APIs, HTTP clients, JSON parsing, and clean OOP design in Java.

## Status

**Sessions 1, 2, and 3 complete.** Reads lecture notes from a file, generates 5 structured flashcards via the Claude API, prints them to the console, and saves them to a timestamped output file. Built with proper class separation, error handling, and exit codes.

## Tech

- Java 25 (language level 21)
- Claude Sonnet 4.5 via Anthropic API
- Gson 2.13.2 for JSON parsing
- `java.net.http.HttpClient` for API requests
- `java.nio.file` for file I/O

## Running

1. Set environment variable `ANTHROPIC_API_KEY` to your Anthropic API key.
2. Open in IntelliJ.
3. In Run Configuration, set **Program arguments** to the path of your notes file (e.g. `test-notes.txt`).
4. Run `FlashcardGenerator`.

A timestamped output file (e.g. `flashcards-2026-04-24-2330.txt`) is created in the project root.

## Project Structure

```
flashcard-generator/
├── lib/
│   └── gson-2.13.2.jar
├── src/
│   └── flashcards/
│       ├── FlashcardGenerator.java   Main entry point and orchestration
│       ├── ClaudeClient.java         Handles all communication with the Claude API
│       ├── PromptBuilder.java        Constructs flashcard prompts from notes
│       ├── FlashcardParser.java      Parses the API response into Flashcard objects
│       ├── NotesReader.java          Reads notes from a text file with validation
│       ├── FlashcardWriter.java      Writes flashcards to a timestamped output file
│       ├── Flashcard.java            Data class for a single flashcard
│       ├── FlashcardResponse.java    Wrapper class for parsing the JSON response
│       └── FlashcardException.java   Custom exception for clear error handling
├── test-notes.txt                    Example input file
├── .gitignore
└── README.md
```

## Exit Codes

The program uses meaningful exit codes for use in scripts:

| Code | Meaning                                        |
|------|------------------------------------------------|
| 0    | Success                                        |
| 1    | Bad usage (no file argument or no API key)     |
| 2    | File error (notes file missing or unreadable)  |
| 3    | Flashcard generation failed (API or parsing)   |
| 99   | Unexpected error                               |

## What I learned (Session 1)

- How HTTP requests and headers work.
- How to authenticate with an API using keys stored in environment variables.
- JSON syntax rules (strings need quotes, colons separate keys from values).
- Reading API error responses to debug issues (400 vs 401 vs parsing errors).
- Builder pattern in Java (`HttpRequest.newBuilder()...`).

## What I learned (Session 2)

- Git fundamentals: `init`, `add`, `commit`, `push`, `.gitignore`.
- GitHub workflow: creating repos, setting remotes, authenticating pushes.
- Why coding projects should live outside cloud-sync folders like OneDrive.
- Prompt engineering: role prompts, format specification, constraints, example shapes.
- Using String placeholders (`%s`) with `String.format` and `.formatted()`.
- Escaping special characters to embed prose safely inside JSON.
- Adding an external JAR library (Gson) to an IntelliJ project.
- Parsing nested JSON responses with Gson (generic `JsonObject` vs direct class mapping).
- POJO design: fields, getters, `toString` override for clean printing.

## What I learned (Session 3)

- **Refactoring without breaking behaviour:** extracted one large file into seven focused classes while keeping output identical.
- **Single Responsibility Principle:** each class does one thing well.
- **Encapsulation:** private helper methods, clean public interfaces.
- **High cohesion and low coupling** in practice.
- File I/O with modern Java: `Files.readString`, `Files.writeString`, `Path.of`.
- `StringBuilder` vs string concatenation for performance.
- Reading command-line arguments via `String[] args`.
- Handling Windows CRLF line endings when escaping for JSON.
- Custom exceptions: defining a `FlashcardException` to distinguish project-specific errors from generic ones.
- Exit codes as a contract for scripts and other tools.
- Handling `InterruptedException` correctly with `Thread.currentThread().interrupt()`.
- Using `git rm --cached` to untrack files without deleting them, plus matching `.gitignore` patterns.

## Known Limitations

- Currently uses a fixed model and request format. No way to swap models without editing code.
- No retry logic if the API is temporarily down.
- Only handles plain text input. PDFs, DOCX, slides not supported.
- Best suited for conceptual material (OOP, databases, networking concepts) rather than calculation-heavy subjects.

## Next Up

Session 4 ideas:
- Configurable number of flashcards (currently fixed at 5).
- Support for multiple output formats (Markdown, JSON, CSV).
- A simple unit test suite for the parser.
- Read API key from a `.env` file as a fallback if environment variable not set.
