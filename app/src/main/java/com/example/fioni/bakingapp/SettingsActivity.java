package com.example.fioni.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.fioni.bakingapp.utilities.Recipe;
import com.example.fioni.bakingapp.utilities.SettingsAdapter;

import java.util.ArrayList;

/**
 * Created by fioni on 9/16/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent recipeIntent = getIntent();
        mRecipes = recipeIntent.getBundleExtra("recipes").getParcelableArrayList("recipes");
        setContentView(R.layout.activity_settings);
        mRecyclerView = (RecyclerView) findViewById(R.id.settings_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SettingsAdapter adapter = new SettingsAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setAdapterData(mRecipes);

    }

    public void saveSettings(View v){
        //TODO: service for widget
    }


}
