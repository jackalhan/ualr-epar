package com.jackalhan.ualr.service.utils;

import com.jackalhan.ualr.domain.ExcelTemplate;
import com.jackalhan.ualr.domain.SimplifiedWorkload;
import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.domain.model.WorkloadReport;
import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import com.jackalhan.ualr.domain.model.WorkloadReportValues;
import com.jackalhan.ualr.enums.InstructionTypeEnum;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.*;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * Created by jackalhan on 6/16/16.
 */
@Service
@Transactional
public class SummarizeReportService extends ExcelHelperService {


    public SummarizeReportService() {
        setLog(LoggerFactory.getLogger(SummarizeReportService.class));
    }

    public ByteArrayOutputStream generateExcelDocument(Faculty faculty, WorkloadReportTerm workloadReportTerm, List<WorkloadReport> groupedWorkloadReports) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = workbook.createSheet(messageSource.getMessage("summary_report.sheet.name", new Object[]{faculty.getShortName(), workloadReportTerm.getSemesterTerm() + " " + workloadReportTerm.getSemesterYear()}, Locale.US), 0);
            ExcelTemplate excelTemplate = new ExcelTemplate();
            final int startingColumnFrame = 1;
            final int endingColumnFrame = 15;

            int startingRowNumber = 1;
            int endingRowNumber = 3;

            // Create cell font and format
            // FACULTY NAME


            excelTemplate.setSheet(sheet);
            excelTemplate.setCellFontColor(Colour.BLACK);
            excelTemplate.setLabelFontSizePropertyName("summary_reports.title.name.fontsize");

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
            excelTemplate.setLabelFontSizePropertyName("summary_reports.subtitle.name.fontsize");

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
            excelTemplate.setLabelNameOrValue("summary_reports.subtitle.name");
            excelTemplate.setLabelReceivedFromProperties(true);
            excelTemplate.setValues(new Object[]{workloadReportTerm.getSemesterTerm() + " - " + workloadReportTerm.getSemesterYear(), workloadReportTerm.getImportedFileDate()});

            excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

            // *****************************************************************************************************
            //COLUMNS
            int subColumnStartingColumnNumber;
            startingRowNumber = excelTemplate.getStartingRowNumber();

            for (int i = 1; i < 12; i++) {

                excelTemplate.setCellFontColor(Colour.BLACK);
                excelTemplate.setLabelFontSizePropertyName("summary_reports.column.name.fontSize");

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
                    excelTemplate.setStartingColumnNumber(startingColumnFrame);
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber() + 4);
                } else {
                    subColumnStartingColumnNumber = excelTemplate.getStartingColumnNumber();
                    excelTemplate.setStartingColumnNumber(subColumnStartingColumnNumber);
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                }
                excelTemplate.setStartingRowNumber(startingRowNumber);
                excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber() + 5);
                excelTemplate.setLabelNameOrValue("summary_reports.column." + i + ".name");
                excelTemplate.setLabelReceivedFromProperties(true);
                excelTemplate.setValues(null);

                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

            }

            boolean doCreateHeaderForDepartment = true;
            int startingColumnNumber = 0;
            for (WorkloadReport groupedWorkloadReport : groupedWorkloadReports) {

                // *****************************************************************************************************
                //DEPARTMENT HEADER
                excelTemplate.setCellFontColor(Colour.BLACK);
                excelTemplate.setLabelFontSizePropertyName("summary_reports.department.name.fontsize");

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
                excelTemplate.setLabelNameOrValue(groupedWorkloadReport.getDepartmentName());
                excelTemplate.setValues(null);
                excelTemplate.setLabelReceivedFromProperties(false);
                excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();
                //startingColumnNumber = excelTemplate.getStartingColumnNumber();
                //startingRowNumber = excelTemplate.getStartingRowNumber();

                List<WorkloadReport> workloadReports = workloadReportDBService.listAllWorkloadReportsBasedOnTermIdAndDepartmentCode(workloadReportTerm.getId(), groupedWorkloadReport.getDepartmentCode());

                for (WorkloadReport workloadReport : workloadReports) {

                    List<WorkloadReportValues> workloadReportValuesList = workloadReportDBService.listAllWorkloadReportValuesBasedOnWorkloadReportTermId(workloadReport.getId());
                    SimplifiedWorkload simplifiedWorkload = new SimplifiedWorkload();
                    double individualInstructionHours = 0;
                    double administrativeTotal = 0, nonadministrativeTotal = 0;
                    startingRowNumber = excelTemplate.getStartingRowNumber();
                    for (WorkloadReportValues workloadReportValues : workloadReportValuesList) {

                        simplifiedWorkload.setTotalTotalIUs(simplifiedWorkload.getTotalTotalIUs() + workloadReportValues.getTotalIus());

                        if (workloadReportValues.getInstructionType().contains(InstructionTypeEnum.PEDAGOGICAL.toString())) {
                            simplifiedWorkload.setTotalTaSupport(simplifiedWorkload.getTotalTaSupport() + workloadReportValues.getTaSupport());
                            simplifiedWorkload.setTotal11thDayCount(simplifiedWorkload.getTotal11thDayCount() + workloadReportValues.getTaEleventhDayCount());
                            simplifiedWorkload.setTotalCreditHours(simplifiedWorkload.getTotalCreditHours() + workloadReportValues.getTaCeditHours());
                            simplifiedWorkload.setTotalLectureHours(simplifiedWorkload.getTotalLectureHours() + workloadReportValues.getTaLectureHours());
                            simplifiedWorkload.setTotalLabHours(simplifiedWorkload.getTotalLabHours() + workloadReportValues.getTaLabHours());
                            simplifiedWorkload.setTotalSsch(simplifiedWorkload.getTotalSsch() + workloadReportValues.getTotalSsch());
                        } else if (workloadReportValues.getInstructionType().contains(InstructionTypeEnum.INDIVIDUALIZED.toString())) {
                            individualInstructionHours = individualInstructionHours + workloadReportValues.getTaEleventhDayCount();
                            simplifiedWorkload.setTotalSsch(simplifiedWorkload.getTotalSsch() + workloadReportValues.getTotalSsch());
                        } else if (workloadReportValues.getInstructionType().contains(InstructionTypeEnum.ADMINISTRATIVE.toString())) {
                            administrativeTotal = 0;
                        } else if (workloadReportValues.getInstructionType().contains(InstructionTypeEnum.NONADMINISTRATIVE.toString())) {
                            nonadministrativeTotal = 0;
                        }
                    }


                    // *****************************************************************************************************
                    // INSTRUCTOR
                    excelTemplate.setCellFontColor(Colour.BLACK);
                    excelTemplate.setLabelFontSizePropertyName("summary_reports.column.value.fontSize");
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
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber() + 4);
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());
                    excelTemplate.setLabelNameOrValue(workloadReport.getInstructorNameSurname());
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);

                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();
                    // TA SUPPORT
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(simplifiedWorkload.getTotalTaSupport()));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();
                    // 11TH DAY
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(simplifiedWorkload.getTotal11thDayCount()));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                    // CREDIT HOURS
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(simplifiedWorkload.getTotalCreditHours()));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                    // LECTURE HOURS
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(simplifiedWorkload.getTotalLectureHours()));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                    // LAB HOURS
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(simplifiedWorkload.getTotalLabHours()));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                    // INDV INST HOURS
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(individualInstructionHours));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                    // ADMIN TOTAL
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(administrativeTotal));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                    // NON-ADMIN TOTAL
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(nonadministrativeTotal));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                    // TOTAL IU
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(simplifiedWorkload.getTotalTotalIUs()));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();

                    // TOTAL SSCH
                    excelTemplate.setStartingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setEndingColumnNumber(excelTemplate.getStartingColumnNumber());
                    excelTemplate.setStartingRowNumber(startingRowNumber);
                    excelTemplate.setEndingRowNumber(excelTemplate.getStartingRowNumber());

                    excelTemplate.setLabelNameOrValue(String.valueOf(simplifiedWorkload.getTotalSsch()));
                    excelTemplate.setLabelReceivedFromProperties(false);
                    excelTemplate.setValues(null);
                    excelTemplate = (ExcelTemplate) createPartsInExcel(excelTemplate).clone();


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
