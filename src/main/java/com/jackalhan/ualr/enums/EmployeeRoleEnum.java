package com.jackalhan.ualr.enums;

/**
 * Created by txcakaloglu on 6/23/16.
 */
public enum EmployeeRoleEnum {
        DEAN("DEAN"),
        CHAIR("CHAIR"),
        FACULTY("FACULTY"),
        OTHER("OTHER");

        private String code;

        private EmployeeRoleEnum(String code) {
            this.code = code;
        }

        @Override
        public String toString(){
            return code;
        }
}
