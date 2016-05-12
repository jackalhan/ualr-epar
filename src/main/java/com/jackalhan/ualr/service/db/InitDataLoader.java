package com.jackalhan.ualr.service.db;

import com.jackalhan.ualr.domain.model.Role;
import com.jackalhan.ualr.enums.RoleEnum;
import com.jackalhan.ualr.repository.RoleRepository;
import com.jackalhan.ualr.service.utils.StringUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jackalhan on 5/11/16.
 */
@Component
public class InitDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (alreadySetup)
            return;

        Role adminRole = new Role();
        adminRole.setName(RoleEnum.ADMIN.getAuthority());
        adminRole.setDescription("Administrative Roles for the faculty");
        createRolesIfNotFound(adminRole);

        Role userRole = new Role();
        userRole.setName(RoleEnum.USER.getAuthority());
        userRole.setDescription("Regular User Roles for the faculty");
        createRolesIfNotFound(userRole);

        Role sysAdminRole = new Role();
        sysAdminRole.setName(RoleEnum.SYSADMIN.getAuthority());
        sysAdminRole.setDescription("Sys Admin Role for the university");
        createRolesIfNotFound(sysAdminRole);

        alreadySetup = true;
    }

    @Transactional
    private Role createRolesIfNotFound(Role role) {
        Role rol = roleRepository.findByName(role.getName());
        if (rol == null) {
            roleRepository.save(role);
        }
        return role;

    }
}
