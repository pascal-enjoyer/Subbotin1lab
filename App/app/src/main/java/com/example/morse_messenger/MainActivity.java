package com.example.morse_messenger;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private EditText inputEditText;
    private EditText outputTextView; // Изменено с TextView на EditText
    private Button translateButton;
    private Button toggleModeButton;
    private boolean isTextToMorseMode = true; // true: текст → Морзе, false: Морзе → текст

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEditText = findViewById(R.id.input_edit_text);
        outputTextView = findViewById(R.id.output_text_view);
        translateButton = findViewById(R.id.translate_button);

        toggleModeButton = findViewById(R.id.toggle_mode_button);

        // Устанавливаем начальный текст кнопки и подсказок
        updateUI();

        // Обработчик для кнопки переключения режима
        toggleModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTextToMorseMode = !isTextToMorseMode;
                updateUI();
                // Очищаем поля при смене режима
                inputEditText.setText("");
                outputTextView.setText("");
                outputTextView.setHint(isTextToMorseMode ? "Morse Text Result" : "Text Result");
            }
        });

        // Обработчик для кнопки перевода
        translateButton.setOnClickListener(v -> {
            if (isTextToMorseMode) {
                translateToMorse();
            } else {
                translateFromMorse();
            }
        });
    }
    private void updateUI() {
        toggleModeButton.setText(isTextToMorseMode ? "Switch to Morse → Text" : "Switch to Text → Morse");
        inputEditText.setHint(isTextToMorseMode ? "Enter Text" : "Enter Morse Code");
        outputTextView.setHint(isTextToMorseMode ? "Morse Text Result" : "Text Result");
    }

    private void translateToMorse() {
        String inputText = inputEditText.getText().toString().trim();
        if (inputText.isEmpty()) {
            outputTextView.setText("");
            return;
        }

        StringBuilder morseResult = new StringBuilder();
        String[] letters = inputText.split("");
        for (String letter : letters) {
            String morse = morseEncode(letter);
            if (!morse.isEmpty()) {
                morseResult.append(morse).append(" ");
            }
        }
        outputTextView.setText(morseResult.toString().trim());
    }

    private void translateFromMorse() {
        String inputMorse = inputEditText.getText().toString().trim();
        if (inputMorse.isEmpty()) {
            outputTextView.setText("");
            return;
        }

        StringBuilder textResult = new StringBuilder();
        // Разделяем входную строку на слова (разделитель - пробел или "/")
        String[] words = inputMorse.split("\\s+|/");
        for (String word : words) {
            if (word.isEmpty()) continue;

            // Разделяем слово на символы Морзе
            String[] morseChars = word.trim().split("\\s+");
            for (String morseChar : morseChars) {
                String decodedChar = morseDecodeSwitch(morseChar);
                if (!decodedChar.isEmpty()) {
                    textResult.append(decodedChar);
                }
            }
            // Добавляем пробел между словами
            textResult.append(" ");
        }
        outputTextView.setText(textResult.toString().trim());
    }

    static String morseEncode(String x) {
        switch (x.toLowerCase(Locale.getDefault())) {
            case "a": return ".-";
            case "b": return "-...";
            case "c": return "-.-.";
            case "d": return "-..";
            case "e": return ".";
            case "f": return "..-.";
            case "g": return "--.";
            case "h": return "....";
            case "i": return "..";
            case "j": return ".---";
            case "k": return "-.-";
            case "l": return ".-..";
            case "m": return "--";
            case "n": return "-.";
            case "o": return "---";
            case "p": return ".--.";
            case "q": return "--.-";
            case "r": return ".-.";
            case "s": return "...";
            case "t": return "-";
            case "u": return "..-";
            case "v": return "...-";
            case "w": return ".--";
            case "x": return "-..-";
            case "y": return "-.--";
            case "z": return "--..";
            case " ": return "/ ";
            case "0": return "-----";
            case "1": return ".----";
            case "2": return "..---";
            case "3": return "...--";
            case "4": return "....-";
            case "5": return ".....";
            case "6": return "-....";
            case "7": return "--...";
            case "8": return "---..";
            case "9": return "----.";
            case "а": return ".-";
            case "б": return "-...";
            case "в": return ".--";
            case "г": return "--.";
            case "д": return "-..";
            case "е": return ".";
            case "ё": return ".";
            case "ж": return "...-";
            case "з": return "--..";
            case "и": return "..";
            case "й": return ".---";
            case "к": return "-.-";
            case "л": return ".-..";
            case "м": return "--";
            case "н": return "-.";
            case "о": return "---";
            case "п": return ".--.";
            case "р": return ".-.";
            case "с": return "...";
            case "т": return "-";
            case "у": return "..-";
            case "ф": return "..-.";
            case "х": return "....";
            case "ц": return "-.-.";
            case "ч": return "---.";
            case "ш": return "----";
            case "щ": return "--.-";
            case "ъ": return "--.--";
            case "ы": return "-.--";
            case "ь": return "-..-";
            case "э": return "..-..";
            case "ю": return "..--";
            case "я": return ".-.-";
            case "ñ": return "--.--";
            case ",": return "--..--";
            case ".": return ".-.-.-";
            case "?": return "..--..";
            case "'": return ".----.";
            case "!": return "-.-.--";
            case "/": return "-..-.";
            case "(": return "-.--.";
            case ")": return "-.--.-";
            case "&": return ".-...";
            case ":": return "---...";
            case ";": return "-.-.-.";
            case "=": return "-...-";
            case "+": return ".-.-.";
            case "-": return "-....-";
            case "_": return "..--.-";
            case "\"": return ".-..-.";
            case "$": return "...-..-";
            case "@": return ".--.-.";
            default: return "";
        }
    }
    public static String morseDecodeSwitch(String morseChar) {
        switch (morseChar) {
            case ".-": return "A"; // или "А"
            case "-...": return "B"; // или "Б"
            case "-.-.": return "C"; // или "Ц"
            case "-..": return "D"; // или "Д"
            case ".": return "E"; // или "Е", "Ё"
            case "..-.": return "F"; // или "Ф"
            case "--.": return "G"; // или "Г"
            case "....": return "H"; // или "Х"
            case "..": return "I"; // или "И"
            case ".---": return "J"; // или "Й"
            case "-.-": return "K"; // или "К"
            case ".-..": return "L"; // или "Л"
            case "--": return "M"; // или "М"
            case "-.": return "N"; // или "Н"
            case "---": return "O"; // или "О"
            case ".--.": return "P"; // или "П"
            case "--.-": return "Q"; // или "Щ"
            case ".-.": return "R"; // или "Р"
            case "...": return "S"; // или "С"
            case "-": return "T"; // или "Т"
            case "..-": return "U"; // или "У"
            case "...-": return "V"; // или "Ж"
            case ".--": return "W"; // или "В"
            case "-..-": return "X"; // или "Ь"
            case "-.--": return "Y"; // или "Ы"
            case "--..": return "Z"; // или "З"
            case "---.": return "Ч";
            case "----": return "Ш";
            case "--.--": return "Ъ";
            case "..-..": return "Э"; // или "É"
            case "..--": return "Ю"; // или "Ì", "Ù"
            case ".-.-": return "Я";
            case ".--.-": return "À";
            case ".-..-": return "È";
            case "---.-": return "Ò";
            case "-.-..": return "Ç";
            case "-----": return "0";
            case ".----": return "1";
            case "..---": return "2";
            case "...--": return "3";
            case "....-": return "4";
            case ".....": return "5";
            case "-....": return "6";
            case "--...": return "7";
            case "---..": return "8";
            case "----.": return "9";
            case "/": return " ";
            case "--..--": return ",";
            case ".-.-.-": return ".";
            case "..--..": return "?";
            case ".----.": return "'";
            case "-.-.--": return "!";
            case "-..-.": return "/";
            case "-.--.": return "(";
            case "-.--.-": return ")";
            case ".-...": return "&";
            case "---...": return ":";
            case "-.-.-.": return ";";
            case "-...-": return "=";
            case ".-.-.": return "+";
            case "-....-": return "-";
            case "..--.-": return "_";
            case ".-..-.": return "\"";
            case "...-..-": return "$";
            case ".--.-.": return "@";
            default: return "";
        }
    }
}