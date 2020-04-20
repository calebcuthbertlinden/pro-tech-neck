package com.example.protechneck.ui;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

public class OnboardingModel implements Parcelable {
    private String title;
    private String description;
    private @DrawableRes int imageId;

    public OnboardingModel(String title, String description, @DrawableRes int imageId) {
        this.title = title;
        this.description = description;
        this.imageId = imageId;
    }

    public final Creator<OnboardingModel> CREATOR = new Creator<OnboardingModel>() {
        public OnboardingModel createFromParcel(Parcel in) {
            return new OnboardingModel(in);
        }
        public OnboardingModel[] newArray(int size) {
            return new OnboardingModel[size];
        }
    };

    protected OnboardingModel(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.imageId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.description);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }


}
