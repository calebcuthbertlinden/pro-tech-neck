package com.example.protechneck.ui;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.protechneck.R;
import com.example.protechneck.models.PostureAnalyticsEvent;
import com.example.protechneck.models.PostureEventType;
import com.example.protechneck.util.AnalyticsUtil;
import com.example.protechneck.util.PreferencesHelper;
import com.example.protechneck.util.SensorUtil;


public class TechNeckActivity extends AppCompatActivity implements SensorEventListener {

    private TextView postureType;
    private ConstraintLayout container;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magneticField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_neck);

        postureType = findViewById(R.id.posture_type);
        container = findViewById(R.id.container);

        PreferencesHelper.getInstance(getApplicationContext()).setAppServiceRunningPreference(true);

        initSensors();
        if (!validateAndRegisterListener(accelerometer, Sensor.TYPE_ACCELEROMETER) &&
                !validateAndRegisterListener(magneticField, Sensor.TYPE_MAGNETIC_FIELD)) {
            Toast.makeText(this, "This device does not have the necessary hardware to support this application",
                    Toast.LENGTH_LONG).show();
        }
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
            SensorUtil.logUnavailableSensor(type);
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
                container.setBackgroundColor(ContextCompat.getColor(this, R.color.badBackground));
                break;
            case LOW_ANGLED:
                // Bad posture
                AnalyticsUtil.sendAnalyticsEvent(PostureAnalyticsEvent.OKAY_POSTURE, null, null);
                postureType.setText(getString(R.string.posture_type_okay));
                container.setBackgroundColor(ContextCompat.getColor(this, R.color.okayBackground));
                break;
            case PERFECT_POSTURE:
                // Perfect posture
                AnalyticsUtil.sendAnalyticsEvent(PostureAnalyticsEvent.PERFECT_POSTURE, null, null);
                postureType.setText(getString(R.string.posture_type_good));
                container.setBackgroundColor(ContextCompat.getColor(this, R.color.goodBackground));
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
