package com.example.protechneck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.protechneck.services.NeckCheckerService;
import com.example.protechneck.util.SensorUtil;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_KEY = "MyPref";
    public static final String PREF_IS_SERVICE_RUNNING = "TECH_NECK_RUNNING";
    private static final String PREF_IS_RETURNING_USER = "IS_RETURNING_USER";

    Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onResume() {
        super.onResume();

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

    private void setAppServiceRunningPreference() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_IS_SERVICE_RUNNING, false);
        editor.apply();
    }

    private boolean isReturningUser() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_KEY, 0);
        return pref.getBoolean(PREF_IS_RETURNING_USER, false);
    }

    private void setupOnboarding() {
        // TODO 11 - go to onboarding
    }

    protected void onStop() {
        super.onStop();
    }
}
