package com.example.protechneck.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.protechneck.R;
import com.example.protechneck.services.NeckCheckerService;
import com.example.protechneck.util.SensorUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.protechneck.MainActivity.PREF_IS_RETURNING_USER;
import static com.example.protechneck.MainActivity.PREF_IS_SERVICE_RUNNING;
import static com.example.protechneck.MainActivity.PREF_KEY;

public class SettingsActivity extends AppCompatActivity {

    Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void onResume() {
        super.onResume();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_continue)
    public void onContinueClick(View view) {
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

}
