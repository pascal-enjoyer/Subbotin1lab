package com.example.morse_messenger;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntegrationRoundTripTest {

    @Test
    public void roundTrip_helloWorld_english() {
        String text = "HELLO WORLD";

        // text -> morse
        String morse = textToMorse(text);
        assertEquals(".... . .-.. .-.. --- / .-- --- .-. .-.. -..", morse);

        // morse -> text
        String decoded = morseToText(morse, "en");
        assertEquals("HELLO WORLD", decoded);
    }

    private String textToMorse(String text) {
        StringBuilder result = new StringBuilder();
        String[] letters = text.split("");
        for (String letter : letters) {
            String morse = MainActivity.morseEncode(letter);
            if (!morse.isEmpty()) {
                // В твоём encode пробел мапится в "/ "
                // поэтому просто добавляем как есть + " " для разделения символов
                result.append(morse).append(" ");
            }
        }
        // Нормализуем: "/  " -> "/ "
        return result.toString().trim().replaceAll("\\s+/\\s+", " / ");
    }

    private String morseToText(String morse, String language) {
        StringBuilder textResult = new StringBuilder();
        String[] words = morse.split("/");

        for (int i = 0; i < words.length; i++) {
            String word = words[i].trim();
            if (word.isEmpty()) continue;

            String[] morseChars = word.split("\\s+");
            for (String morseChar : morseChars) {
                if (!morseChar.isEmpty()) {
                    String decodedChar = MainActivity.morseDecodeSwitch(morseChar, language);
                    if (!decodedChar.isEmpty()) {
                        textResult.append(decodedChar);
                    }
                }
            }

            if (i < words.length - 1) textResult.append(" ");
        }

        return textResult.toString();
    }
}
