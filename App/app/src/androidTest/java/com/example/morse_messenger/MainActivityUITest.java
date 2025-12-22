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

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testTextToMorse_English() {
        // Вводим "HI"
        onView(withId(R.id.input_edit_text)).perform(typeText("HI"));
        Espresso.closeSoftKeyboard();

        // Нажимаем "Translate"
        onView(withId(R.id.translate_button)).perform(click());

        // Проверяем результат: ".... .."
        onView(withId(R.id.output_text_view))
                .check(ViewAssertions.matches(ViewMatchers.withText(".... ..")));
    }

    @Test
    public void testMorseToText_English() {
        // Переключаем режим
        onView(withId(R.id.toggle_mode_button)).perform(click());

        // Вводим морзе ".... .."
        onView(withId(R.id.input_edit_text)).perform(typeText(".... .."));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.translate_button)).perform(click());

        onView(withId(R.id.output_text_view))
                .check(ViewAssertions.matches(withText("HI")));
    }

    @Test
    public void testToggleButtonTextChanges() {
        onView(withId(R.id.toggle_mode_button))
                .check(ViewAssertions.matches(withText("Switch to Morse → Text")));

        onView(withId(R.id.toggle_mode_button)).perform(click());

        onView(withId(R.id.toggle_mode_button))
                .check(ViewAssertions.matches(withText("Switch to Text → Morse")));
    }
}