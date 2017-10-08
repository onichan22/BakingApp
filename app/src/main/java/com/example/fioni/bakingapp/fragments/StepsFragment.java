package com.example.fioni.bakingapp.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
 * Created by fioni on 9/3/2017.
 */

public class StepsFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler{
    final String LIST_STATE_KEY = "List state key";
    public
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    public int mRecipeId;
    OnObjectClickListener mCallback;
    private View mView;
    private RecipeAdapter mRecipeAdapter;
    private Parcelable mListState;
    private LinearLayoutManager mLayoutManager;
    private Unbinder unbinder;

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
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
            Log.i("This state", mListState.toString());

        } else {
            mLayoutManager = new LinearLayoutManager(getContext());
        }

        mView = inflater.inflate(R.layout.recycler_view, container, false);

        ButterKnife.bind(this, mView);
        unbinder = ButterKnife.bind(this, mView);
        mRecyclerView.setLayoutManager(mLayoutManager);

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

    public void setRecipeId(String recipeId) {
        mRecipeId = Integer.parseInt(recipeId) - 1;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);

    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        // Retrieve list state and list/item positions
        if (state != null)
            mListState = state.getParcelable(LIST_STATE_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface OnObjectClickListener {
        void onSelectedStep(Step step);
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
}
