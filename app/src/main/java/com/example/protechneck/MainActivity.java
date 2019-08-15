package com.example.protechneck;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.protechneck.Util.NeckCheckerService;
import com.example.protechneck.Util.NeckCheckerServiceTemp;

public class MainActivity extends AppCompatActivity {

    Intent mServiceIntent;
    private NeckCheckerService mSensorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorService = new NeckCheckerService(this);
        mServiceIntent = new Intent(this, mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }
        this.startService(mServiceIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }
}
