package com.example.morse_messenger;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityIntegrationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void typingText_shouldAutoTranslateToMorseAndShowInOutput() {
        // По умолчанию isTextToMorseMode = true, значит автоперевод в Морзе должен сработать
        onView(withId(R.id.input_edit_text))
                .perform(click(), replaceText("ab"), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withText(".- -...")));
    }
}
