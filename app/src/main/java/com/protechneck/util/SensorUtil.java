package com.protechneck.util;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.protechneck.models.PostureEventType;

public class SensorUtil {

    private static float[] accelerometerReading = new float[3];
    private static float[] magnetometerReading = new float[3];
    private static float[] rotationMatrix = new float[9];
    private static float[] orientationAngles = new float[3];

    private static SensorUtil instance;
    private final Context context;

    private SensorUtil(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    public static SensorUtil getInstance(@NonNull Context context) {
        synchronized(SensorUtil.class) {
            if (instance == null) {
                instance = new SensorUtil(context);
            }
            return instance;
        }
    }


    /**
     * @param pitch
     * @return
     */
    public static PostureEventType determineViewTypeUsingPitch(float pitch) {
        if (pitch > PostureEventType.FLAT_PHONE.pitchLower && pitch < PostureEventType.FLAT_PHONE.pitchUpper) {
            return PostureEventType.FLAT_PHONE;
        } else if (pitch > PostureEventType.LOW_ANGLED.pitchLower && pitch < PostureEventType.LOW_ANGLED.pitchUpper) {
            return PostureEventType.LOW_ANGLED;
        } else if (pitch > PostureEventType.PERFECT_POSTURE.pitchLower && pitch < PostureEventType.PERFECT_POSTURE.pitchUpper) {
            return PostureEventType.PERFECT_POSTURE;
        } else {
            return PostureEventType.NA;
        }
    }

    /**
     * @param azimuth
     * @param pitch
     * @param roll
     * @return
     */
    public static PostureEventType determineViewType(float azimuth, float pitch, float roll) {
        if (azimuth > PostureEventType.FLAT_PHONE.azimuthLower && azimuth < PostureEventType.FLAT_PHONE.azimuthUpper
                && pitch > PostureEventType.FLAT_PHONE.pitchLower && pitch < PostureEventType.FLAT_PHONE.pitchUpper
                && roll > PostureEventType.FLAT_PHONE.rollLower && roll < PostureEventType.FLAT_PHONE.rollUpper) {
            return PostureEventType.FLAT_PHONE;
        } else if (azimuth > PostureEventType.LOW_ANGLED.azimuthLower && azimuth < PostureEventType.LOW_ANGLED.azimuthUpper &&
                pitch > PostureEventType.LOW_ANGLED.pitchLower && pitch < PostureEventType.LOW_ANGLED.pitchUpper &&
                roll > PostureEventType.LOW_ANGLED.rollLower && roll < PostureEventType.LOW_ANGLED.rollUpper) {
            return PostureEventType.LOW_ANGLED;
        } else if (azimuth > PostureEventType.PERFECT_POSTURE.azimuthLower && azimuth < PostureEventType.PERFECT_POSTURE.azimuthUpper
                && pitch > PostureEventType.PERFECT_POSTURE.pitchLower && pitch < PostureEventType.PERFECT_POSTURE.pitchUpper
                && roll > PostureEventType.PERFECT_POSTURE.rollLower && roll < PostureEventType.PERFECT_POSTURE.rollUpper) {
            return PostureEventType.PERFECT_POSTURE;
        } else {
            return PostureEventType.NA;
        }
    }

    /**
     * @param event
     * @return
     */
    public static float[] getSensorEvent(SensorEvent event) {
        return event.values.clone();
    }

    /**
     * @param type
     */
    public void logUnavailableSensor(int type) {
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                Toast.makeText(context, "No Accelerometer available on this device", Toast.LENGTH_LONG).show();
                Log.e("", "No Accelerometer available on this device");
                break;
            case Sensor.TYPE_GYROSCOPE:
                Toast.makeText(context, "No Gyroscope available on this device", Toast.LENGTH_LONG).show();
                Log.e("", "No Gyroscope available on this device");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                Toast.makeText(context, "No [*] available on this device", Toast.LENGTH_LONG).show();
                Log.e("", "No [*] available on this device");
                break;
        }
    }

    /**
     * @param serviceClass
     * @return
     */
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    /**
     * @param event
     * @param index
     * @return
     */
    public static float getOrientationValue(SensorEvent event, int index) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerReading = SensorUtil.getSensorEvent(event);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magnetometerReading = SensorUtil.getSensorEvent(event);
                break;
            default:
                return 0F;
        }

        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, accelerometerReading, magnetometerReading);

        float[] orientationValues = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);
        }

        return orientationValues[index];
    }

    /**
     *
     */
    public static void setRotationMatrix() {
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);
    }

    /**
     *
     */
    public static void setOrientation() {
        SensorManager.getOrientation(rotationMatrix, orientationAngles);
    }
}
