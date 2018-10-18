package com.jagdish.bakingapp;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.jagdish.bakingapp.adapter.RecipeAdapter;
import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.utility.ConnectionDetector;
import com.jagdish.bakingapp.viewmodel.MainViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private static final String BUNDLE_RECIPE_LIST = "recipeList";

    @BindView(R.id.recipe_recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private MainViewModel mViewModel;

    private ConnectionDetector mConnectionDetector;

    private ArrayList<Recipe> mRecipeList;
    private RecipeAdapter mRecipeAdapter;
    private boolean isTablet = false;
    SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mConnectionDetector = new ConnectionDetector(this);
        getIdlingResource();

        String screenType = getResources().getString(R.string.screen_type);
        if (screenType.equalsIgnoreCase("tablet")) {
            isTablet = true;
        }

        setupRecyclerView();
        if (savedInstanceState == null) {
            bindData();
        } else {
            mRecipeList = savedInstanceState.getParcelableArrayList(BUNDLE_RECIPE_LIST);
            if (mRecipeList != null) {
                setAdapter();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_RECIPE_LIST, mRecipeList);
    }


    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT && !isTablet) {
            mRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        }
    }


    private void bindData() {

        if (mConnectionDetector.isConnectedToInternet()) {
            mIdlingResource.setIdleState(false);
            progressBar.setVisibility(View.VISIBLE);
            mViewModel.getRecipeList(progressBar).observe(this,
                    new Observer<ArrayList<Recipe>>() {
                        @Override
                        public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                            if (recipes != null && recipes.size() > 0) {
                                // mIdlingResource.isIdleNow();

                                if (mIdlingResource != null) {
                                    mIdlingResource.setIdleState(true);
                                }

                                mRecipeList = recipes;
                                setAdapter();
                            }
                        }
                    });
        } else {
            showNoInternetDialog();
        }

    }

    private void setAdapter() {
        mRecipeAdapter = new RecipeAdapter(MainActivity.this, this.mRecipeList);
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    private void showNoInternetDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.no_internet_connection));
        builder.setMessage(getResources().getString(R.string.message_no_internet));

        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
