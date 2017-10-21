package com.example.fioni.bakingapp.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fioni.bakingapp.R;
import com.example.fioni.bakingapp.data.BakingContract;
import com.example.fioni.bakingapp.utilities.Ingredients;
import com.example.fioni.bakingapp.utilities.Recipe;
import com.example.fioni.bakingapp.utilities.RecipeAdapter;
import com.example.fioni.bakingapp.utilities.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.fioni.bakingapp.data.BakingContract.Ingredients.COL_I_RID;

/**
 * Created by fioni on 9/15/2017.
 */

public class IngredientsFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler{

    public int mRecipeId;
    public
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    String mArgs[] = {String.valueOf(mRecipeId)};
    private View mView;
    private RecipeAdapter mRecipeAdapter;
    private Unbinder unbinder;
    private ArrayList<Ingredients> mIngredientSet = new ArrayList<Ingredients>();

    public IngredientsFragment(){

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

        Cursor mCursor = getActivity().getContentResolver().query(
                BakingContract.Ingredients.CONTENT_URI_INGREDIENTS,
                null,
                COL_I_RID + " = ?",
                mArgs,
                null);


        try {
            while (mCursor.moveToNext()) {

                // The Cursor is now set to the right position
                mIngredientSet.add(new Ingredients(mCursor.getString(1),
                        mCursor.getString(2),
                        mCursor.getString(3),
                        mCursor.getString(4)));
            }
        } finally {
            mCursor.close();
        }

        mRecipeAdapter.setIngredientsData(mIngredientSet);

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

}
