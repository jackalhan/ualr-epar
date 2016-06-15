package com.jackalhan.ualr.constant;

/**
 * Created by jackalhan on 4/26/16.
 */
public class SchedulingConstant {

    public static final long WORKLOAD_REPORT_SERVICE_EXECUTE_FIXED_DELAY = 5000; //600000  : 10 minutes
    public static final String FILE_DELETE_SERVICE_EXECUTE_CRON = "0 0 0/12 * * ?"; //Every 12 hours
    public static final String EMPLOYEE_SYNC_SERVICE_EXECUTE_CRON = "0 0 0/1 * * ?"; //Every 1 hours
    public static final long TEST_SERVICE_EXECUTE_FIXED_DELAY = 600000000; //600000  : 10 minutes
}
