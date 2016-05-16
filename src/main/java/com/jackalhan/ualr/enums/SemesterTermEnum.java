package com.jackalhan.ualr.enums;

/**
 * Created by jackalhan on 5/15/16.
 */
public enum SemesterTermEnum {
    SPRING (10),
    FALL (60),
    SUMMER (30);


    private int value;
    private SemesterTermEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
