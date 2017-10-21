package com.example.fioni.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.fioni.bakingapp.MainActivity;
import com.example.fioni.bakingapp.R;
import com.example.fioni.bakingapp.SettingsActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    public static final String SELECTED_RECIPE = "selected recipe";
    private static final String WIDGET_ID_KEY = "widget id key";
    private static final String WIDGET_DATA_KEY = "widget data key";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Intent intentWidget) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widgetTitleLabel, pendingIntent);
        views.setRemoteAdapter(R.id.widgetListView, intentWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, views.getLayoutId());
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void selectRecipeToDisplay(SettingsActivity settingsActivity) {

        Context context = settingsActivity.getApplicationContext();
        String data = String.valueOf(context.getSharedPreferences(SELECTED_RECIPE, Context.MODE_PRIVATE));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        //updateIntent.putExtra(BakingAppWidgetProvider.WIDGET_ID_KEY, ids);
        updateIntent.putExtra(BakingAppWidgetProvider.WIDGET_DATA_KEY, data);
        context.sendBroadcast(updateIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(
                new ComponentName(context, BakingAppWidgetProvider.class));
        this.onUpdate(context, man, ids);

        /*if (intent.hasExtra(WIDGET_ID_KEY)) {
            int[] ids = intent.getExtras().getIntArray(WIDGET_ID_KEY);
            int id = ids[0];
            if (intent.hasExtra(WIDGET_DATA_KEY)) {
                //String data = intent.getExtras().getParcelable(WIDGET_DATA_KEY);
                this.updateAppWidget(context, AppWidgetManager.getInstance(context), id, intent);
            } else {
                this.onUpdate(context, AppWidgetManager.getInstance(context), ids);
            }
        } else super.onReceive(context, intent);*/
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        Intent intentWidget = new Intent(context, BakingAppRemoteViewsService.class);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, intentWidget);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

