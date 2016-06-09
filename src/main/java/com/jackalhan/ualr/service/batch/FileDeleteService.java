package com.jackalhan.ualr.service.batch;

import com.jackalhan.ualr.service.rest.FTPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by txcakaloglu on 6/9/16.
 */
@Component
@Service
public class FileDeleteService {

    @Autowired
    private FTPService ftpService;

    @Autowired
    private WorkloadReportService workloadReportService;

    private final Logger log = LoggerFactory.getLogger(FileDeleteService.class);



}
