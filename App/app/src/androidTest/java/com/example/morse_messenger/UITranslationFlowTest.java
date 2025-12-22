package com.example.morse_messenger;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.view.View;
import android.widget.TextView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
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
        onView(withId(R.id.input_edit_text))
                .check(matches(withHint("Enter Text")));

        onView(withId(R.id.toggle_mode_button))
                .check(matches(withText("Switch to Morse → Text")));

        onView(withId(R.id.input_edit_text))
                .perform(replaceText("HELLO WORLD"), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withNormalizedText(".... . .-.. .-.. --- / .-- --- .-. .-.. -..")));

        onView(withId(R.id.translate_button))
                .perform(click());

        onView(withId(R.id.output_text_view))
                .check(matches(withNormalizedText(".... . .-.. .-.. --- / .-- --- .-. .-.. -..")));

        onView(withId(R.id.input_edit_text))
                .perform(clearText(), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withNormalizedText("")));
    }

    @Test
    public void testModeSwitchingWithData() {
        onView(withId(R.id.input_edit_text))
                .perform(replaceText("SOS"), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withNormalizedText("... --- ...")));

        onView(withId(R.id.toggle_mode_button))
                .perform(click());

        onView(withId(R.id.input_edit_text))
                .check(matches(withText("")));

        onView(withId(R.id.output_text_view))
                .check(matches(withText("")));

        onView(withId(R.id.language_spinner))
                .check(matches(isDisplayed()));

        onView(withId(R.id.input_edit_text))
                .perform(replaceText("... --- ..."), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withText("SOS")));

        onView(withId(R.id.toggle_mode_button))
                .perform(click());

        onView(withId(R.id.language_spinner))
                .check(matches(Matchers.not(isDisplayed())));
    }

    @Test
    public void testRussianLanguageTranslation() {
        onView(withId(R.id.toggle_mode_button))
                .perform(click());

        onView(withId(R.id.language_spinner))
                .perform(click());

        onData(anything()).atPosition(1).perform(click());

        onView(withId(R.id.input_edit_text))
                .perform(replaceText(".--. .-. .. .-- . -"), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withText("ПРИВЕТ")));

        onView(withId(R.id.language_spinner))
                .perform(click());

        onData(anything()).atPosition(0).perform(click());

        onView(withId(R.id.output_text_view))
                .check(matches(withText("PRIWET")));
    }

    @Test
    public void testInputValidationAndErrorCases() {
        onView(withId(R.id.input_edit_text))
                .perform(replaceText("Hello#World@"), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withNormalizedText(".... . .-.. .-.. --- .-- --- .-. .-.. -.. .--.-.")));

        onView(withId(R.id.input_edit_text))
                .perform(clearText(), closeSoftKeyboard());

        onView(withId(R.id.toggle_mode_button))
                .perform(click());

        onView(withId(R.id.input_edit_text))
                .perform(replaceText("...... ......"), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withNormalizedText("")));

        onView(withId(R.id.input_edit_text))
                .perform(replaceText("... --- ...... .-"), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withText("SOA")));
    }

    @Test
    public void testRealTimeTranslationPerformance() {
        String longText = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG 1234567890";

        onView(withId(R.id.input_edit_text))
                .perform(clearText(), closeSoftKeyboard());

        onView(withId(R.id.input_edit_text))
                .perform(typeText(longText), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(Matchers.not(withText(""))));

        onView(withId(R.id.input_edit_text))
                .perform(clearText(), closeSoftKeyboard());

        onView(withId(R.id.output_text_view))
                .check(matches(withText("")));
    }

    @Test
    public void testButtonStatesAndVisibility() {
        onView(withId(R.id.translate_button))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()));

        onView(withId(R.id.toggle_mode_button))
                .check(matches(isDisplayed()))
                .check(matches(isEnabled()));

        onView(withId(R.id.language_spinner))
                .check(matches(Matchers.not(isDisplayed())));

        onView(withId(R.id.toggle_mode_button))
                .perform(click());

        onView(withId(R.id.language_spinner))
                .check(matches(isDisplayed()));

        onView(withId(R.id.toggle_mode_button))
                .check(matches(withText("Switch to Text → Morse")));

        onView(withId(R.id.toggle_mode_button))
                .perform(click());

        onView(withId(R.id.language_spinner))
                .check(matches(Matchers.not(isDisplayed())));
    }

    private static Matcher<View> withNormalizedText(String expected) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with normalized text: ").appendValue(expected);
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof TextView)) return false;
                String actual = ((TextView) view).getText().toString().trim().replaceAll("\\s+", " ");
                String exp = expected == null ? "" : expected.trim().replaceAll("\\s+", " ");
                return actual.equals(exp);
            }
        };
    }
}
