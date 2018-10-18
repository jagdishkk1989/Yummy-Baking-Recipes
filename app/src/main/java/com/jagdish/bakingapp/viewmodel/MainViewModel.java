package com.jagdish.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.webservices.RestApiClient;
import com.jagdish.bakingapp.webservices.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getName();

    private MutableLiveData<ArrayList<Recipe>> recipeList;
    private RestApiClient apiClient;

    public MainViewModel(@NonNull Application application) {
        super(application);
        apiClient = ServiceGenerator.createService(RestApiClient.class);
    }

    public LiveData<ArrayList<Recipe>> getRecipeList(final ProgressBar progressBar) {
        if (recipeList == null) {
            recipeList = new MutableLiveData<>();
            loadRecipes(progressBar);
        }
        return recipeList;
    }


    public void loadRecipes(final ProgressBar progressBar) {

        Call<ArrayList<Recipe>> call = apiClient.getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    ArrayList<Recipe> result = response.body();
                    ArrayList<Recipe> value = recipeList.getValue();
                    if (value == null || value.isEmpty()) {
                        recipeList.setValue(result);
                    } else {
                        value.addAll(result);
                        recipeList.setValue(value);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                recipeList = null;
            }
        });
    }
}
