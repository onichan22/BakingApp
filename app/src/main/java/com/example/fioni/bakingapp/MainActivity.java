package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.fioni.bakingapp.utilities.Recipe;

public class MainActivity extends AppCompatActivity implements RecipeFragment.OnObjectClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            RecipeFragment recipeFragment = new RecipeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_container, recipeFragment)
                    .commit();

    }

    @Override
    public void onSelectedObj(Recipe recipe) {

        final Intent intent = new Intent(this, StepActivity.class);
        intent.putExtra("thisRecipe", recipe);
        startActivity(intent);
    }
}
