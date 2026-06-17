package com.rentacar.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RentalStatus {
    ACTIVE("active"),
    CANCELLED("cancelled");

    private final String value;

    RentalStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RentalStatus fromValue(String value){
        for(RentalStatus status :RentalStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
            throw new IllegalArgumentException("Invalid RentalStatus value" + value);
    }

}
