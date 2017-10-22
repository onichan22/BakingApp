package com.example.fioni.bakingapp.utilities;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.fioni.bakingapp.R;

import java.util.ArrayList;

/**
 * Created by fioni on 9/16/2017.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsAdapterViewHolder>{
    private final SettingsAdapterOnClickHandler mClickHandler;
    public int mSelectedItem = -1;
    public ArrayList<Recipe> mRecipe;

    public SettingsAdapter(SettingsAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
    }

    @Override
    public SettingsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        final View view = inflater.inflate(R.layout.settings_list, viewGroup, false);
        return new SettingsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SettingsAdapterViewHolder holder, int position) {
        holder.mRadio.setChecked(position == mSelectedItem);
        holder.mText.setText(mRecipe.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }

    public void setAdapterData(ArrayList<Recipe> recipeData) {
        mRecipe = recipeData;
        notifyDataSetChanged();
    }

    public interface SettingsAdapterOnClickHandler {
        void onClick(Recipe aRecipe);
    }

    public class SettingsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RadioButton mRadio;
        public TextView mText;

        public SettingsAdapterViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.set_recipe_tv);
            mRadio = (RadioButton) itemView.findViewById(R.id.radio_button);

            itemView.setOnClickListener(this);
            //this.itemView.setOnClickListener(clickListener);
            mRadio.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mSelectedItem = getAdapterPosition();
            Log.i("getAdapterPosition", getAdapterPosition() + "");
            mClickHandler.onClick(mRecipe.get(mSelectedItem - 1));
            //handler.onClickRecipe(mRecipe.get(mSelectedItem));
            notifyItemRangeChanged(0, mRecipe.size());
        }
    }
}
