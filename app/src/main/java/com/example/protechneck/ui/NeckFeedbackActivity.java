package com.example.protechneck.ui;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.protechneck.R;
import com.example.protechneck.models.PostureAnalyticsEvent;
import com.example.protechneck.models.PostureEventType;
import com.example.protechneck.models.Strictness;
import com.example.protechneck.services.NeckCheckerService;
import com.example.protechneck.util.AnalyticsUtil;
import com.example.protechneck.util.PreferencesHelper;
import com.example.protechneck.util.SensorUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NeckFeedbackActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magneticField;

    @BindView(R.id.btn_continue) Button btnFinish;
    @BindView(R.id.heading) TextView postureType;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.container) ConstraintLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_neck);
        ButterKnife.bind(this);

        PreferencesHelper.getInstance(getApplicationContext()).setAppServiceRunningPreference(true);

        initSensors();
        if (!validateAndRegisterListener(accelerometer, Sensor.TYPE_ACCELEROMETER) &&
                !validateAndRegisterListener(magneticField, Sensor.TYPE_MAGNETIC_FIELD)) {
            Toast.makeText(this, "This device does not have the necessary hardware to support this application",
                    Toast.LENGTH_LONG).show();
        }

        btnFinish.setOnClickListener(view -> {
            finish();
            NeckCheckerService mSensorService = new NeckCheckerService();
            Intent mServiceIntent = new Intent(this, mSensorService.getClass());
            this.stopService(mServiceIntent);
        });
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        SensorUtil.setRotationMatrix();
        SensorUtil.setOrientation();
    }

    /**
     * @param event any event triggered by a sensor with a listener attached
     */
    public void onSensorChanged(SensorEvent event) {
        determineAction(SensorUtil.determineViewTypeUsingPitch(
                SensorUtil.getOrientationValue(event, 1)));
    }

    /**
     * @param sensor the sensor to attach the listener to
     * @param type   the type of sensor we want to attach a listener to
     */
    private boolean validateAndRegisterListener(Sensor sensor, int type) {
        if (sensor != null) {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
            return true;
        } else {
            SensorUtil.getInstance(this).logUnavailableSensor(type);
            return false;
        }
    }

    /**
     * @param viewType
     */
    private void determineAction(PostureEventType viewType) {
        if (viewType == null) {
            return;
        }

        switch (viewType) {
            case FLAT_PHONE:
                // Bad posture
                AnalyticsUtil.sendAnalyticsEvent(PostureAnalyticsEvent.BAD_POSTURE, null, null);
                postureType.setText(getString(R.string.posture_type_bad));
                description.setText(getString(R.string.posture_type_bad_description));
                container.setBackground(ContextCompat.getDrawable(this, R.drawable.feedback_background_bad));
                btnFinish.setVisibility(View.GONE);
                break;
            case LOW_ANGLED:
                // Bad posture
                AnalyticsUtil.sendAnalyticsEvent(PostureAnalyticsEvent.OKAY_POSTURE, null, null);
                postureType.setText(getString(R.string.posture_type_okay));
                description.setText(getString(R.string.posture_type_okay_description));
                container.setBackground(ContextCompat.getDrawable(this, R.drawable.feedback_background_okay));
                btnFinish.setVisibility(View.GONE);
                break;
            case PERFECT_POSTURE:
                // Perfect posture
                AnalyticsUtil.sendAnalyticsEvent(PostureAnalyticsEvent.PERFECT_POSTURE, null, null);
                postureType.setText(getString(R.string.posture_type_good));
                description.setText(getString(R.string.posture_type_good_description));
                container.setBackground(ContextCompat.getDrawable(this, R.drawable.feedback_background_good));
                btnFinish.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        PreferencesHelper.getInstance(getApplicationContext()).setAppServiceRunningPreference(false);
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("MainActivity", "onAccuracyChanged: " + accuracy);
    }

    protected void onResume() {
        super.onResume();
    }

}
