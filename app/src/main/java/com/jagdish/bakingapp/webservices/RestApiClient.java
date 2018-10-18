package com.jagdish.bakingapp.webservices;

import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.utility.AppConstants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestApiClient {

    @GET(AppConstants.GET_RECIPES)
    Call<ArrayList<Recipe>> getRecipes();
}