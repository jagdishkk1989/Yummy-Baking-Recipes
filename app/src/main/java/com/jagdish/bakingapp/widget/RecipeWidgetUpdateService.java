package com.jagdish.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.jagdish.bakingapp.data.Ingredient;
import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.db.AppDatabase;

import java.util.List;


public class RecipeWidgetUpdateService extends IntentService {

    public static final String UPDATE_RECIPE_INGREDIENTS = "com.jagdish.bakingapp.action.update_recipe";

    private List<Ingredient> mIngredients;
    private static Recipe mRecipe;
    private static Context mContext;
    private AppDatabase appDatabase;

    public RecipeWidgetUpdateService() {
        super("RecipeWidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_RECIPE_INGREDIENTS.equals(action)) {

                // update recipe to database
                appDatabase = AppDatabase.getDatabase(mContext);
                appDatabase.recipeDao().insertOrReplaceRecipe(mRecipe);

                handleActionUpdateRecipe();
            }
        }

    }

    private void handleActionUpdateRecipe() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientProvider.class));
        RecipeIngredientProvider.updateAppWidget(this, appWidgetManager, appWidgetIds, mRecipe.getName(), mRecipe);
    }

    public static void startActionUpdateRecipe(Context context, Recipe recipe) {
        mContext = context;
        mRecipe = recipe;

        Intent intent = new Intent(context, RecipeWidgetUpdateService.class);
        intent.setAction(UPDATE_RECIPE_INGREDIENTS);
        context.startService(intent);
    }
}
