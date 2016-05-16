package com.jackalhan.ualr.service.db;

import com.jackalhan.ualr.domain.model.Role;
import com.jackalhan.ualr.repository.RoleRepository;
import com.jackalhan.ualr.repository.UserRepository;
import com.jackalhan.ualr.service.utils.StringUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by txcakaloglu on 5/16/16.
 */
@Service
@Transactional
public class UserRoleDBService {
    private final Logger log = LoggerFactory.getLogger(UserRoleDBService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public Role createRolesIfNotFound(Role role) {
        Role rol = roleRepository.findByName(role.getName());
        if (rol == null) {
            roleRepository.save(role);
            log.info("createRolesIfNotFound " + "saved data successfully");
        }
        return role;
    }
}
