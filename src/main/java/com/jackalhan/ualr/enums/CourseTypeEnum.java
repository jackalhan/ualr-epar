package com.jackalhan.ualr.enums;

/**
 * Created by jackalhan on 5/15/16.
 */
public enum CourseTypeEnum {
    UNDERGRADUATE_COURSE("U"),
    DUAL_LISTED_COURSE_L("DL"),
    DUAL_LISTED_COURSE_U("DU"),
    GRADUATE_COURSE("G"),
    MASTER_STUDIES("MS"),
    DOCTORAL_STUDIES("PhD"),
    OTHER_STUDIES("O");

    private String code;

    private CourseTypeEnum(String code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return code;
    }

}
