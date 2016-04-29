package com.jackalhan.ualr.service.batch;

import com.jackalhan.ualr.config.*;
import com.jackalhan.ualr.domain.*;
import com.jackalhan.ualr.domain.validate.RawWorkloadValidator;
import com.jackalhan.ualr.service.utils.FileUtilService;
import com.jackalhan.ualr.service.utils.StringUtilService;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jackalhan on 4/18/16.
 */

@Component
public class WorkloadReportService {

    @Autowired
    private MessageSource messageSource;

    private final Logger log = LoggerFactory.getLogger(WorkloadReportService.class);

    private static final int excelZoomFactor = 85;
    private static final int excelScaleFactor = 50;
    private static final double excelLeftMargin = 0.75;
    private static final double excelRightMargin = 0.75;
    private static final double excelTopMargin = 0.30;
    private static final double excelBottomMargin = 0.33;
    private static final String fileNamePattern = "2016_S";

    @Scheduled(fixedDelay = SchedulingConstants.WORKLOAD_REPORT_SERVICE_EXECUTE_FIXED_DELAY)
    private void executeService() throws IOException, WriteException, CloneNotSupportedException {
        log.info("TypeSafeTypeSafeRawWorkload Reports execution started");
        List<RawWorkload> rawWorkloadList = prepareTestData(fileNamePattern);
        log.info(String.valueOf(rawWorkloadList.size()));
        /*List<SimplifiedWorkload> simplifiedWorkloadList = simplifyWorkloadData(TypeSafeTypeSafeRawWorkloadList);
        generateExcelContent(simplifiedWorkloadList);
        log.info(simplifyWorkloadData(TypeSafeTypeSafeRawWorkloadList).toString());*/
        log.info("TypeSafeRawWorkload Reports execution ended");
    }


    private int convertToDualCourse(int courseCodeNumber, int dualStartWith) {
        String newCourseNumberForDualCheck = "";
        try {

            newCourseNumberForDualCheck = dualStartWith + String.valueOf(courseCodeNumber).substring(1, 4);
            log.info("Converted Dual Corse from : " + courseCodeNumber + " to " + newCourseNumberForDualCheck);

        } catch (Exception ex) {
            log.debug(ex.toString());
        }
        return Integer.parseInt(newCourseNumberForDualCheck);
    }


    private CourseDetail calculateCourseDualState(List<TypeSafeRawWorkload> TypeSafeRawWorkloadList, int courseCodeNumber, int dualStartWith) {
        CourseDetail courseDualState = new CourseDetail();
        courseDualState.setHasDualCourse(false);
        courseDualState.setNumberOfTotalEnrollmentInDualCourse(0);
        int newCourseNumberForDualCheck = convertToDualCourse(courseCodeNumber, dualStartWith);
        for (TypeSafeRawWorkload TypeSafeRawWorkload : TypeSafeRawWorkloadList) {
            if (TypeSafeRawWorkload.getCourseNumber() == newCourseNumberForDualCheck) {
                //Do we need to add other data in order to calculate ?
                courseDualState.setHasDualCourse(true);
                courseDualState.setDualCourseCode(newCourseNumberForDualCheck);
                courseDualState.setNumberOfTotalEnrollmentInDualCourse(TypeSafeRawWorkload.getTaEleventhDayCount());
                return courseDualState;
            }
        }
        return courseDualState;

    }


    private CourseDetail calculateIUMultiplier(boolean isLectureHour, CourseDetail courseDetail) {
        double result = 0;
        double multiplier = 0;
        CourseDetail courseTypeIUValues = null;
        if (isLectureHour) {
            if (courseDetail.getCourseTypeCode().equals(Constants.COURSE_TYPE_CODE_U)) {
                multiplier = 1;
                result = courseDetail.getIuMultiplierLectureHours() * multiplier;
            } else if (courseDetail.getCourseTypeCode().equals(Constants.COURSE_TYPE_CODE_DL)) {
                multiplier = 1;
                result = courseDetail.getIuMultiplierLectureHours() * multiplier;
            } else if (courseDetail.getCourseTypeCode().equals(Constants.COURSE_TYPE_CODE_DU)) {
                multiplier = 1.33;
                result = courseDetail.getIuMultiplierLectureHours() * multiplier;
            } else if (courseDetail.getCourseTypeCode().equals(Constants.COURSE_TYPE_CODE_G)) {
                multiplier = 1.33;
                result = courseDetail.getIuMultiplierLectureHours() * multiplier;
            } else if (courseDetail.getCourseTypeCode().equals(Constants.COURSE_TYPE_CODE_MS)) {
                multiplier = 0.75;
                result = multiplier;
            } else if (courseDetail.getCourseTypeCode().equals(Constants.COURSE_TYPE_CODE_PHD)) {
                multiplier = 1;
                result = multiplier;
            } else if (courseDetail.getCourseTypeCode().equals(Constants.COURSE_TYPE_CODE_O)) {
                multiplier = 0.375;
                result = multiplier;
            }
        }
        courseTypeIUValues = new CourseDetail();
        courseTypeIUValues.setIuMultiplierLectureHours(multiplier);
        courseTypeIUValues.setIuMultiplicationResultOfLectureHours(result);
        return courseTypeIUValues;


    }

    private CourseDetail getCourseType(CourseDetail courseDetail) {

        CourseDetail courseType = new CourseDetail();
        if (courseDetail.getInstructionType().toUpperCase().trim().contains(Constants.INSTRUCTION_TYPE_CONTAINS_KEY_WORD_PED)) {
            if (!courseDetail.isHasDualCourse()) {
                if (courseDetail.getCodeNumber() >= 1000 && courseDetail.getCodeNumber() < 5000) {
                    courseType.setCourseTypeCode(Constants.COURSE_TYPE_CODE_U);
                    courseType.setCourseTypeName(Constants.UNDERGRADUATE_COURSE);
                } else if (courseDetail.getCodeNumber() >= 7000 && courseDetail.getCodeNumber() < 8000) {
                    courseType.setCourseTypeCode(Constants.COURSE_TYPE_CODE_G);
                    courseType.setCourseTypeName(Constants.GRADUATE_COURSE);
                }
            } else {
                if (courseDetail.getCodeNumber() >= 4000 && courseDetail.getCodeNumber() < 6000) {
                    if (courseDetail.getNumberOfTotalEnrollmentInDualCourse() >= 1 && courseDetail.getNumberOfTotalEnrollmentInDualCourse() < 5) {
                        courseType.setCourseTypeCode(Constants.COURSE_TYPE_CODE_DL);
                        courseType.setCourseTypeName(Constants.DUAL_LISTED_COURSE);
                    } else if (courseDetail.getNumberOfTotalEnrollmentInDualCourse() >= 5) {
                        courseType.setCourseTypeCode(Constants.COURSE_TYPE_CODE_DU);
                        courseType.setCourseTypeName(Constants.DUAL_LISTED_COURSE);
                    } else {
                        courseType.setCourseTypeCode(Constants.COURSE_TYPE_CODE_U);
                        courseType.setCourseTypeName(Constants.UNDERGRADUATE_COURSE);
                    }
                }
            }
        } else {
            if (courseDetail.getCodeNumber() >= 9000 && courseDetail.getCodeNumber() < 10000) {
                courseType.setCourseTypeCode(Constants.COURSE_TYPE_CODE_PHD);
                courseType.setCourseTypeName(Constants.DOCTORAL_STUDIES);
            } else if (courseDetail.getCodeNumber() >= 7000 && courseDetail.getCodeNumber() < 8000) {
                courseType.setCourseTypeCode(Constants.COURSE_TYPE_CODE_MS);
                courseType.setCourseTypeName(Constants.MASTER_STUDIES);
            } else {
                courseType.setCourseTypeCode(Constants.COURSE_TYPE_CODE_O);
                courseType.setCourseTypeName(Constants.OTHER_STUDIES);
            }
        }

        return courseType;
    }

    private List<SimplifiedWorkload> simplifyWorkloadData(List<TypeSafeRawWorkload> TypeSafeRawWorkloads) throws CloneNotSupportedException {
        log.info("Simplifying WorkLoad Data");
        //Old Implementation with just prof. name
        /* Map<String, List<TypeSafeRawWorkload>> distinctValuesInList = TypeSafeRawWorkloads.stream()
                .collect(Collectors.groupingBy(TypeSafeRawWorkload::getInstructorNameSurname));*/

        // Since list contains adjusct prof. who has courses in more than one department.
        // had to group name and department.
        Map<Pair<String, String>, List<TypeSafeRawWorkload>> distinctValuesInList = TypeSafeRawWorkloads.stream()
                .collect(Collectors.groupingBy(p -> Pair.of(p.getInstructorNameSurname(), p.getInstructorDepartment())));


        List<SimplifiedWorkload> simplifiedWorkloadList = new ArrayList<SimplifiedWorkload>();
        SimplifiedWorkload simplifiedWorkload = null;
        List<TypeSafeRawWorkload> newRawDataList = null;
        for (Map.Entry<Pair<String, String>, List<TypeSafeRawWorkload>> entry : distinctValuesInList.entrySet()) {
            simplifiedWorkload = new SimplifiedWorkload();
            TypeSafeRawWorkload newRawData = null;
            CourseDetail courseDetail = null;
            newRawDataList = new ArrayList<TypeSafeRawWorkload>();

            boolean isInstructorNameParsed = false;
            boolean isDeanNameParsed = false;
            boolean isChairNameParsed = false;
            boolean isDateParsed = false;
            boolean isDepartmentNameGathered = false;

            for (TypeSafeRawWorkload rawData : entry.getValue()) {

                newRawData = (TypeSafeRawWorkload) rawData.clone();

                if (String.valueOf(rawData.getCourseNumber()).startsWith("4")) {
                    courseDetail = calculateCourseDualState(entry.getValue(), newRawData.getCourseNumber(), 5);

                } else if (String.valueOf(rawData.getCourseNumber()).startsWith("5")) {
                    courseDetail = calculateCourseDualState(entry.getValue(), newRawData.getCourseNumber(), 4);
                    if (courseDetail.isHasDualCourse()) {
                        continue;
                    }
                }


                courseDetail = getCourseType(courseDetail);
                newRawData.setCourseTypeCode(courseDetail.getCourseTypeCode());
                newRawData.setCourseTypeName(courseDetail.getCourseTypeName());

                courseDetail = calculateIUMultiplier(true, courseDetail);

                newRawData.setIuMultipliertaLectureHours(courseDetail.getIuMultiplierLectureHours());
                newRawData.setTotalIus(courseDetail.getTotalSsch());


                if (!isInstructorNameParsed) {
                    isInstructorNameParsed = true;
                    simplifiedWorkload.setInstructorNameAndSurname(StringUtilService.getInstance().switchText(newRawData.getInstructorNameSurname(), Constants.COMA_CHARACTER));
                }
                if (!isDeanNameParsed) {
                    isDeanNameParsed = true;
                    simplifiedWorkload.setDeanNameAndSurname(StringUtilService.getInstance().switchText(newRawData.getDean(), Constants.COMA_CHARACTER));
                }
                if (!isChairNameParsed) {
                    isChairNameParsed = true;
                    simplifiedWorkload.setChairNameAndSurname(StringUtilService.getInstance().switchText(newRawData.getChair(), Constants.COMA_CHARACTER));
                }
                if (!isDateParsed) {
                    if (!StringUtilService.getInstance().isEmpty(String.valueOf(newRawData.getSemesterTermCode()))) {
                        isDateParsed = true;
                        simplifiedWorkload.setSemesterTerm(decidePeriod(Integer.parseInt(String.valueOf(newRawData.getSemesterTermCode()).substring(4, 5))));
                        simplifiedWorkload.setSemesterYear(Integer.valueOf(String.valueOf(newRawData.getSemesterTermCode()).substring(0, 4)));
                    }
                }
                if (!isDepartmentNameGathered) {
                    isDepartmentNameGathered = true;
                    simplifiedWorkload.setDepartmentName(newRawData.getInstructorDepartment());
                    simplifiedWorkload.setChairNameAndSurname(StringUtilService.getInstance().switchText(newRawData.getChair(), Constants.COMA_CHARACTER));
                }


                simplifiedWorkload.setTotalSsch(simplifiedWorkload.getTotalSsch() + newRawData.getTotalSsch());
                simplifiedWorkload.setTotal11thDayCount(simplifiedWorkload.getTotal11thDayCount() + newRawData.getTaEleventhDayCount());
                simplifiedWorkload.setTotalCreditHours(simplifiedWorkload.getTotalCreditHours() + newRawData.getTaCeditHours());
                simplifiedWorkload.setTotalIUMultiplierForLectureHours(simplifiedWorkload.getTotalIUMultiplierForLectureHours() + newRawData.getIuMultipliertaLectureHours());
                simplifiedWorkload.setTotalLabHours(simplifiedWorkload.getTotalLabHours() + newRawData.getTaLabHours());
                if (newRawData.getInstructionType().toUpperCase().trim().contains(Constants.INSTRUCTION_TYPE_CONTAINS_KEY_WORD_PED)) {
                    simplifiedWorkload.setTotalLectureHours(simplifiedWorkload.getTotalLectureHours() + newRawData.getTaLectureHours());
                }
                simplifiedWorkload.setTotalTaSupport(simplifiedWorkload.getTotalTaSupport() + newRawData.getTaSupport());
                simplifiedWorkload.setTotalTotalIUs(simplifiedWorkload.getTotalTotalIUs() + newRawData.getTotalIus());
                newRawDataList.add(newRawData);
            }
            simplifiedWorkload.setTypeSafeRawWorkloads(newRawDataList);
            simplifiedWorkloadList.add(simplifiedWorkload);
        }

        log.info("Simplifying WorkLoad Data Completed");
        return simplifiedWorkloadList;

    }

    private String decidePeriod(int codeNumber) {

        if (codeNumber == 10)
            return Constants.TERM_SPRING;
        else if (codeNumber == 30)
            return Constants.TERM_SUMMER;
        else
            return Constants.TERM_FALL;

    }

    private void generateExcelContent(List<SimplifiedWorkload> simplifiedWorkloadList) throws IOException, WriteException {

        FileUtilService.getInstance().createDirectory(Constants.WORKLOAD_REPORTS_TEMP_PATH);

        for (SimplifiedWorkload simplifiedWorkload : simplifiedWorkloadList) {

            File file = new File(Constants.WORKLOAD_REPORTS_TEMP_PATH + simplifiedWorkload.getSemesterYear() + "_" + simplifiedWorkload.getSemesterTerm() + "_WLReport_of_" + simplifiedWorkload.getInstructorNameAndSurname().replace(" ", "_") + ".xls");

            int startingColumnFrame = 1;
            int endingColumnFrame = 25;
            //Creates a writable workbook with the given file name
            //WritableWorkbook workbook = Workbook.createWorkbook(new File(simplifiedWorkload.getSemesterYear() + "_" + simplifiedWorkload.getSemesterTerm() + "_WLReport_of_" + simplifiedWorkload.getInstructorNameAndSurname().replace(" ", "_") + ".xls"));
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet(simplifiedWorkload.getInstructorNameAndSurname().replace(" ", "_") + "_" + simplifiedWorkload.getSemesterYear() + "_" + simplifiedWorkload.getSemesterTerm(), 0);

            // Create cell font and format
            // REPORT HEADER
            WritableFont cellFont = createCellFont("workloadReport.faculty.name.fontsize", Colour.BLACK, true);
            WritableCellFormat cellFormat = createCellFormat(cellFont, Colour.GRAY_25, BorderLineStyle.THICK, false, true, true, true, true);
            sheet.mergeCells(startingColumnFrame, 1, endingColumnFrame, 3);
            createText(sheet, "workloadReport.faculty.name", null, cellFormat, startingColumnFrame, 1);

            // *****************************************************************************************************
            //REPORT NAME

            cellFont = createCellFont("workloadReport.report.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, false, true, true, false);
            sheet.mergeCells(startingColumnFrame, 4, endingColumnFrame, 5);
            createText(sheet, "workloadReport.report.name", null, cellFormat, startingColumnFrame, 4);

            // *****************************************************************************************************
            //DEPARTMENT HEADER

            cellFont = createCellFont("workloadReport.department.name.fontsize", Colour.BLACK, false);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, false, true, true, true);
            sheet.mergeCells(startingColumnFrame, 6, endingColumnFrame, 7);
            createText(sheet, "workloadReport.department.name", new Object[]{simplifiedWorkload.getDepartmentName(), simplifiedWorkload.getSemesterTerm(), String.valueOf(simplifiedWorkload.getSemesterYear())}, cellFormat, startingColumnFrame, 6);

            // *****************************************************************************************************
            //INSTRUCTOR NAME
            cellFont = createCellFont("workloadReport.instructor.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.GRAY_25, BorderLineStyle.THICK, false, false, true, true, true);
            createText(sheet, "workloadReport.instructor.name", new Object[]{simplifiedWorkload.getInstructorNameAndSurname()}, cellFormat, startingColumnFrame, 8);
            sheet.mergeCells(startingColumnFrame, 8, endingColumnFrame, 9);

            // *****************************************************************************************************
            //COLUMNS
            int startingColHeadercolumnNumber = 0;
            int endingColHeadercolumnNumber = startingColHeadercolumnNumber;
            int startingColHeaderRowNumber = 10;
            int endingColHeaderRowNumber = 18;
            String columnTitle = "";
            for (int i = 1; i < 18; i++) {
                startingColHeadercolumnNumber = endingColHeadercolumnNumber + 1;
                if (i >= 6 && i <= 7) {
                    endingColHeadercolumnNumber = startingColHeadercolumnNumber + 4;
                } else {
                    endingColHeadercolumnNumber = startingColHeadercolumnNumber;
                }

                columnTitle = "workloadReport.column." + i + ".name";

                cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
                cellFormat = createCellFormat(cellFont, Colour.GRAY_25, BorderLineStyle.THICK, true, true, true, true, true);
                createText(sheet, columnTitle, null, cellFormat, startingColHeadercolumnNumber, startingColHeaderRowNumber);
                sheet.mergeCells(startingColHeadercolumnNumber, startingColHeaderRowNumber, endingColHeadercolumnNumber, endingColHeaderRowNumber);
            }

            // *****************************************************************************************************
            //PEDAGOGICAL INSTR
            int startingPedaRowNumber = endingColHeaderRowNumber + 1;
            int endingPedaRowNumber = startingPedaRowNumber + 2;

            cellFont = createCellFont("workloadReport.pedagogical.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, false, true, true, true);
            createText(sheet, "workloadReport.pedagogical.name", null, cellFormat, startingColumnFrame, startingPedaRowNumber);
            sheet.mergeCells(startingColumnFrame, startingPedaRowNumber, endingColumnFrame, endingPedaRowNumber);

            // *****************************************************************************************************
            //DATA UNDER PEDAGOGICAL
            int startingDataPedaRowNumber = endingPedaRowNumber + 1;
            int endingDataPedaRowNumber = startingDataPedaRowNumber;

            int startingDataPedaColumnNumber = 1;
            int endingDataPedaColumnNumber = startingDataPedaColumnNumber;


            int rawPedaCounter = 0;
            // FOR LOOP WILL BE BUILDING FOR FILTERED PEDA DATA
            for (TypeSafeRawWorkload pedWorkloadItems : simplifiedWorkload.getTypeSafeRawWorkloads()) {
                if (pedWorkloadItems.getInstructionType().toUpperCase().trim().contains(Constants.INSTRUCTION_TYPE_CONTAINS_KEY_WORD_PED)) {
                    rawPedaCounter++;
                    startingDataPedaColumnNumber = 1;
                    boolean topBorder = true;
                    if (rawPedaCounter == 1) {
                        topBorder = false;
                    }

                    cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, false);
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, topBorder, true, false, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getCrn()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getSubjectCode()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getCourseNumber()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getSection()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, pedWorkloadItems.getCourseTypeCode(), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    endingDataPedaColumnNumber = startingDataPedaColumnNumber + 4;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, true, topBorder, true, true, false);
                    createText(sheet, pedWorkloadItems.getCourseTitle(), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    sheet.mergeCells(startingDataPedaColumnNumber, startingDataPedaRowNumber, endingDataPedaColumnNumber, endingDataPedaRowNumber);
                    startingDataPedaColumnNumber = endingDataPedaColumnNumber + 1;
                    endingDataPedaColumnNumber = startingDataPedaColumnNumber + 4;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, pedWorkloadItems.getTaStudent(), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    sheet.mergeCells(startingDataPedaColumnNumber, startingDataPedaRowNumber, endingDataPedaColumnNumber, endingDataPedaRowNumber);
                    startingDataPedaColumnNumber = endingDataPedaColumnNumber + 1;
                    cellFormat = createCellFormat(cellFont, Colour.RED, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, "", cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getTaEleventhDayCount()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getTaCeditHours()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getTaLectureHours()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getIuMultipliertaLectureHours()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getTaLabHours()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.RED, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, "", cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.RED, true);
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getTotalIus()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, false);
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, VerticalAlignment.TOP, Alignment.LEFT, BorderLineStyle.THIN, true, topBorder, true, true, false);
                    createText(sheet, pedWorkloadItems.getOtherInstructorsInTeam(), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);
                    startingDataPedaColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, topBorder, false, true, false);
                    createText(sheet, String.valueOf(pedWorkloadItems.getTotalSsch()), cellFormat, startingDataPedaColumnNumber, startingDataPedaRowNumber);

                    startingDataPedaRowNumber++;
                    endingDataPedaRowNumber = startingDataPedaRowNumber;

                }

            }


            // *****************************************************************************************************
            //INDIVIDUAL INSTR
            int startingIndivRowNumber = endingDataPedaRowNumber;
            int endingIndivRowNumber = startingIndivRowNumber + 2;

            cellFont = createCellFont("workloadReport.individualized.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.individualized.name", null, cellFormat, startingColumnFrame, startingIndivRowNumber);
            sheet.mergeCells(startingColumnFrame, startingIndivRowNumber, endingColumnFrame, endingIndivRowNumber);

            // *****************************************************************************************************
            //DATA UNDER INDIVIDUAL
            int startingDataIndivRowNumber = endingIndivRowNumber + 1;
            int endingDataIndivRowNumber = startingDataIndivRowNumber;

            int startingDataIndivColumnNumber = 1;
            int endingDataIndivColumnNumber = startingDataIndivColumnNumber;

            // FOR LOOP WILL BE BUILDING FOR FILTERED INDIV DATA
            int firstNotApplicableFromColumnNumber = 0,
                    firstNotApplicableToColumnNumber = 0,
                    secondNotApplicableFromColumnNumber = 0,
                    secondNotApplicableToColumnNumber = 0;
            int rawIndivCounter = 0;
            for (TypeSafeRawWorkload indWorkloadItems : simplifiedWorkload.getTypeSafeRawWorkloads()) {
                if (indWorkloadItems.getInstructionType().toUpperCase().trim().contains(Constants.INSTRUCTION_TYPE_CONTAINS_KEY_WORD_IND)) {
                    rawIndivCounter++;
                    startingDataIndivColumnNumber = 1;
                    boolean topBorder = true;
                    if (rawIndivCounter == 1) {
                        topBorder = false;
                    }

                    cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, false);
                    //Exception For Initial Columns in order to draw differntTypeOf
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, topBorder, true, false, false);
                    createText(sheet, String.valueOf(indWorkloadItems.getCrn()), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    startingDataIndivColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(indWorkloadItems.getSubjectCode()), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    startingDataIndivColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(indWorkloadItems.getCourseNumber()), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    startingDataIndivColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(indWorkloadItems.getSection()), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    startingDataIndivColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, indWorkloadItems.getCourseTypeCode(), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    startingDataIndivColumnNumber++;
                    endingDataIndivColumnNumber = startingDataIndivColumnNumber + 4;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, true, topBorder, true, true, false);
                    createText(sheet, indWorkloadItems.getCourseTitle(), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    sheet.mergeCells(startingDataIndivColumnNumber, startingDataIndivRowNumber, endingDataIndivColumnNumber, endingDataIndivRowNumber);
                    startingDataIndivColumnNumber = endingDataIndivColumnNumber + 1;
                    endingDataIndivColumnNumber = startingDataIndivColumnNumber + 4;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, indWorkloadItems.getTaStudent(), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    sheet.mergeCells(startingDataIndivColumnNumber, startingDataIndivRowNumber, endingDataIndivColumnNumber, endingDataIndivRowNumber);
                    startingDataIndivColumnNumber = endingDataIndivColumnNumber + 1;

                    // NOT APPLICABLE 1
                    firstNotApplicableFromColumnNumber = startingDataIndivColumnNumber;
                    firstNotApplicableToColumnNumber = firstNotApplicableFromColumnNumber;

                    startingDataIndivColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(indWorkloadItems.getTaEleventhDayCount()), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    startingDataIndivColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(indWorkloadItems.getTaCeditHours()), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    startingDataIndivColumnNumber++;

                    // NOT APPLICABLE 2
                    secondNotApplicableFromColumnNumber = startingDataIndivColumnNumber;
                    startingDataIndivColumnNumber = startingDataIndivColumnNumber + 3;
                    secondNotApplicableToColumnNumber = startingDataIndivColumnNumber;


                    startingDataIndivColumnNumber++;
                    cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.RED, true);
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, false, topBorder, true, true, false);
                    createText(sheet, String.valueOf(indWorkloadItems.getTotalIus()), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    startingDataIndivColumnNumber++;
                    cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, false);
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, VerticalAlignment.TOP, Alignment.LEFT, BorderLineStyle.THIN, true, topBorder, true, true, false);
                    createText(sheet, indWorkloadItems.getOtherInstructorsInTeam(), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);
                    startingDataIndivColumnNumber++;
                    cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, topBorder, false, true, false);
                    createText(sheet, String.valueOf(indWorkloadItems.getTotalSsch()), cellFormat, startingDataIndivColumnNumber, startingDataIndivRowNumber);

                    startingDataIndivRowNumber++;
                    endingDataIndivRowNumber = startingDataIndivRowNumber;

                }

            }
            // NOT APPLICABLE 1 PRINTING
            cellFont = createCellFont("workloadReport.notapplicable.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, true, false, false, false, false);
            createText(sheet, "workloadReport.notapplicable.name", null, cellFormat, firstNotApplicableFromColumnNumber, endingIndivRowNumber + 1);
            // mergeCells(colStart, rowStart, colEnd, rowEnd)
            sheet.mergeCells(firstNotApplicableFromColumnNumber, endingIndivRowNumber + 1, firstNotApplicableToColumnNumber, endingDataIndivRowNumber - 1);

            // NOT APPLICABLE 2 PRINTING
            cellFont = createCellFont("workloadReport.notapplicable.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THIN, true, false, false, false, false);
            createText(sheet, "workloadReport.notapplicable.name", null, cellFormat, secondNotApplicableFromColumnNumber, endingIndivRowNumber + 1);
            // mergeCells(colStart, rowStart, colEnd, rowEnd)
            sheet.mergeCells(secondNotApplicableFromColumnNumber, endingIndivRowNumber + 1, secondNotApplicableToColumnNumber, endingDataIndivRowNumber - 1);


            // *****************************************************************************************************
            //ADMINISTRATIVE REASSIGN
            int startingAdminRowNumber = endingDataIndivRowNumber;
            int endingAdminRowNumber = startingAdminRowNumber + 2;

            cellFont = createCellFont("workloadReport.administrative.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.administrative.name", null, cellFormat, startingColumnFrame, startingAdminRowNumber);
            sheet.mergeCells(startingColumnFrame, startingAdminRowNumber, endingColumnFrame, endingAdminRowNumber);


            // *****************************************************************************************************
            //DATA UNDER ADMINISTRATIVE
            int startingDataAdminRowNumber = endingAdminRowNumber + 1;
            int endingDataAdminRowNumber = startingDataAdminRowNumber + 9;

            int startingDataAdminColumnNumber = startingColumnFrame;
            int endingDataAdminColumnNumber = startingDataAdminColumnNumber;

            // FOR LOOP WILL BE BUILDING FOR FILTERED ADMIN DATA

            endingDataAdminColumnNumber = startingDataAdminColumnNumber + 15;
            cellFont = createCellFont("workloadReport.notapplicable.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
            createText(sheet, "workloadReport.notapplicable.name", null, cellFormat, startingDataAdminColumnNumber, startingDataAdminRowNumber);
            // mergeCells(colStart, rowStart, colEnd, rowEnd)
            sheet.mergeCells(startingColumnFrame, startingDataAdminRowNumber, endingDataAdminColumnNumber, endingDataAdminRowNumber);

            startingDataAdminColumnNumber = endingDataAdminColumnNumber + 1;
            endingDataAdminColumnNumber = startingDataAdminColumnNumber + 2;
            endingDataAdminRowNumber = startingDataAdminRowNumber + 1;
            for (int i = 1; i < 6; i++) {
                cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
                cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
                createText(sheet, "workloadReport.administrative" + i + "Col1.name", null, cellFormat, startingDataAdminColumnNumber, startingDataAdminRowNumber);
                // mergeCells(colStart, rowStart, colEnd, rowEnd)1
                sheet.mergeCells(startingDataAdminColumnNumber, startingDataAdminRowNumber, endingDataAdminColumnNumber, endingDataAdminRowNumber);
                startingDataAdminRowNumber = endingDataAdminRowNumber + 1;
                endingDataAdminRowNumber = startingDataAdminRowNumber + 1;
            }
            startingDataAdminRowNumber = endingAdminRowNumber + 1;
            endingDataAdminRowNumber = startingDataAdminRowNumber + 1;

            startingDataAdminColumnNumber = endingDataAdminColumnNumber + 1;
            endingDataAdminColumnNumber = startingDataAdminColumnNumber + 2;

            cellFont = createCellFont("workloadReport.administrativeCol2.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
            createText(sheet, "workloadReport.administrative1Col2.name", null, cellFormat, startingDataAdminColumnNumber, startingDataAdminRowNumber);
            // mergeCells(colStart, rowStart, colEnd, rowEnd)1
            sheet.mergeCells(startingDataAdminColumnNumber, startingDataAdminRowNumber, endingDataAdminColumnNumber, endingDataAdminRowNumber);

            startingDataAdminRowNumber = endingDataAdminRowNumber + 1;
            endingDataAdminRowNumber = startingDataAdminRowNumber + 1;

            cellFont = createCellFont("workloadReport.notapplicable.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
            createText(sheet, "workloadReport.notapplicable.name", null, cellFormat, startingDataAdminColumnNumber, startingDataAdminRowNumber);
            // mergeCells(colStart, rowStart, colEnd, rowEnd)1
            sheet.mergeCells(startingDataAdminColumnNumber, startingDataAdminRowNumber, endingDataAdminColumnNumber, endingDataAdminRowNumber);

            startingDataAdminRowNumber = endingDataAdminRowNumber + 1;
            endingDataAdminRowNumber = startingDataAdminRowNumber + 1;

            for (int i = 1; i < 4; i++) {
                cellFont = createCellFont("workloadReport.administrativeCol2.name.fontsize", Colour.BLACK, true);
                cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
                createText(sheet, "", cellFormat, startingDataAdminColumnNumber, startingDataAdminRowNumber);
                // mergeCells(colStart, rowStart, colEnd, rowEnd)1
                sheet.mergeCells(startingDataAdminColumnNumber, startingDataAdminRowNumber, endingDataAdminColumnNumber, endingDataAdminRowNumber);
                startingDataAdminRowNumber = endingDataAdminRowNumber + 1;
                endingDataAdminRowNumber = startingDataAdminRowNumber + 1;
            }
            startingDataAdminRowNumber = endingAdminRowNumber + 1;
            endingDataAdminRowNumber = startingDataAdminRowNumber + 1;

            startingDataAdminColumnNumber = endingDataAdminColumnNumber + 1;
            endingDataAdminColumnNumber = startingDataAdminColumnNumber;

            cellFont = createCellFont("workloadReport.administrativeCol3.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
            createText(sheet, "workloadReport.administrative1Col3.name", null, cellFormat, startingDataAdminColumnNumber, startingDataAdminRowNumber);
            // mergeCells(colStart, rowStart, colEnd, rowEnd)1
            sheet.mergeCells(startingDataAdminColumnNumber, startingDataAdminRowNumber, endingDataAdminColumnNumber, endingDataAdminRowNumber);

            startingDataAdminRowNumber = endingDataAdminRowNumber + 1;
            endingDataAdminRowNumber = startingDataAdminRowNumber + 1;

            for (int i = 1; i < 5; i++) {
                cellFont = createCellFont("workloadReport.administrativeCol3.name.fontsize", Colour.BLACK, true);
                cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
                createText(sheet, "", cellFormat, startingDataAdminColumnNumber, startingDataAdminRowNumber);
                // mergeCells(colStart, rowStart, colEnd, rowEnd)1
                sheet.mergeCells(startingDataAdminColumnNumber, startingDataAdminRowNumber, endingDataAdminColumnNumber, endingDataAdminRowNumber);
                startingDataAdminRowNumber = endingDataAdminRowNumber + 1;
                endingDataAdminRowNumber = startingDataAdminRowNumber + 1;
            }

            startingDataAdminRowNumber = endingAdminRowNumber + 1;
            endingDataAdminRowNumber = startingDataAdminRowNumber + 9;

            startingDataAdminColumnNumber = endingDataAdminColumnNumber + 1;
            endingDataAdminColumnNumber = startingDataAdminColumnNumber + 1;

            cellFont = createCellFont("workloadReport.notapplicable.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
            createText(sheet, "workloadReport.notapplicable.name", null, cellFormat, startingDataAdminColumnNumber, startingDataAdminRowNumber);
            // mergeCells(colStart, rowStart, colEnd, rowEnd)
            sheet.mergeCells(startingDataAdminColumnNumber, startingDataAdminRowNumber, endingDataAdminColumnNumber, endingDataAdminRowNumber);


            // *****************************************************************************************************
            //NON ADMINISTRATIVE REASSIGN
            int startingNonAdminRowNumber = endingDataAdminRowNumber + 1;
            int endingNonAdminRowNumber = startingNonAdminRowNumber + 2;

            cellFont = createCellFont("workloadReport.nonadministrative.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.nonadministrative.name", null, cellFormat, startingColumnFrame, startingNonAdminRowNumber);
            sheet.mergeCells(startingColumnFrame, startingNonAdminRowNumber, endingColumnFrame, endingNonAdminRowNumber);


            // *****************************************************************************************************
            //DATA UNDER NON ADMINISTRATIVE
            int startingDataNonAdminRowNumber = endingNonAdminRowNumber + 1;
            int endingDataNonAdminRowNumber = startingDataNonAdminRowNumber;

            int startingDataNonAdminColumnNumber = startingColumnFrame;
            int endingDataNonAdminColumnNumber = startingDataNonAdminColumnNumber + 3;

            // COLUMN HEADER

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.item.name", null, cellFormat, startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber);
            sheet.mergeCells(startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber, endingDataNonAdminColumnNumber, endingDataNonAdminRowNumber);

            for (int i = 1; i < 15; i++) {
                startingDataNonAdminRowNumber = endingDataNonAdminRowNumber + 1;
                endingDataNonAdminRowNumber = startingDataNonAdminRowNumber;
                cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, true);
                cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
                createText(sheet, "", cellFormat, startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber);
                sheet.mergeCells(startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber, endingDataNonAdminColumnNumber, endingDataNonAdminRowNumber);

            }

            startingDataNonAdminRowNumber = endingNonAdminRowNumber + 1;
            endingDataNonAdminRowNumber = startingDataNonAdminRowNumber;

            startingDataNonAdminColumnNumber = endingDataNonAdminColumnNumber + 1;
            endingDataNonAdminColumnNumber = startingDataNonAdminColumnNumber + 18;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.detailsofitem.name", null, cellFormat, startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber);
            sheet.mergeCells(startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber, endingDataNonAdminColumnNumber, endingDataNonAdminRowNumber);

            endingDataNonAdminColumnNumber = startingDataNonAdminColumnNumber + 16;

            for (int i = 1; i < 15; i++) {
                startingDataNonAdminRowNumber = endingDataNonAdminRowNumber + 1;
                endingDataNonAdminRowNumber = startingDataNonAdminRowNumber;
                cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, true);
                cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
                createText(sheet, "", cellFormat, startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber);
                sheet.mergeCells(startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber, endingDataNonAdminColumnNumber, endingDataNonAdminRowNumber);

            }
            startingDataNonAdminColumnNumber = endingDataNonAdminColumnNumber + 1;
            endingDataNonAdminColumnNumber = startingDataNonAdminColumnNumber;

            startingDataNonAdminRowNumber = endingNonAdminRowNumber + 2;
            endingDataNonAdminRowNumber = startingDataNonAdminRowNumber + 13;

            cellFont = createCellFont("workloadReport.notapplicable.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
            createText(sheet, "workloadReport.notapplicable.name", null, cellFormat, startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber);
            sheet.mergeCells(startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber, endingDataNonAdminColumnNumber, endingDataNonAdminRowNumber);

            startingDataNonAdminRowNumber = endingNonAdminRowNumber + 1;
            endingDataNonAdminRowNumber = startingDataNonAdminRowNumber;

            startingDataNonAdminColumnNumber = endingDataNonAdminColumnNumber + 1;
            endingDataNonAdminColumnNumber = startingDataNonAdminColumnNumber;

            for (int i = 1; i < 15; i++) {
                startingDataNonAdminRowNumber = endingDataNonAdminRowNumber + 1;
                endingDataNonAdminRowNumber = startingDataNonAdminRowNumber;
                cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, true);
                cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
                createText(sheet, "", cellFormat, startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber);
                sheet.mergeCells(startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber, endingDataNonAdminColumnNumber, endingDataNonAdminRowNumber);

            }

            startingDataNonAdminRowNumber = endingNonAdminRowNumber + 1;
            endingDataNonAdminRowNumber = startingDataNonAdminRowNumber + 14;

            startingDataNonAdminColumnNumber = endingDataNonAdminColumnNumber + 1;
            endingDataNonAdminColumnNumber = startingDataNonAdminColumnNumber + 1;

            cellFont = createCellFont("workloadReport.notapplicable.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, true, true, true, true, true);
            createText(sheet, "workloadReport.notapplicable.name", null, cellFormat, startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber);
            sheet.mergeCells(startingDataNonAdminColumnNumber, startingDataNonAdminRowNumber, endingDataNonAdminColumnNumber, endingDataNonAdminRowNumber);


            // *****************************************************************************************************
            //TOTAL DATA  UNDER NON ADMINISTRATIVE'S ITEMS

            int startingTotalEverythingRowNumber = endingDataNonAdminRowNumber + 1;
            int endingTotalEverythingRowNumber = startingTotalEverythingRowNumber;

            int startingTotalEverythingColumnNumber = startingColumnFrame;
            int endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber + 9;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber + 4;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, VerticalAlignment.CENTRE, Alignment.RIGHT, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.total.name", null, cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.RED, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, String.valueOf(simplifiedWorkload.getTotal11thDayCount()), cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, String.valueOf(simplifiedWorkload.getTotalCreditHours()), cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, String.valueOf(simplifiedWorkload.getTotalLectureHours()), cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.WHITE, true);
            cellFormat = createCellFormat(cellFont, Colour.PALETTE_BLACK, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "N/A", cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, String.valueOf(simplifiedWorkload.getTotalLabHours()), cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.WHITE, true);
            cellFormat = createCellFormat(cellFont, Colour.PALETTE_BLACK, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "N/A", cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.RED, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, String.valueOf(simplifiedWorkload.getTotalTotalIUs()), cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.WHITE, true);
            cellFormat = createCellFormat(cellFont, Colour.PALETTE_BLACK, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "N/A", cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);

            startingTotalEverythingColumnNumber = endingTotalEverythingColumnNumber + 1;
            endingTotalEverythingColumnNumber = startingTotalEverythingColumnNumber;

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, String.valueOf(simplifiedWorkload.getTotalSsch()), cellFormat, startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber);
            sheet.mergeCells(startingTotalEverythingColumnNumber, startingTotalEverythingRowNumber, endingTotalEverythingColumnNumber, endingTotalEverythingRowNumber);


            // *****************************************************************************************************
            // ADDITIONAL COMMENTS

            int startingAddCommentsRowNumber = endingTotalEverythingRowNumber + 1;
            int endingAddCommentsRowNumber = startingAddCommentsRowNumber + 2;

            int startingAddCommentsColumnNumber = startingColumnFrame;
            int endingAddCommentsColumnNumber = endingColumnFrame;

            cellFont = createCellFont("workloadReport.comments.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.comments.name", null, cellFormat, startingAddCommentsColumnNumber, startingAddCommentsRowNumber);
            sheet.mergeCells(startingAddCommentsColumnNumber, startingAddCommentsRowNumber, endingAddCommentsColumnNumber, endingAddCommentsRowNumber);

            startingAddCommentsRowNumber = endingAddCommentsRowNumber + 1;
            endingAddCommentsRowNumber = startingAddCommentsRowNumber + 7;

            cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, VerticalAlignment.CENTRE, Alignment.LEFT, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingAddCommentsColumnNumber, startingAddCommentsRowNumber);
            sheet.mergeCells(startingAddCommentsColumnNumber, startingAddCommentsRowNumber, endingAddCommentsColumnNumber, endingAddCommentsRowNumber);

            startingAddCommentsRowNumber = endingAddCommentsRowNumber + 1;
            endingAddCommentsRowNumber = startingAddCommentsRowNumber;

            cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, VerticalAlignment.CENTRE, Alignment.LEFT, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingAddCommentsColumnNumber, startingAddCommentsRowNumber);
            sheet.mergeCells(startingAddCommentsColumnNumber, startingAddCommentsRowNumber, endingAddCommentsColumnNumber, endingAddCommentsRowNumber);

            // *****************************************************************************************************
            // SIGNATURE

            int startingSignatureRowNumber = endingAddCommentsRowNumber + 1;
            int endingSignatureRowNumber = startingSignatureRowNumber + 6;

            int startingSignatureColumnNumber = startingColumnFrame;
            int endingSignatureColumnNumber = startingSignatureColumnNumber + 5;

            cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);


            startingSignatureColumnNumber = endingSignatureColumnNumber + 1;
            endingSignatureColumnNumber = startingSignatureColumnNumber + 3;

            startingSignatureRowNumber = endingAddCommentsRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.chair.name", null, cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);


            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.dean.name", null, cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber;

            /*cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);*/


            startingSignatureColumnNumber = endingSignatureColumnNumber + 1;
            endingSignatureColumnNumber = startingSignatureColumnNumber + 4;

            startingSignatureRowNumber = endingAddCommentsRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.signatures.name", null, cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber;

           /* cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);*/

            startingSignatureColumnNumber = endingSignatureColumnNumber + 1;
            endingSignatureColumnNumber = startingSignatureColumnNumber + 1;

            startingSignatureRowNumber = endingAddCommentsRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.date.name", null, cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);
            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber;

           /* cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);*/

            startingSignatureColumnNumber = endingSignatureColumnNumber + 1;
            endingSignatureColumnNumber = startingSignatureColumnNumber + 1;

            startingSignatureRowNumber = endingAddCommentsRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "workloadReport.names.name", null, cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);
            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, simplifiedWorkload.getChairNameAndSurname(), cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 1;

            cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, simplifiedWorkload.getDeanNameAndSurname(), cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            startingSignatureRowNumber = endingSignatureRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber;

           /* cellFont = createCellFont("workloadReport.administrativeCol1.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);*/

            startingSignatureColumnNumber = endingSignatureColumnNumber + 1;
            endingSignatureColumnNumber = endingColumnFrame;
            startingSignatureRowNumber = endingAddCommentsRowNumber + 1;
            endingSignatureRowNumber = startingSignatureRowNumber + 6;


            cellFont = createCellFont("workloadReport.regularvalue.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true, true, true, true);
            createText(sheet, "", cellFormat, startingSignatureColumnNumber, startingSignatureRowNumber);
            sheet.mergeCells(startingSignatureColumnNumber, startingSignatureRowNumber, endingSignatureColumnNumber, endingSignatureRowNumber);

            //Writes out the data held in this workbook in Excel format
            SheetSettings sheetSettings = sheet.getSettings();
            sheetSettings.setShowGridLines(false);
            sheetSettings.setZoomFactor(excelZoomFactor);
            sheetSettings.setFitToPages(true);
            sheetSettings.setScaleFactor(excelScaleFactor);
            sheetSettings.setLeftMargin(excelLeftMargin);
            sheetSettings.setRightMargin(excelRightMargin);
            sheetSettings.setBottomMargin(excelBottomMargin);
            sheetSettings.setTopMargin(excelTopMargin);
            sheetSettings.setPaperSize(PaperSize.A4);
            sheetSettings.setOrientation(PageOrientation.LANDSCAPE);
            sheetSettings.setPrintArea(startingColumnFrame, 1, endingColumnFrame, endingSignatureRowNumber);

            workbook.write();

            //Close and free allocated memory
            workbook.close();
        }
    }

    private WritableFont createCellFont(String propertyNameForFontSize, Colour color, boolean isBold) throws WriteException {
        WritableFont cellFont = new WritableFont(WritableFont.ARIAL,
                Integer.valueOf(messageSource.getMessage(propertyNameForFontSize, null, Locale.US)),
                (isBold) ? WritableFont.BOLD : WritableFont.NO_BOLD);
        cellFont.setColour(color);
        return cellFont;
    }

    private WritableCellFormat createCellFormat(WritableFont cellFont, Colour backroundColor, BorderLineStyle borderStyle, boolean isWrapped, boolean hasTopBorder, boolean hasLeftBorder, boolean hasRightBorder, boolean hasBottomBorder) throws WriteException {
        return createCellFormat(cellFont, backroundColor, VerticalAlignment.CENTRE, Alignment.CENTRE, borderStyle, isWrapped, hasTopBorder, hasLeftBorder, hasRightBorder, hasBottomBorder);
    }

    private WritableCellFormat createCellFormat(WritableFont cellFont, Colour backroundColor, VerticalAlignment verticalAlignment, Alignment horizontalAlignment, BorderLineStyle borderStyle, boolean isWrapped, boolean hasTopBorder, boolean hasLeftBorder, boolean hasRightBorder, boolean hasBottomBorder) throws WriteException {
        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setBackground(backroundColor);
        cellFormat.setAlignment(horizontalAlignment);
        cellFormat.setWrap(isWrapped);
        cellFormat.setVerticalAlignment(verticalAlignment);
        cellFormat.setBorder(Border.TOP, (hasTopBorder) ? borderStyle : BorderLineStyle.NONE);
        cellFormat.setBorder(Border.LEFT, (hasLeftBorder) ? borderStyle : BorderLineStyle.NONE);
        cellFormat.setBorder(Border.RIGHT, (hasRightBorder) ? borderStyle : BorderLineStyle.NONE);
        cellFormat.setBorder(Border.BOTTOM, (hasBottomBorder) ? borderStyle : BorderLineStyle.NONE);
        return cellFormat;
    }

    private void createText(WritableSheet sheet, String propertyName, Object[] appendedTextValues, WritableCellFormat cell, int startingColIndex, int startingRowIndex) throws WriteException {
        Label label = new Label(startingColIndex, startingRowIndex,
                messageSource.getMessage(propertyName, appendedTextValues, Locale.US), cell);
        sheet.addCell(label);
    }

    private void createText(WritableSheet sheet, String textValue, WritableCellFormat cell, int startingColIndex, int startingRowIndex) throws WriteException {
        Label label = new Label(startingColIndex, startingRowIndex,
                textValue, cell);
        sheet.addCell(label);
    }

  /*  private List<TypeSafeRawWorkload> validateRawData(List<RawWorkload> rawWorkloads)
    {
        log.info("Started to validate workload data");
        List<TypeSafeRawWorkload> typeSafeRawWorkloadList = new ArrayList<TypeSafeRawWorkload>();
        TypeSafeRawWorkload typeSafeRawWorkload;
        for(RawWorkload rawWorkload : rawWorkloads)
        {
            if rawWorkload

        }
        return typeSafeRawWorkloadList;
    }*/
    private List<RawWorkload> prepareTestData(String filePattern) {
        log.info("Started to importing testdata data");
        BufferedReader br = FileUtilService.getInstance().getFile(filePattern).getCSVFileContent();
        String line = ""; //lineDelimiter
        String cvsSplitBy = ":"; //fieldDelimiter
        List<RawWorkload> rawWorkloadList = new ArrayList<RawWorkload>();
        RawWorkload rawWorkload;
        try {
            while ((line = br.readLine()) != null) {

                String[] workload = line.split(cvsSplitBy);

                rawWorkload = new RawWorkload();
                rawWorkload.setInstructionType(workload[0]);
                rawWorkload.setIstructionPidm(workload[1]);
                rawWorkload.setInstructorTNumber(workload[2]);
                rawWorkload.setInstructorNameSurname(workload[3]);
                rawWorkload.setSemesterTermCode(workload[4]);
                rawWorkload.setCrn(workload[5]);
                rawWorkload.setSubjectCode(workload[6]);
                rawWorkload.setCourseNumber(workload[7]);
                rawWorkload.setSection(workload[8]);
                rawWorkload.setPct_response(workload[9]);
                rawWorkload.setCourseTitle(workload[10]);
                rawWorkload.setCollCode(workload[11]);
                rawWorkload.setInstructorDepartmentCode(workload[12]);
                rawWorkload.setInstructorDepartmentDescription(workload[13]);
                rawWorkload.setTaStudent(workload[14]);
                rawWorkload.setDeptChair(workload[15]);
                rawWorkload.setDean(workload[16]);
                rawWorkload.setTaEleventhDayCount(workload[17]);
                rawWorkload.setTaCeditHours(workload[18]);
                rawWorkload.setTaLectureHours(workload[19]);
                rawWorkload.setTaLabHours(workload[20]);
                rawWorkload.setTotalSsch(workload[21]);
              /*  MapBindingResult err = new MapBindingResult(new HashMap<String,String>(), RawWorkload.class.getName());
                RawWorkloadValidator.getInstance().validate(rawWorkload, err);
                List<ObjectError> list = err.getAllErrors();
                for(ObjectError objErr : list){
                    System.out.println(objErr.getDefaultMessage());
                }
                if (list.size() == 0) {*/
                rawWorkloadList.add(rawWorkload);
                //}

            }
            log.info("Finished importing");
        } catch (IOException e) {
            log.info("Error occured during importing");
            log.error(e.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.warn(e.toString());
                }
            }
        }
        return rawWorkloadList;
    }




}