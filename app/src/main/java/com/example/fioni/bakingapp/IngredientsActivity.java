package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fioni.bakingapp.fragments.IngredientsFragment;
import com.example.fioni.bakingapp.utilities.Global;

/**
 * Created by fioni on 9/15/2017.
 */

public class IngredientsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Intent ingredientIntent = getIntent();

        setTitle(ingredientIntent.getStringExtra("thisRecipeName"));

        if (savedInstanceState == null) {

            String recipeId = ingredientIntent.getStringExtra("thisRecipe");

            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_container, ingredientsFragment)
                    .commit();

            ingredientsFragment.setRecipeId(recipeId);
        }
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
