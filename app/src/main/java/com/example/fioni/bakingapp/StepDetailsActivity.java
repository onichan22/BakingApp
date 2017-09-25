package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.fioni.bakingapp.fragments.StepDetailsFragment;
import com.example.fioni.bakingapp.utilities.Step;

import java.util.ArrayList;

/**
 * Created by fioni on 9/9/2017.
 */

public class StepDetailsActivity extends AppCompatActivity implements StepDetailsFragment.ClickToListen {
    Step mStep;
    StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
    FragmentManager fragmentManager = getSupportFragmentManager();
    public static String ON_STEP_KEY = "ON STEP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_steps_detail);
        if (savedInstanceState == null) {
            Intent recipeIntent = getIntent();
            mStep = recipeIntent.getParcelableExtra("thisStep");


            fragmentManager.beginTransaction()
                    .add(R.id.steps_detail_container, stepDetailsFragment)
                    .commit();

            stepDetailsFragment.setStepId(mStep);
        }
    }

    public void handleClick(View v, ArrayList<Step> stepArray) {
        if (v.getId() == R.id.next_step) {
            stepDetailsFragment.nextStep(stepArray);
        }
        if (v.getId() == R.id.prev_step) {
            stepDetailsFragment.prevStep(stepArray);
        }
    }

   /* public void next_step(View v){
        //Toast.makeText(this, "next button clicked", Toast.LENGTH_SHORT).show();
        stepDetailsFragment.nextStep();

    }

    public void prev_step(View v){
        //Toast.makeText(this, "next button clicked", Toast.LENGTH_SHORT).show();
        stepDetailsFragment.prevStep();

    }*/

}
