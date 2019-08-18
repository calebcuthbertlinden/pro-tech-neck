package com.example.protechneck;

import android.content.Intent;
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
        NeckCheckerService mSensorService = new NeckCheckerService();
        mServiceIntent = new Intent(this, mSensorService.getClass());
        if (!SensorUtil.isMyServiceRunning(mSensorService.getClass(), this)) {
            startService(mServiceIntent);
        }
        this.startService(mServiceIntent);
        finish();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }
}
