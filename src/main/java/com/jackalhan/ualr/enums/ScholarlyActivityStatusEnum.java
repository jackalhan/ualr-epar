package com.jackalhan.ualr.enums;

/**
 * Created by txcakaloglu on 6/1/16.
 */
public enum ScholarlyActivityStatusEnum {
    AWARDED("AWARDED"),
    PENDING("PENDING"),
    IN_WORK("IN-WORK"),
    DECLINED("DECLINED");

    private String definition;

    private ScholarlyActivityStatusEnum(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString(){
        return definition;
    }


}
