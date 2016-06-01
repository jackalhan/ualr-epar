package com.jackalhan.ualr.enums;

/**
 * Created by txcakaloglu on 6/1/16.
 */
public enum ScholarActivityTypeEnum {
    GRANT("GRANT"),
    OTHER("OTHER");

    private String definition;

    private ScholarActivityTypeEnum(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString(){
        return definition;
    }
}
