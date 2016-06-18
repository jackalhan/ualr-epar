package com.jackalhan.ualr.enums;

/**
 * Created by jackalhan on 5/15/16.
 */
public enum InstructionTypeEnum {
    PEDAGOGICAL("PED"),
    INDIVIDUALIZED("IND"),
    ADMINISTRATIVE("ADM"),
    NONADMINISTRATIVE("NON");

    private String type;
    private InstructionTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }


}
