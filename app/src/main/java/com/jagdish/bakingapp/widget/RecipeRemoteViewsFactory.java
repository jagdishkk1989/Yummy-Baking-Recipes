package com.jagdish.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.jagdish.bakingapp.R;
import com.jagdish.bakingapp.data.Ingredient;
import com.jagdish.bakingapp.data.RecipeFilter;
import com.jagdish.bakingapp.db.AppDatabase;
import com.jagdish.bakingapp.utility.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = RecipeRemoteViewsFactory.class.getName();
    private Context context;
    private List<String> ingredients;

    private AppDatabase appDatabase;

    public RecipeRemoteViewsFactory(Context context) {
        this.context = context;
        appDatabase = AppDatabase.getDatabase(this.context);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        int recipeId = SessionManager.getIntegerSharedPrefs(SessionManager.RECIPE_ID);

        if (recipeId != -1) {
            ingredients = new ArrayList<>();

            RecipeFilter recipeFilter =
                    appDatabase.recipeDao().getRecipeById(recipeId);

            if (recipeFilter != null) {
                for (Ingredient ingredient : recipeFilter.ingredients) {
                    ingredients.add("\u25CF "+String.format(Locale.getDefault(), "%.1f %s %s",
                            ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getName()));
                }
            }
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (ingredients == null) {
            return 0;
        }
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || ingredients == null) {
            return null;
        }

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_ingradient_list_item);
        rv.setTextViewText(R.id.widget_item_textview, ingredients.get(position));

        Intent fillInIntent = new Intent();
        rv.setOnClickFillInIntent(R.id.widget_item_textview, fillInIntent);

        return rv;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
