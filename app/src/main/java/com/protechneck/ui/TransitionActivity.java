package com.protechneck.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.protechneck.services.NeckCheckerService;

import protechneck.R;

public class TransitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
    }

    protected void onResume() {
        super.onResume();

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            // close app and start service
            NeckCheckerService mSensorService = new NeckCheckerService();
            Intent mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());
            getApplicationContext().startService(mServiceIntent);
            finish();
        }, 3000);

    }
}
