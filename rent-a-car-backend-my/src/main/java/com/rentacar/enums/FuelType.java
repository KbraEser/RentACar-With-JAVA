package com.rentacar.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FuelType {
    BENZIN("benzin"),
    DIZEL("dizel"),
    ELEKTRIK("elektrik"),
    HYBRID("hybrid");

    private final String value;

    FuelType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static FuelType fromValue(String value){

        for(FuelType fuelType:FuelType.values()){
            if(fuelType.getValue().equals(value)){
                return fuelType;
            }
        }
        throw new IllegalArgumentException("No enum const FuelType from value " + value);
    }
}
