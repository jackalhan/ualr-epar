package com.jackalhan.ualr.enums;

/**
 * Created by txcakaloglu on 6/1/16.
 */

public enum PublicationStatusEnum {
    PUBLISHED("PUBLISHED"),
    ACCEPTED("ACCEPTED"),
    SUBMITTED("SUBMITTED"),
    PLANNED("PLANNED"),
    IN_PRESS("IN PRESS");

    private String definition;

    private PublicationStatusEnum(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString(){
        return definition;
    }
}
