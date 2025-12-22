package com.example.morse_messenger;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class ShowLanguageSpinnerTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /** UI-тест: Проверяет, что при переключении режима появляется Spinner выбора язык */
    @Test
    public void toggleMode_shouldShowLanguageSpinnerInMorseToTextMode() {
        // Act: нажимаем кнопку переключения режима
        onView(withId(R.id.toggle_mode_button))
                .perform(click());

        // Assert: Spinner выбора языка стал видимым
        onView(withId(R.id.language_spinner))
                .check(matches(isDisplayed()));
    }
}
