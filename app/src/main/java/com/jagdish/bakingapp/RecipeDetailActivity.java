package com.jagdish.bakingapp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jagdish.bakingapp.data.CurrentStep;
import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.data.Step;
import com.jagdish.bakingapp.fragments.RecipeStepFragment;
import com.jagdish.bakingapp.fragments.StepVideoFragment;
import com.jagdish.bakingapp.viewmodel.StepViewModel;

import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepFragment.OnStepSelectedListener {

    private static final String TAG = RecipeDetailActivity.class.getName();
    private static final String BUNDLE_STEP_POS = "currentStepPos";

    public static final String TAG_FRAGMENT_STEP_VIDEO_DETAILS = "TagFragmentStepVideo";
    private static final int ID_MASTER_FRAGMENT = R.id.master_fragment_steps;
    private static final int ID_STEP_VIDEO_FRAGMENT = R.id.step_video_fragment;
    Recipe mRecipe;

    private boolean isTablet = false;
    private boolean isLandscape = false;
    private int currentStepPos = 0;

    private StepVideoFragment mStepVideoFragment;
    private StepViewModel mStepViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        mStepViewModel = ViewModelProviders.of(this).get(StepViewModel.class);

        if (getIntent().hasExtra("recipe")) {
            mRecipe = getIntent().getExtras().getParcelable("recipe");
        }

        if (mRecipe != null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(mRecipe.getName());
            }
        }

        String screenType = getResources().getString(R.string.screen_type);
        if (screenType.equalsIgnoreCase("tablet")) {
            isTablet = true;
        }

        isLandscape = getResources().getBoolean(R.bool.is_landscape);

        if (savedInstanceState != null) {
            currentStepPos = savedInstanceState.getInt(BUNDLE_STEP_POS);
            setCurrentStepPos(currentStepPos);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            RecipeStepFragment masterFragment =
                    (RecipeStepFragment) getSupportFragmentManager().findFragmentById(ID_MASTER_FRAGMENT);

            // validate the instance of fragment
            if (masterFragment == null) {
                masterFragment = RecipeStepFragment.newInstance(mRecipe, currentStepPos);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(ID_MASTER_FRAGMENT, masterFragment);
                transaction.commit();
            }

            if (isTablet) {
                // select first step
                mStepViewModel.selectStep(new CurrentStep(0, mRecipe.getSteps().get(0)));

                mStepVideoFragment =
                        (StepVideoFragment) getSupportFragmentManager().findFragmentById(ID_STEP_VIDEO_FRAGMENT);
                if (mStepVideoFragment == null) {
                    mStepVideoFragment = new StepVideoFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("stepList", mRecipe.getSteps());
                    mStepVideoFragment.setArguments(bundle);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(ID_STEP_VIDEO_FRAGMENT, mStepVideoFragment);
                    transaction.commit();
                }
            }
        }
    }


    public void setCurrentStepPos(int currentStepPos) {
        this.currentStepPos = currentStepPos;
    }

    public int getCurrentStepPos() {
        return this.currentStepPos;
    }

    @Override
    public void onSelectedStep(int index, Step step) {
        mStepViewModel.selectStep(new CurrentStep(index, step));
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (isTablet) {

            mStepVideoFragment =
                    (StepVideoFragment) getSupportFragmentManager().findFragmentById(R.id.step_video_fragment);
            if (mStepVideoFragment == null) {
                mStepVideoFragment = new StepVideoFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("stepList", mRecipe.getSteps());
                mStepVideoFragment.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(ID_STEP_VIDEO_FRAGMENT, mStepVideoFragment, TAG_FRAGMENT_STEP_VIDEO_DETAILS);
                transaction.commit();
            }
        } else {

            mStepVideoFragment =
                    (StepVideoFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_STEP_VIDEO_DETAILS);

            if (mStepVideoFragment == null) {
                mStepVideoFragment = new StepVideoFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("stepList", mRecipe.getSteps());
                mStepVideoFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(ID_MASTER_FRAGMENT, mStepVideoFragment, TAG_FRAGMENT_STEP_VIDEO_DETAILS);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        }

    }


}
