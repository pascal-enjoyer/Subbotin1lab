package com.example.morse_messenger;

import org.junit.Test;

import static org.junit.Assert.*;

public class EdgeCasesMorseTest {

    @Test
    public void encode_shouldHandleEmptyString() {
        String result = textToMorse("");
        assertEquals("", result);
    }

    @Test
    public void encode_shouldHandleOnlySpaces() {
        String result = textToMorse("   ");
        assertEquals("", result);
    }

    @Test
    public void encode_shouldHandleMixedCase() {
        assertEquals(".-", MainActivity.morseEncode("a"));
        assertEquals(".-", MainActivity.morseEncode("A"));
        assertEquals(".-", MainActivity.morseEncode("а"));
        assertEquals(".-", MainActivity.morseEncode("А"));
    }

    @Test
    public void encode_shouldHandleUnknownCharacters() {
        assertEquals("", MainActivity.morseEncode("#"));
        assertEquals("", MainActivity.morseEncode("~"));
        assertEquals("", MainActivity.morseEncode("`"));
        assertEquals("", MainActivity.morseEncode("|"));
    }

    @Test
    public void decode_shouldHandleInvalidMorseCode() {
        assertEquals("", MainActivity.morseDecodeSwitch("......", "en"));
        assertEquals("", MainActivity.morseDecodeSwitch("......", "ru"));
        assertEquals("", MainActivity.morseDecodeSwitch("", "en"));
        assertEquals("", MainActivity.morseDecodeSwitch(" ", "en"));
    }

    @Test
    public void decode_shouldHandleMixedLanguageSymbols() {
        assertEquals("1", MainActivity.morseDecodeSwitch(".----", "en"));
        assertEquals("1", MainActivity.morseDecodeSwitch(".----", "ru"));

        assertEquals(",", MainActivity.morseDecodeSwitch("--..--", "en"));
        assertEquals(",", MainActivity.morseDecodeSwitch("--..--", "ru"));
    }

    @Test
    public void roundTrip_shouldPreservePunctuation() {
        String text = "Hello, World! How are you?";
        String morse = textToMorse(text);
        String decoded = morseToText(morse, "en");
        assertEquals(text.toUpperCase(), decoded);
    }

    @Test
    public void roundTrip_shouldHandleMultipleSpacesBetweenWords() {
        String morse = ".... . .-.. .-.. ---   /   .-- --- .-. .-.. -..";
        String decoded = morseToText(morse, "en");
        assertEquals("HELLO WORLD", decoded);
    }

    private String textToMorse(String text) {
        if (text == null || text.trim().isEmpty()) return "";

        StringBuilder result = new StringBuilder();
        String[] letters = text.split("");

        for (String letter : letters) {
            String morse = MainActivity.morseEncode(letter);
            if (!morse.isEmpty()) {
                result.append(morse).append(" ");
            }
        }

        return result.toString()
                .trim()
                .replaceAll("\\s+/\\s+", " / ")
                .replaceAll("\\s{2,}", " ");
    }

    private String morseToText(String morse, String language) {
        if (morse == null || morse.trim().isEmpty()) return "";

        String normalized = morse.trim().replaceAll("\\s+", " ");

        StringBuilder textResult = new StringBuilder();
        String[] words = normalized.split("/");

        for (int i = 0; i < words.length; i++) {
            String word = words[i].trim();
            if (word.isEmpty()) continue;

            String[] morseChars = word.split(" ");
            for (String morseChar : morseChars) {
                if (!morseChar.isEmpty()) {
                    String decoded = MainActivity.morseDecodeSwitch(morseChar, language);
                    if (!decoded.isEmpty()) {
                        textResult.append(decoded);
                    }
                }
            }

            if (i < words.length - 1) {
                textResult.append(" ");
            }
        }

        return textResult.toString();
    }
}
