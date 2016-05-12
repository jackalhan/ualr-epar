package com.jackalhan.ualr.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by jackalhan on 5/11/16.
 */
public enum RoleEnum implements GrantedAuthority {
    ADMIN, USER, SYSADMIN;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
