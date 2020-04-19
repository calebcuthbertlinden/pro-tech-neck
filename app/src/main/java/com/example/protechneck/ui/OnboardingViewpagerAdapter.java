package com.example.protechneck.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class OnboardingViewpagerAdapter extends FragmentStatePagerAdapter {

    private final Fragment[] fragments;
    private List<OnboardingModel> model;

    public OnboardingViewpagerAdapter(@NonNull FragmentManager fm, List<OnboardingModel> model) {
        super(fm);
        this.model = model;
        this.fragments = new Fragment[ model.size() ];
        for (int index = 0 ; index < model.size() ; index++){
            fragments[index] = OnboardingFragment
                    .newInstance(this.model.get(index), index);
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return this.model.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }
}
