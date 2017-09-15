package com.example.fioni.bakingapp;

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
import android.widget.Toast;

import com.example.fioni.bakingapp.utilities.BakingJSonUtil;
import com.example.fioni.bakingapp.utilities.NetworkUtils;
import com.example.fioni.bakingapp.utilities.Recipe;
import com.example.fioni.bakingapp.utilities.RecipeAdapter;
import com.example.fioni.bakingapp.utilities.Step;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by fioni on 9/3/2017.
 */

public class StepsFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler{
    private View mView;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    public int mRecipeId;
    OnObjectClickListener mCallback;

    public interface OnObjectClickListener{
        void onSelectedStep(Step step);
    }

    public  StepsFragment(){

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

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (StepsFragment.OnObjectClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.recycler_view, container, false);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        //do nothing
    }

    @Override
    public void onClick(Step aStep) {
        Toast.makeText(getContext(), aStep.getShort_desc(), Toast.LENGTH_SHORT).show();
        mCallback.onSelectedStep(aStep);
    }

    public class QueryTask extends AsyncTask<URL, Void, ArrayList<Step>> {

        @Override
        protected ArrayList<Step> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String recipeSearchResults = null;
            try {
                recipeSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList<Step> steps = BakingJSonUtil.getStepsFromJson(getActivity(), recipeSearchResults, mRecipeId);

                return steps;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Step> recipeDataResults) {
            if (recipeDataResults != null) {
                mRecipeAdapter.setStepData(recipeDataResults);
            }
        }
    }

    public void setRecipeId (String recipeId){
        mRecipeId = Integer.parseInt(recipeId)-1;
    }
}
