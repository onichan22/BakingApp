package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fioni.bakingapp.utilities.Recipe;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RecipeFragment.OnObjectClickListener{
    public ArrayList<Recipe> mRecipeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeFragment recipeFragment = new RecipeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_container, recipeFragment)
                .commit();

        mRecipeArrayList = recipeFragment.getRecipeList();

    }

    @Override
    public void onSelectedObj(Recipe recipe) {

        final Intent intent = new Intent(this, StepActivity.class);
        intent.putExtra("thisRecipe", recipe);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intentSettings = new Intent(this, SettingsActivity.class);
        /*Bundle b = new Bundle();
        b.putParcelableArrayList("recipes", mRecipeArrayList);
        intentSettings.putExtra("recipes", b);*/
        intentSettings.putParcelableArrayListExtra("recipes",mRecipeArrayList);
        startActivity(intentSettings);
        return super.onOptionsItemSelected(item);
    }
}
