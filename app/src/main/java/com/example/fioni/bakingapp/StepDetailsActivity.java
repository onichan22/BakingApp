package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.fioni.bakingapp.fragments.StepDetailsFragment;
import com.example.fioni.bakingapp.utilities.Global;
import com.example.fioni.bakingapp.utilities.Step;

/**
 * Created by fioni on 9/9/2017.
 */

public class StepDetailsActivity extends AppCompatActivity {
    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    public static String ON_STEP_KEY = "ON STEP";
    Step mStep;
    int mOnStep;
    private StepDetailsFragment mRetainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent recipeIntent = getIntent();
        mStep = recipeIntent.getParcelableExtra("thisStep");

        setContentView(R.layout.activity_steps);

        final View nextButton = findViewById(R.id.next_step);
        final View prevButton = findViewById(R.id.prev_step);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        mRetainedFragment = (StepDetailsFragment) fragmentManager.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if (mRetainedFragment == null) {
            // add the fragment
            mRetainedFragment = new StepDetailsFragment();
            // load data from a data source or perform any calculation
        }

        View.OnClickListener clickNext = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //StepDetailsFragment newStepDetailsFragment = new StepDetailsFragment();
                //mRetainedFragment.setRecipeId(mStep.getR_id());
                if (v == nextButton) {
                    if (mOnStep < Global.stepSetSize - 1) {
                        mOnStep = mOnStep + 1;
                        StepDetailsFragment newRetainedFragment = new StepDetailsFragment();
                        mRetainedFragment = newRetainedFragment;
                        mRetainedFragment.setStepId(mOnStep);
                        //fragmentManager.beginTransaction().remove(mRetainedFragment).commit();
                        fragmentManager.beginTransaction()
                                .replace(R.id.steps_detail_container, mRetainedFragment)
                                .commit();
                    } else {
                        mOnStep = Global.stepSetSize;
                    }

                }
                if (v == prevButton) {
                    if (mOnStep > 0) {
                        mOnStep = mOnStep - 1;
                        StepDetailsFragment newRetainedFragment = new StepDetailsFragment();
                        mRetainedFragment = newRetainedFragment;
                        mRetainedFragment.setStepId(mOnStep);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.steps_detail_container, mRetainedFragment)
                                .commit();
                    } else {
                        mOnStep = 0;
                    }
                }
            }
        };

        nextButton.setOnClickListener(clickNext);
        prevButton.setOnClickListener(clickNext);

        if (savedInstanceState == null) {

            mOnStep = Integer.parseInt(mStep.getId());
            //StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            //FragmentManager fragmentManager = getSupportFragmentManager();

            mRetainedFragment.setRecipeId(mStep.getR_id());
            mRetainedFragment.setStepId(mOnStep);

            fragmentManager.beginTransaction()
                    .add(R.id.steps_detail_container, mRetainedFragment)
                    .commit();
        }
        if (savedInstanceState != null) {
            mOnStep = savedInstanceState.getInt(ON_STEP_KEY);
            fragmentManager.beginTransaction()
                    .attach(mRetainedFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ON_STEP_KEY, mOnStep);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().remove(mRetainedFragment).commit();
        }
    }
}
