package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.fioni.bakingapp.fragments.IngredientsFragment;
import com.example.fioni.bakingapp.fragments.StepDetailsFragment;
import com.example.fioni.bakingapp.fragments.StepsFragment;
import com.example.fioni.bakingapp.utilities.Recipe;
import com.example.fioni.bakingapp.utilities.Step;

/**
 * Created by fioni on 9/6/2017.
 */

public class StepActivity extends AppCompatActivity implements StepsFragment.OnObjectClickListener{

    Recipe mRecipe;
    Step mStep;

    Boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_steps);
        Intent recipeIntent = getIntent();
        mRecipe =   recipeIntent.getParcelableExtra("thisRecipe");

        setTitle(mRecipe.getName());

        if(!getResources().getBoolean(R.bool.small_screen)){
            Toast.makeText(this, "not small screen", Toast.LENGTH_SHORT).show();
            mTwoPane = true;
            FragmentManager fragmentManager = getSupportFragmentManager();

            StepsFragment stepsFragment = new StepsFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.detail_container, stepsFragment)
                    .commit();

            //stepsFragment.setRecipeId(mRecipe.getId());

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.steps_detail_container, ingredientsFragment)
                    .commit();
           /* if(mStep == null){
                Toast.makeText(this, "no step", Toast.LENGTH_SHORT).show();
            }else {
                stepDetailsFragment.setStepId(mStep);
            }*/
        }
        if(getResources().getBoolean(R.bool.small_screen)) {
            mTwoPane = false;
            StepsFragment stepsFragment = new StepsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.detail_container, stepsFragment)
                    .commit();

            stepsFragment.setRecipeId(mRecipe.getId());
        }
    }

    @Override
    public void onSelectedStep(Step step) {
        mStep = step;
        if (!mTwoPane) {
            final Intent intent = new Intent(this, StepDetailsActivity.class);
            //Toast.makeText(this, step.getShort_desc(), Toast.LENGTH_SHORT).show();
            intent.putExtra("thisStep", step);
            startActivity(intent);
        }
        if (mTwoPane){
            mStep = step;
            StepDetailsFragment newStepDetailsFragment = new StepDetailsFragment();
            newStepDetailsFragment.setStepId(mStep);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_detail_container, newStepDetailsFragment)
                    .commit();
        }

    }

    public void display_ingr(View v){
        Toast.makeText(this, "ingredients clicked", Toast.LENGTH_SHORT).show();
        final Intent intent = new Intent(this, IngredientsActivity.class);
        intent.putExtra("thisRecipe", mRecipe.getId());
        startActivity(intent);
    }
}
