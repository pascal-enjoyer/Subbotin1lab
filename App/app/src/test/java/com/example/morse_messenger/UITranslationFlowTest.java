package com.example.morse_messenger;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.widget.Spinner;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UITranslationFlowTest {
    
    @Rule
    public ActivityScenarioRule<MainActivity> rule = 
            new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void testCompleteTranslationFlow_English() {
        // Полный поток: ввод текста -> перевод -> проверка -> очистка
        
        // 1. Проверяем начальное состояние (режим Text → Morse)
        onView(withId(R.id.input_edit_text))
                .check(matches(withHint("Enter Text")));
        
        onView(withId(R.id.toggle_mode_button))
                .check(matches(withText("Switch to Morse → Text")));
        
        // 2. Вводим текст
        onView(withId(R.id.input_edit_text))
                .perform(clearText(), typeText("HELLO WORLD"), closeSoftKeyboard());
        
        // 3. Проверяем автоматический перевод (должен сработать TextWatcher)
        onView(withId(R.id.output_text_view))
                .check(matches(withText(".... . .-.. .-.. --- / .-- --- .-. .-.. -..")));
        
        // 4. Нажимаем кнопку перевода (хотя перевод уже был)
        onView(withId(R.id.translate_button))
                .perform(click());
        
        // 5. Проверяем, что результат не изменился
        onView(withId(R.id.output_text_view))
                .check(matches(withText(".... . .-.. .-.. --- / .-- --- .-. .-.. -..")));
        
        // 6. Очищаем поле ввода
        onView(withId(R.id.input_edit_text))
                .perform(clearText(), closeSoftKeyboard());
        
        // 7. Проверяем, что поле вывода тоже очистилось
        onView(withId(R.id.output_text_view))
                .check(matches(withText("")));
    }
    
    @Test
    public void testModeSwitchingWithData() {
        // Тестируем переключение режимов с сохраненными данными
        
        // 1. Вводим текст в режиме Text → Morse
        onView(withId(R.id.input_edit_text))
                .perform(typeText("SOS"), closeSoftKeyboard());
        
        onView(withId(R.id.output_text_view))
                .check(matches(withText("... --- ...")));
        
        // 2. Переключаемся на режим Morse → Text
        onView(withId(R.id.toggle_mode_button))
                .perform(click());
        
        // 3. Проверяем, что поля очистились
        onView(withId(R.id.input_edit_text))
                .check(matches(withText("")));
        
        onView(withId(R.id.output_text_view))
                .check(matches(withText("")));
        
        // 4. Проверяем, что появился Spinner для выбора языка
        onView(withId(R.id.language_spinner))
                .check(matches(isDisplayed()));
        
        // 5. Вводим морзе-код
        onView(withId(R.id.input_edit_text))
                .perform(typeText("... --- ..."), closeSoftKeyboard());
        
        // 6. Проверяем перевод на английском (по умолчанию)
        onView(withId(R.id.output_text_view))
                .check(matches(withText("SOS")));
        
        // 7. Возвращаемся в исходный режим
        onView(withId(R.id.toggle_mode_button))
                .perform(click());
        
        // 8. Проверяем, что Spinner скрылся
        onView(withId(R.id.language_spinner))
                .check(matches(Matchers.not(isDisplayed())));
    }
    
    @Test
    public void testRussianLanguageTranslation() {
        // Тестируем перевод на русском языке
        
        // 1. Переключаемся в режим Morse → Text
        onView(withId(R.id.toggle_mode_button))
                .perform(click());
        
        // 2. Выбираем русский язык в Spinner
        onView(withId(R.id.language_spinner))
                .perform(click());
        
        // Выбираем русский язык (позиция 1)
        onView(withText("Русский"))
                .inRoot(isDialog())
                .perform(click());
        
        // 3. Вводим русскую морзе
        onView(withId(R.id.input_edit_text))
                .perform(clearText(), typeText(".--. .-. .. .-- . -"), closeSoftKeyboard());
        
        // 4. Проверяем перевод на русском
        onView(withId(R.id.output_text_view))
                .check(matches(withText("ПРИВЕТ")));
        
        // 5. Меняем обратно на английский
        onView(withId(R.id.language_spinner))
                .perform(click());
        
        onView(withText("English"))
                .inRoot(isDialog())
                .perform(click());
        
        // 6. Проверяем, что тот же морзе-код переводится на английском
        onView(withId(R.id.output_text_view))
                .check(matches(withText("PRIWET")));
    }
    
    @Test
    public void testInputValidationAndErrorCases() {
        // Тестируем различные сценарии ввода
        
        // 1. Пробуем ввести неизвестные символы в режиме Text → Morse
        onView(withId(R.id.input_edit_text))
                .perform(clearText(), typeText("Hello#World@"), closeSoftKeyboard());
        
        // Символы # и @ не должны кодироваться
        onView(withId(R.id.output_text_view))
                .check(matches(withText(".... . .-.. .-.. --- / .-- --- .-. .-.. -..")));
        
        // 2. Очищаем
        onView(withId(R.id.input_edit_text))
                .perform(clearText(), closeSoftKeyboard());
        
        // 3. Переключаемся в Morse → Text
        onView(withId(R.id.toggle_mode_button))
                .perform(click());
        
        // 4. Вводим неверный морзе-код
        onView(withId(R.id.input_edit_text))
                .perform(typeText("...... ....."), closeSoftKeyboard());
        
        // Неверные коды должны игнорироваться
        onView(withId(R.id.output_text_view))
                .check(matches(withText("")));
        
        // 5. Вводим смесь правильного и неправильного кода
        onView(withId(R.id.input_edit_text))
                .perform(clearText(), typeText("... --- ... ...... .-"), closeSoftKeyboard());
        
        // Только правильные коды должны декодироваться
        onView(withId(R.id.output_text_view))
                .check(matches(withText("SOA")));
    }
    
    @Test
    public void testRealTimeTranslationPerformance() {
        // Тестируем производительность и работу TextWatcher
        
        // 1. Вводим длинный текст
        String longText = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG 1234567890";
        
        onView(withId(R.id.input_edit_text))
                .perform(clearText());
        
        // 2. Вводим по одному символу (имитируем реальный ввод)
        for (char c : longText.toCharArray()) {
            String charStr = String.valueOf(c);
            onView(withId(R.id.input_edit_text))
                    .perform(typeText(charStr));
            
            // Небольшая задержка между символами
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        onView(withId(R.id.input_edit_text))
                .perform(closeSoftKeyboard());
        
        // 3. Проверяем, что перевод произошел
        onView(withId(R.id.output_text_view))
                .check(matches(Matchers.not(withText(""))));
        
        // 4. Очищаем быстро
        onView(withId(R.id.input_edit_text))
                .perform(clearText(), closeSoftKeyboard());
        
        // 5. Проверяем, что вывод тоже очистился
        onView(withId(R.id.output_text_view))
                .check(matches(withText("")));
    }
    
    @Test
    public void testButtonStatesAndVisibility() {
        // Проверяем состояния кнопок и видимость элементов
        
        // 1. В начальном состоянии
        onView(withId(R.id.translate_button))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()));
        
        onView(withId(R.id.toggle_mode_button))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()));
        
        onView(withId(R.id.language_spinner))
                .check(matches(Matchers.not(isDisplayed()))); // Скрыт в режиме Text→Morse
        
        // 2. Переключаем режим
        onView(withId(R.id.toggle_mode_button))
                .perform(click());
        
        // 3. Проверяем новое состояние
        onView(withId(R.id.language_spinner))
                .check(matches(isDisplayed())); // Теперь виден
        
        onView(withId(R.id.toggle_mode_button))
                .check(matches(withText("Switch to Text → Morse")));
        
        // 4. Возвращаем обратно
        onView(withId(R.id.toggle_mode_button))
                .perform(click());
        
        onView(withId(R.id.language_spinner))
                .check(matches(Matchers.not(isDisplayed()))); // Снова скрыт
    }
}