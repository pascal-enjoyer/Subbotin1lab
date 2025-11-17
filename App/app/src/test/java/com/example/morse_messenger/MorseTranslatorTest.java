package com.example.morse_messenger;

import org.junit.Test;
import static org.junit.Assert.*;

public class MorseTranslatorTest {

    // ===== ТЕСТЫ КОДИРОВАНИЯ (ТЕКСТ → МОРЗЕ) =====

    @Test
    public void testMorseEncode_EnglishLetters() {
        assertEquals(".-", MainActivity.morseEncode("a"));
        assertEquals("-...", MainActivity.morseEncode("b"));
        assertEquals("-.-.", MainActivity.morseEncode("c"));
        assertEquals("-..", MainActivity.morseEncode("d"));
        assertEquals(".", MainActivity.morseEncode("e"));
        assertEquals("..-.", MainActivity.morseEncode("f"));
        assertEquals("--.", MainActivity.morseEncode("g"));
        assertEquals("....", MainActivity.morseEncode("h"));
        assertEquals("..", MainActivity.morseEncode("i"));
        assertEquals(".---", MainActivity.morseEncode("j"));
        assertEquals("-.-", MainActivity.morseEncode("k"));
        assertEquals(".-..", MainActivity.morseEncode("l"));
        assertEquals("--", MainActivity.morseEncode("m"));
        assertEquals("-.", MainActivity.morseEncode("n"));
        assertEquals("---", MainActivity.morseEncode("o"));
        assertEquals(".--.", MainActivity.morseEncode("p"));
        assertEquals("--.-", MainActivity.morseEncode("q"));
        assertEquals(".-.", MainActivity.morseEncode("r"));
        assertEquals("...", MainActivity.morseEncode("s"));
        assertEquals("-", MainActivity.morseEncode("t"));
        assertEquals("..-", MainActivity.morseEncode("u"));
        assertEquals("...-", MainActivity.morseEncode("v"));
        assertEquals(".--", MainActivity.morseEncode("w"));
        assertEquals("-..-", MainActivity.morseEncode("x"));
        assertEquals("-.--", MainActivity.morseEncode("y"));
        assertEquals("--..", MainActivity.morseEncode("z"));
    }

    @Test
    public void testMorseEncode_RussianLetters() {
        assertEquals(".-", MainActivity.morseEncode("а"));
        assertEquals("-...", MainActivity.morseEncode("б"));
        assertEquals(".--", MainActivity.morseEncode("в"));
        assertEquals("--.", MainActivity.morseEncode("г"));
        assertEquals("-..", MainActivity.morseEncode("д"));
        assertEquals(".", MainActivity.morseEncode("е"));
        assertEquals(".", MainActivity.morseEncode("ё"));
        assertEquals("...-", MainActivity.morseEncode("ж"));
        assertEquals("--..", MainActivity.morseEncode("з"));
        assertEquals("..", MainActivity.morseEncode("и"));
        assertEquals(".---", MainActivity.morseEncode("й"));
        assertEquals("-.-", MainActivity.morseEncode("к"));
        assertEquals(".-..", MainActivity.morseEncode("л"));
        assertEquals("--", MainActivity.morseEncode("м"));
        assertEquals("-.", MainActivity.morseEncode("н"));
        assertEquals("---", MainActivity.morseEncode("о"));
        assertEquals(".--.", MainActivity.morseEncode("п"));
        assertEquals(".-.", MainActivity.morseEncode("р"));
        assertEquals("...", MainActivity.morseEncode("с"));
        assertEquals("-", MainActivity.morseEncode("т"));
        assertEquals("..-", MainActivity.morseEncode("у"));
        assertEquals("..-.", MainActivity.morseEncode("ф"));
        assertEquals("....", MainActivity.morseEncode("х"));
        assertEquals("-.-.", MainActivity.morseEncode("ц"));
        assertEquals("---.", MainActivity.morseEncode("ч"));
        assertEquals("----", MainActivity.morseEncode("ш"));
        assertEquals("--.-", MainActivity.morseEncode("щ"));
        assertEquals("--.--", MainActivity.morseEncode("ъ"));
        assertEquals("-.--", MainActivity.morseEncode("ы"));
        assertEquals("-..-", MainActivity.morseEncode("ь"));
        assertEquals("..-..", MainActivity.morseEncode("э"));
        assertEquals("..--", MainActivity.morseEncode("ю"));
        assertEquals(".-.-", MainActivity.morseEncode("я"));
    }

    @Test
    public void testMorseEncode_Numbers() {
        assertEquals("-----", MainActivity.morseEncode("0"));
        assertEquals(".----", MainActivity.morseEncode("1"));
        assertEquals("..---", MainActivity.morseEncode("2"));
        assertEquals("...--", MainActivity.morseEncode("3"));
        assertEquals("....-", MainActivity.morseEncode("4"));
        assertEquals(".....", MainActivity.morseEncode("5"));
        assertEquals("-....", MainActivity.morseEncode("6"));
        assertEquals("--...", MainActivity.morseEncode("7"));
        assertEquals("---..", MainActivity.morseEncode("8"));
        assertEquals("----.", MainActivity.morseEncode("9"));
    }

    @Test
    public void testMorseEncode_SpecialCharacters() {
        assertEquals("/ ", MainActivity.morseEncode(" "));
        assertEquals("--..--", MainActivity.morseEncode(","));
        assertEquals(".-.-.-", MainActivity.morseEncode("."));
        assertEquals("..--..", MainActivity.morseEncode("?"));
        assertEquals(".----.", MainActivity.morseEncode("'"));
        assertEquals("-.-.--", MainActivity.morseEncode("!"));
        assertEquals("-..-.", MainActivity.morseEncode("/"));
        assertEquals("-.--.", MainActivity.morseEncode("("));
        assertEquals("-.--.-", MainActivity.morseEncode(")"));
        assertEquals(".-...", MainActivity.morseEncode("&"));
        assertEquals("---...", MainActivity.morseEncode(":"));
        assertEquals("-.-.-.", MainActivity.morseEncode(";"));
        assertEquals("-...-", MainActivity.morseEncode("="));
        assertEquals(".-.-.", MainActivity.morseEncode("+"));
        assertEquals("-....-", MainActivity.morseEncode("-"));
        assertEquals("..--.-", MainActivity.morseEncode("_"));
        assertEquals(".-..-.", MainActivity.morseEncode("\""));
        assertEquals("...-..-", MainActivity.morseEncode("$"));
        assertEquals(".--.-.", MainActivity.morseEncode("@"));
    }

    @Test
    public void testMorseEncode_UpperCase() {
        assertEquals(".-", MainActivity.morseEncode("A"));
        assertEquals("-...", MainActivity.morseEncode("B"));
        assertEquals(".-", MainActivity.morseEncode("А"));
        assertEquals("-...", MainActivity.morseEncode("Б"));
    }

    @Test
    public void testMorseEncode_UnknownCharacter() {
        assertEquals("", MainActivity.morseEncode("#"));
        assertEquals("", MainActivity.morseEncode("`"));
        assertEquals("", MainActivity.morseEncode("~"));
        assertEquals("", MainActivity.morseEncode("|"));
    }

    @Test
    public void testMorseEncode_EmptyString() {
        assertEquals("", MainActivity.morseEncode(""));
    }

    // ===== ТЕСТЫ ДЕКОДИРОВАНИЯ (МОРЗЕ → ТЕКСТ) =====

    @Test
    public void testMorseDecode_EnglishLetters() {
        assertEquals("A", MainActivity.morseDecodeSwitch(".-", "en"));
        assertEquals("B", MainActivity.morseDecodeSwitch("-...", "en"));
        assertEquals("C", MainActivity.morseDecodeSwitch("-.-.", "en"));
        assertEquals("D", MainActivity.morseDecodeSwitch("-..", "en"));
        assertEquals("E", MainActivity.morseDecodeSwitch(".", "en"));
        assertEquals("F", MainActivity.morseDecodeSwitch("..-.", "en"));
        assertEquals("G", MainActivity.morseDecodeSwitch("--.", "en"));
        assertEquals("H", MainActivity.morseDecodeSwitch("....", "en"));
        assertEquals("I", MainActivity.morseDecodeSwitch("..", "en"));
        assertEquals("J", MainActivity.morseDecodeSwitch(".---", "en"));
        assertEquals("K", MainActivity.morseDecodeSwitch("-.-", "en"));
        assertEquals("L", MainActivity.morseDecodeSwitch(".-..", "en"));
        assertEquals("M", MainActivity.morseDecodeSwitch("--", "en"));
        assertEquals("N", MainActivity.morseDecodeSwitch("-.", "en"));
        assertEquals("O", MainActivity.morseDecodeSwitch("---", "en"));
        assertEquals("P", MainActivity.morseDecodeSwitch(".--.", "en"));
        assertEquals("Q", MainActivity.morseDecodeSwitch("--.-", "en"));
        assertEquals("R", MainActivity.morseDecodeSwitch(".-.", "en"));
        assertEquals("S", MainActivity.morseDecodeSwitch("...", "en"));
        assertEquals("T", MainActivity.morseDecodeSwitch("-", "en"));
        assertEquals("U", MainActivity.morseDecodeSwitch("..-", "en"));
        assertEquals("V", MainActivity.morseDecodeSwitch("...-", "en"));
        assertEquals("W", MainActivity.morseDecodeSwitch(".--", "en"));
        assertEquals("X", MainActivity.morseDecodeSwitch("-..-", "en"));
        assertEquals("Y", MainActivity.morseDecodeSwitch("-.--", "en"));
        assertEquals("Z", MainActivity.morseDecodeSwitch("--..", "en"));
    }

    @Test
    public void testMorseDecode_RussianLetters() {
        assertEquals("А", MainActivity.morseDecodeSwitch(".-", "ru"));
        assertEquals("Б", MainActivity.morseDecodeSwitch("-...", "ru"));
        assertEquals("В", MainActivity.morseDecodeSwitch(".--", "ru"));
        assertEquals("Г", MainActivity.morseDecodeSwitch("--.", "ru"));
        assertEquals("Д", MainActivity.morseDecodeSwitch("-..", "ru"));
        assertEquals("Е", MainActivity.morseDecodeSwitch(".", "ru"));
        assertEquals("Ж", MainActivity.morseDecodeSwitch("...-", "ru"));
        assertEquals("З", MainActivity.morseDecodeSwitch("--..", "ru"));
        assertEquals("И", MainActivity.morseDecodeSwitch("..", "ru"));
        assertEquals("Й", MainActivity.morseDecodeSwitch(".---", "ru"));
        assertEquals("К", MainActivity.morseDecodeSwitch("-.-", "ru"));
        assertEquals("Л", MainActivity.morseDecodeSwitch(".-..", "ru"));
        assertEquals("М", MainActivity.morseDecodeSwitch("--", "ru"));
        assertEquals("Н", MainActivity.morseDecodeSwitch("-.", "ru"));
        assertEquals("О", MainActivity.morseDecodeSwitch("---", "ru"));
        assertEquals("П", MainActivity.morseDecodeSwitch(".--.", "ru"));
        assertEquals("Р", MainActivity.morseDecodeSwitch(".-.", "ru"));
        assertEquals("С", MainActivity.morseDecodeSwitch("...", "ru"));
        assertEquals("Т", MainActivity.morseDecodeSwitch("-", "ru"));
        assertEquals("У", MainActivity.morseDecodeSwitch("..-", "ru"));
        assertEquals("Ф", MainActivity.morseDecodeSwitch("..-.", "ru"));
        assertEquals("Х", MainActivity.morseDecodeSwitch("....", "ru"));
        assertEquals("Ц", MainActivity.morseDecodeSwitch("-.-.", "ru"));
        assertEquals("Ч", MainActivity.morseDecodeSwitch("---.", "ru"));
        assertEquals("Ш", MainActivity.morseDecodeSwitch("----", "ru"));
        assertEquals("Щ", MainActivity.morseDecodeSwitch("--.-", "ru"));
        assertEquals("Ъ", MainActivity.morseDecodeSwitch("--.--", "ru"));
        assertEquals("Ы", MainActivity.morseDecodeSwitch("-.--", "ru"));
        assertEquals("Ь", MainActivity.morseDecodeSwitch("-..-", "ru"));
        assertEquals("Э", MainActivity.morseDecodeSwitch("..-..", "ru"));
        assertEquals("Ю", MainActivity.morseDecodeSwitch("..--", "ru"));
        assertEquals("Я", MainActivity.morseDecodeSwitch(".-.-", "ru"));
    }

    @Test
    public void testMorseDecode_Numbers() {
        assertEquals("0", MainActivity.morseDecodeSwitch("-----", "en"));
        assertEquals("1", MainActivity.morseDecodeSwitch(".----", "en"));
        assertEquals("2", MainActivity.morseDecodeSwitch("..---", "en"));
        assertEquals("3", MainActivity.morseDecodeSwitch("...--", "en"));
        assertEquals("4", MainActivity.morseDecodeSwitch("....-", "en"));
        assertEquals("5", MainActivity.morseDecodeSwitch(".....", "en"));
        assertEquals("6", MainActivity.morseDecodeSwitch("-....", "en"));
        assertEquals("7", MainActivity.morseDecodeSwitch("--...", "en"));
        assertEquals("8", MainActivity.morseDecodeSwitch("---..", "en"));
        assertEquals("9", MainActivity.morseDecodeSwitch("----.", "en"));
    }

    @Test
    public void testMorseDecode_SpecialCharacters() {
        assertEquals(" ", MainActivity.morseDecodeSwitch("/", "en"));
        assertEquals(",", MainActivity.morseDecodeSwitch("--..--", "en"));
        assertEquals(".", MainActivity.morseDecodeSwitch(".-.-.-", "en"));
        assertEquals("?", MainActivity.morseDecodeSwitch("..--..", "en"));
        assertEquals("'", MainActivity.morseDecodeSwitch(".----.", "en"));
        assertEquals("!", MainActivity.morseDecodeSwitch("-.-.--", "en"));
        assertEquals("/", MainActivity.morseDecodeSwitch("-..-.", "en"));
        assertEquals("(", MainActivity.morseDecodeSwitch("-.--.", "en"));
        assertEquals(")", MainActivity.morseDecodeSwitch("-.--.-", "en"));
        assertEquals("&", MainActivity.morseDecodeSwitch(".-...", "en"));
        assertEquals(":", MainActivity.morseDecodeSwitch("---...", "en"));
        assertEquals(";", MainActivity.morseDecodeSwitch("-.-.-.", "en"));
        assertEquals("=", MainActivity.morseDecodeSwitch("-...-", "en"));
        assertEquals("+", MainActivity.morseDecodeSwitch(".-.-.", "en"));
        assertEquals("-", MainActivity.morseDecodeSwitch("-....-", "en"));
        assertEquals("_", MainActivity.morseDecodeSwitch("..--.-", "en"));
        assertEquals("\"", MainActivity.morseDecodeSwitch(".-..-.", "en"));
        assertEquals("$", MainActivity.morseDecodeSwitch("...-..-", "en"));
        assertEquals("@", MainActivity.morseDecodeSwitch(".--.-.", "en"));
    }

    @Test
    public void testMorseDecode_UnknownMorseCode() {
        assertEquals("", MainActivity.morseDecodeSwitch("......", "en"));
        assertEquals("", MainActivity.morseDecodeSwitch("-------", "en"));
        assertEquals("", MainActivity.morseDecodeSwitch(".-.-.-.-", "en"));
    }

    @Test
    public void testMorseDecode_EmptyString() {
        assertEquals("", MainActivity.morseDecodeSwitch("", "en"));
    }

    @Test
    public void testLanguageSwitching() {
        assertEquals("A", MainActivity.morseDecodeSwitch(".-", "en"));
        assertEquals("А", MainActivity.morseDecodeSwitch(".-", "ru"));
    }

    // ===== ИНТЕГРАЦИОННЫЕ ТЕСТЫ =====

    @Test
    public void testFullTranslation_EnglishWord() {
        String morse = textToMorse("HELLO");
        assertEquals(".... . .-.. .-.. ---", morse);

        String text = morseToText(".... . .-.. .-.. ---", "en");
        assertEquals("HELLO", text);
    }

    @Test
    public void testFullTranslation_RussianWord() {
        String morse = textToMorse("ПРИВЕТ");
        assertEquals(".--. .-. .. .-- . -", morse);

        String text = morseToText(".--. .-. .. .-- . -", "ru");
        assertEquals("ПРИВЕТ", text);
    }

    @Test
    public void testFullTranslation_SOS() {
        String morse = textToMorse("SOS");
        assertEquals("... --- ...", morse);

        String text = morseToText("... --- ...", "en");
        assertEquals("SOS", text);
    }

    @Test
    public void testFullTranslation_Numbers() {
        String morse = textToMorse("123");
        assertEquals(".---- ..--- ...--", morse);

        String text = morseToText(".---- ..--- ...--", "en");
        assertEquals("123", text);
    }

    @Test
    public void testEmptyInput() {
        String morse = textToMorse("");
        assertEquals("", morse);

        String text = morseToText("", "en");
        assertEquals("", text);
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =====

    private String textToMorse(String text) {
        StringBuilder result = new StringBuilder();
        String[] letters = text.split("");
        for (String letter : letters) {
            String morse = MainActivity.morseEncode(letter);
            if (!morse.isEmpty()) {
                result.append(morse).append(" ");
            }
        }
        return result.toString().trim();
    }

    private String morseToText(String morse, String language) {
        StringBuilder textResult = new StringBuilder();
        String[] words = morse.split("/");

        for (int i = 0; i < words.length; i++) {
            String word = words[i].trim();
            if (word.isEmpty()) continue;

            String[] morseChars = word.split(" ");
            for (String morseChar : morseChars) {
                if (!morseChar.isEmpty()) {
                    String decodedChar = MainActivity.morseDecodeSwitch(morseChar, language);
                    if (!decodedChar.isEmpty()) {
                        textResult.append(decodedChar);
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