package com.jackalhan.ualr.config;

/**
 * Created by jackalhan on 4/18/16.
 */
public final class Constants {
    public static final String APPLICATION_NAME = "ualr-epar";
    public static final String PROFILE_DEVELOPMENT = "dev";
    public static final String PROFILE_PRODUCTION = "prod";
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

    public static final String INSTRUCTION_TYPE_CONTAINS_KEY_WORD_PED = "PED";
    public static final String INSTRUCTION_TYPE_CONTAINS_KEY_WORD_IND = "IND";

    public static final String COURSE_TYPE_CODE_U = "U";
    public static final String UNDERGRADUATE_COURSE = "UNDERGRADUATE COURSE";

    public static final String COURSE_TYPE_CODE_DL = "DL";
    public static final String COURSE_TYPE_CODE_DU = "DU";
    public static final String DUAL_LISTED_COURSE = "DUAL-LISTED COURSE";

    public static final String COURSE_TYPE_CODE_G = "G";
    public static final String GRADUATE_COURSE = "GRADUATE COURSE";

    public static final String COURSE_TYPE_CODE_MS = "MS";
    public static final String MASTER_STUDIES = "MASTER STUDIES";

    public static final String COURSE_TYPE_CODE_PHD = "PhD";
    public static final String DOCTORAL_STUDIES = "DOCTORAL STUDIES";

    public static final String COURSE_TYPE_CODE_O = "O";
    public static final String OTHER_STUDIES = "OTHER STUDIES";

    public static final String TERM_SPRING="SPRING";
    public static final String TERM_FALL="FALL";
    public static final String TERM_SUMMER="SUMMER";
}
