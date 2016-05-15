package com.jackalhan.ualr.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by jackalhan on 5/11/16.
 */
public enum ImageTypeEnum {
    ICON("image/x-icon"),
    PNG("image/png"),
    JPEG("image/jpeg");

    private String type;
    private ImageTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }


}
