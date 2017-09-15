package com.example.fioni.bakingapp.utilities;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fioni.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by fioni on 9/2/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Recipe> mRecipeData;
    private ArrayList<Step> mStepData;
    private ArrayList<Ingredients> mIngrData;
    private String mText;
    private int mIndicator;
    private static int RECIPE_INDICATOR = 1;
    private static int STEP_INDICATOR = 2;
    private static int INGR_INDICATOR = 3;
    private static final int TYPE_ONE = 0;
    private static final int TYPE_GROUP = 1;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {

        void onClick(Recipe aRecipe);

        void onClick(Step aStep);
    }

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class RecipeAdapterViewHolder0 extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView textView;

        public RecipeAdapterViewHolder0(View itemView) {
            super(itemView);
            if (mIndicator == RECIPE_INDICATOR) {
                cardView = (CardView) itemView.findViewById(R.id.card_view);
                textView = (TextView) itemView.findViewById(R.id.recipename_text);
            }
            if (mIndicator == STEP_INDICATOR) {
                textView = (TextView) itemView.findViewById(R.id.stepname_text);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (mIndicator == RECIPE_INDICATOR) {
                Recipe aRecipe = mRecipeData.get(adapterPosition);
                mClickHandler.onClick(aRecipe);
            }
            if (mIndicator == STEP_INDICATOR) {
                Step aStep = mStepData.get(adapterPosition);
                mClickHandler.onClick(aStep);
            }
        }
    }

    public class RecipeAdapterViewHolder1 extends RecyclerView.ViewHolder{
        TextView textView_q;
        TextView textView_m;
        TextView textView_in;

        public RecipeAdapterViewHolder1(View itemView) {
            super(itemView);

            textView_q = (TextView) itemView.findViewById(R.id.quantity_tv);
            textView_m = (TextView) itemView.findViewById(R.id.measure_tv);
            textView_in = (TextView) itemView.findViewById(R.id.ingr_name_tv);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mIndicator){
            case 1:
                position = TYPE_ONE;
                break;
            case 2:
                position = TYPE_ONE;
                break;
            case 3:
                position = TYPE_GROUP;
                break;
        }
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        switch (viewType){
            case 0:
                int layoutIdForListItem = 0;
                if (mIndicator == RECIPE_INDICATOR) {
                    layoutIdForListItem = R.layout.recipe_list;
                }
                if (mIndicator == STEP_INDICATOR) {
                    layoutIdForListItem = R.layout.step_list;
                }
                View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
                viewHolder = new RecipeAdapterViewHolder0(view);
                ;break;
            case 1:
                layoutIdForListItem = R.layout.ingredient_list;
                View view2 = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
                viewHolder = new RecipeAdapterViewHolder1(view2);
                ;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        /*if (mIndicator == RECIPE_INDICATOR) {
            mText = mRecipeData.get(position).getName();
            holder.textView.setText(mText);
        }
        if (mIndicator == STEP_INDICATOR) {
            mText = mStepData.get(position).getShort_desc();
            holder.textView.setText(mText);
        }*/
        switch(holder.getItemViewType()){
            case 0:
                RecipeAdapterViewHolder0 viewHolder0 = (RecipeAdapterViewHolder0)holder;
                switch (mIndicator) {
                    case 1:
                        mText = mRecipeData.get(position).getName();
                        viewHolder0.textView.setText(mText);
                        break;
                    case 2:
                        mText = mStepData.get(position).getShort_desc();
                        viewHolder0.textView.setText(mText);
                        break;
                    case 3:
                        break;
                };break;
            case 1:
                RecipeAdapterViewHolder1 viewHolder1 = (RecipeAdapterViewHolder1)holder;
                viewHolder1.textView_q.setText(mIngrData.get(position).getQuantity());
                viewHolder1.textView_m.setText(mIngrData.get(position).getMeasure());
                viewHolder1.textView_in.setText(mIngrData.get(position).getIngr_name());
                ;break;
        }

    }

    @Override
    public int getItemCount() {
        int count = 0;
        switch (mIndicator) {
            case 1:
                if (null == mRecipeData) {
                    return 0;
                } else {
                    count = mRecipeData.size();
                }
                ;
                break;
            case 2:
                if (null == mStepData) {
                    return 0;
                } else {
                    count = mStepData.size();
                }
                ;
                break;
            case 3:
                if (null == mIngrData) {
                    return 0;
                } else {
                    count = mIngrData.size();
                }
                ;
                break;
        }

        return count;
    }

    public void setRecipeData(ArrayList<Recipe> recipeData) {
        mRecipeData = recipeData;
        mIndicator = RECIPE_INDICATOR;
        notifyDataSetChanged();
    }

    public void setStepData(ArrayList<Step> recipeData) {
        mStepData = recipeData;
        mIndicator = STEP_INDICATOR;
        notifyDataSetChanged();
    }

    public void setIngredientsData(ArrayList<Ingredients> ingredientsData) {
        mIngrData = ingredientsData;
        mIndicator = INGR_INDICATOR;
        notifyDataSetChanged();
    }
}
