package com.jackalhan.ualr.service;

import jxl.Workbook;
import jxl.format.*;
import jxl.format.Alignment;
import jxl.format.BoldStyle;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by jackalhan on 4/18/16.
 */

@Component
public class ExcelGenerationService {

    @Autowired
    private MessageSource messageSource;

    private final Logger log = LoggerFactory.getLogger(ExcelGenerationService.class);


    @Scheduled(fixedDelay = 5000)//Everyday 12 pm
    private void generateExcelContent() throws IOException, WriteException {
        log.info("Started");
        System.out.println("Started");

        //Creates a writable workbook with the given file name
        WritableWorkbook workbook = Workbook.createWorkbook(new File("MergeCells.xls"));
        WritableSheet sheet = workbook.createSheet("InstructorNameSurname-Term", 0);

        // Create cell font and format
        // REPORT HEADER
        WritableFont cellFont = new WritableFont(WritableFont.ARIAL,
                Integer.valueOf(messageSource.getMessage("workloadReport.faculty.name.fontsize",null, Locale.US)),
                WritableFont.BOLD);
        cellFont.setColour(Colour.BLACK);
        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setBackground(Colour.GRAY_25);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormat.setBorder(Border.TOP, BorderLineStyle.THICK);
        cellFormat.setBorder(Border.LEFT, BorderLineStyle.THICK);
        cellFormat.setBorder(Border.RIGHT, BorderLineStyle.THICK);
        cellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
        // mergeCells(colStart, rowStart, colEnd, rowEnd)
        // new Label(col, row)
        sheet.mergeCells(1, 1, 20, 3);
        Label label = new Label(1, 1,
                messageSource.getMessage("workloadReport.faculty.name", null, Locale.US), cellFormat);
        sheet.addCell(label);
        // *****************************************************************************************************
        //REPORT NAME
        cellFont = new WritableFont(WritableFont.ARIAL,
                Integer.valueOf(messageSource.getMessage("workloadReport.report.name.fontsize",null, Locale.US)),
                WritableFont.BOLD);
        cellFont.setColour(Colour.BLACK);
        cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setBackground(Colour.WHITE);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormat.setBorder(Border.LEFT, BorderLineStyle.THICK);
        cellFormat.setBorder(Border.RIGHT, BorderLineStyle.THICK);
        cellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
        sheet.mergeCells(1, 4, 20, 5);
        label = new Label(1, 4,
                messageSource.getMessage("workloadReport.report.name", null, Locale.US), cellFormat);
        sheet.addCell(label);
        // *****************************************************************************************************
        //DEPARTMENT HEADER
        cellFont = new WritableFont(WritableFont.ARIAL,
                Integer.valueOf(messageSource.getMessage("workloadReport.department.name.fontsize",null, Locale.US)));
        cellFont.setColour(Colour.BLACK);
        cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setBackground(Colour.WHITE);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
        cellFormat.setBorder(Border.LEFT, BorderLineStyle.THICK);
        cellFormat.setBorder(Border.RIGHT, BorderLineStyle.THICK);
        cellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
        label = new Label(1, 6,
                messageSource.getMessage("workloadReport.department.name",
                        new Object[] { "System Engineering","Spring", "2016" }, // <================ REPLACE WITH REAL VALUES
                        Locale.US), cellFormat);
        sheet.addCell(label);
        sheet.mergeCells(1, 6, 20, 7);


        //Writes out the data held in this workbook in Excel format
        workbook.write();

        //Close and free allocated memory
        workbook.close();

    }



}
