package com.example.morse_messenger;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void textToMorse_thenToggleAndBackToText() {
        onView(withId(R.id.input_edit_text))
                .perform(replaceText("SOS"), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withText("... --- ...")));

        onView(withId(R.id.toggle_mode_button)).perform(click());

        onView(withId(R.id.language_spinner))
                .check(matches(isDisplayed()));

        onView(withId(R.id.input_edit_text))
                .perform(replaceText("... --- ..."), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withText("SOS")));
    }
}
