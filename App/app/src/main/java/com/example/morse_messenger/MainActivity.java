package com.example.morse_messenger;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;

public class MainActivity extends AppCompatActivity {

    private EditText inputEditText;
    private EditText outputTextView;
    private Button translateButton;
    private Button toggleModeButton;
    private Spinner languageSpinner;
    private boolean isTextToMorseMode = true;
    private String currentLanguage = "en"; // en, ru

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEditText = findViewById(R.id.input_edit_text);
        outputTextView = findViewById(R.id.output_text_view);
        translateButton = findViewById(R.id.translate_button);
        toggleModeButton = findViewById(R.id.toggle_mode_button);
        languageSpinner = findViewById(R.id.language_spinner);

        // Настройка Spinner для выбора языка
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: currentLanguage = "en"; break;
                    case 1: currentLanguage = "ru"; break;
                }
                // Переводим заново при смене языка в режиме "Морзе → Текст"
                if (!isTextToMorseMode && !inputEditText.getText().toString().trim().isEmpty()) {
                    translateFromMorse();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentLanguage = "en";
            }

        });

        updateUI();

        // Автоперевод при вводе текста
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // не нужно
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // каждый раз, когда пользователь что-то ввёл/стер
                if (isTextToMorseMode) {
                    translateToMorse();
                } else {
                    translateFromMorse();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // не нужно
            }

        });
        
        // Обработчик для кнопки переключения режима
        toggleModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTextToMorseMode = !isTextToMorseMode;
                updateUI();
                inputEditText.setText("");
                outputTextView.setText("");
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
        startActivity(new android.content.Intent(this, SwaggerActivity.class));
    }

    private void updateUI() {
        toggleModeButton.setText(isTextToMorseMode ? "Switch to Morse → Text" : "Switch to Text → Morse");
        inputEditText.setHint(isTextToMorseMode ? "Enter Text" : "Enter Morse Code");
        outputTextView.setHint(isTextToMorseMode ? "Morse Text Result" : "Text Result");

        // Показываем/скрываем выбор языка
        languageSpinner.setVisibility(isTextToMorseMode ? View.GONE : View.VISIBLE);
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

        // Заменяем несколько пробелов подряд на один пробел
        String normalizedMorse = inputMorse.replaceAll("\\s+", " ");

        // Разделяем на слова (слово разделяется "/" или несколькими пробелами)
        String[] words = normalizedMorse.split("/");

        for (int i = 0; i < words.length; i++) {
            String word = words[i].trim();
            if (word.isEmpty()) continue;

            // Разделяем слово на отдельные символы Морзе
            String[] morseChars = word.split(" ");

            for (String morseChar : morseChars) {
                if (!morseChar.isEmpty()) {
                    String decodedChar = morseDecodeWithLanguage(morseChar);
                    if (!decodedChar.isEmpty()) {
                        textResult.append(decodedChar);
                    }
                }
            }

            // Добавляем пробел между словами, но не после последнего слова
            if (i < words.length - 1) {
                textResult.append(" ");
            }
        }

        outputTextView.setText(textResult.toString());
    }

    // Простой метод декодирования с учетом выбранного языка
    private String morseDecodeWithLanguage(String morseChar) {
        return morseDecodeSwitch(morseChar, currentLanguage);
    }

    // Статический метод для кодирования (для тестов)
    public static String morseEncode(String x) {
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

    // Нестатический метод для использования в Activity
    public String morseDecodeSwitch(String morseChar) {
        return morseDecodeSwitch(morseChar, currentLanguage);
    }

    // Статический метод для тестов с параметром языка
    public static String morseDecodeSwitch(String morseChar, String language) {
        if (language.equals("ru")) {
            // Декодирование для русского языка
            switch (morseChar) {
                case ".-": return "А";
                case "-...": return "Б";
                case ".--": return "В";
                case "--.": return "Г";
                case "-..": return "Д";
                case ".": return "Е";
                case "...-": return "Ж";
                case "--..": return "З";
                case "..": return "И";
                case ".---": return "Й";
                case "-.-": return "К";
                case ".-..": return "Л";
                case "--": return "М";
                case "-.": return "Н";
                case "---": return "О";
                case ".--.": return "П";
                case ".-.": return "Р";
                case "...": return "С";
                case "-": return "Т";
                case "..-": return "У";
                case "..-.": return "Ф";
                case "....": return "Х";
                case "-.-.": return "Ц";
                case "---.": return "Ч";
                case "----": return "Ш";
                case "--.-": return "Щ";
                case "--.--": return "Ъ";
                case "-.--": return "Ы";
                case "-..-": return "Ь";
                case "..-..": return "Э";
                case "..--": return "Ю";
                case ".-.-": return "Я";
                // Цифры и символы одинаковы для всех языков
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
        } else {
            // Декодирование для английского языка (по умолчанию)
            switch (morseChar) {
                case ".-": return "A";
                case "-...": return "B";
                case "-.-.": return "C";
                case "-..": return "D";
                case ".": return "E";
                case "..-.": return "F";
                case "--.": return "G";
                case "....": return "H";
                case "..": return "I";
                case ".---": return "J";
                case "-.-": return "K";
                case ".-..": return "L";
                case "--": return "M";
                case "-.": return "N";
                case "---": return "O";
                case ".--.": return "P";
                case "--.-": return "Q";
                case ".-.": return "R";
                case "...": return "S";
                case "-": return "T";
                case "..-": return "U";
                case "...-": return "V";
                case ".--": return "W";
                case "-..-": return "X";
                case "-.--": return "Y";
                case "--..": return "Z";
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
}