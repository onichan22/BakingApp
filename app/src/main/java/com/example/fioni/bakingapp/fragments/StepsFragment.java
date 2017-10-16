package com.example.fioni.bakingapp.fragments;

import android.content.Context;
import android.database.Cursor;
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
import com.example.fioni.bakingapp.data.BakingContract;
import com.example.fioni.bakingapp.utilities.Recipe;
import com.example.fioni.bakingapp.utilities.RecipeAdapter;
import com.example.fioni.bakingapp.utilities.Step;

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
    private ArrayList<Step> mStepSet = new ArrayList<Step>();

    public  StepsFragment(){

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

        Cursor mCursor = getActivity().getContentResolver().query(
                BakingContract.Steps.CONTENT_URI,
                null,
                null,
                null,
                null);

        try {
            while (mCursor.moveToNext()) {

                // The Cursor is now set to the right position
                mStepSet.add(new Step(mCursor.getString(1),
                        mCursor.getString(2),
                        mCursor.getString(3),
                        mCursor.getString(4),
                        mCursor.getString(5),
                        mCursor.getString(6)));
            }
        } finally {
            mCursor.close();
        }

        mRecipeAdapter.setStepData(mStepSet);
        mRecyclerView.setAdapter(mRecipeAdapter);

/*
        if (isOnline()) {
            URL recipeSearchUrl = NetworkUtils.buildUrl();
            new QueryTask().execute(recipeSearchUrl);
            //Toast.makeText(getContext(), "Test", Toast.LENGTH_LONG);
        }
*/
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

/*    public void setRecipeId(String recipeId) {
        mRecipeId = Integer.parseInt(recipeId) - 1;
    }*/

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

}
