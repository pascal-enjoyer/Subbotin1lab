package com.example.morse_messenger;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.scrollTo;

@RunWith(AndroidJUnit4.class)
public class MorseIntegrationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testRussianMorseDecoding_FullFlow() {
        // Переключаем в режим "Морзе → Текст"
        onView(withId(R.id.toggle_mode_button)).perform(click());

        // Выбираем русский язык (позиция 1 в Spinner)
        onView(withId(R.id.language_spinner)).perform(click());
        // Эмулируем выбор второго элемента (ru)
        Espresso.onData(org.hamcrest.Matchers.anything())
                .atPosition(1)
                .perform(click());

        // Вводим морзе для "САША"
        String morseForSasha = "... .- ---- .-"; // С=..., А=.-, Ш=----, А=.-
        onView(withId(R.id.input_edit_text)).perform(typeText(morseForSasha));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.translate_button)).perform(click());

        // Проверяем, что результат — "САША"
        onView(withId(R.id.output_text_view))
                .check(ViewAssertions.matches(withText("САША")));
    }
}