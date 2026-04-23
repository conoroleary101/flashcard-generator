# Flashcard Generator

A java command-line tool that uses Claude API to generate flashcards from lecture notes.

## About

This is a learning project that I'm building as a 2nd year Software Development student at MTU Cork.
The goal is to improve my skills with LLM APIs, HTTP clients, and JSON parsing in Java.

## Status

**Session 1 complete.** Currently, it sends a simple "hello" prompt to the Claude API and prints the response.

## Tech

- Java 25 
- Claude Sonnet 4.5 via Anthropic API
- No external dependencies (uses built-in `java.net.http.HttpClient`)

## Running

1. Set environment variable `ANTHROPIC_API_KEY` to your Anthropic API key.
2. Open in IntelliJ.
3. Run `HelloClaude.java`.

## What I learned (Session 1)

- How HTTP requests and headers work.
- How to authenticate with an API using keys stored in environment variables.
- JSON syntax rules (strings need quotes, colons separate keys from values, etc.).
- Reading API error responses to debug issues (400 vs 401 vs parsing errors).
- Builder pattern in Java (`HttpRequest.newBuilder()...`).

## Next Up

Session 2: Make Claude return structured flashcards as JSON, parse them into a `Flashcard` class.