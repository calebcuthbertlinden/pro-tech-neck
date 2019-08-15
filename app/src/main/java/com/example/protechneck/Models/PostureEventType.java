package com.example.protechneck.Models;

/**
 * This enum specifies the types of posture events exist according to the orientation and angle of the device
 * Three types of measurements are used to determine this. Azimuth, Pitch and Roll
 */
public enum PostureEventType {
    NA,
    HIGH_ANGLED,
    FLAT_PHONE(1.6F, 2.10F, -0.49F, 5F, -0.20F, 0.20F),
    LOW_ANGLED(0.7F, 1.7F, -0.94F, -0.5F, -0.20F, 0.20F),
    PERFECT_POSTURE(0.32F, 0.75F, -50F, -0.95F, -0.20F, 0.20F);

    /**
     *
     * @param azimuthLower
     * @param azimuthUpper
     * @param pitchLower this angle determines the lower bound of the z axis of the phone
     * @param pitchUpper this angle determines the upper bound of the z axis of the phone
     * @param rollLower
     * @param rollUpper
     */
    PostureEventType(float azimuthLower, float azimuthUpper, float pitchLower, float pitchUpper, float rollLower, float rollUpper) {
        this.azimuthLower = azimuthLower;
        this.azimuthUpper = azimuthUpper;
        this.pitchLower = pitchLower;
        this.pitchUpper = pitchUpper;
        this.rollLower = rollLower;
        this.rollUpper = rollUpper;
    }

    PostureEventType() {}

    public float azimuthLower;
    public float azimuthUpper;
    public float pitchLower;
    public float pitchUpper;
    public float rollLower;
    public float rollUpper;

    public enum OrientationMeasurementType {
        AZIMUTH,
        ROLL,
        PITCH
    }

}
