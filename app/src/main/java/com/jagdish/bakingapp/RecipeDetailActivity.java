package com.jagdish.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.fragments.BaseFragment;
import com.jagdish.bakingapp.fragments.RecipeStepFragment;
import com.jagdish.bakingapp.fragments.StepVideoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailActivity.class.getName();
    private static final String BUNDLE_STEP_POS = "currentStepPos";
    private static final String BUNDLE_IS_VIDEO_PLAYING = "isVideoPlaying";
    private static final String BUNDLE_VIDEO_POSITION = "videoPosition";

    Recipe mRecipe;

    private boolean isTablet = false;
    private boolean isLandscape = false;
    private int currentStepPos = 0;
    private boolean isVideoPlaying = false;

    @BindView(R.id.containerVideo)
    FrameLayout containerVideo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("recipe")) {
            mRecipe = getIntent().getExtras().getParcelable("recipe");
        }

        if (savedInstanceState != null) {
            currentStepPos = savedInstanceState.getInt(BUNDLE_STEP_POS);
            isVideoPlaying = savedInstanceState.getBoolean(BUNDLE_IS_VIDEO_PLAYING);
        }


        if (mRecipe != null) {

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(mRecipe.getName());
            }
        }


        String screenType = getResources().getString(R.string.screen_type);
        if (screenType.equalsIgnoreCase("tablet")) {
            isTablet = true;
        }

        isLandscape = getResources().getBoolean(R.bool.is_landscape);

        if (isLandscape)
            Log.d(TAG, "jk landscape");
        else
            Log.d(TAG, "jk portrait");

        addStepFragment();

        if (isTablet && isLandscape) {
            containerVideo.setVisibility(View.VISIBLE);
            addVideoFragment();
        } else {
            containerVideo.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_STEP_POS, currentStepPos);
        outState.putBoolean(BUNDLE_IS_VIDEO_PLAYING, isVideoPlaying);
        Log.d(TAG, "jk Saving current step pos:" + currentStepPos + ", isVideo Playing: " + isVideoPlaying);
    }

    public void setCurrentStepPos(int currentStepPos) {
        this.currentStepPos = currentStepPos;
    }

    public void setIsVideoPlaying(boolean isVideoPlaying) {
        this.isVideoPlaying = isVideoPlaying;
        Log.d(TAG, "jk set is video playing:" + isVideoPlaying);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            goBack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goBack();
    }

    private void goBack() {

        if (isTablet && isLandscape) {
            finish();
        } else {
            RecipeStepFragment recipeStepFragment = (RecipeStepFragment) getSupportFragmentManager().findFragmentByTag("RecipeStepFragment");
            StepVideoFragment stepVideoFragment = (StepVideoFragment) getSupportFragmentManager().findFragmentByTag("StepVideoFragment");

            if (recipeStepFragment != null && recipeStepFragment.isVisible()) {
                finish();
            } else if (stepVideoFragment != null && stepVideoFragment.isVisible()) {
                setIsVideoPlaying(false);
                addStepFragment();
            }
        }
    }


    private void addStepFragment() {

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("recipeDetail", mRecipe);
        bundle.putInt("currentStepPos", currentStepPos);
        bundle.putBoolean("isVideoPlaying", isVideoPlaying);
        recipeStepFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.containerStep, recipeStepFragment, "RecipeStepFragment");

        fragmentTransaction.commit();

    }

    private void addVideoFragment() {

        StepVideoFragment stepVideoFragment = new StepVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("stepList", mRecipe.getSteps());
        bundle.putInt("currentPos", currentStepPos);
        stepVideoFragment.setArguments(bundle);
        BaseFragment.replaceFragment(RecipeDetailActivity.this, R.id.containerVideo, stepVideoFragment, "StepVideoFragment");

    }

}
