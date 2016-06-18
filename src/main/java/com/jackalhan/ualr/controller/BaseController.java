package com.jackalhan.ualr.controller;

import com.jackalhan.ualr.service.LogService;
import com.jackalhan.ualr.service.db.WorkloadReportDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
 * Created by jackalhan on 6/17/16.
 */
public abstract class BaseController extends LogService {
    @Autowired
    public MessageSource messageSource;
}
