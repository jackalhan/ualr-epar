package com.jackalhan.ualr.service.db;

import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.repository.FacultyRepository;
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
public class FacultyDBService {

    private final Logger log = LoggerFactory.getLogger(FacultyDBService.class);

    @Autowired
    private FacultyRepository facultyRepository;


    @Transactional
    public Faculty createFacultyIfNotFound(Faculty faculty) {
        Faculty fac = facultyRepository.findByCode(faculty.getCode());
        if (fac == null) {
            facultyRepository.save(faculty);
            log.info("createFacultyIfNotFound " + "saved data successfully");
        }
        return fac;
    }

    @Transactional
    public Faculty listAllTermsBasedOnFacultyAndYear(String facultyCode, int year) {
        Faculty fac = facultyRepository.findByCode(faculty.getCode());
        if (fac == null) {
            facultyRepository.save(faculty);
            log.info("createFacultyIfNotFound " + "saved data successfully");
        }
        return fac;
    }
}
