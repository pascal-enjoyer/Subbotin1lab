package com.example.morse_messenger;

import org.junit.Test;

import static org.junit.Assert.*;

public class BigUnitMorseTranslatorTest {

    @Test
    public void encode_shouldHandleCaseLettersDigitsAndPunctuation() {
        assertEquals(".-", MainActivity.morseEncode("a"));
        assertEquals(".-", MainActivity.morseEncode("A"));

        assertEquals(".-", MainActivity.morseEncode("а"));
        assertEquals(".-", MainActivity.morseEncode("А"));

        assertEquals(".----", MainActivity.morseEncode("1"));
        assertEquals("-----", MainActivity.morseEncode("0"));

        assertEquals("--..--", MainActivity.morseEncode(","));
        assertEquals(".-.-.-", MainActivity.morseEncode("."));
        assertEquals("..--..", MainActivity.morseEncode("?"));
        assertEquals("-.-.--", MainActivity.morseEncode("!"));

        assertEquals("/ ", MainActivity.morseEncode(" "));

        assertEquals("", MainActivity.morseEncode("#"));
        assertEquals("", MainActivity.morseEncode("~"));
    }

    @Test
    public void decode_shouldReturnDifferentLettersDependingOnLanguage() {
        assertEquals("A", MainActivity.morseDecodeSwitch(".-", "en"));
        assertEquals("А", MainActivity.morseDecodeSwitch(".-", "ru"));

        assertEquals("S", MainActivity.morseDecodeSwitch("...", "en"));
        assertEquals("O", MainActivity.morseDecodeSwitch("---", "en"));

        assertEquals("П", MainActivity.morseDecodeSwitch(".--.", "ru"));
        assertEquals("Р", MainActivity.morseDecodeSwitch(".-.", "ru"));

        assertEquals("9", MainActivity.morseDecodeSwitch("----.", "en"));
        assertEquals("9", MainActivity.morseDecodeSwitch("----.", "ru"));

        assertEquals("", MainActivity.morseDecodeSwitch("......", "en"));
        assertEquals("", MainActivity.morseDecodeSwitch("......", "ru"));
    }

    @Test
    public void roundTrip_englishPhraseWithDigitsAndPunctuation() {
        String text = "HELLO, WORLD! 123";

        String morse = textToMorse(text);
        assertEquals(
                ".... . .-.. .-.. --- --..-- / .-- --- .-. .-.. -.. -.-.-- / .---- ..--- ...--",
                morse
        );

        String decoded = morseToText(morse, "en");
        assertEquals("HELLO, WORLD! 123", decoded);
    }

    @Test
    public void roundTrip_russianWord() {
        String text = "ПРИВЕТ";

        String morse = textToMorse(text);
        assertEquals(".--. .-. .. .-- . -", morse);

        String decoded = morseToText(morse, "ru");
        assertEquals("ПРИВЕТ", decoded);
    }

    @Test
    public void decode_shouldHandleExtraSpacesAndSlashes() {
        String morse = "....   .  .-.. .-.. ---   /   .-- --- .-. .-.. -..";
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
