package com.example.fioni.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by fioni on 9/17/2017.
 */

@RunWith(AndroidJUnit4.class)
public class NextPreviousStepButtonBasicTest {
    @Rule
    public ActivityTestRule<StepDetailsActivity> mActivityTestRule
            = new ActivityTestRule<StepDetailsActivity>(StepDetailsActivity.class);

    @Test
    public void clickNextButton_ChangesStepDetailsFragment() {

        onView(withId(R.id.next_step)).perform(click());
        //inAdapterView(withId(R.id.step_details_tv)).matches(atPosition(1));

    }
}
