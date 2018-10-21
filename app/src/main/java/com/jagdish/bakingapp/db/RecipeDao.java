package com.jagdish.bakingapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.jagdish.bakingapp.data.Ingredient;
import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.data.RecipeFilter;
import com.jagdish.bakingapp.data.Step;

import java.util.List;

@Dao
public abstract class RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertIngredients(List<Ingredient> ingredients);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSteps(List<Step> steps);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void _insertRecipe(Recipe recipe);


    public void insertOrReplaceRecipe(Recipe recipe) {

        List<Ingredient> ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) {
            ingredient.setRecipeId(recipe.getId());
        }
        insertIngredients(ingredients);

        List<Step> steps = recipe.getSteps();
        for (Step step : steps) {
            step.setRecipeId(recipe.getId());
        }
        insertSteps(steps);

        _insertRecipe(recipe);
    }

    @Transaction
    @Query("SELECT * FROM Recipe WHERE id = :id")
    public abstract RecipeFilter getRecipeById(int id);

}