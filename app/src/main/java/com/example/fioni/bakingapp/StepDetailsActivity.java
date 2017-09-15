package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.fioni.bakingapp.utilities.Step;

/**
 * Created by fioni on 9/9/2017.
 */

public class StepDetailsActivity extends AppCompatActivity {
    Step mStep;
    StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_steps_detail);
        Intent recipeIntent = getIntent();
        mStep =   recipeIntent.getParcelableExtra("thisStep");
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.steps_detail_container, stepDetailsFragment)
                .commit();

        stepDetailsFragment.setStepId(mStep);
    }

    public void next_step(View v){
        //Toast.makeText(this, "next button clicked", Toast.LENGTH_SHORT).show();
        stepDetailsFragment.nextStep();

    }

    public void prev_step(View v){
        //Toast.makeText(this, "next button clicked", Toast.LENGTH_SHORT).show();
        stepDetailsFragment.prevStep();

    }

}
