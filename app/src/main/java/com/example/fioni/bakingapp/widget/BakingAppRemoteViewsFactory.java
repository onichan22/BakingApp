package com.example.fioni.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.fioni.bakingapp.R;
import com.example.fioni.bakingapp.data.BakingContract;
import com.example.fioni.bakingapp.utilities.BakingJSonUtil;
import com.example.fioni.bakingapp.utilities.Ingredients;
import com.example.fioni.bakingapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fioni on 9/17/2017.
 */

public class BakingAppRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String SELECTED_RECIPE = "selected recipe";
    public static final int DEFAULT_RECIPE = 1;
    public int aRecipeId;
    List<Ingredients> ingredientsArrayList = new ArrayList<>();
    private Context mContext;
    private Cursor mCursor;

    public BakingAppRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

        Uri uri = Uri.parse(BakingContract.PATH_INGREDIENTS);

        mCursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                String.valueOf(DEFAULT_RECIPE));
        //aRecipeId = DEFAULT_RECIPE;
        //URL recipeSearchUrl = NetworkUtils.buildUrl();
        //new QueryAllIngredientsTask().execute(recipeSearchUrl);


    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }

        Uri uri = Uri.parse(BakingContract.PATH_INGREDIENTS);
        mCursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                String.valueOf(DEFAULT_RECIPE));
        /*aRecipeId = mContext.getSharedPreferences(SELECTED_RECIPE, 0).getInt(SELECTED_RECIPE, 1);
        URL recipeSearchUrl = NetworkUtils.buildUrl();
        new QueryIngredientsTask().execute(recipeSearchUrl);*/
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        //int count = 0;
        //return ingredientsArrayList.size();
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list);
        rv.setTextViewText(R.id.quantity_tv, ingredientsArrayList.get(position).getQuantity());
        rv.setTextViewText(R.id.measure_tv, ingredientsArrayList.get(position).getMeasure());
        rv.setTextViewText(R.id.ingr_name_tv, ingredientsArrayList.get(position).getIngr_name());

        return rv;
        //TODO 002 pending intent to open the application
        //return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public class QueryIngredientsTask extends AsyncTask<URL, Void, ArrayList<Ingredients>> {

        @Override
        protected ArrayList<Ingredients> doInBackground(URL... params) {
            URL searchUrl = params[0];
            String recipeSearchResults = null;
            try {
                recipeSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                ArrayList<Ingredients> ingredients = BakingJSonUtil.getIngredientsFromJson(mContext, recipeSearchResults, aRecipeId);

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
                ingredientsArrayList = recipeDataResults;
                onDataSetChanged();
            }
        }
    }

}
