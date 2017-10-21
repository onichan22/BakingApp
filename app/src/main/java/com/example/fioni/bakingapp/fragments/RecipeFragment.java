package com.example.fioni.bakingapp.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
import android.widget.Toast;

import com.example.fioni.bakingapp.R;
import com.example.fioni.bakingapp.data.BakingContract;
import com.example.fioni.bakingapp.utilities.BakingJSonUtil;
import com.example.fioni.bakingapp.utilities.Global;
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
    private URL mRecipeSearchUrl = NetworkUtils.buildUrl();

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
            //URL recipeSearchUrl = NetworkUtils.buildUrl();
            new QueryRecipeTask().execute(mRecipeSearchUrl);
            new QueryAllIngredientsTask().execute(mRecipeSearchUrl);
            new QueryAllStepsTask().execute(mRecipeSearchUrl);
            //Toast.makeText(getContext(), "Test", Toast.LENGTH_LONG);

        } else if (!isOnline()) {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
            Cursor mCursor = getActivity().getContentResolver().query(
                    BakingContract.Recipes.CONTENT_URI_RECIPES,
                    null,
                    null,
                    null,
                    null);

            ArrayList<Recipe> mArrayList = new ArrayList<Recipe>();
            try {
                while (mCursor.moveToNext()) {

                    // The Cursor is now set to the right position
                    mArrayList.add(new Recipe(mCursor.getString(1),
                            mCursor.getString(2),
                            mCursor.getString(3),
                            mCursor.getString(4)));
                }
            } finally {
                mCursor.close();
            }

            Global.recipes = mArrayList;
            mRecipeAdapter.setRecipeData(mArrayList);
            mCallback_rec.recipeList(mArrayList);
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

    private void addRecipesToDB(ArrayList<Recipe> recipeDataResults) {
        for (int i = 0; i < recipeDataResults.size(); i++) {
            Recipe currentRecipe = recipeDataResults.get(i);
            String id = currentRecipe.getId();
            String name = currentRecipe.getName();
            String image = currentRecipe.getImage();
            String servings = currentRecipe.getServings();

            ContentValues cv = new ContentValues();
            cv.put(BakingContract.Recipes.COL_R_ID, id);
            cv.put(BakingContract.Recipes.COL_R_NAME, name);
            cv.put(BakingContract.Recipes.COL_R_IMAGE, image);
            cv.put(BakingContract.Recipes.COL_R_SERVINGS, servings);

            getActivity().getContentResolver().insert(BakingContract.Recipes.CONTENT_URI_RECIPES, cv);
        }
        Global.recipes = recipeDataResults;
    }

    private void addIngredientsToDB(ArrayList<Ingredients> recipeDataResults) {
        for (int i = 0; i < recipeDataResults.size(); i++) {
            Ingredients currentIngredient = recipeDataResults.get(i);
            String id = currentIngredient.getR_id();
            String name = currentIngredient.getIngr_name();
            String measure = currentIngredient.getMeasure();
            String quantity = currentIngredient.getQuantity();

            ContentValues cv = new ContentValues();
            cv.put(BakingContract.Ingredients.COL_I_RID, id);
            cv.put(BakingContract.Ingredients.COL_I_NAME, name);
            cv.put(BakingContract.Ingredients.COL_I_MEASURE, measure);
            cv.put(BakingContract.Ingredients.COL_I_QUANTITY, quantity);

            getActivity().getContentResolver().insert(BakingContract.Ingredients.CONTENT_URI_INGREDIENTS, cv);
        }
        Global.ingredients = recipeDataResults;
    }

    private void addStepsToDB(ArrayList<Step> recipeDataResults) {
        for (int i = 0; i < recipeDataResults.size(); i++) {
            Step currentStep = recipeDataResults.get(i);
            String rid = currentStep.getR_id();
            String id = currentStep.getId();
            String shortDesc = currentStep.getShort_desc();
            String desc = currentStep.getDesc();
            String videoURL = currentStep.getVideo_url();
            String thumbURL = currentStep.getThumb_url();


            ContentValues cv = new ContentValues();
            cv.put(BakingContract.Steps.COL_S_RID, rid);
            cv.put(BakingContract.Steps.COL_S_ID, id);
            cv.put(BakingContract.Steps.COL_S_SHORT_DESC, shortDesc);
            cv.put(BakingContract.Steps.COL_S_DESC, desc);
            cv.put(BakingContract.Steps.COL_S_VIDEO_URL, videoURL);
            cv.put(BakingContract.Steps.COL_S_THUMB_URL, thumbURL);

            getActivity().getContentResolver().insert(BakingContract.Steps.CONTENT_URI_STEPS, cv);
        }
        Global.steps = recipeDataResults;
    }

    public interface OnObjectClickListener {
        void onSelectedObj(Recipe recipe);
    }

    public interface GiveRecipeList {
        void recipeList(ArrayList<Recipe> recipe);
    }

    public class QueryRecipeTask extends AsyncTask<URL, Void, ArrayList<Recipe>> {

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
                addRecipesToDB(recipeDataResults);
            }
        }

    }

    public class QueryAllIngredientsTask extends AsyncTask<URL, Void, ArrayList<Ingredients>> {

        @Override
        protected ArrayList<Ingredients> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String recipeSearchResults = null;
            try {
                recipeSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList<Ingredients> ingredients = BakingJSonUtil.getAllIngredientsFromJson(getActivity(), recipeSearchResults);

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
                //mRecipeAdapter.setIngredientsData(recipeDataResults);
                addIngredientsToDB(recipeDataResults);
                //Global.ingredients = recipeDataResults;
            }
        }
    }

    public class QueryAllStepsTask extends AsyncTask<URL, Void, ArrayList<Step>> {

        @Override
        protected ArrayList<Step> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String recipeSearchResults = null;
            try {
                recipeSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList<Step> steps = BakingJSonUtil.getAllStepsFromJson(getActivity(), recipeSearchResults);

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
                //mRecipeAdapter.setStepData(recipeDataResults);
                addStepsToDB(recipeDataResults);
                //Global.steps = recipeDataResults;
            }
        }
    }
}
