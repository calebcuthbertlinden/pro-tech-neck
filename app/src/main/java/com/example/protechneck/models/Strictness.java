package com.example.protechneck.models;

public enum Strictness {

    STRICT,
    NOT_SO_STRICT,
    LENIANT;

    public static Strictness fromValue(String value) {
        for (Strictness strictness : Strictness.values()) {
            if (strictness.toString().equals(value)) {
                return strictness;
            }
        }
        return null;
    }

}
