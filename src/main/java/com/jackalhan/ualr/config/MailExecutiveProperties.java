package com.jackalhan.ualr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * Created by jackalhan on 5/1/16.
 */
@PropertySource("classpath:mail.properties")
public class MailExecutiveProperties {

    @Value("${workloadreport.mailenvelope.executive.subject}")
    public String subject;
    @Value("${workloadreport.mailenvelope.executive.to}")
    private String coreTo;
    @Value("${application.developer}")
    private String coreDeveloper;
    @Value("${mail.from}")
    public String from;

    private String[] to;

    private String[] developer;

    public String[] getTo() {
        return coreTo.split(";");
    }

    public String[] getDeveloper() {
        return coreDeveloper.split(";");
    }

}
