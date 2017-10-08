package com.example.fioni.bakingapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fioni.bakingapp.R;
import com.example.fioni.bakingapp.utilities.BakingJSonUtil;
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
 * Created by fioni on 9/17/2017.
 */

public class RecipeFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    public
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    OnObjectClickListener mCallback;
    GiveRecipeList mCallback_rec;
    private View mView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipeList;
    private Unbinder unbinder;

    public RecipeFragment() {

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnObjectClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnObjectClickListener");
        }
        try {
            mCallback_rec = (GiveRecipeList) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement GiveRecipeList");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.recycler_view, container, false);

        ButterKnife.bind(this, mView);
        unbinder = ButterKnife.bind(this, mView);

        if (getResources().getBoolean(R.bool.small_screen)) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        if (getResources().getBoolean(R.bool.horizontal)) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }

        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        if (isOnline()) {
            URL recipeSearchUrl = NetworkUtils.buildUrl();
            new QueryTask().execute(recipeSearchUrl);
            //Toast.makeText(getContext(), "Test", Toast.LENGTH_LONG);

        }

        return mView;
    }

    @Override
    public void onClick(Recipe aRecipe) {
        mCallback.onSelectedObj(aRecipe);

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

    public interface OnObjectClickListener {
        void onSelectedObj(Recipe recipe);
    }

    public interface GiveRecipeList {
        void recipeList(ArrayList<Recipe> recipe);
    }

    public class QueryTask extends AsyncTask<URL, Void, ArrayList<Recipe>> {

        @Override
        protected ArrayList<Recipe> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String recipeSearchResults = null;
            try {
                recipeSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList<Recipe> recipes = BakingJSonUtil.getRecipeFromJson(getActivity(), recipeSearchResults);

                return recipes;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipeDataResults) {
            if (recipeDataResults != null) {
                mRecipeAdapter.setRecipeData(recipeDataResults);
                mCallback_rec.recipeList(recipeDataResults);
            }
        }


    }

    //public ArrayList<Recipe> getRecipeList(){
    //    return mRecipeList;
    //}

}
