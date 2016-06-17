package com.jackalhan.ualr.service.utils;

import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.domain.model.WorkloadReport;
import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import com.jackalhan.ualr.domain.model.WorkloadReportValues;
import jxl.Workbook;
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
            WritableSheet sheet = workbook.createSheet(faculty.getShortName() + messageSource.getMessage(propertyName, appendedTextValues, Locale.US)+ , 0); // Buranin degeri property den okunacak.
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
