package com.example.morse_messenger;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LanguageIntegrationTest {

    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        sharedPreferences = context.getSharedPreferences("test_morse_prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    @Test
    public void testLanguageSwitchingInRealScenario() {
        String englishText = "HELLO WORLD";
        String expectedEnglishMorse = ".... . .-.. .-.. --- / .-- --- .-. .-.. -..";

        String morseResult = textToMorse(englishText);
        assertEquals(expectedEnglishMorse, morseResult);

        String decodedEnglish = morseToText(morseResult, "en");
        assertEquals(englishText, decodedEnglish);

        String russianText = "ПРИВЕТ МИР";
        String russianMorse = textToMorse(russianText);

        String decodedRussian = morseToText(russianMorse, "ru");
        assertEquals(russianText, decodedRussian);

        String wrongDecoded = morseToText(russianMorse, "en");
        assertNotEquals(russianText, wrongDecoded);

        String mixedText = "TEST 123!";
        String mixedMorse = textToMorse(mixedText);

        String decodedFromEnglish = morseToText(mixedMorse, "en");
        assertEquals(mixedText, decodedFromEnglish);

        String decodedFromRussian = morseToText(mixedMorse, "ru");
        assertEquals("ТЕСТ 123!", decodedFromRussian);
    }

    @Test
    public void testRealTimeLanguageSwitch() {
        String morseCode = ".- .-. .. .-- . .-";

        String englishResult = morseToText(morseCode, "en");
        assertEquals("ARIWEA", englishResult);

        String russianResult = morseToText(morseCode, "ru");
        assertEquals("АРИВЕА", russianResult);

        String realRussianMorse = ".--. .-. .. .-- . -";
        String realRussianText = morseToText(realRussianMorse, "ru");
        assertEquals("ПРИВЕТ", realRussianText);

        String englishFromRussianMorse = morseToText(realRussianMorse, "en");
        assertEquals("PRIWET", englishFromRussianMorse);
    }

    @Test
    public void testSharedPreferencesIntegration() {
        sharedPreferences.edit().putString("selected_language", "ru").apply();

        String savedLanguage = sharedPreferences.getString("selected_language", "en");
        assertEquals("ru", savedLanguage);

        String morseCode = ".- .-. .. .-- . .-";
        String result = morseToText(morseCode, savedLanguage);
        assertEquals("АРИВЕА", result);

        sharedPreferences.edit().putString("selected_language", "en").apply();
        savedLanguage = sharedPreferences.getString("selected_language", "ru");
        assertEquals("en", savedLanguage);

        result = morseToText(morseCode, savedLanguage);
        assertEquals("ARIWEA", result);
    }

    @Test
    public void testComplexMultilingualScenario() {
        String englishPart = "HELLO, MY NAME IS";
        String morseEnglish = textToMorse(englishPart);
        String decodedEnglish = morseToText(morseEnglish, "en");
        assertEquals(englishPart, decodedEnglish);

        String russianPart = "АЛЕКСЕЙ";
        String morseRussian = textToMorse(russianPart);

        String wrongDecode = morseToText(morseRussian, "en");
        assertEquals("ALEKSEJ", wrongDecode);

        String correctDecode = morseToText(morseRussian, "ru");
        assertEquals(russianPart, correctDecode);

        String numbers = "12345";
        String morseNumbers = textToMorse(numbers);

        assertEquals("12345", morseToText(morseNumbers, "en"));
        assertEquals("12345", morseToText(morseNumbers, "ru"));
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
