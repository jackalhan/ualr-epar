package com.jackalhan.ualr.service.utils;

import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.domain.model.WorkloadReport;
import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import com.jackalhan.ualr.domain.model.WorkloadReportValues;
import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;

/**
 * Created by jackalhan on 6/16/16.
 */
public class SummarizeReportService extends ExcelHelperService {


    public SummarizeReportService() {
        setLog(LoggerFactory.getLogger(SummarizeReportService.class));
    }

    public ByteArrayOutputStream generateExcelDocument (Faculty faculty, WorkloadReportTerm workloadReportTerm)
    {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = workbook.createSheet(faculty.getShortName() + " - " + messageSource.getMessage("summary_reports.subtitle.name", new Object[]{workloadReportTerm.getSemesterTerm() + " - " + workloadReportTerm.getSemesterYear(), workloadReportTerm.getImportedFileDate()}, Locale.US), 0);

            int startingColumnFrame = 1;
            int endingColumnFrame = 25;

            // Create cell font and format
            // FACULTY NAME
            WritableFont cellFont = createCellFont("summary_reports.title.name.fontsize", Colour.BLACK, true);
            WritableCellFormat cellFormat = createCellFormat(cellFont, Colour.PALE_BLUE, BorderLineStyle.THICK, false, true, true, true, true);
            sheet.mergeCells(startingColumnFrame, 1, endingColumnFrame, 3);
            createText(sheet, faculty.getName(), null, cellFormat, startingColumnFrame, 1);

            // *****************************************************************************************************
            //REPORT NAME

            cellFont = createCellFont("summary_reports.subtitle.name.fontsize", Colour.BLACK, true);
            cellFormat = createCellFormat(cellFont, Colour.PALE_BLUE, BorderLineStyle.THICK, false, false, true, true, false);
            sheet.mergeCells(startingColumnFrame, 4, endingColumnFrame, 5);
            createText(sheet, "summary_reports.subtitle.name", new Object[]{workloadReportTerm.getSemesterTerm() + " - " + workloadReportTerm.getSemesterYear(), workloadReportTerm.getImportedFileDate()}, cellFormat, startingColumnFrame, 4);

            // burada fillCOntentExcel iterative bir ssekilde cagrilabilir. DUsunelecek.

        }
        catch (Exception ex)
        {
            getLog().error(" generateExcelDocument has errors " + ex.toString());
        }


    }
    public WritableSheet fillContentToExcelSheet(WritableSheet originSheet, WorkloadReport workloadReport)
    {
        WritableSheet sheet = null;
        try
        {

            List<WorkloadReportValues> workloadReportValuesList = workloadReportDBService.listAllWorkloadReportValuesBasedOnWorkloadReportTermId(workloadReport.getId());

            for (WorkloadReportValues workloadReportValues : workloadReportValuesList)
            {

            }

        }
        catch (Exception ex)
        {
            log.error("Error during generation generatePartAsAnExcel " + ex.toString());

        }

        return sheet;

    }
}
