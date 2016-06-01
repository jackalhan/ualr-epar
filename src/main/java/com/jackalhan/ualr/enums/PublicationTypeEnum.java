package com.jackalhan.ualr.enums;

/**
 * Created by txcakaloglu on 6/1/16.
 */
public enum PublicationTypeEnum {
    JOURNAL("JOURNAL"),
    CONFERENCE("CONFERENCE"),
    BOOK_CHAPTER("BOOK CHAPTER"),
    OTHER("OTHER");

    private String definition;

    private PublicationTypeEnum(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString(){
        return definition;
    }
}
