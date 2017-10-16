package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fioni.bakingapp.fragments.RecipeFragment;
import com.example.fioni.bakingapp.utilities.Global;
import com.example.fioni.bakingapp.utilities.Recipe;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RecipeFragment.OnObjectClickListener, RecipeFragment.GiveRecipeList {
    public ArrayList<Recipe> mRecipeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (savedInstanceState == null) {
            RecipeFragment recipeFragment = new RecipeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_container, recipeFragment)
                    .commit();
        }

    }

    @Override
    public void onSelectedObj(Recipe recipe) {

        final Intent intent = new Intent(this, StepActivity.class);
        intent.putExtra("thisRecipe", recipe);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void recipeList(ArrayList<Recipe> recipe) {
        mRecipeArrayList = recipe;
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

}
