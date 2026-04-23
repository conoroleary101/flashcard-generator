# Flashcard Generator

A java command-line tool that uses Claude API to generate flashcards from lecture notes.

## About

This is a learning project that I'm building as a 2nd year Software Development student at MTU Cork.
The goal is to improve my skills with LLM APIs, HTTP clients, and JSON parsing in Java.

## Status

**Session 1 and 2 complete.** Generates 5 structured flashcards from hardcoded lecture notes,
parses the JSON response, and prints them cleanly to the console.

## Tech

- Java 25 (language level 21)
- Claude Sonnet 4.5 via Anthropic API
- Gson 2.13.2 for JSON parsing
- `java.net.http.HttpClient` for API requests

## Running

1. Set environment variable `ANTHROPIC_API_KEY` to your Anthropic API key.
2. Open in IntelliJ.
3. Edit the `SAMPLE_NOTES` constant in the `HelloClaude.java` with your own notes.
4. Run `HelloClaude.java`.

## Project Structure
```
flashcard-generator/
├── lib/
│   └── gson-2.13.2.jar
├── src/
│   └── flashcards/
│       ├── HelloClaude.java            //Main entry point
│       ├── Flashcard.java              //Data class for a single card
│       └── FlashcardResponse.java      //Wrapper for parsing the response
├── .gitignore
└── README.md
```
## What I learned (Session 1)

- How HTTP requests and headers work.
- How to authenticate with an API using keys stored in environment variables.
- JSON syntax rules (strings need quotes, colons separate keys from values, etc.).
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
- POJO design: fields, getters, `toString`, override for clean printing.
- Evaluating a tool honestly against real use cases (found it fits conceptual subjects better than calculation-heavy ones like statistics).

## Known Limitations

- Input is hardcoded in source file. No file or CLI input yet.
- Special characters (double quotes, backslashes) in notes can break the JSON request.
- Output prints to console only, no save or export.
- Best suited for conceptual material(OOP, databases, networking concepts) rather than calculation-heavy subjects.

## Next Up

Session 3: Refactor into separate classes (ClaudeClient, PromptBuilder), read notes from a file instead of hardcoding, save output to a file, improve error handling.
