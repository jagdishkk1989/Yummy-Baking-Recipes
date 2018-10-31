package com.jagdish.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentStep  implements Parcelable  {

    int index;
    Step step;

    protected CurrentStep(Parcel in) {
        index = in.readInt();
        step = in.readTypedObject(Step.CREATOR);
    }

    public CurrentStep(int index, Step step) {
        this.index = index;
        this.step = step;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeTypedObject(step,1);

    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

}
