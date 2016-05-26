package com.jackalhan.ualr.constant;

/**
 * Created by jackalhan on 4/18/16.
 */
public final class GenericConstant {
    public static final String APPLICATION_NAME = "ualr-epar";
    public static final String PROFILE_DEVELOPMENT = "dev";
    public static final String PROFILE_PRODUCTION = "prod";
    public static final String PROFILE_TEST = "test";
    private static final String BASE_PATH = "files";
    private static final String WORKLOAD_REPORTS_PATH = BASE_PATH + "/workloadReports";
    private static final String WORKLOAD_REPORTS_RAWTXT_PATH = WORKLOAD_REPORTS_PATH + "/rawtxtfiles";
    private static final String WORKLOAD_REPORTS_EXCEL_PATH = WORKLOAD_REPORTS_PATH + "/generatedexcels";
    private static final String TEMP_PATH = "/temp";
    private static final String PROCESSED_PATH =  "/processed";
    private static final String ERROR_PATH =  "/error";
    public static final String WORKLOAD_REPORTS_RAWTXT_TEMP_PATH = WORKLOAD_REPORTS_RAWTXT_PATH + TEMP_PATH + "/";
    public static final String WORKLOAD_REPORTS_RAWTXT_PROCESSED_PATH = WORKLOAD_REPORTS_RAWTXT_PATH + PROCESSED_PATH + "/";
    public static final String WORKLOAD_REPORTS_RAWTXT_ERROR_PATH = WORKLOAD_REPORTS_RAWTXT_PATH + ERROR_PATH + "/";
    public static final String WORKLOAD_REPORTS_EXCEL_PROCESSED_PATH = WORKLOAD_REPORTS_EXCEL_PATH + PROCESSED_PATH + "/";

    private static final String RESOURCE_PATH = "src/main/resources/";
    private static final String RESOURCE_IMAGE_PATH= RESOURCE_PATH + "static/img/";
    public static final String RESOURCE_UALR_IMAGE_PATH = RESOURCE_IMAGE_PATH + "ualr/";
    public static final String RESOURCE_MAIL_IMAGE_PATH = RESOURCE_IMAGE_PATH + "mails/";

    public static final String COMA_CHARACTER = ",";
    public static final String DOT_CHARACTER = ".";

    public static final String SIMPLE_DATE_TIME_FORMAT_WITH_MILISECOND = "EEE MMM dd HH:mm:ss zzz yyyy";

}
