package com.jagdish.bakingapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.jagdish.bakingapp.data.Ingredient;
import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.data.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDbInstance;

    public static AppDatabase getDatabase(Context context) {
        if (appDbInstance == null) {
            synchronized (AppDatabase.class) {
                if (appDbInstance == null) {
                    appDbInstance = Room.databaseBuilder(context,
                            AppDatabase.class, "bakingapp")
                            .build();
                }
            }
        }
        return appDbInstance;
    }

    public abstract RecipeDao recipeDao();
}