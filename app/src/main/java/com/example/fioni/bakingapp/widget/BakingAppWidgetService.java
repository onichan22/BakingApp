package com.example.fioni.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by fioni on 9/17/2017.
 */

public class BakingAppWidgetService extends IntentService {

    public static final String ACTION_SELECT_RECIPE = "com.example.android.bakingapp.action.select_recipe";

    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    public static void startActionSelectRecipe(Context context) {
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_SELECT_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SELECT_RECIPE.equals(action)) {
                handleActionSelectRecipe();
            }
        }
    }

    private void handleActionSelectRecipe() {
        startActionSelectRecipe(getApplicationContext());

    }
}
