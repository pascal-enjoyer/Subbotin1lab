package com.example.morse_messenger;

import org.junit.Test;
import static org.junit.Assert.*;

public class EdgeCasesMorseTest {
    
    @Test
    public void encode_shouldHandleEmptyString() {
        // Проверяем, что пустая строка не вызывает ошибок
        String result = textToMorse("");
        assertEquals("", result);
    }
    
    @Test
    public void encode_shouldHandleOnlySpaces() {
        // Проверяем обработку строки только из пробелов
        String result = textToMorse("   ");
        assertEquals("/ /", result); // Три пробела -> три разделителя слов
    }
    
    @Test
    public void encode_shouldHandleMixedCase() {
        // Проверяем, что регистр не имеет значения
        assertEquals(".-", MainActivity.morseEncode("a"));
        assertEquals(".-", MainActivity.morseEncode("A"));
        assertEquals(".-", MainActivity.morseEncode("а"));
        assertEquals(".-", MainActivity.morseEncode("А"));
    }
    
    @Test
    public void encode_shouldHandleUnknownCharacters() {
        // Проверяем, что неизвестные символы возвращают пустую строку
        assertEquals("", MainActivity.morseEncode("#"));
        assertEquals("", MainActivity.morseEncode("~"));
        assertEquals("", MainActivity.morseEncode("`"));
        assertEquals("", MainActivity.morseEncode("|"));
    }
    
    @Test
    public void decode_shouldHandleInvalidMorseCode() {
        // Проверяем декодирование несуществующего кода Морзе
        assertEquals("", MainActivity.morseDecodeSwitch("......", "en"));
        assertEquals("", MainActivity.morseDecodeSwitch("......", "ru"));
        assertEquals("", MainActivity.morseDecodeSwitch("", "en"));
        assertEquals("", MainActivity.morseDecodeSwitch(" ", "en"));
    }
    
    @Test
    public void decode_shouldHandleMixedLanguageSymbols() {
        // Проверяем, что символы, одинаковые в обоих языках, декодируются правильно
        assertEquals("1", MainActivity.morseDecodeSwitch(".----", "en"));
        assertEquals("1", MainActivity.morseDecodeSwitch(".----", "ru"));
        
        assertEquals(",", MainActivity.morseDecodeSwitch("--..--", "en"));
        assertEquals(",", MainActivity.morseDecodeSwitch("--..--", "ru"));
    }
    
    @Test
    public void roundTrip_shouldPreservePunctuation() {
        // Проверяем полный цикл с пунктуацией
        String text = "Hello, World! How are you?";
        String morse = textToMorse(text);
        String decoded = morseToText(morse, "en");
        
        // Приводим к верхнему регистру для сравнения (так как decode возвращает заглавные)
        assertEquals(text.toUpperCase(), decoded);
    }
    
    @Test
    public void roundTrip_shouldHandleMultipleSpacesBetweenWords() {
        // Проверяем обработку нескольких пробелов между словами
        String morse = ".... . .-.. .-.. ---   /   .-- --- .-. .-.. -..";
        String decoded = morseToText(morse, "en");
        assertEquals("HELLO WORLD", decoded);
    }
    
    // Вспомогательные методы для тестирования (аналогичные тем, что в IntegrationRoundTripTest)
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