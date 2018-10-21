package com.jagdish.bakingapp.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class RecipeFilter {
    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id",
            entityColumn = "recipeId")
    public List<Ingredient> ingredients;

    @Relation(parentColumn = "id",
            entityColumn = "recipeId")
    public List<Step> steps;
}
