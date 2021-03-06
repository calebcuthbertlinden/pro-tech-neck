package com.example.protechneck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.protechneck.services.NeckCheckerService;
import com.example.protechneck.ui.onboarding.OnboardingModel;
import com.example.protechneck.ui.onboarding.OnboardingViewpagerAdapter;
import com.example.protechneck.ui.SettingsActivity;
import com.example.protechneck.util.PreferencesHelper;
import com.example.protechneck.util.SensorUtil;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    Intent mServiceIntent;

    @BindView(R.id.onboarding_pager) ViewPager viewPager;
    @BindView(R.id.onboarding_indicator) InkPageIndicator indicator;
    @BindView(R.id.done_button) Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onResume() {
        super.onResume();
        ButterKnife.bind(this);

        viewPager = findViewById(R.id.onboarding_pager);
        indicator = findViewById(R.id.onboarding_indicator);

        if (PreferencesHelper.getInstance(getApplicationContext()).isReturningUser()) {
            NeckCheckerService mSensorService = new NeckCheckerService();
            mServiceIntent = new Intent(this, mSensorService.getClass());
            if (!SensorUtil.getInstance(this).isMyServiceRunning(mSensorService.getClass())) {
                PreferencesHelper.getInstance(getApplicationContext()).setAppServiceRunningPreference(false);
                // close app and start service
                this.startService(mServiceIntent);
            }
            finish();
        } else {
            setupOnboarding();
        }
    }

    private void setupOnboarding() {
        List<OnboardingModel> pages = new ArrayList<>();
        pages.add(new OnboardingModel(getResources().getString(R.string.onboarding_page_1_title),
                getResources().getString(R.string.onboarding_page_1_description),
                R.drawable.onboarding_2));
        pages.add(new OnboardingModel(getResources().getString(R.string.onboarding_page_2_title),
                getResources().getString(R.string.onboarding_page_2_description),
                R.drawable.onboarding_1));

        if (viewPager != null) {
            viewPager.setAdapter(new OnboardingViewpagerAdapter(getSupportFragmentManager(), pages));
            indicator.setViewPager(viewPager);
        }
    }

    @OnClick(R.id.done_button)
    public void onGetStartedClick(View view) {
        this.startActivity(new Intent(this, SettingsActivity.class));
    }

    protected void onStop() {
        super.onStop();
    }
}
