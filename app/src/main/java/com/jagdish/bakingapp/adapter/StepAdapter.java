package com.jagdish.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jagdish.bakingapp.R;
import com.jagdish.bakingapp.StepNavigatorCallBack;
import com.jagdish.bakingapp.data.Step;

import java.util.ArrayList;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;

public class StepAdapter implements IStepperAdapter {

    private ArrayList<Step> stepList;
    private StepNavigatorCallBack stepNavigatorCallBack;

    public StepAdapter(ArrayList<Step> stepList, StepNavigatorCallBack stepNavigatorCallBack) {
        this.stepList = stepList;
        this.stepNavigatorCallBack = stepNavigatorCallBack;
    }

    @NonNull
    @Override
    public CharSequence getTitle(int i) {
        return stepList.get(i).getShortDescription();
    }

    @Nullable
    @Override
    public CharSequence getSummary(int i) {
        return stepList.get(i).getDescription();
    }

    @Override
    public int size() {
        return stepList.size();
    }

    @Override
    public View onCreateCustomView(final int i, Context context, VerticalStepperItemView parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int layoutIdForListItem = R.layout.step_list_item;

        View view = layoutInflater.inflate(layoutIdForListItem, parent, false);

        TextView mStepDesc = (TextView) view.findViewById(R.id.step_textview);

        Button btnWatch = (Button) view.findViewById(R.id.btn_watch);
        Button btnNext = (Button) view.findViewById(R.id.btn_next);

        final Step step = stepList.get(i);

        if (step.getShortDescription() != null) {
            mStepDesc.setText(step.getDescription());
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepNavigatorCallBack.onClickNextStep();
            }
        });

        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepNavigatorCallBack.onClickWatch();
            }
        });

        return view;
    }

    @Override
    public void onShow(int i) {

    }

    @Override
    public void onHide(int i) {

    }
}
