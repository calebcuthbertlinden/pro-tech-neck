package com.example.protechneck.Util;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.protechneck.Models.PostureEventType;

public class NeckCheckerService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magneticField;

    private boolean enabled = false;

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
     *
     * @param event any event triggered by a sensor with a listener attached
     */
    public void onSensorChanged(SensorEvent event) {
        determineAction(SensorUtil.getOrientationValue(event, 1));
    }

    /**
     *
     * @param sensor the sensor to attach the listener to
     * @param type the type of sensor we want to attach a listener to
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
     *
     * @param pitch the z axis of the phone in portrait mode
     */
    private void determineAction(float pitch) {
        PostureEventType viewType = SensorUtil.determineViewTypeUsingPitch(pitch);
        if (viewType == PostureEventType.FLAT_PHONE && !enabled && pitch != 0.0) {
            Intent intent = new Intent(this, TechNeckActivity.class);
            intent.putExtra("VIEW_TYPE_EXTRA", viewType.toString());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            enabled = true;
            startActivity(intent);
        }
    }
}
