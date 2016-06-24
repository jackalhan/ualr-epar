package com.jackalhan.ualr.service.db;


import com.jackalhan.ualr.domain.model.Department;
import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by txcakaloglu on 6/23/16.
 */
@Service
@Transactional
public class DepartmentDBService {


    private final Logger log = LoggerFactory.getLogger(DepartmentDBService.class);

    @Autowired
    private DepartmentRepository departmentRepository;


    @Transactional
    public Department createDepartmentIfNotFound(Department department, Faculty faculty) {
        Department dep = departmentRepository.findByCodeAndFacultyCode(department.getCode(), faculty);
        if (dep == null) {
            departmentRepository.save(department);
            log.info("createDepartmentIfNotFound " + "saved data successfully");
        }
        return dep;
    }

/*    @Transactional
    public Faculty findByCode(String code) {
        log.info("findByCode " + " listed data successfully");
        return facultyRepository.findByCode(code);
    }
*/
    @Transactional
    public List<Department> listAll() {
        log.info("listAll " + " listed data successfully");
        return departmentRepository.findAll();
    }


}
