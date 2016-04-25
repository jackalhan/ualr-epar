package com.jackalhan.ualr.service;

import com.jackalhan.ualr.config.Constants;
import com.jackalhan.ualr.domain.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jackalhan on 4/18/16.
 */

@Component
public class WorkloadReportService {

    @Autowired
    private MessageSource messageSource;

    private final Logger log = LoggerFactory.getLogger(WorkloadReportService.class);

    private final String splitChar = ",";
    private final int fromt5XXXCourseCode = 5000;
    private final int to5XXXCourseCode = 6000;
    private final String instructionTypeContainsKeyWordPED = "PED";
    private final String instructionTypeContainsKeyWordIND = "IND";
    private final String CourseTypeCodeU = "U";
    private final String CourseTypeCodeDL = "DL";
    private final String CourseTypeCodeDU = "DU";
    private final String CourseTypeCodeG = "G";
    private final String CourseTypeCodeMS = "MS";
    private final String CourseTypeCodePhD = "PhD";
    private final String CourseTypeCodeO = "O";
    private final int excelZoomFactor = 85;
    private final int excelScaleFactor = 50;
    private final double excelLeftMargin = 0.75;
    private final double excelRightMargin = 0.75;
    private final double excelTopMargin = 0.30;
    private final double excelBottomMargin = 0.33;

    // mergeCells(colStart, rowStart, colEnd, rowEnd)
    // new Label(col, row)

    @Scheduled(fixedDelay = 5000)//Everyday 12 pm
    private void executeWorkloadReports() throws IOException, WriteException, CloneNotSupportedException {
        log.info("RawWorkloadData Reports execution started");
        List<RawWorkloadData> rawWorkloadDataList = prepareTestData();
        List<SimplifiedWorkload> simplifiedWorkloadList = simplifyWorkloadData(rawWorkloadDataList);
        generateExcelContent(simplifiedWorkloadList);
        log.info(simplifyWorkloadData(rawWorkloadDataList).toString());
        log.info("RawWorkloadData Reports execution ended");
    }

    // COUNTER IN GENERAL 5XXX
/*
        private int findNumberOfEnrolledStudents(List<RawWorkloadData> rawWorkloadDatas, int fromCourseCodeNumber, int toCourseCodeNumber) {
        log.info("Finding number of Enrolled Students from " + fromCourseCodeNumber + " to " + toCourseCodeNumber);
        int numberOfStudents = rawWorkloadDatas.stream().filter(x -> x.getCourseNumber() >= fromCourseCodeNumber && x.getCourseNumber() < toCourseCodeNumber).collect(Collectors.toList()).size();
        log.info(numberOfStudents + " number of Enrolled Students");
        return numberOfStudents;

    }*/


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

    private CourseDualState calculateCourseDualState(List<RawWorkloadData> rawWorkloadDataList, int courseCodeNumber, int dualStartWith) {
        CourseDualState courseDualState = new CourseDualState();
        courseDualState.setHasDualCourse(false);
        courseDualState.setNumberOfTotalEnrollmentInDualCourse(0);
        int newCourseNumberForDualCheck = convertToDualCourse(courseCodeNumber, dualStartWith);
        for (RawWorkloadData rawWorkloadData : rawWorkloadDataList) {
            if (rawWorkloadData.getCourseNumber() == newCourseNumberForDualCheck) {
                //Do we need to add other data in order to calculate ?
                courseDualState.setHasDualCourse(true);
                courseDualState.setDualCourseCode(newCourseNumberForDualCheck);
                courseDualState.setNumberOfTotalEnrollmentInDualCourse(rawWorkloadData.getTaEleventhDayCount());
                return courseDualState;
            }
        }
        return courseDualState;

    }


    private CourseTypeIUValues calculateIUMultiplier(boolean isLectureHour, int lectureHours, String courseTypeCode) {
        double result = 0;
        double multiplier = 0;
        CourseTypeIUValues courseTypeIUValues = null;
        if (isLectureHour) {
            if (courseTypeCode.equals(CourseTypeCodeU)) {
                multiplier = 1;
                result = lectureHours * multiplier;
            } else if (courseTypeCode.equals(CourseTypeCodeDL)) {
                multiplier = 1;
                result = lectureHours * multiplier;
            } else if (courseTypeCode.equals(CourseTypeCodeDU)) {
                multiplier = 1.33;
                result = lectureHours * multiplier;
            } else if (courseTypeCode.equals(CourseTypeCodeG)) {
                multiplier = 1.33;
                result = lectureHours * multiplier;
            } else if (courseTypeCode.equals(CourseTypeCodeMS)) {
                multiplier = 0.75;
                result = multiplier;
            } else if (courseTypeCode.equals(CourseTypeCodePhD)) {
                multiplier = 1;
                result = multiplier;
            } else if (courseTypeCode.equals(CourseTypeCodeO)) {
                multiplier = 0.375;
                result = multiplier;
            }
        }
        courseTypeIUValues = new CourseTypeIUValues(multiplier, result);
        return courseTypeIUValues;


    }

    private CourseType getCourseType(String instructionType, String courseTitle, int courseCodeNumber, int numberOfEnrolledStudents, boolean hasDual) {

        CourseType courseType = new CourseType();
        if (instructionType.toUpperCase().trim().contains(instructionTypeContainsKeyWordPED)) {
            if (!hasDual) {
                if (courseCodeNumber >= 1000 && courseCodeNumber < 5000) {
                    courseType.setCode(CourseTypeCodeU);
                    courseType.setName("UNDERGRADUATE COURSE");
                } else if (courseCodeNumber >= 7000 && courseCodeNumber < 8000) {
                    courseType.setCode(CourseTypeCodeG);
                    courseType.setName("GRADUATE COURSE");
                }
            } else {
                if (courseCodeNumber >= 4000 && courseCodeNumber < 6000) {
                    if (numberOfEnrolledStudents >= 1 && numberOfEnrolledStudents < 5) {
                        courseType.setCode(CourseTypeCodeDL);
                        courseType.setName("DUAL-LISTED COURSE");
                    } else if (numberOfEnrolledStudents >= 5) {
                        courseType.setCode(CourseTypeCodeDU);
                        courseType.setName("DUAL-LISTED COURSE");
                    } else {
                        courseType.setCode(CourseTypeCodeU);
                        courseType.setName("UNDERGRADUATE COURSE");
                    }
                }
            }
        } else {
            if (courseTitle.toUpperCase().trim().contains("MASTER'S THESIS") || courseTitle.toUpperCase().trim().contains("MS THESIS") || courseTitle.toUpperCase().trim().contains("GRADUATE")) {
                courseType.setCode(CourseTypeCodeMS);
            } else if (courseTitle.toUpperCase().trim().contains("DOCTORAL RESEARCH") || courseTitle.toUpperCase().trim().contains("DISSERTATION") || courseTitle.toUpperCase().trim().contains("RESEARCH")) {
                courseType.setCode(CourseTypeCodePhD);
            } else {
                courseType.setCode(CourseTypeCodeO);
            }

        }
        return courseType;
    }

    private List<SimplifiedWorkload> simplifyWorkloadData(List<RawWorkloadData> rawWorkloadDatas) throws CloneNotSupportedException {
        log.info("Simplifying WorkLoad Data");
        //int numberofEnrolledStudents = findNumberOfEnrolledStudents(rawWorkloadDatas, fromt5XXXCourseCode, to5XXXCourseCode);
        Map<String, List<RawWorkloadData>> distinctValuesInList = rawWorkloadDatas.stream()
                .collect(Collectors.groupingBy(RawWorkloadData::getInstructorNameSurname));
        List<SimplifiedWorkload> simplifiedWorkloadList = new ArrayList<SimplifiedWorkload>();
        SimplifiedWorkload simplifiedWorkload = null;
        List<RawWorkloadData> newRawDataList = null;
        for (Map.Entry<String, List<RawWorkloadData>> entry : distinctValuesInList.entrySet()) {
            simplifiedWorkload = new SimplifiedWorkload();
            RawWorkloadData newRawData = null;
            CourseType courseType = null;
            CourseTypeIUValues courseTypeIUValues = null;
            newRawDataList = new ArrayList<RawWorkloadData>();

            boolean isInstructorNameParsed = false;
            boolean isDeanNameParsed = false;
            boolean isChairNameParsed = false;
            boolean isDateParsed = false;
            boolean isDepartmentNameGathered = false;

            for (RawWorkloadData rawData : entry.getValue()) {


                newRawData = (RawWorkloadData) rawData.clone();

                if (String.valueOf(rawData.getCourseNumber()).startsWith("4") ||
                        String.valueOf(rawData.getCourseNumber()).startsWith("5")) {

                    if (String.valueOf(rawData.getCourseNumber()).startsWith("4")) {
                        CourseDualState courseDualState = calculateCourseDualState(entry.getValue(), newRawData.getCourseNumber(), 5);
                        courseType = getCourseType(newRawData.getInstructionType(), newRawData.getCourseTitle(), newRawData.getCourseNumber(), courseDualState.getNumberOfTotalEnrollmentInDualCourse(), courseDualState.hasDualCourse());

                    } else if (String.valueOf(rawData.getCourseNumber()).startsWith("5")) {
                        CourseDualState courseDualState = calculateCourseDualState(entry.getValue(), newRawData.getCourseNumber(), 4);

                        if (courseDualState.hasDualCourse()) {
                            continue;
                        } else {
                            newRawData = (RawWorkloadData) rawData.clone();
                            courseType = getCourseType(newRawData.getInstructionType(), newRawData.getCourseTitle(), newRawData.getCourseNumber(), courseDualState.getNumberOfTotalEnrollmentInDualCourse(), courseDualState.hasDualCourse());

                        }
                    }


                }
                else {
                    courseType = getCourseType(newRawData.getInstructionType(), newRawData.getCourseTitle(), newRawData.getCourseNumber(), 0, false);

                }

                newRawData.setCourseTypeCode(courseType.getCode());
                newRawData.setCourseTypeName(courseType.getName());
                courseTypeIUValues = calculateIUMultiplier(true, newRawData.getTaLectureHours(), newRawData.getCourseTypeCode());
                newRawData.setIuMultipliertaLectureHours(courseTypeIUValues.getIuMultiplier());
                newRawData.setTotalIus(courseTypeIUValues.getIuMultiplicationResult());


                if (!isInstructorNameParsed) {
                    isInstructorNameParsed = true;
                    simplifiedWorkload.setInstructorNameAndSurname(StringUtilService.getInstance().switchText(newRawData.getInstructorNameSurname(), splitChar));
                }
                if (!isDeanNameParsed) {
                    isDeanNameParsed = true;
                    simplifiedWorkload.setDeanNameAndSurname(StringUtilService.getInstance().switchText(newRawData.getDean(), splitChar));
                }
                if (!isChairNameParsed) {
                    isChairNameParsed = true;
                    simplifiedWorkload.setChairNameAndSurname(StringUtilService.getInstance().switchText(newRawData.getChair(), splitChar));
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
                    simplifiedWorkload.setChairNameAndSurname(StringUtilService.getInstance().switchText(newRawData.getChair(), splitChar));
                }


                simplifiedWorkload.setTotalSsch(simplifiedWorkload.getTotalSsch() + newRawData.getTotalSsch());
                simplifiedWorkload.setTotal11thDayCount(simplifiedWorkload.getTotal11thDayCount() + newRawData.getTaEleventhDayCount());
                simplifiedWorkload.setTotalCreditHours(simplifiedWorkload.getTotalCreditHours() + newRawData.getTaCeditHours());
                simplifiedWorkload.setTotalIUMultiplierForLectureHours(simplifiedWorkload.getTotalIUMultiplierForLectureHours() + newRawData.getIuMultipliertaLectureHours());
                simplifiedWorkload.setTotalLabHours(simplifiedWorkload.getTotalLabHours() + newRawData.getTaLabHours());
                if (newRawData.getInstructionType().toUpperCase().trim().contains(instructionTypeContainsKeyWordPED)) {
                    simplifiedWorkload.setTotalLectureHours(simplifiedWorkload.getTotalLectureHours() + newRawData.getTaLectureHours());
                }
                simplifiedWorkload.setTotalTaSupport(simplifiedWorkload.getTotalTaSupport() + newRawData.getTaSupport());
                simplifiedWorkload.setTotalTotalIUs(simplifiedWorkload.getTotalTotalIUs() + newRawData.getTotalIus());
                newRawDataList.add(newRawData);
            }
            simplifiedWorkload.setRawWorkloadDataDetails(newRawDataList);
            simplifiedWorkloadList.add(simplifiedWorkload);
        }
        log.info("Simplifying WorkLoad Data Completed");
        return simplifiedWorkloadList;

    }

    private String decidePeriod(int month) {

        if (month <= 6)
            return "Spring";
        return "Fall";

    }

    private void generateExcelContent(List<SimplifiedWorkload> simplifiedWorkloadList) throws IOException, WriteException {

        FileUtilService.getInstance().createDirectory(Constants.workloadReportsTempPath);

        for (SimplifiedWorkload simplifiedWorkload : simplifiedWorkloadList) {

            File file = new File(Constants.workloadReportsTempPath + simplifiedWorkload.getSemesterYear() + "_" + simplifiedWorkload.getSemesterTerm() + "_WLReport_of_" + simplifiedWorkload.getInstructorNameAndSurname().replace(" ", "_") + ".xls");

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
            for (RawWorkloadData pedWorkloadItems : simplifiedWorkload.getRawWorkloadDataDetails()) {
                if (pedWorkloadItems.getInstructionType().toUpperCase().trim().contains(instructionTypeContainsKeyWordPED)) {
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
            for (RawWorkloadData indWorkloadItems : simplifiedWorkload.getRawWorkloadDataDetails()) {
                if (indWorkloadItems.getInstructionType().toUpperCase().trim().contains(instructionTypeContainsKeyWordIND)) {
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

    private List<RawWorkloadData> prepareTestData() {
        log.info("Start to preparting test data");
        long index = 1;

        List<RawWorkloadData> rawWorkloadDataList = new ArrayList<RawWorkloadData>();
        RawWorkloadData rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4176);
        rawWorkloadData.setCourseTitle("Mechanics of Materials Lab");
        rawWorkloadData.setCrn(12193);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(10);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(1);
        rawWorkloadData.setTaEleventhDayCount(3);
        rawWorkloadData.setTaLabHours(2);
        rawWorkloadData.setTaLectureHours(0);
        rawWorkloadData.setTaStudent("Qian Lui");
        rawWorkloadData.setTaSupport(5);
        rawWorkloadData.setTotalSsch(3);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;
        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4176);
        rawWorkloadData.setCourseTitle("Mechanics of Materials Lab");
        rawWorkloadData.setCrn(12194);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(20);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(1);
        rawWorkloadData.setTaEleventhDayCount(8);
        rawWorkloadData.setTaLabHours(2);
        rawWorkloadData.setTaLectureHours(0);
        rawWorkloadData.setTaStudent("Qian Lui");
        rawWorkloadData.setTaSupport(5);
        rawWorkloadData.setTotalSsch(8);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;
        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(5399);
        rawWorkloadData.setCourseTitle("Mechanics of Materials Lab");
        rawWorkloadData.setCrn(12194);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(20);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(1);
        rawWorkloadData.setTaEleventhDayCount(8);
        rawWorkloadData.setTaLabHours(2);
        rawWorkloadData.setTaLectureHours(0);
        rawWorkloadData.setTaStudent("Sang Sang");
        rawWorkloadData.setTaSupport(5);
        rawWorkloadData.setTotalSsch(8);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;
        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4376);
        rawWorkloadData.setCourseTitle("Mechanics of Materials");
        rawWorkloadData.setCrn(12200);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(3);
        rawWorkloadData.setTaEleventhDayCount(16);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Wissam Albaidi");
        rawWorkloadData.setTaSupport(10);
        rawWorkloadData.setTotalSsch(48);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4399);
        rawWorkloadData.setCourseTitle("Automative Engineering");
        rawWorkloadData.setCrn(14610);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(3);
        rawWorkloadData.setTaEleventhDayCount(14);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Sheng Sang");
        rawWorkloadData.setTaSupport(10);
        rawWorkloadData.setTotalSsch(42);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(7399);
        rawWorkloadData.setCourseTitle("Advanced Engineering Mathematics");
        rawWorkloadData.setCrn(12327);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(10);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(3);
        rawWorkloadData.setTaEleventhDayCount(10);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("BOSMUS");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(30);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(5199);
        rawWorkloadData.setCourseTitle("Research Tools");
        rawWorkloadData.setCrn(13458);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(10);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(1);
        rawWorkloadData.setTaEleventhDayCount(2);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(2);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(7385);
        rawWorkloadData.setCourseTitle("Graduate Project");
        rawWorkloadData.setCrn(15349);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(3);
        rawWorkloadData.setTaEleventhDayCount(1);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Dallas Thompkins");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(3);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(9700);
        rawWorkloadData.setCourseTitle("Doctoral Thesis Dissertation");
        rawWorkloadData.setCrn(15073);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(7);
        rawWorkloadData.setTaEleventhDayCount(1);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Khalid Aljabori");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(7);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;


        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(9800);
        rawWorkloadData.setCourseTitle("Doctoral Thesis Dissertation");
        rawWorkloadData.setCrn(15340);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(8);
        rawWorkloadData.setTaEleventhDayCount(1);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Homadi ALdulrahman");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(8);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;


        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T1");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Anderson, Gary");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(9900);
        rawWorkloadData.setCourseTitle("Doctoral Thesis Dissertation");
        rawWorkloadData.setCrn(12697);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Sandgren, Eric");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("System Engineering"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(9);
        rawWorkloadData.setTaEleventhDayCount(1);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Wissam ALbaidi");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(9);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        // OTHER INSTRUCTOR

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4176);
        rawWorkloadData.setCourseTitle("Mechanics of Materials Lab");
        rawWorkloadData.setCrn(12193);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(10);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(1);
        rawWorkloadData.setTaEleventhDayCount(3);
        rawWorkloadData.setTaLabHours(2);
        rawWorkloadData.setTaLectureHours(0);
        rawWorkloadData.setTaStudent("Qian Lui");
        rawWorkloadData.setTaSupport(5);
        rawWorkloadData.setTotalSsch(3);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;
        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4176);
        rawWorkloadData.setCourseTitle("Mechanics of Materials Lab");
        rawWorkloadData.setCrn(12194);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(20);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(1);
        rawWorkloadData.setTaEleventhDayCount(8);
        rawWorkloadData.setTaLabHours(2);
        rawWorkloadData.setTaLectureHours(0);
        rawWorkloadData.setTaStudent("Qian Lui");
        rawWorkloadData.setTaSupport(5);
        rawWorkloadData.setTotalSsch(8);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;
        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4376);
        rawWorkloadData.setCourseTitle("Mechanics of Materials");
        rawWorkloadData.setCrn(12200);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(3);
        rawWorkloadData.setTaEleventhDayCount(16);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Wissam Albaidi");
        rawWorkloadData.setTaSupport(10);
        rawWorkloadData.setTotalSsch(48);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4399);
        rawWorkloadData.setCourseTitle("Automative Engineering");
        rawWorkloadData.setCrn(14610);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(3);
        rawWorkloadData.setTaEleventhDayCount(14);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Sheng Sang");
        rawWorkloadData.setTaSupport(10);
        rawWorkloadData.setTotalSsch(42);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(7399);
        rawWorkloadData.setCourseTitle("Advanced Engineering Mathematics");
        rawWorkloadData.setCrn(12327);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setInstructionType("PED INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(10);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(3);
        rawWorkloadData.setTaEleventhDayCount(10);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("BOSMUS");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(30);
        rawWorkloadData.setTst(0);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(5199);
        rawWorkloadData.setCourseTitle("Research Tools");
        rawWorkloadData.setCrn(13458);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(10);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(1);
        rawWorkloadData.setTaEleventhDayCount(2);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("BOSMUS");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(3);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(7385);
        rawWorkloadData.setCourseTitle("Graduate Project");
        rawWorkloadData.setCrn(15349);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(3);
        rawWorkloadData.setTaEleventhDayCount(1);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Dallas Thompkins");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(3);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(9700);
        rawWorkloadData.setCourseTitle("Doctoral Thesis Dissertation");
        rawWorkloadData.setCrn(15073);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(7);
        rawWorkloadData.setTaEleventhDayCount(1);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Khalid Aljabori");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(7);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;


        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(9800);
        rawWorkloadData.setCourseTitle("Doctoral Thesis Dissertation");
        rawWorkloadData.setCrn(15340);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(8);
        rawWorkloadData.setTaEleventhDayCount(1);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Homadi ALdulrahman");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(8);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;


        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(9900);
        rawWorkloadData.setInstructorNameSurname("Cakaloglu, Tolgahan");
        rawWorkloadData.setCourseTitle("Doctoral Thesis Dissertation");
        rawWorkloadData.setCrn(12697);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructionType("IND INSTUCT");
        rawWorkloadData.setInstructorDepartment("Computer Science"); // PROVIDED NOT YET
        rawWorkloadData.setOtherInstructorsInTeam("XXXXX YYYYY, AAAAA BBBBBB"); // NEEDS TO BE CHECKED OUT
        rawWorkloadData.setSection(1);
        rawWorkloadData.setSemesterTermCode(201610);
        rawWorkloadData.setSubjectCode("SYEN");
        rawWorkloadData.setTaCeditHours(9);
        rawWorkloadData.setTaEleventhDayCount(1);
        rawWorkloadData.setTaLabHours(0);
        rawWorkloadData.setTaLectureHours(3);
        rawWorkloadData.setTaStudent("Wissam ALbaidi");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(9);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index = index + 1;

        log.info("Finishing preperation");
        return rawWorkloadDataList;
    }


}
