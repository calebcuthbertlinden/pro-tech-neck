package com.example.protechneck;

public enum PostureEventType {
    NA,
    HIGH_ANGLED,
    FLAT_PHONE(1.6F, 2.10F, -0.10F, 0.10F, -0.20F, 0.20F),
    LOW_ANGLED(0.7F, 1.7F, -0.6F, -0.35F, -0.20F, 0.20F),
    PERFECT_POSTURE(0.32F, 0.75F, -1.3F, 1.3F, -0.20F, 0.20F);

    /**
     *
     * @param azimuthLower
     * @param azimuthUpper
     * @param pitchLower
     * @param pitchUpper
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
