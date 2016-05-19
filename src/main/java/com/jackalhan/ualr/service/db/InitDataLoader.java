package com.jackalhan.ualr.service.db;

import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.domain.model.Role;
import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import com.jackalhan.ualr.enums.RoleEnum;
import com.jackalhan.ualr.repository.FacultyRepository;
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
    private UserRoleDBService userRoleDBService;

    @Autowired
    private FacultyDBService facultyDBService;

    @Autowired
    private WorkloadReportDBService workloadReportDBService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (alreadySetup)
            return;

        Role adminRole = new Role();
        adminRole.setName(RoleEnum.ADMIN.getAuthority());
        adminRole.setDescription("Administrative Roles for the faculty");
        userRoleDBService.createRolesIfNotFound(adminRole);

        Role userRole = new Role();
        userRole.setName(RoleEnum.USER.getAuthority());
        userRole.setDescription("Regular User Roles for the faculty");
        userRoleDBService.createRolesIfNotFound(userRole);

        Role sysAdminRole = new Role();
        sysAdminRole.setName(RoleEnum.SYSADMIN.getAuthority());
        sysAdminRole.setDescription("Sys Admin Role for the university");
        userRoleDBService.createRolesIfNotFound(sysAdminRole);

        Faculty faculty = new Faculty();
        faculty.setCode("SS");
        faculty.setDeanNameSurname("Lawrence E. Whitman");
        faculty.setName("Engineering and Information Technology Faculty");
        facultyDBService.createFacultyIfNotFound(faculty);

        WorkloadReportTerm wlReportTerm = null; //new WorkloadReportTerm();
       /* wlReportTerm.setFaculty(faculty);
        wlReportTerm.setSemesterTermCode(10);
        wlReportTerm.setSemesterTerm("SPRING");
        wlReportTerm.setSemesterYear(2016);
        workloadReportDBService.createWorkloadReportTermIfNotFound(wlReportTerm);
*/
        wlReportTerm = new WorkloadReportTerm();
        wlReportTerm.setFaculty(faculty);
        wlReportTerm.setSemesterTermCode(30);
        wlReportTerm.setSemesterTerm("SUMMER");
        wlReportTerm.setSemesterYear(2016);

        workloadReportDBService.createWorkloadReportTermIfNotFound(wlReportTerm);

        alreadySetup = true;
    }



}
