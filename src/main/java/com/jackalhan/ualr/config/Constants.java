package com.jackalhan.ualr.config;

/**
 * Created by jackalhan on 4/18/16.
 */
public final class Constants {
    public static final String PROFILE_DEVELOPMENT = "dev";
    public static final String PROFILE_PRODUCTION = "prod";
    private static final String basePath = "files";
    private static final String workloadReportsPath = basePath + "/workloadReports";
    private static final String tempPath = "/temp";
    private static final String processedPath=  "/processed";
    public static final String workloadReportsTempPath = workloadReportsPath + tempPath + "/";
    public static final String workloadReportsProcessedPath = workloadReportsPath + processedPath + "/";
}
