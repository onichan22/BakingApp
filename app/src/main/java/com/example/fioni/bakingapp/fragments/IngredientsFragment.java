package com.example.fioni.bakingapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fioni.bakingapp.R;
import com.example.fioni.bakingapp.utilities.BakingJSonUtil;
import com.example.fioni.bakingapp.utilities.Ingredients;
import com.example.fioni.bakingapp.utilities.NetworkUtils;
import com.example.fioni.bakingapp.utilities.Recipe;
import com.example.fioni.bakingapp.utilities.RecipeAdapter;
import com.example.fioni.bakingapp.utilities.Step;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by fioni on 9/15/2017.
 */

public class IngredientsFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler{

    public int mRecipeId;
    public
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private View mView;
    private RecipeAdapter mRecipeAdapter;
    private Unbinder unbinder;

    public IngredientsFragment(){

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.recycler_view, container, false);

        ButterKnife.bind(this, mView);
        unbinder = ButterKnife.bind(this, mView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        if (isOnline()) {
            URL recipeSearchUrl = NetworkUtils.buildUrl();
            new IngredientsFragment.QueryIngredientsTask().execute(recipeSearchUrl);
            //Toast.makeText(getContext(), "Test", Toast.LENGTH_LONG);
        }
        return mView;
    }

    @Override
    public void onClick(Recipe aRecipe) {
        //do nothing
    }

    @Override
    public void onClick(Step aStep) {
        //do nothing
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setRecipeId(String recipeId) {
        mRecipeId = Integer.parseInt(recipeId) - 1;
    }

    public class QueryIngredientsTask extends AsyncTask<URL, Void, ArrayList<Ingredients>> {

        @Override
        protected ArrayList<Ingredients> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String recipeSearchResults = null;
            try {
                recipeSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList<Ingredients> ingredients = BakingJSonUtil.getIngredientsFromJson(getActivity(), recipeSearchResults, mRecipeId);

                return ingredients;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Ingredients> recipeDataResults) {
            if (recipeDataResults != null) {
                mRecipeAdapter.setIngredientsData(recipeDataResults);
            }
        }
    }

}
