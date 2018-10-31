package com.jagdish.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.jagdish.bakingapp.data.CurrentStep;

public class StepViewModel extends ViewModel {

    private final MutableLiveData<CurrentStep> mSelectedStep = new MutableLiveData<>();

    public void selectStep(CurrentStep item) {
        mSelectedStep.setValue(item);
    }

    public LiveData<CurrentStep> getSelectedStep() {
        return mSelectedStep;
    }

}
