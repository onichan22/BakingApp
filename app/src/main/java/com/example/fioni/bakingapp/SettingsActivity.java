package com.example.fioni.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.fioni.bakingapp.utilities.Ingredients;
import com.example.fioni.bakingapp.utilities.Recipe;
import com.example.fioni.bakingapp.utilities.SettingsAdapter;
import com.example.fioni.bakingapp.widget.BakingAppWidgetProvider;

import java.util.ArrayList;

/**
 * Created by fioni on 9/16/2017.
 */

public class SettingsActivity extends AppCompatActivity implements SettingsAdapter.SettingsAdapterOnClickHandler {

    private static final String SELECTED_RECIPE = "selected recipe";
    public int mRecipeId;
    private RecyclerView mRecyclerView;
    private ArrayList<Recipe> mRecipes;
    private ArrayList<Ingredients> mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent recipeIntent = getIntent();
        //mRecipes = recipeIntent.getBundleExtra("recipes").getParcelableArrayList("recipes");
        if (recipeIntent.getParcelableArrayListExtra("recipes") == null) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        } else {
            mRecipes = recipeIntent.getParcelableArrayListExtra("recipes");
            setContentView(R.layout.activity_settings);
            mRecyclerView = (RecyclerView) findViewById(R.id.settings_rv);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            SettingsAdapter adapter = new SettingsAdapter();
            mRecyclerView.setAdapter(adapter);
            adapter.setAdapterData(mRecipes);
        }
    }

    public void saveSettings(View v){
        //TODO: service for widget
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidgetProvider.class));
        Intent updateIntent = new Intent(this, BakingAppWidgetProvider.class);
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        this.sendBroadcast(updateIntent);
    }


    @Override
    public void onClick(Recipe aRecipe) {
        mRecipeId = Integer.parseInt(aRecipe.getId());
        //URL recipeSearchUrl = NetworkUtils.buildUrl();
        //new QueryAllIngredientsTask().execute(recipeSearchUrl);

        SharedPreferences preferredRecipe = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferredRecipe.edit();
        editor.putString(SELECTED_RECIPE, aRecipe.getId());
        editor.apply();

        //BakingAppWidgetProvider.selectRecipeToDisplay(this);

    }


}
