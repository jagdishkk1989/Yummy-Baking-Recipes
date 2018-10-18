package com.jagdish.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jagdish.bakingapp.R;
import com.jagdish.bakingapp.RecipeDetailActivity;
import com.jagdish.bakingapp.StepNavigatorCallBack;
import com.jagdish.bakingapp.adapter.StepAdapter;
import com.jagdish.bakingapp.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.feng.common.stepperview.VerticalStepperView;

public class RecipeStepFragment extends BaseFragment {

    private static final String TAG = RecipeStepFragment.class.getName();


    private View rootView;

    @BindView(R.id.tvIngredients)
    TextView tvIngredients;


    @BindView(R.id.vertical_stepper_view)
    VerticalStepperView vertical_stepper_view;

    private Recipe mRecipe = null;
    private RecipeDetailActivity mParentActivity;
    private int currentStepPos = 0;
    private boolean isVideoPlaying = false;
    private boolean isTablet = false;
    private boolean isLandscape = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLandscape = getResources().getBoolean(R.bool.is_landscape);
        String screenType = getResources().getString(R.string.screen_type);
        if (screenType.equalsIgnoreCase("tablet")) {
            isTablet = true;
        } else {
            isTablet = false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() != null) {
            mParentActivity = (RecipeDetailActivity) getActivity();
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                mRecipe = bundle.getParcelable("recipeDetail");
                currentStepPos = bundle.getInt("currentStepPos");
                isVideoPlaying = bundle.getBoolean("isVideoPlaying");
            }
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);

        if (mRecipe != null) {

            if (mRecipe.getIngredients() != null) {
                String gradients = "";
                for (int i = 0; i < mRecipe.getIngredients().size(); i++) {
                    gradients = gradients.concat(mRecipe.getIngredients().get(i).toString()) + "\n";
                }
                tvIngredients.setText(gradients);
            }

            setStepsAdapter();

            Log.d(TAG, "jk fragment current step pos:" + currentStepPos);
            Log.d(TAG, "jk fragment  is video playing:" + isVideoPlaying);
            vertical_stepper_view.setCurrentStep(currentStepPos);


            if (isVideoPlaying) {
                stepNavigatorCallBack.onClickWatch();
            }
        }
        return rootView;
    }

    private void setStepsAdapter() {

        StepAdapter stepsAdapter = new StepAdapter(mRecipe.getSteps(), stepNavigatorCallBack);
        vertical_stepper_view.setStepperAdapter(stepsAdapter);
    }

    StepNavigatorCallBack stepNavigatorCallBack = new StepNavigatorCallBack() {
        @Override
        public void onClickNextStep() {
            int current = vertical_stepper_view.getCurrentStep();
            currentStepPos = current + 1;
            mParentActivity.setCurrentStepPos(currentStepPos);
            vertical_stepper_view.setCurrentStep(current + 1);
            if (isTablet && isLandscape) {
                goToVideoFragment(current + 1);
            }
        }

        @Override
        public void onClickWatch() {
            int pos = vertical_stepper_view.getCurrentStep();
            mParentActivity.setIsVideoPlaying(true);
            goToVideoFragment(pos);
        }
    };

    private void goToVideoFragment(int position) {

        StepVideoFragment stepVideoFragment = new StepVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("stepList", mRecipe.getSteps());
        bundle.putInt("currentPos", position);
        stepVideoFragment.setArguments(bundle);

        if (isTablet && isLandscape)
            BaseFragment.replaceFragment(mParentActivity, R.id.containerVideo, stepVideoFragment, "StepVideoFragment");
        else if (isTablet) {
            BaseFragment.replaceFragment(mParentActivity, R.id.containerStep, stepVideoFragment, "StepVideoFragment");
        }
    }


}
