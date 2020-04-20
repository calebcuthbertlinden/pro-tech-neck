package com.example.protechneck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.protechneck.services.NeckCheckerService;
import com.example.protechneck.ui.OnboardingFragment;
import com.example.protechneck.ui.OnboardingModel;
import com.example.protechneck.ui.OnboardingViewpagerAdapter;
import com.example.protechneck.util.SensorUtil;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_KEY = "MyPref";
    public static final String PREF_IS_SERVICE_RUNNING = "TECH_NECK_RUNNING";
    private static final String PREF_IS_RETURNING_USER = "IS_RETURNING_USER";

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

        if (isReturningUser()) {
            NeckCheckerService mSensorService = new NeckCheckerService();
            mServiceIntent = new Intent(this, mSensorService.getClass());
            if (!SensorUtil.isMyServiceRunning(mSensorService.getClass(), this)) {
                startService(mServiceIntent);
            }
            setAppServiceRunningPreference();
            // close app and start service
            this.startService(mServiceIntent);
            finish();
        } else {
            setupOnboarding();
        }
    }

    private boolean isReturningUser() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_KEY, 0);
        return pref.getBoolean(PREF_IS_RETURNING_USER, false);
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
        // TODO this code will sit here until next ticket implemented
        NeckCheckerService mSensorService = new NeckCheckerService();
        mServiceIntent = new Intent(this, mSensorService.getClass());
        if (!SensorUtil.isMyServiceRunning(mSensorService.getClass(), this)) {
            startService(mServiceIntent);
        }
        setAppServiceRunningPreference();
        // close app and start service
        this.startService(mServiceIntent);
        setIsReturningUserPreference(true);
        finish();
    }

    private void setAppServiceRunningPreference() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_IS_SERVICE_RUNNING, false);
        editor.apply();
    }

    private void setIsReturningUserPreference(boolean value) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_IS_RETURNING_USER, value);
        editor.apply();
    }

    protected void onStop() {
        super.onStop();
    }
}
