package com.example.morse_messenger;

import org.junit.Test;

import static org.junit.Assert.*;

public class MorseUnitTest {

    @Test
    public void morseEncode_shouldEncodeEnglishLettersAndDigits() {
        // Arrange
        String a = "a";
        String b = "B";
        String five = "5";
        String unknown = "#";

        // Act + Assert
        assertEquals(".-", MainActivity.morseEncode(a));
        assertEquals("-...", MainActivity.morseEncode(b));     // проверяем lower/upper
        assertEquals(".....", MainActivity.morseEncode(five));
        assertEquals("", MainActivity.morseEncode(unknown));   // неизвестный символ → пусто
    }
}
