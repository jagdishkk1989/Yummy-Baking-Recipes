package com.jagdish.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jagdish.bakingapp.R;
import com.jagdish.bakingapp.RecipeDetailActivity;
import com.jagdish.bakingapp.data.Recipe;


public class RecipeIngredientProvider extends AppWidgetProvider {

    private static String mRecipeName;
    private static Recipe mRecipe;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetId, String recipeName,
                                       Recipe recipe) {

        mRecipeName = recipeName;
        mRecipe = recipe;

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra("recipe", recipe);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_provider);

        views.setTextViewText(R.id.widget_recipe_name, recipeName);

        views.setRemoteAdapter(R.id.widget_listview_ingredients,
                RecipeRemoteViewsService.getIntent(context));

        views.setPendingIntentTemplate(R.id.widget_listview_ingredients, pendingIntent);

        views.setOnClickPendingIntent(R.id.recipe_layout, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, new int[]{appWidgetId}, mRecipeName, mRecipe);
        }
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

