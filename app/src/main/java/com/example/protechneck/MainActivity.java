package com.example.protechneck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.protechneck.services.NeckCheckerService;
import com.example.protechneck.util.SensorUtil;

public class MainActivity extends AppCompatActivity {

    Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onResume() {
        super.onResume();

        if (isFirstTimeUser()) {
            NeckCheckerService mSensorService = new NeckCheckerService();
            mServiceIntent = new Intent(this, mSensorService.getClass());
            if (!SensorUtil.isMyServiceRunning(mSensorService.getClass(), this)) {
                startService(mServiceIntent);
            }
            updateSharedPref();
            this.startService(mServiceIntent);
            finish();
        } else {
            // TODO 11 - go to onboarding
        }
    }

    private void updateSharedPref() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("TECH_NECK_RUNNING", false);
        editor.apply();
    }

    private boolean isFirstTimeUser() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        return pref.getBoolean("IS_RETURNING_USER", false);
    }

    protected void onStop() {
        super.onStop();
    }
}
