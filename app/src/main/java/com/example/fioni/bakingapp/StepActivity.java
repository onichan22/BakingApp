package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fioni.bakingapp.fragments.IngredientsFragment;
import com.example.fioni.bakingapp.fragments.RecipeFragment;
import com.example.fioni.bakingapp.fragments.StepDetailsFragment;
import com.example.fioni.bakingapp.fragments.StepsFragment;
import com.example.fioni.bakingapp.utilities.Global;
import com.example.fioni.bakingapp.utilities.Recipe;
import com.example.fioni.bakingapp.utilities.Step;

import java.util.ArrayList;

/**
 * Created by fioni on 9/6/2017.
 */

public class StepActivity extends AppCompatActivity implements StepsFragment.OnObjectClickListener, RecipeFragment.GiveRecipeList {

    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    public ArrayList<Recipe> mRecipeArrayList;
    Step mStep;
    Boolean mTwoPane;
    private Recipe mRecipe;
    private StepsFragment mRetainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_steps);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent recipeIntent = getIntent();
        mRecipe = recipeIntent.getParcelableExtra("thisRecipe");
        Global.recipeId = Integer.parseInt(mRecipe.getId()) - 1;

        setTitle(mRecipe.getName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        mRetainedFragment = (StepsFragment) fragmentManager.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if (mRetainedFragment == null) {
            // add the fragment
            mRetainedFragment = new StepsFragment();
            //mRetainedFragment.setRecipeId(mRecipe.getId());
            // load data from a data source or perform any calculation
        }


        if (!getResources().getBoolean(R.bool.small_screen)) {
            Toast.makeText(this, "not small screen", Toast.LENGTH_SHORT).show();
            mTwoPane = true;
            if (savedInstanceState == null) {

                //StepsFragment stepsFragment = new StepsFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.detail_container, mRetainedFragment, TAG_RETAINED_FRAGMENT)
                        .commit();

                //stepsFragment.setRecipeId(mRecipe.getId());

                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.steps_detail_container, ingredientsFragment)
                        .commit();
            }
        }
        if (getResources().getBoolean(R.bool.small_screen)) {
            mTwoPane = false;
            //StepsFragment stepsFragment = new StepsFragment();
            //FragmentManager fragmentManager = getSupportFragmentManager();

            if (savedInstanceState == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.detail_container, mRetainedFragment)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .attach(mRetainedFragment)
                        .commit();
            }
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
        if (mTwoPane) {
            mStep = step;
            //findViewById(R.id.prev_step).setVisibility(View.INVISIBLE);
            //findViewById(R.id.next_step).setVisibility(View.INVISIBLE);
            StepDetailsFragment newStepDetailsFragment = new StepDetailsFragment();
            newStepDetailsFragment.setRecipeId(mStep.getR_id());
            newStepDetailsFragment.setStepId(Integer.parseInt(mStep.getId()));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_detail_container, newStepDetailsFragment)
                    .commit();
        }

    }

    public void display_ingr(View v) {
        Toast.makeText(this, "ingredients clicked", Toast.LENGTH_SHORT).show();
        final Intent intent = new Intent(this, IngredientsActivity.class);
        intent.putExtra("thisRecipe", mRecipe.getId());
        intent.putExtra("thisRecipeName", mRecipe.getName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemClicked = item.getItemId();
        switch (itemClicked) {
            case (R.id.recipes):
                Intent intentRecipe = new Intent(this, MainActivity.class);
                startActivity(intentRecipe);
                break;
            case (R.id.settings):
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                intentSettings.putParcelableArrayListExtra("recipes", Global.recipes);
                startActivity(intentSettings);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void recipeList(ArrayList<Recipe> recipe) {
        mRecipeArrayList = recipe;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
