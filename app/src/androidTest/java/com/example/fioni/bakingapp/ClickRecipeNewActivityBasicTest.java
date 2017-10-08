package com.example.fioni.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.lang.Thread.sleep;

/**
 * Created by fioni on 9/17/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ClickRecipeNewActivityBasicTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Test
    public void clickNextButton_ChangesStepDetailsFragment() {
        try {
            sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(StepActivity.class.getName()));

        //inAdapterView(withId(R.id.step_details_tv)).matches(atPosition(1));

    }
}
