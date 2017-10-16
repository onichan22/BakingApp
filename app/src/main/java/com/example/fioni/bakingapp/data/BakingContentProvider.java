package com.example.fioni.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.example.fioni.bakingapp.data.BakingContract.Ingredients.TABLE_NAME_INGREDIENTS;
import static com.example.fioni.bakingapp.data.BakingContract.Recipes.TABLE_NAME_RECIPES;
import static com.example.fioni.bakingapp.data.BakingContract.Steps.TABLE_NAME_STEPS;

public class BakingContentProvider extends ContentProvider {

    public static final int ALL_RECIPES = 100;
    public static final int LIST_INGREDIENTS = 101;
    private static final int LIST_STEPS = 102;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BakingDbHelper mDbHelper;

    public BakingContentProvider() {
    }

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_RECIPES, ALL_RECIPES);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_INGREDIENTS, LIST_INGREDIENTS);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_STEPS, LIST_STEPS);

        return uriMatcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        //long id = db.insert(TABLE_NAME_RECIPES, null, values);
        switch (match) {
            case ALL_RECIPES:
                long id = db.insert(TABLE_NAME_RECIPES, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(BakingContract.Recipes.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case LIST_INGREDIENTS:
                long idI = db.insert(TABLE_NAME_INGREDIENTS, null, values);
                if (idI > 0) {
                    returnUri = ContentUris.withAppendedId(BakingContract.Ingredients.CONTENT_URI, idI);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case LIST_STEPS:
                long idS = db.insert(TABLE_NAME_STEPS, null, values);
                if (idS > 0) {
                    returnUri = ContentUris.withAppendedId(BakingContract.Steps.CONTENT_URI, idS);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new BakingDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {

            case ALL_RECIPES:
                retCursor = db.query(TABLE_NAME_RECIPES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case LIST_INGREDIENTS:
                retCursor = db.query(TABLE_NAME_INGREDIENTS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case LIST_STEPS:
                retCursor = db.query(TABLE_NAME_STEPS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
