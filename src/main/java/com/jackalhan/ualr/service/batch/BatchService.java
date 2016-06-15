package com.jackalhan.ualr.service.batch;

import com.jackalhan.ualr.service.rest.FTPService;
import com.jackalhan.ualr.service.rest.MailService;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.validation.Validation;

/**
 * Created by txcakaloglu on 6/15/16.
 */
public abstract class BatchService {

    @Autowired
    public FTPService ftpService;

    @Autowired
    public MailService mailService;

    public Logger log;

    private String mailSubject;


    public void setLog(Logger log) {
        this.log = log;
    }

    public Logger getLog() {
        return log;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    @PostConstruct
    public abstract void initialize() ;
    /*public BatchService() {
        initialize();
    }*/
}
