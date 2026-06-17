package com.rentacar.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Transmission {
    Otamatik("otomatik"),
    Manuel("manuel");

    private final String value;

    Transmission(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Transmission fromValue(String value) {
        for (Transmission transmission : Transmission.values()) {
            if (transmission.value.equals(value)) {
                return transmission;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found in inventory");
    }
}
