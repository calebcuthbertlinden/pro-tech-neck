package com.example.protechneck.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.protechneck.models.PostureEventType;
import com.example.protechneck.ui.TechNeckActivity;
import com.example.protechneck.util.SensorUtil;

public class NeckCheckerService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magneticField;

    private boolean enabled = false;
    private final float INITIALISED_PITCH = 0.0F;

    public NeckCheckerService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        initSensors();
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
            SensorUtil.logUnavailableSensor(type);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.e("MainActivity", "onAccuracyChanged: " + accuracy);
    }

    /**
     * @param pitch the z axis of the phone in portrait mode
     */
    private void determineAction(float pitch) {
        if (!enabled && pitch != INITIALISED_PITCH) {
            PostureEventType viewType = SensorUtil.determineViewTypeUsingPitch(pitch);
            switch (viewType) {
                case FLAT_PHONE:
                case LOW_ANGLED:
                    startService(viewType);
                    break;
                case HIGH_ANGLED:
                    break;
                case PERFECT_POSTURE:
                    break;
                case NA:
                    break;
                default:
                    break;
            }
        }
    }

    private void startService(PostureEventType viewType) {
        Intent intent = new Intent(this, TechNeckActivity.class);
        intent.putExtra("VIEW_TYPE_EXTRA", viewType.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        enabled = true;
        startActivity(intent);

//          TODO implement notification service
//            Intent intent = new Intent(getApplicationContext(), NotificationService.class);
//            intent.setAction(NotificationService.ACTION_START_FOREGROUND_SERVICE);
//            startService(intent);
//            enabled = SensorUtil.isMyServiceRunning(this.getClass(), this);
//            stopSelf();

    }
}
