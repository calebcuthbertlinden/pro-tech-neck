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
        // TODO
        return PostureEventType.LOW_ANGLED;
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
