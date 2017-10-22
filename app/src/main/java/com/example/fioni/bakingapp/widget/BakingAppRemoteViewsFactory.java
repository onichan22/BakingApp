package com.example.fioni.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.fioni.bakingapp.R;
import com.example.fioni.bakingapp.data.BakingContract;
import com.example.fioni.bakingapp.utilities.Ingredients;

import java.util.ArrayList;
import java.util.List;

import static com.example.fioni.bakingapp.data.BakingContract.Ingredients.COL_I_RID;

/**
 * Created by fioni on 9/17/2017.
 */

public class BakingAppRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final int DEFAULT_RECIPE = 1;
    private static final String SELECTED_RECIPE = "selected recipe";
    private static final String SETTINGS_PREFS = "Settings Preferences";
    public String aRecipeId = "0";
    List<Ingredients> ingredientsArrayList = new ArrayList<>();
    //String mArgs[] = {String.valueOf(DEFAULT_RECIPE)};
    String mArgs[] = {aRecipeId};
    private Context mContext;
    private Cursor mCursor;

    public BakingAppRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        aRecipeId = mContext.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE).getString(SELECTED_RECIPE, "0");

    }

    @Override
    public void onCreate() {
        //String mArgs[] = {String.valueOf(DEFAULT_RECIPE)};

        Uri uri = BakingContract.Ingredients.CONTENT_URI_INGREDIENTS;

        mCursor = mContext.getContentResolver().query(uri,
                null,
                COL_I_RID + " = ?",
                mArgs,
                null);

        //DatabaseUtils.dumpCursor(mCursor);
        //aRecipeId = DEFAULT_RECIPE;
        //URL recipeSearchUrl = NetworkUtils.buildUrl();
        //new QueryAllIngredientsTask().execute(recipeSearchUrl);


    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }
        aRecipeId = mContext.getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE).getString(SELECTED_RECIPE, "0");
        mArgs[0] = aRecipeId;

        Uri uri = BakingContract.Ingredients.CONTENT_URI_INGREDIENTS;
        mCursor = mContext.getContentResolver().query(uri,
                null,
                COL_I_RID + " = ?",
                mArgs,
                null);

        DatabaseUtils.dumpCursor(mCursor);
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
        rv.setTextViewText(R.id.quantity_tv, mCursor.getString(2));
        rv.setTextViewText(R.id.measure_tv, mCursor.getString(3));
        rv.setTextViewText(R.id.ingr_name_tv, mCursor.getString(4));

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


}
