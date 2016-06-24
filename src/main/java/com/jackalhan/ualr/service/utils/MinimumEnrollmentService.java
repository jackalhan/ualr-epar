package com.jackalhan.ualr.service.utils;

import com.jackalhan.ualr.domain.ExcelTemplate;
import com.jackalhan.ualr.domain.SimplifiedWorkload;
import com.jackalhan.ualr.domain.TypeSafeRawWorkload;
import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.domain.model.WorkloadReport;
import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import com.jackalhan.ualr.domain.model.WorkloadReportValues;
import com.jackalhan.ualr.enums.InstructionTypeEnum;
import com.jackalhan.ualr.service.db.WorkloadReportDBService;
import com.sun.deploy.security.ValidationState;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.*;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * Created by jackalhan on 6/23/16.
 */
@Service
@Transactional
public class MinimumEnrollmentService extends ExcelHelperService {

    public MinimumEnrollmentService() {
        setLog(LoggerFactory.getLogger(MinimumEnrollmentService.class));
    }

    public ByteArrayOutputStream generateExcelDocument(Faculty faculty, WorkloadReportTerm workloadReportTerm, List<WorkloadReport> groupedWorkloadReports) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = workbook.createSheet(messageSource.getMessage("minimum_enrollment_report.sheet.name", new Object[]{faculty.getShortName(), workloadReportTerm.getSemesterTerm() + " " + workloadReportTerm.getSemesterYear()}, Locale.US), 0);
            ExcelTemplate excelTemplate = new ExcelTemplate();
            final int startingColumnFrame = 1;
            final int endingColumnFrame = 17;

            int startingRowNumber = 1;
            int endingRowNumber = 3;

            // Create cell font and format
            // FACULTY NAME


            excelTemplate.setSheet(sheet);
            excelTemplate.setCellFontColor(Colour.BLACK);
            excelTemplate.setLabelFontSizePropertyName("minimum_enrollment_report.title.name.fontsize");

            excelTemplate.setCellBackgroundColor(Colour.PALE_BLUE);
            excelTemplate.setCellBorderLineStyle(BorderLineStyle.THICK);
            excelTemplate.setCellHorizontalAlignment(Alignment.CENTRE);
            excelTemplate.setCellVerticalAlignment(VerticalAlignment.CENTRE);
            excelTemplate.setCellWrapped(false);
            excelTemplate.setHasCellBottomBorder(true);
            excelTemplate.setHasCellLeftBorder(true);
            excelTemplate.setHasCellRightBorder(true);
            excelTemplate.setHasCellTopBorder(true);
            excelTemplate.setStartingColumnNumber(startingColumnFrame);
            excelTemplate.setEndingColumnNumber(endingColumnFrame);
            excelTemplate.setStartingRowNumber(startingRowNumber);
            excelTemplate.setEndingRowNumber(endingRowNumber);
            excelTemplate.setLabelNameOrValue(faculty.getName());
            excelTemplate.setLabelReceivedFromProperties(false);
            excelTemplate.setValues(null);


            excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();


            // *****************************************************************************************************
            //REPORT NAME

            excelTemplate.setCellFontColor(Colour.BLACK);
            excelTemplate.setLabelFontSizePropertyName("minimum_enrollment_report.subtitle.name.fontsize");

            excelTemplate.setCellBackgroundColor(Colour.PALE_BLUE);
            excelTemplate.setCellBorderLineStyle(BorderLineStyle.THICK);
            excelTemplate.setCellHorizontalAlignment(Alignment.CENTRE);
            excelTemplate.setCellVerticalAlignment(VerticalAlignment.CENTRE);
            excelTemplate.setCellWrapped(false);
            excelTemplate.setHasCellBottomBorder(true);
            excelTemplate.setHasCellLeftBorder(true);
            excelTemplate.setHasCellRightBorder(true);
            excelTemplate.setHasCellTopBorder(false);
            excelTemplate.setStartingColumnNumber(startingColumnFrame);
            excelTemplate.setEndingColumnNumber(endingColumnFrame);
            excelTemplate.setStartingRowNumber(excelTemplate.getStartingRowNumber());
            excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber() + 2);
            excelTemplate.setLabelNameOrValue("minimum_enrollment_report.subtitle.name");
            excelTemplate.setLabelReceivedFromProperties(true);
            excelTemplate.setValues(new Object[]{workloadReportTerm.getSemesterTerm() + " - " + workloadReportTerm.getSemesterYear(), workloadReportTerm.getImportedFileDate()});

            excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

            // *****************************************************************************************************
            //COLUMNS
            int subColumnStartingColumnNumber;
            startingRowNumber = excelTemplate.getStartingRowNumber();

            for (int i = 1; i < 8; i++) {


                excelTemplate.setCellFontColor(Colour.BLACK);
                excelTemplate.setLabelFontSizePropertyName("minimum_enrollment_report.column.name.fontSize");

                excelTemplate.setCellBackgroundColor(Colour.WHITE);
                excelTemplate.setCellBorderLineStyle(BorderLineStyle.THICK);
                excelTemplate.setCellHorizontalAlignment(Alignment.CENTRE);
                excelTemplate.setCellVerticalAlignment(VerticalAlignment.CENTRE);
                excelTemplate.setCellWrapped(true);
                excelTemplate.setHasCellBottomBorder(true);
                excelTemplate.setHasCellLeftBorder(true);
                excelTemplate.setHasCellRightBorder(true);
                excelTemplate.setHasCellTopBorder(true);
                if (i == 1) {
                    //subColumnStartingColumnNumber = excelTemplate.getStartingColumnNumber();
                    excelTemplate.setStartingColumnNumber(startingColumnFrame);
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                } else if (i == 5) {
                    //subColumnStartingColumnNumber = excelTemplate.getStartingColumnNumber();
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber() + 4);
                } else if (i == 7) {
                    //subColumnStartingColumnNumber = excelTemplate.getStartingColumnNumber();
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber() + 6);
                } else {
                    //subColumnStartingColumnNumber = excelTemplate.getStartingColumnNumber();
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                }
                excelTemplate.setStartingRowNumber(startingRowNumber);
                excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber() + 5);
                excelTemplate.setLabelNameOrValue("minimum_enrollment_report.column." + i + ".name");
                excelTemplate.setLabelReceivedFromProperties(true);
                excelTemplate.setValues(null);

                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

            }

            // *****************************************************************************************************
           /*
            //DEPARTMENT HEADER
            excelTemplate.setCellFontColor(Colour.BLACK);
            excelTemplate.setLabelFontSizePropertyName("minimum_enrollment_report.department.name.fontsize");

            excelTemplate.setCellBackgroundColor(Colour.PALE_BLUE);
            excelTemplate.setCellBorderLineStyle(BorderLineStyle.THICK);
            excelTemplate.setCellHorizontalAlignment(Alignment.CENTRE);
            excelTemplate.setCellVerticalAlignment(VerticalAlignment.CENTRE);
            excelTemplate.setCellWrapped(false);
            excelTemplate.setHasCellBottomBorder(true);
            excelTemplate.setHasCellLeftBorder(true);
            excelTemplate.setHasCellRightBorder(true);
            excelTemplate.setHasCellTopBorder(true);
            excelTemplate.setStartingColumnNumber(startingColumnFrame);
            excelTemplate.setEndingColumnNumber(endingColumnFrame);
            excelTemplate.setStartingRowNumber(excelTemplate.getStartingRowNumber());
            excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber() + 1);
            excelTemplate.setLabelNameOrValue(//ALL DEPARTMENTS//);
            excelTemplate.setValues(null);
            excelTemplate.setLabelReceivedFromProperties(true);
            excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();
            //startingColumnNumber = excelTemplate.getStartingColumnNumber();
            //startingRowNumber = excelTemplate.getStartingRowNumber();
            */

            boolean doCreateHeaderForDepartment = true;
            int startingColumnNumber = 0;
            for (WorkloadReport groupedWorkloadReport : groupedWorkloadReports) {

                List<WorkloadReport> workloadReports = workloadReportDBService.listAllWorkloadReportsBasedOnTermIdAndDepartmentCodeOrderByDepartmentCode(workloadReportTerm.getId(), groupedWorkloadReport.getDepartmentCode());

                for (WorkloadReport workloadReport : workloadReports) {

                    List<WorkloadReportValues> workloadReportValuesList = workloadReportDBService.listAllWorkloadReportValuesBasedOnWorkloadReportTermId(workloadReport.getId());
                    TypeSafeRawWorkload typeSafeRawWorkload = null;

                    for (WorkloadReportValues workloadReportValues : workloadReportValuesList) {
                        startingRowNumber = excelTemplate.getStartingRowNumber();
                        boolean takeConsideration = false;
                        if (workloadReportValues.getInstructionType().contains(InstructionTypeEnum.PEDAGOGICAL.toString())) {

                            if (workloadReportValues.getCourseNumber() >= 1000 && workloadReportValues.getCourseNumber() < 3000) {
                                if (workloadReportValues.getTaEleventhDayCount() < 14) {
                                    takeConsideration = true;
                                }
                            } else if (workloadReportValues.getCourseNumber() >= 3000 && workloadReportValues.getCourseNumber() < 5000) {
                                if (workloadReportValues.getTaEleventhDayCount() < 10) {
                                    takeConsideration = true;
                                }
                            } else {
                                if (workloadReportValues.getTaEleventhDayCount() < 5) {
                                    takeConsideration = true;
                                }
                            }
                            if (takeConsideration) {
                                typeSafeRawWorkload = new TypeSafeRawWorkload();
                                typeSafeRawWorkload.setCourseTypeCode(workloadReportValues.getCourseTypeCode());
                                typeSafeRawWorkload.setCourseNumber(workloadReportValues.getCourseNumber());
                                typeSafeRawWorkload.setCourseTitle(workloadReportValues.getCourseTitle());
                                typeSafeRawWorkload.setTaEleventhDayCount(workloadReportValues.getTaEleventhDayCount());
                                typeSafeRawWorkload.setSubjectCode(workloadReportValues.getSubjectCode());


                                // *****************************************************************************************************
                                // Department Code
                                excelTemplate.setCellFontColor(Colour.BLACK);
                                excelTemplate.setLabelFontSizePropertyName("minimum_enrollment_report.column.value.fontSize");
                                excelTemplate.setCellBackgroundColor(Colour.WHITE);
                                excelTemplate.setCellBorderLineStyle(BorderLineStyle.THIN);
                                excelTemplate.setCellHorizontalAlignment(Alignment.CENTRE);
                                excelTemplate.setCellVerticalAlignment(VerticalAlignment.CENTRE);
                                excelTemplate.setCellWrapped(false);
                                excelTemplate.setHasCellBottomBorder(true);
                                excelTemplate.setHasCellLeftBorder(true);
                                excelTemplate.setHasCellRightBorder(true);
                                excelTemplate.setHasCellTopBorder(true);
                                excelTemplate.setStartingColumnNumber(startingColumnFrame);
                                excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setStartingRowNumber(startingRowNumber);
                                excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());
                                excelTemplate.setLabelNameOrValue(workloadReport.getDepartmentCode());
                                excelTemplate.setLabelReceivedFromProperties(false);
                                excelTemplate.setValues(null);

                                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();
                                // subject code
                                excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setStartingRowNumber(startingRowNumber);
                                excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                                excelTemplate.setLabelNameOrValue(typeSafeRawWorkload.getSubjectCode());
                                excelTemplate.setLabelReceivedFromProperties(false);
                                excelTemplate.setValues(null);
                                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                                // course code
                                excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setStartingRowNumber(startingRowNumber);
                                excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                                excelTemplate.setLabelNameOrValue(typeSafeRawWorkload.getCourseTypeCode());
                                excelTemplate.setLabelReceivedFromProperties(false);
                                excelTemplate.setValues(null);
                                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                                // COURSE NUMBER
                                excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setStartingRowNumber(startingRowNumber);
                                excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                                excelTemplate.setLabelNameOrValue(String.valueOf(typeSafeRawWorkload.getCourseNumber()));
                                excelTemplate.setLabelReceivedFromProperties(false);
                                excelTemplate.setValues(null);
                                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();
                                // COURSE TITTLE
                                excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber() + 4);
                                excelTemplate.setStartingRowNumber(startingRowNumber);
                                excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());
                                excelTemplate.setCellHorizontalAlignment(Alignment.LEFT);

                                excelTemplate.setLabelNameOrValue(String.valueOf(typeSafeRawWorkload.getCourseTitle()));
                                excelTemplate.setLabelReceivedFromProperties(false);
                                excelTemplate.setValues(null);
                                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                                // 11TH NUMBER ENROLLED
                                excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setStartingRowNumber(startingRowNumber);
                                excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                                excelTemplate.setLabelNameOrValue(String.valueOf(typeSafeRawWorkload.getTaEleventhDayCount()));
                                excelTemplate.setLabelReceivedFromProperties(false);
                                excelTemplate.setValues(null);
                                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                                // JUSTIFICATION
                                excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                                excelTemplate.setEndingColumnNumber(endingColumnFrame   );
                                excelTemplate.setStartingRowNumber(startingRowNumber);
                                excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());
                                excelTemplate.setCellHorizontalAlignment(Alignment.LEFT);

                                excelTemplate.setLabelNameOrValue("");
                                excelTemplate.setLabelReceivedFromProperties(false);
                                excelTemplate.setValues(null);
                                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();
                            }
                        }
                    }
                }

            }


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
            //sheetSettings.setPrintArea(startingColumnFrame, startingColumnFrame, endingColumnFrame, excelTemplate.getEndingRowNumber());

            workbook.write();

            //Close and free allocated memory
            workbook.close();


            // burada fillCOntentExcel iterative bir ssekilde cagrilabilir. DUsunelecek.
        } catch (
                Exception ex
                )

        {
            getLog().error(" generateExcelDocument has errors " + ex.toString());
        }

        return outputStream;
    }
}
