package com.example.protechneck.Util;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
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
    private Context applicationContext;

    private float[] accelerometerReading = new float[3];
    private float[] magnetometerReading = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientationAngles = new float[3];

    private boolean enabled = false;

    public NeckCheckerService() {
        super();
    }

    public NeckCheckerService(Context applicationContext) {
        super();
        this.applicationContext = applicationContext;
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

        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
    }

    /**
     *
     * @param event any event triggered by a sensor with a listener attached
     */
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerReading = SensorUtil.getSensorEvent(event);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometerReading = SensorUtil.getSensorEvent(event);
                break;
            default:
                return;
        }

        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, accelerometerReading, magnetometerReading);

        float[] orientationValues = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }

        determineAction(orientationValues[1]);
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
