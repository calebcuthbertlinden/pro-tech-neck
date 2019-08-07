package com.example.protechneck;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

public class SensorUtil {

    public SensorUtil(){}

    /**
     *
     * @param azimuth
     * @param pitch
     * @param roll
     * @return
     */
    static PostureEventType determineViewType(float azimuth, float pitch, float roll) {
        if (azimuth > PostureEventType.FLAT_PHONE.azimuthLower && azimuth < PostureEventType.FLAT_PHONE.azimuthUpper
                && pitch > PostureEventType.FLAT_PHONE.pitchLower && pitch < PostureEventType.FLAT_PHONE.pitchUpper
                && roll > PostureEventType.FLAT_PHONE.rollLower && roll < PostureEventType.FLAT_PHONE.rollUpper) {
            return PostureEventType.FLAT_PHONE;
        } else if (azimuth > PostureEventType.LOW_ANGLED.azimuthLower && azimuth < PostureEventType.LOW_ANGLED.azimuthUpper
                && pitch > PostureEventType.LOW_ANGLED.pitchLower && pitch < PostureEventType.LOW_ANGLED.pitchUpper
                && roll > PostureEventType.LOW_ANGLED.rollLower && roll < PostureEventType.LOW_ANGLED.rollUpper) {
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
     *
     * @param event
     * @return
     */
    static float[] getSensorEvent(SensorEvent event) {
        return event.values.clone();
    }

    /**
     *
     * @param type
     */
    static void logUnavailableSensor(int type) {
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                Log.e("", "No Accelerometer available on this device");
                break;
            case Sensor.TYPE_GYROSCOPE:
                Log.e("", "No Gyroscope available on this device");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                Log.e("", "No [*] available on this device");
                break;
        }
    }

}
