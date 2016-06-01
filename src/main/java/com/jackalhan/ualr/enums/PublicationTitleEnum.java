package com.jackalhan.ualr.enums;

/**
 * Created by txcakaloglu on 6/1/16.
 */
public enum  PublicationTitleEnum {
    JOURNAL("JOURNAL"),
    CONFERENCE("CONFERENCE");

    private String definition;

    private PublicationTitleEnum(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString(){
        return definition;
    }
}
