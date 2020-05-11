package com.example.protechneck.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.protechneck.models.PostureEventType;
import com.example.protechneck.models.Strictness;
import com.example.protechneck.ui.NeckFeedbackActivity;
import com.example.protechneck.util.PreferencesHelper;
import com.example.protechneck.util.SensorUtil;

import static com.example.protechneck.models.PostureEventType.FLAT_PHONE;
import static com.example.protechneck.models.PostureEventType.LOW_ANGLED;

public class NeckCheckerService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magneticField;

    private boolean showing = false;
    private final static float INITIALISED_PITCH = 0.0F;
    private final static long TIME_DELAY_STRICT = 10000;
    private final static long TIME_DELAY_NOT_SO_STRICT = 30000;
    private final static long TIME_DELAY_LENIANT = 240000;

    public NeckCheckerService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        initSensors();
        setResetTimer(getTime());
        validateAndRegisterListener(accelerometer, Sensor.TYPE_ACCELEROMETER);
        validateAndRegisterListener(magneticField, Sensor.TYPE_MAGNETIC_FIELD);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, SensorRestartBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent broadcastIntent = new Intent(this, SensorRestartBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);
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
        determineAction(SensorUtil.getOrientationValue(event, 1));
    }

    /**
     * @param sensor the sensor to attach the listener to
     * @param type   the type of sensor we want to attach a listener to
     */
    private void validateAndRegisterListener(Sensor sensor, int type) {
        if (sensor != null) {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        } else {
            SensorUtil.getInstance(this).logUnavailableSensor(type);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("MainActivity", "onAccuracyChanged: " + accuracy);
    }

    /**
     * @param pitch the z axis of the phone in portrait mode
     */
    private void determineAction(float pitch) {
        if (!showing && pitch != INITIALISED_PITCH && PreferencesHelper.getInstance(this).getAllowShow()) {
            PostureEventType viewType = SensorUtil.determineViewTypeUsingPitch(pitch);

            Strictness strictness = Strictness
                    .fromValue(PreferencesHelper.getInstance(getApplicationContext()).getPrefAppStrictness());

            if (strictness != null) {
                switch (strictness) {
                    case STRICT:
                        if (viewType.equals(FLAT_PHONE) || viewType.equals(LOW_ANGLED)) {
                            startApp(viewType);
                        }
                        break;
                    case NOT_SO_STRICT:
                        if (viewType.equals(FLAT_PHONE)) {
                            startApp(viewType);
                        }
                        break;
                    case LENIANT:
                        if (viewType.equals(FLAT_PHONE)) {
                            startApp(viewType);
                        }
                        break;
                }
            }
        }
    }

    private void startApp(PostureEventType viewType) {
        Intent intent = new Intent(this, NeckFeedbackActivity.class);
        intent.putExtra("VIEW_TYPE_EXTRA", viewType.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        showing = true;
        startActivity(intent);
        PreferencesHelper.getInstance(this).setAllowedToShow(false);
    }

    private long getTime() {
        Strictness strictness = Strictness
                .fromValue(PreferencesHelper.getInstance(getApplicationContext()).getPrefAppStrictness());
        if (strictness != null) {
            switch (strictness) {
                case STRICT:
                    return TIME_DELAY_STRICT;
                case NOT_SO_STRICT:
                    return TIME_DELAY_NOT_SO_STRICT;
                case LENIANT:
                    return TIME_DELAY_LENIANT;
            }
        }
        // default to leniant so as to not annoy the user
        return TIME_DELAY_LENIANT;
    }

    private void setResetTimer(long time) {
        final Context context = this;
        new Handler().postDelayed(() -> PreferencesHelper.getInstance(context).setAllowedToShow(true), time);
    }
}
