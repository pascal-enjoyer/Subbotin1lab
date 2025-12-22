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
    
    private Context context;
    private SharedPreferences sharedPreferences;
    
    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        
        // Используем тестовые SharedPreferences
        sharedPreferences = context.getSharedPreferences(
            "test_morse_prefs", 
            Context.MODE_PRIVATE
        );
        
        // Очищаем настройки перед каждым тестом
        sharedPreferences.edit().clear().apply();
    }
    
    @Test
    public void testLanguageSwitchingInRealScenario() {
        // Симулируем реальное использование: несколько переводов с переключением языка
        
        // 1. Создаем тестовую строку на английском
        String englishText = "HELLO WORLD";
        String expectedEnglishMorse = ".... . .-.. .-.. --- / .-- --- .-. .-.. -..";
        
        // 2. Переводим на английском языке
        String morseResult = textToMorse(englishText);
        assertEquals(expectedEnglishMorse, morseResult);
        
        // 3. Декодируем обратно на английском
        String decodedEnglish = morseToText(morseResult, "en");
        assertEquals(englishText, decodedEnglish);
        
        // 4. Теперь проверяем русский язык
        String russianText = "ПРИВЕТ МИР";
        
        // 5. Переводим русский текст в морзе
        String russianMorse = textToMorse(russianText);
        
        // 6. Декодируем русскую морзе на русском языке
        String decodedRussian = morseToText(russianMorse, "ru");
        assertEquals(russianText, decodedRussian);
        
        // 7. Проверяем, что если декодировать русскую морзе на английском,
        // получится неверный результат (буквы английские)
        String wrongDecoded = morseToText(russianMorse, "en");
        assertNotEquals(russianText, wrongDecoded);
        
        // 8. Проверяем, что цифры и знаки препинания работают в обоих языках
        String mixedText = "TEST 123!";
        String mixedMorse = textToMorse(mixedText);
        
        String decodedFromEnglish = morseToText(mixedMorse, "en");
        assertEquals(mixedText, decodedFromEnglish);
        
        // В русском языке цифры и пунктуация должны декодироваться так же
        String decodedFromRussian = morseToText(mixedMorse, "ru");
        assertEquals(mixedText, decodedFromRussian);
    }
    
    @Test
    public void testRealTimeLanguageSwitch() {
        // Тест имитирует поведение приложения при смене языка
        
        // 1. Пользователь вводит морзе-код
        String morseCode = ".- .-. .. .-- . .-";
        
        // 2. Сначала декодируем на английском
        String englishResult = morseToText(morseCode, "en");
        assertEquals("ARIVEA", englishResult); // "Привет" на английском даст ARIVEA
        
        // 3. Пользователь переключается на русский
        String russianResult = morseToText(morseCode, "ru");
        assertEquals("АРИВЕА", russianResult); // Те же символы на русском
        
        // 4. Проверяем реальное слово
        String realRussianMorse = ".--. .-. .. .-- . -"; // "ПРИВЕТ"
        String realRussianText = morseToText(realRussianMorse, "ru");
        assertEquals("ПРИВЕТ", realRussianText);
        
        // 5. На английском это декодируется как "PRIWET"
        String englishFromRussianMorse = morseToText(realRussianMorse, "en");
        assertEquals("PRIWET", englishFromRussianMorse);
    }
    
    @Test
    public void testSharedPreferencesIntegration() {
        // Интеграционный тест с SharedPreferences
        // (имитирует сохранение выбранного языка в настройках)
        
        // 1. Сохраняем выбор языка в SharedPreferences
        sharedPreferences.edit().putString("selected_language", "ru").apply();
        
        // 2. "Получаем" выбранный язык из настроек
        String savedLanguage = sharedPreferences.getString("selected_language", "en");
        assertEquals("ru", savedLanguage);
        
        // 3. Используем сохраненный язык для декодирования
        String morseCode = ".- .-. .. .-- . .-";
        String result = morseToText(morseCode, savedLanguage);
        assertEquals("АРИВЕА", result);
        
        // 4. Меняем язык в настройках
        sharedPreferences.edit().putString("selected_language", "en").apply();
        savedLanguage = sharedPreferences.getString("selected_language", "ru");
        assertEquals("en", savedLanguage);
        
        // 5. Декодируем с новым языком
        result = morseToText(morseCode, savedLanguage);
        assertEquals("ARIVEA", result);
    }
    
    @Test
    public void testComplexMultilingualScenario() {
        // Комплексный сценарий: смешанный текст с переключением языков
        
        // Сценарий: пользователь переводит сообщение на двух языках
        
        // Часть 1: Английское предложение
        String englishPart = "HELLO, MY NAME IS";
        String morseEnglish = textToMorse(englishPart);
        String decodedEnglish = morseToText(morseEnglish, "en");
        assertEquals(englishPart, decodedEnglish);
        
        // Часть 2: Русское слово (как если бы пользователь вставил русское слово)
        String russianPart = "АЛЕКСЕЙ";
        String morseRussian = textToMorse(russianPart);
        
        // Если декодировать русскую морзе на английском - получится "АЛЕКСЕЙ" латиницей
        String wrongDecode = morseToText(morseRussian, "en");
        assertEquals("ALEKSEJ", wrongDecode); // Транслитерация
        
        // Правильное декодирование на русском
        String correctDecode = morseToText(morseRussian, "ru");
        assertEquals(russianPart, correctDecode);
        
        // Часть 3: Цифры (одинаковы в обоих языках)
        String numbers = "12345";
        String morseNumbers = textToMorse(numbers);
        
        assertEquals("12345", morseToText(morseNumbers, "en"));
        assertEquals("12345", morseToText(morseNumbers, "ru"));
    }
    
    // Вспомогательные методы
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