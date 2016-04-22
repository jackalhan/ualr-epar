package com.jackalhan.ualr.service;

import com.jackalhan.ualr.domain.CourseType;
import com.jackalhan.ualr.domain.RawWorkloadData;
import com.jackalhan.ualr.domain.SimplifiedWorkload;
import com.jackalhan.ualr.service.utils.ListUtilService;
import com.jackalhan.ualr.service.utils.StringUtilService;
import com.sun.org.apache.xpath.internal.axes.ReverseAxesWalker;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.scenario.effect.impl.sw.sse.SSEInvertMaskPeer;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.codehaus.groovy.runtime.metaclass.ConcurrentReaderHashMap;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
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


    // mergeCells(colStart, rowStart, colEnd, rowEnd)
    // new Label(col, row)

    @Scheduled(fixedDelay = 5000)//Everyday 12 pm
    private void executeWorkloadReports() throws IOException, WriteException {
        log.info("RawWorkloadData Reports execution started");
        List<RawWorkloadData> rawWorkloadDataList = prepareTestData();

        log.info(simplifyWorkloadData(rawWorkloadDataList).toString());
        log.info("RawWorkloadData Reports execution ended");
    }

    private int findNumberOfEnrolledStudents(List<RawWorkloadData> rawWorkloadDatas, int fromCourseCodeNumber, int toCourseCodeNumber)
    {
        return rawWorkloadDatas.stream().filter(x -> x.getCourseNumber() >= fromCourseCodeNumber &&  x.getCourseNumber() <=toCourseCodeNumber).collect(Collectors.toList()).size();
    }

    private CourseType getCourseType (String instructionType, String courseTitle, int courseCodeNumber, int numberOfEnrolledStudents)
    {
        CourseType courseType = new CourseType();
        if (instructionType.toUpperCase().trim().contains("PED")) {
            if (courseCodeNumber >= 1000 && courseCodeNumber < 5000) {
                courseType.setCode("U");
                courseType.setName("UNDERGRADUATE COURSE");
            }
            else if (courseCodeNumber >= 4000 && courseCodeNumber < 6000)
            {
                if (numberOfEnrolledStudents < 5)
                {
                    courseType.setCode("DL");
                    courseType.setName("DUAL-LISTED COURSE");
                }
                else
                {
                    courseType.setCode("DU");
                    courseType.setName("DUAL-LISTED COURSE");
                }
            }
            else if (courseCodeNumber >= 7000 && courseCodeNumber < 8000)
            {
                courseType.setCode("G");
                courseType.setName("GRADUATE COURSE");
            }
        }
        else
        {
            if (courseTitle.toUpperCase().trim().contains("MASTER'S THESIS") || courseTitle.toUpperCase().trim().contains("MS THESIS"))
            {
                courseType.setCode("MS");
            }
            else if (courseTitle.toUpperCase().trim().contains("DOCTORAL RESEARCH") || courseTitle.toUpperCase().trim().contains("DISSERTATION"))
            {
                courseType.setCode("PhD");
            }
            else
            {
                courseType.setCode("O");
            }

        }
        return courseType;
    }

    private List<SimplifiedWorkload> simplifyWorkloadData(List<RawWorkloadData> rawWorkloadDatas)
    {
        log.info("Simplifying WorkLoad Data");

        Map<String, List<RawWorkloadData>> distinctValuesInList = rawWorkloadDatas.stream()
                .collect(Collectors.groupingBy(RawWorkloadData :: getInstructorNameSurname));
        List<SimplifiedWorkload> simplifiedWorkloadList = new ArrayList<SimplifiedWorkload>();
        SimplifiedWorkload simplifiedWorkload = null;
        for (Map.Entry<String, List<RawWorkloadData>> entry : distinctValuesInList.entrySet())
        {
            simplifiedWorkload = new SimplifiedWorkload();
            simplifiedWorkload.setRawWorkloadDataDetails(entry.getValue());
            boolean isInstructorNameParsed = false;
            boolean isDeanNameParsed= false;
            boolean isChairNameParsed=false;
            boolean isDateParsed=false;
            for ( RawWorkloadData rawData : entry.getValue())
            {
                if (!isInstructorNameParsed)
                {
                    isInstructorNameParsed = true;
                    simplifiedWorkload.setInstructorNameAndSurname(StringUtilService.getInstance().switchText(rawData.getInstructorNameSurname(), splitChar));
                }
                if (!isDeanNameParsed)
                {
                    isDeanNameParsed = true;
                    simplifiedWorkload.setDeanNameAndSurname(StringUtilService.getInstance().switchText(rawData.getDean(), splitChar));
                }
                if (!isChairNameParsed)
                {
                    isChairNameParsed = true;
                    simplifiedWorkload.setChairNameAndSurname(StringUtilService.getInstance().switchText(rawData.getChair(), splitChar));
                }
                if (!isDateParsed)
                {
                    if(!StringUtilService.getInstance().isEmpty(String.valueOf(rawData.getSemesterTermCode()))) {
                        isDateParsed = true;
                        simplifiedWorkload.setSemesterTerm(decidePeriod(Integer.parseInt(String.valueOf(rawData.getSemesterTermCode()).substring(4,5))));
                        simplifiedWorkload.setSemesterYear(Integer.valueOf(String.valueOf(rawData.getSemesterTermCode()).substring(0,4)));
                    }
                }
                simplifiedWorkload.setTotalSsch(simplifiedWorkload.getTotalSsch() + rawData.getTotalSsch());
                simplifiedWorkload.setTotal11thDayCount(simplifiedWorkload.getTotal11thDayCount() + rawData.getTaEleventhDayCount());
                simplifiedWorkload.setTotalCreditHours(simplifiedWorkload.getTotalCreditHours() + rawData.getTaCeditHours());
                simplifiedWorkload.setTotalIUMultiplierForLectureHours(simplifiedWorkload.getTotalIUMultiplierForLectureHours() + rawData.getIuMultipliertaLectureHours());
                simplifiedWorkload.setTotalLabHours(simplifiedWorkload.getTotalLabHours() + rawData.getTaLabHours());
                simplifiedWorkload.setTotalLectureHours(simplifiedWorkload.getTotalLectureHours() + rawData.getTaLectureHours());
                simplifiedWorkload.setTotalTaSupport(simplifiedWorkload.getTotalTaSupport() + rawData.getTaSupport());
                simplifiedWorkload.setTotalTotalIUs(simplifiedWorkload.getTotalTotalIUs() + rawData.getTotalIus());
            }
            simplifiedWorkloadList.add(simplifiedWorkload);
        }
        return simplifiedWorkloadList;

    }

    private String decidePeriod(int month)
    {
        if(month <= 6)
            return "Spring";
        return "Fall";

    }

    private void generateExcelContent() throws IOException, WriteException
    {
        int startingColumnFrame = 1;
        int endingColumnFrame = 19;
        //Creates a writable workbook with the given file name
        WritableWorkbook workbook = Workbook.createWorkbook(new File("MergeCells.xls"));
        WritableSheet sheet = workbook.createSheet("InstructorNameSurname-Term", 0);

        // Create cell font and format
        // REPORT HEADER
        WritableFont cellFont = createCellFont("workloadReport.faculty.name.fontsize", Colour.BLACK, true);
        WritableCellFormat cellFormat = createCellFormat(cellFont, Colour.GRAY_25, BorderLineStyle.THICK, false, true,true,true,true);
        sheet.mergeCells(startingColumnFrame, 1, endingColumnFrame, 3);
        createText(sheet, "workloadReport.faculty.name", null, cellFormat, startingColumnFrame, 1 );

        // *****************************************************************************************************
        //REPORT NAME

        cellFont = createCellFont("workloadReport.report.name.fontsize", Colour.BLACK, true);
        cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, false,true,true,false);
        sheet.mergeCells(startingColumnFrame, 4, endingColumnFrame, 5);
        createText(sheet, "workloadReport.report.name", null, cellFormat, startingColumnFrame, 4);

        // *****************************************************************************************************
        //DEPARTMENT HEADER

        cellFont = createCellFont("workloadReport.department.name.fontsize", Colour.BLACK, false);
        cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false,false,true,true,true);
        sheet.mergeCells(startingColumnFrame, 6, endingColumnFrame, 7);
        createText(sheet, "workloadReport.department.name", new Object[]{"System Engineering", "Spring", "2016"}, cellFormat, startingColumnFrame, 6);

        // *****************************************************************************************************
        //INSTRUCTOR NAME
        cellFont = createCellFont("workloadReport.instructor.name.fontsize", Colour.BLACK, true);
        cellFormat = createCellFormat(cellFont, Colour.GRAY_25, BorderLineStyle.THICK, false, false,true,true,true);
        createText(sheet, "workloadReport.instructor.name", new Object[]{"Tolgahan Cakaloglu", "Spring", "2016"}, cellFormat, startingColumnFrame, 8);
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
                endingColHeadercolumnNumber = startingColHeadercolumnNumber + 1;
            } else {
                endingColHeadercolumnNumber = startingColHeadercolumnNumber;
            }

            columnTitle = "workloadReport.column." + i + ".name";

            cellFont = createCellFont("workloadReport.columnHeaders.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.GRAY_25, BorderLineStyle.THICK, true, true,true,true,true);
            createText(sheet, columnTitle, null, cellFormat, startingColHeadercolumnNumber, startingColHeaderRowNumber);
            sheet.mergeCells(startingColHeadercolumnNumber, startingColHeaderRowNumber, endingColHeadercolumnNumber, endingColHeaderRowNumber);
        }

        // *****************************************************************************************************
        //PEDAGOGICAL INSTR
        int startingPedaRowNumber = endingColHeaderRowNumber + 1;
        int endingPedaRowNumber = startingPedaRowNumber + 2;

        cellFont = createCellFont("workloadReport.pedagogical.name.fontsize", Colour.BLACK, true);
        cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, false,true,true,true);
        createText(sheet, "workloadReport.pedagogical.name", null, cellFormat, startingColumnFrame, startingPedaRowNumber);
        sheet.mergeCells(startingColumnFrame, startingPedaRowNumber, endingColumnFrame, endingPedaRowNumber);

        // *****************************************************************************************************
        //DATA UNDER PEDAGOGICAL
        int startingDataPedaRowNumber = endingPedaRowNumber + 1;
        int endingDataPedaRowNumber = startingDataPedaRowNumber;

        int startingDataPedaColumnNumber = 0;
        int endingDataPedaColumnNumber = startingDataPedaColumnNumber;

        // FOR LOOP WILL BE BUILDING FOR FILTERED PEDA DATA


        // *****************************************************************************************************
        //INDIVIDUAL INSTR
        int startingIndivRowNumber = endingDataPedaRowNumber + 1;
        int endingIndivRowNumber = startingIndivRowNumber + 2;

        cellFont = createCellFont("workloadReport.individualized.name.fontsize", Colour.BLACK, true);
        cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK, false, true,true,true,true);
        createText(sheet, "workloadReport.individualized.name", null, cellFormat, startingColumnFrame, startingIndivRowNumber);
        sheet.mergeCells(startingColumnFrame, startingIndivRowNumber, endingColumnFrame, endingIndivRowNumber);

        // *****************************************************************************************************
        //DATA UNDER INDIVIDUAL
        int startingDataIndivRowNumber = startingIndivRowNumber + 1;
        int endingDataIndivRowNumber = startingDataIndivRowNumber;

        int startingDataIndivColumnNumber = 0;
        int endingDataIndivColumnNumber = startingDataIndivColumnNumber;

        // FOR LOOP WILL BE BUILDING FOR FILTERED INDIV DATA


        /*// *****************************************************************************************************
        //ADMINISTRATIVE REASSIGN
        int startingAdminRowNumber = endingDataIndivRowNumber + 1;
        int endingAdminRowNumber = startingAdminRowNumber + 2;

        cellFont = createCellFont("workloadReport.administrative.name.fontsize", Colour.BLACK, true);
        cellFormat = createCellFormat(cellFont, Colour.WHITE, BorderLineStyle.THICK,false, true,true,true,true);
        createText(sheet, "workloadReport.administrative.name", null, cellFormat, startingColumnFrame, startingAdminRowNumber);
        sheet.mergeCells(startingColumnFrame, startingAdminRowNumber, endingColumnFrame, endingAdminRowNumber);

        // *****************************************************************************************************
        //DATA UNDER ADMINISTRATIVE
        int startingDataAdminRowNumber = startingAdminRowNumber + 1;
        int endingDataAdminRowNumber = startingDataAdminRowNumber;

        int startingDataAdminColumnNumber = 0;
        int endingDataAdminColumnNumber = startingDataAdminColumnNumber;*/

        // FOR LOOP WILL BE BUILDING FOR FILTERED INDIV DATA




        //Writes out the data held in this workbook in Excel format
        workbook.write();

        //Close and free allocated memory
        workbook.close();
    }

    private WritableFont createCellFont(String propertyNameForFontSize, Colour color, boolean isBold) throws WriteException {
        WritableFont cellFont = new WritableFont(WritableFont.ARIAL,
                Integer.valueOf(messageSource.getMessage(propertyNameForFontSize, null, Locale.US)),
                (isBold) ? WritableFont.BOLD : WritableFont.NO_BOLD);
        cellFont.setColour(color);
        return cellFont;
    }

    private WritableCellFormat createCellFormat(WritableFont cellFont, Colour backroundColor, BorderLineStyle borderStyle, boolean isWrapped, boolean hasTopBorder, boolean hasLeftBorder, boolean hasRightBorder, boolean hasBottomBorder) throws WriteException {
        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setBackground(backroundColor);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setWrap(isWrapped);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormat.setBorder(Border.TOP, (hasTopBorder) ? borderStyle : BorderLineStyle.NONE);
        cellFormat.setBorder(Border.LEFT, (hasLeftBorder) ? borderStyle : BorderLineStyle.NONE);
        cellFormat.setBorder(Border.RIGHT, (hasRightBorder) ? borderStyle : BorderLineStyle.NONE);
        cellFormat.setBorder(Border.BOTTOM, (hasBottomBorder) ? borderStyle : BorderLineStyle.NONE);
        return cellFormat;
    }

    private void createText(WritableSheet sheet, String propertyNameForFontSize, Object[] textValues, WritableCellFormat cell, int startingColIndex, int startingRowIndex ) throws WriteException {
        Label label = new Label(startingColIndex, startingRowIndex,
                messageSource.getMessage(propertyNameForFontSize, textValues, Locale.US), cell);
        sheet.addCell(label);
    }

    private List<RawWorkloadData> prepareTestData()
    {
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
        index= index + 1;
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
        index= index + 1;
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
        index= index + 1;

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
        index= index + 1;

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
        index= index + 1;

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
        rawWorkloadData.setTaStudent("BOSMUS");
        rawWorkloadData.setTaSupport(0);
        rawWorkloadData.setTotalSsch(3);
        rawWorkloadData.setTst(2);
        rawWorkloadDataList.add(rawWorkloadData);
        index= index + 1;

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
        index= index + 1;

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
        index= index + 1;


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
        index= index + 1;


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
        index= index + 1;

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
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;
        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4176);
        rawWorkloadData.setCourseTitle("Mechanics of Materials Lab");
        rawWorkloadData.setCrn(12194);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;
        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4376);
        rawWorkloadData.setCourseTitle("Mechanics of Materials");
        rawWorkloadData.setCrn(12200);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(4399);
        rawWorkloadData.setCourseTitle("Automative Engineering");
        rawWorkloadData.setCrn(14610);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(7399);
        rawWorkloadData.setCourseTitle("Advanced Engineering Mathematics");
        rawWorkloadData.setCrn(12327);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(5199);
        rawWorkloadData.setCourseTitle("Research Tools");
        rawWorkloadData.setCrn(13458);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(7385);
        rawWorkloadData.setCourseTitle("Graduate Project");
        rawWorkloadData.setCrn(15349);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;

        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(9700);
        rawWorkloadData.setCourseTitle("Doctoral Thesis Dissertation");
        rawWorkloadData.setCrn(15073);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;


        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(9800);
        rawWorkloadData.setCourseTitle("Doctoral Thesis Dissertation");
        rawWorkloadData.setCrn(15340);
        rawWorkloadData.setDean("Whitman, Lawrence");
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;


        rawWorkloadData = new RawWorkloadData();
        rawWorkloadData.setInstructorTNumber("T2");
        rawWorkloadData.setId(index);
        rawWorkloadData.setChair("Yoshigoe, Kenji");
        rawWorkloadData.setCollCode("SS");
        rawWorkloadData.setCourseNumber(9900);
        rawWorkloadData.setInstructorNameSurname("Tolgahan, Cakaloglu");
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
        index= index + 1;

        log.info("Finishing preperation");
        return rawWorkloadDataList;
    }


}
