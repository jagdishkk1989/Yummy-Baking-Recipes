package com.jagdish.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jagdish.bakingapp.R;
import com.jagdish.bakingapp.RecipeDetailActivity;
import com.jagdish.bakingapp.StepNavigatorCallBack;
import com.jagdish.bakingapp.adapter.StepAdapter;
import com.jagdish.bakingapp.data.Recipe;
import com.jagdish.bakingapp.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.feng.common.stepperview.VerticalStepperView;

public class RecipeStepFragment extends Fragment {

    private static final String TAG = RecipeStepFragment.class.getName();
    private static final String BUNDLE_STEP_POS = "currentStepPos";

    private static final String KEY_RECIPE_DETAIL = "recipeDetail";
    private static final String KEY_SELECT_STEP_POS = "currentStepPos";


    private View rootView;

    @BindView(R.id.tvIngredients)
    TextView tvIngredients;

    @BindView(R.id.vertical_stepper_view)
    VerticalStepperView vertical_stepper_view;

    private Recipe mRecipe = null;
    private RecipeDetailActivity mParentActivity;
    private int currentStepPos = 0;
    private boolean isTablet = false;
    private boolean isLandscape = false;

    private OnStepSelectedListener onStepSelectedListener;


    public static RecipeStepFragment newInstance(Recipe recipe, int position) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_RECIPE_DETAIL, recipe);
        bundle.putInt(KEY_SELECT_STEP_POS, position);
        fragment.setArguments(bundle);
        return fragment;
    }

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

        if (getActivity() != null) {
            mParentActivity = (RecipeDetailActivity) getActivity();
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                mRecipe = bundle.getParcelable(KEY_RECIPE_DETAIL);
                currentStepPos = bundle.getInt(KEY_SELECT_STEP_POS);
            }
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnStepSelectedListener) {
            onStepSelectedListener = (OnStepSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepSelectedListener");
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

            if (savedInstanceState != null) {
                currentStepPos = savedInstanceState.getInt(BUNDLE_STEP_POS);
            }


            // active previous step
            vertical_stepper_view.setCurrentStep(currentStepPos);
            if (onStepSelectedListener != null && isTablet) {
                Step step = mRecipe.getSteps().get(currentStepPos);
                onStepSelectedListener.onSelectedStep(currentStepPos, step);
            }
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
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

            if (isTablet) {
                onStepSelectedListener.onSelectedStep(current + 1, mRecipe.getSteps().get(current + 1));
            }
        }

        @Override
        public void onClickWatch() {
            int pos = vertical_stepper_view.getCurrentStep();
            mParentActivity.setCurrentStepPos(pos);
            onStepSelectedListener.onSelectedStep(pos, mRecipe.getSteps().get(pos));
        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_STEP_POS, currentStepPos);
    }

    public void updateCurrentPosition(int position) {
        this.currentStepPos = position;
        vertical_stepper_view.setCurrentStep(position);
    }

    public interface OnStepSelectedListener {
        void onSelectedStep(int index, Step step);
    }
}
