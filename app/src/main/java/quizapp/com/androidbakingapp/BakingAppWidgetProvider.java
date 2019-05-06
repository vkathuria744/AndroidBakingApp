package quizapp.com.androidbakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

public class BakingAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetid) {

        SharedPreferences preferences = context.getSharedPreferences(Activity_RecipeDetail.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String recipeName = preferences.getString(Activity_RecipeDetail.RECIPE_NAME_KEY, "Recipe Name");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
        views.setTextViewText(R.id.ingredientsLabel_widget_tv, recipeName);

        Intent intent = new Intent(context, GridRemoteViewsService.class);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.recipes_gv_widget, intent);

        appWidgetManager.updateAppWidget(appWidgetid, views);

    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName((context), BakingAppWidgetProvider.class));
        context.sendBroadcast(intent);
    }
    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if
                (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, BakingAppWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(cn);

            if (appWidgetIds != null && appWidgetIds.length > 0)
            {
                onUpdate(context, appWidgetManager, appWidgetIds);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipes_gv_widget);

            }
        }
        super
                .onReceive(context, intent);
    }
}
