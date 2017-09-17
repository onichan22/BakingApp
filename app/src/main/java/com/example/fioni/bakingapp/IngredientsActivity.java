package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.fioni.bakingapp.fragments.IngredientsFragment;

/**
 * Created by fioni on 9/15/2017.
 */

public class IngredientsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent ingredientIntent = getIntent();
        String recipeId = ingredientIntent.getStringExtra("thisRecipe");

        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_container, ingredientsFragment)
                .commit();

        ingredientsFragment.setRecipeId(recipeId);
    }
}
