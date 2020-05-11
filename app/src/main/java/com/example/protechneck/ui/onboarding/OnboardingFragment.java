package com.example.protechneck.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.protechneck.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnboardingFragment extends Fragment {

    private static final String PAGE = "page";
    private static final String EXTRA_POSITION = "position";
    private OnboardingModel page;
    private int pageNumber;

    @BindView(R.id.title) TextView title;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.onboardingImage) AppCompatImageView imageView;

    public static Fragment newInstance(OnboardingModel page, int position) {
        OnboardingFragment onBoardingFragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_POSITION, position);
        args.putParcelable(PAGE + position, page);
        onBoardingFragment.setArguments(args);
        return onBoardingFragment;
    }

    public OnboardingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (this.getArguments() != null) {
            this.page = this.getArguments().getParcelable(PAGE + this.getArguments().getInt(EXTRA_POSITION));
            this.pageNumber = this.getArguments().getInt(EXTRA_POSITION);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.onboarding_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     *
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title.setText(page.getTitle());
        description.setText(page.getDescription());
        switch (pageNumber) {
            case 0:
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), page.getImageId()));
                break;
            case 1:
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), page.getImageId()));
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
