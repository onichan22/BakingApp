package com.example.fioni.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest2 {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void mainActivityTest2() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(
                allOf(withText("Nutella Pie"),
                        childAtPosition(
                                allOf(withId(R.id.my_toolbar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Nutella Pie")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.stepname_text), withText("Recipe Introduction"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Recipe Introduction")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.stepname_text), withText("Starting prep"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview),
                                        1),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Starting prep")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.stepname_text), withText("Prep the cookie crust."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview),
                                        2),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Prep the cookie crust.")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.stepname_text), withText("Press the crust into baking form."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview),
                                        3),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Press the crust into baking form.")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.stepname_text), withText("Start filling prep"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview),
                                        4),
                                0),
                        isDisplayed()));
        textView6.check(matches(withText("Start filling prep")));

    }
}
