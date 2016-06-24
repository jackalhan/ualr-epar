package com.jackalhan.ualr.service.batch;

import com.jackalhan.ualr.config.FTPConfiguration;
import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.domain.SimplifiedWorkload;
import com.jackalhan.ualr.domain.TypeSafeRawWorkload;
import com.jackalhan.ualr.service.LogService;
import com.jackalhan.ualr.service.db.DepartmentDBService;
import com.jackalhan.ualr.service.db.EmployeeDBService;
import com.jackalhan.ualr.service.db.FacultyDBService;
import com.jackalhan.ualr.service.rest.FTPService;
import com.jackalhan.ualr.service.rest.MailService;
import com.jackalhan.ualr.service.utils.FileUtilService;
import com.jackalhan.ualr.service.utils.StringUtilService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

/**
 * Created by txcakaloglu on 6/15/16.
 */
public abstract class BatchService extends LogService {


    public ValidatorFactory validatorFactory;
    public Validator validator;

    @Autowired
    public FTPService ftpService;

    @Autowired
    public FTPConfiguration ftpConfiguration;

    @Autowired
    public FacultyDBService facultyDBService;

    @Autowired
    public DepartmentDBService departmentDBService;

    @Autowired
    public EmployeeDBService employeeDBService;

    @Autowired
    public MailService mailService;

    private String mailSubject;

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    @PostConstruct
    public abstract void initialize();
    /*public BatchService() {
        initialize();
    }*/



}
