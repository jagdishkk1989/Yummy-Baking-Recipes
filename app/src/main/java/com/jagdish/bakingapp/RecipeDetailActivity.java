package com.jagdish.bakingapp;

import android.app.PictureInPictureParams;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

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
    private static final String BUNDLE_PREVIOUS_POSITION = "previousPos";
    private static final String BUNDLE_PLAYER_STATE = "playerState";

    Recipe mRecipe;

    private boolean isTablet = false;
    private boolean isLandscape = false;
    private int currentStepPos = 0;
    private boolean isVideoPlaying = false;

    // video state variable
    private long previousPosition;
    private boolean playState;

    @BindView(R.id.containerVideo)
    FrameLayout containerVideo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

//        PictureInPictureParams params = new PictureInPictureParams.Builder()
//                        // Set actions or aspect ratio.
//                        .build();
//        enterPictureInPictureMode(params);

        if (getIntent().hasExtra("recipe")) {
            mRecipe = getIntent().getExtras().getParcelable("recipe");
        }

        if (savedInstanceState != null) {
            currentStepPos = savedInstanceState.getInt(BUNDLE_STEP_POS);
            isVideoPlaying = savedInstanceState.getBoolean(BUNDLE_IS_VIDEO_PLAYING);
            previousPosition = savedInstanceState.getLong(BUNDLE_PREVIOUS_POSITION);
            isVideoPlaying = savedInstanceState.getBoolean(BUNDLE_IS_VIDEO_PLAYING);
            Log.d(TAG,"jk restore player pos: "+previousPosition+", state: "+playState);
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
        outState.putLong(BUNDLE_PREVIOUS_POSITION, previousPosition);
        outState.putBoolean(BUNDLE_PLAYER_STATE, playState);

        Log.d(TAG,"jk saving player pos: "+previousPosition+", state: "+playState);
    }

    public void setCurrentStepPos(int currentStepPos) {
        this.currentStepPos = currentStepPos;
    }

    public void setIsVideoPlaying(boolean isVideoPlaying) {
        this.isVideoPlaying = isVideoPlaying;
    }

    public void setPreviousPosition(long previousPosition) {
        this.previousPosition = previousPosition;
    }

    public void setPlayState(boolean playState) {
        this.playState = playState;
    }

    public long getPreviousPosition() {
        return previousPosition;
    }

    public boolean getPlayState() {
        return playState;
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
