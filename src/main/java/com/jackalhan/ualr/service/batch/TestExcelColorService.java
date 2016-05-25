package com.jackalhan.ualr.service.batch;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.constant.SchedulingConstant;
import com.jackalhan.ualr.domain.NewColour;
import com.jackalhan.ualr.service.utils.FileUtilService;
import com.jackalhan.ualr.service.utils.StringUtilService;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by txcakaloglu on 5/25/16.
 */
@Component
public class TestExcelColorService {

    @Scheduled(fixedDelay = SchedulingConstant.TEST_SERVICE_EXECUTE_FIXED_DELAY)
    private void executeService() throws IOException, WriteException {
        //FileUtilService.getInstance().createDirectory(GenericConstant.WORKLOAD_REPORTS_RAWTXT_TEMP_PATH);
        System.out.println("**********************************************************************************");
        System.out.println("**********************************************************************************");
        System.out.println("**********************************************************************************");
        System.out.println("**********************************************************************************");
        System.out.println("**********************************************************************************");


        File file = new File(GenericConstant.WORKLOAD_REPORTS_RAWTXT_TEMP_PATH + "Colors.xls");
        int startingColumnFrame = 1;
        WritableWorkbook workbook = Workbook.createWorkbook(file);
        WritableSheet sheet = workbook.createSheet("Colors", 0);
        NewColour newColour = null;
        int rowNumber = 0;
        int blockRowCounter = 0;
        int k = 0;
        int row = 0;
        for (int r = 0 ; r<=250 ; r++)
        {
            for (int g = 0 ; g<=250 ; g++)
            {
                for (int b = 0 ; b<=250 ; b++)
                {
                    if(rowNumber % 60000 == 0)
                    {
                        k++;
                        workbook.createSheet("Colors" + k , k);
                    }
                    sheet = workbook.getSheet(k);
                    row = rowNumber % 60000;
                    String colorText = "r: " + r + " g: " + g + " b: " + b;
                    newColour = new NewColour(rowNumber, colorText, r, g, b);
                    WritableFont cellFont = createCellFont("", Colour.WHITE, true);
                    WritableCellFormat cellFormat = createCellFormat(cellFont, newColour);
                    sheet.mergeCells(startingColumnFrame,row , startingColumnFrame, row);
                    createText(sheet, colorText, cellFormat, startingColumnFrame, row);
                    rowNumber++;
                    //System.out.println(colorText);


                }
            }
        }




        workbook.write();

        //Close and free allocated memory
        workbook.close();

        System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
    }

    private WritableFont createCellFont(String propertyNameForFontSize, Colour color, boolean isBold) throws WriteException {
        WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
        cellFont.setColour(color);
        return cellFont;
    }

    private WritableCellFormat createCellFormat(WritableFont cellFont, Colour backroundColor) throws WriteException {
        WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
        cellFormat.setBackground(backroundColor);
        return cellFormat;
    }

    private void createText(WritableSheet sheet, String textValue, WritableCellFormat cell, int startingColIndex, int startingRowIndex) throws WriteException {
        Label label = new Label(startingColIndex, startingRowIndex,
                textValue, cell);
        sheet.addCell(label);
    }
}
