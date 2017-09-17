package com.example.fioni.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.fioni.bakingapp.utilities.Ingredients;
import com.example.fioni.bakingapp.utilities.Recipe;
import com.example.fioni.bakingapp.utilities.SettingsAdapter;
import com.example.fioni.bakingapp.widget.BakingAppWidgetProvider;

import java.util.ArrayList;

/**
 * Created by fioni on 9/16/2017.
 */

public class SettingsActivity extends AppCompatActivity implements SettingsAdapter.SettingsAdapterOnClickHandler {

    public static final String SELECTED_RECIPE = "selected recipe";
    private RecyclerView mRecyclerView;
    private ArrayList<Recipe> mRecipes;
    private ArrayList<Ingredients> mIngredients;
    public int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent recipeIntent = getIntent();
        //mRecipes = recipeIntent.getBundleExtra("recipes").getParcelableArrayList("recipes");
        mRecipes = recipeIntent.getParcelableArrayListExtra("recipes");
        setContentView(R.layout.activity_settings);
        mRecyclerView = (RecyclerView) findViewById(R.id.settings_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SettingsAdapter adapter = new SettingsAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setAdapterData(mRecipes);

    }

    public void saveSettings(View v){
        //TODO: service for widget

        BakingAppWidgetProvider.selectRecipeToDisplay(this);
    }


    @Override
    public void onClick(Recipe aRecipe) {
        mRecipeId = Integer.parseInt(aRecipe.getId());
        //URL recipeSearchUrl = NetworkUtils.buildUrl();
        //new QueryIngredientsTask().execute(recipeSearchUrl);

        SharedPreferences preferredRecipe = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferredRecipe.edit();
        editor.putString(SELECTED_RECIPE, aRecipe.getId());
        editor.apply();

        BakingAppWidgetProvider.selectRecipeToDisplay(this);

    }


}
