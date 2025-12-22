package com.example.morse_messenger;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MorseTranslationTest {

    @Test
    public void encodeEnglishLetter() {
        assertEquals(".-", MainActivity.morseEncode("A"));
        assertEquals("--..", MainActivity.morseEncode("Z"));
    }

    @Test
    public void encodeRussianLetter() {
        assertEquals(".-", MainActivity.morseEncode("а"));
        assertEquals(".-.-", MainActivity.morseEncode("я"));
    }

    @Test
    public void encodeDigit() {
        assertEquals("-----", MainActivity.morseEncode("0"));
        assertEquals("....-", MainActivity.morseEncode("4"));
    }

    @Test
    public void decodeEnglishMorse() {
        assertEquals("A", MainActivity.morseDecodeSwitch(".-", "en"));
        assertEquals("HELLO", "H E L L O".replace(" ", "")
                .replaceAll("H", MainActivity.morseDecodeSwitch("....", "en"))
                .replaceAll("E", MainActivity.morseDecodeSwitch(".", "en"))
                .replaceAll("L", MainActivity.morseDecodeSwitch(".-..", "en"))
                .replaceAll("O", MainActivity.morseDecodeSwitch("---", "en")));
        // Проще по одному:
        assertEquals("SOS",
                MainActivity.morseDecodeSwitch("...", "en") +
                        MainActivity.morseDecodeSwitch("---", "en") +
                        MainActivity.morseDecodeSwitch("...", "en")
        );
    }

    @Test
    public void decodeRussianMorse() {
        assertEquals("А", MainActivity.morseDecodeSwitch(".-", "ru"));
        assertEquals("ПРИВЕТ",
                MainActivity.morseDecodeSwitch(".--.", "ru") +
                        MainActivity.morseDecodeSwitch(".-.", "ru") +
                        MainActivity.morseDecodeSwitch("..", "ru") +
                        MainActivity.morseDecodeSwitch(".--", "ru") +
                        MainActivity.morseDecodeSwitch(".", "ru") +
                        MainActivity.morseDecodeSwitch("-", "ru")
        );
    }

    @Test
    public void decodeSpace() {
        assertEquals(" ", MainActivity.morseDecodeSwitch("/", "en"));
        assertEquals(" ", MainActivity.morseDecodeSwitch("/", "ru"));
    }

    @Test
    public void decodeUnknownReturnsEmpty() {
        assertEquals("", MainActivity.morseDecodeSwitch("........", "en"));
        assertEquals("", MainActivity.morseEncode("!@#$%"));
    }
}