package com.jackalhan.ualr.service.utils;

import com.jackalhan.ualr.domain.ExcelTemplate;
import com.jackalhan.ualr.service.LogService;
import com.jackalhan.ualr.service.db.WorkloadReportDBService;
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
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by jackalhan on 6/16/16.
 */
@Service
public class ExcelHelperService extends LogService {


    @Autowired
    public WorkloadReportDBService workloadReportDBService;

    public final int excelZoomFactor = 85;
    public final int excelScaleFactor = 50;
    public final double excelLeftMargin = 0.75;
    public final double excelRightMargin = 0.75;
    public final double excelTopMargin = 0.30;
    public final double excelBottomMargin = 0.33;


    @Autowired
    public MessageSource messageSource;

    public ExcelHelperService() {
        setLog(LoggerFactory.getLogger(ExcelHelperService.class));
    }


    public WritableFont createCellFont(String propertyNameForFontSize, Colour color, boolean isBold) throws WriteException {
        WritableFont cellFont = new WritableFont(WritableFont.ARIAL,
                Integer.valueOf(messageSource.getMessage(propertyNameForFontSize, null, Locale.US)),
                (isBold) ? WritableFont.BOLD : WritableFont.NO_BOLD);
        cellFont.setColour(color);
        return cellFont;
    }

    public WritableCellFormat createCellFormat(WritableFont cellFont, Colour backroundColor, BorderLineStyle borderStyle, boolean isWrapped, boolean hasTopBorder, boolean hasLeftBorder, boolean hasRightBorder, boolean hasBottomBorder) throws WriteException {
        return createCellFormat(cellFont, backroundColor, VerticalAlignment.CENTRE, Alignment.CENTRE, borderStyle, isWrapped, hasTopBorder, hasLeftBorder, hasRightBorder, hasBottomBorder);
    }

    public WritableCellFormat createCellFormat(WritableFont cellFont, Colour backroundColor, VerticalAlignment verticalAlignment, Alignment horizontalAlignment, BorderLineStyle borderStyle, boolean isWrapped, boolean hasTopBorder, boolean hasLeftBorder, boolean hasRightBorder, boolean hasBottomBorder) throws WriteException {
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

    public void createText(WritableSheet sheet, String propertyName, Object[] appendedTextValues, WritableCellFormat cell, int startingColIndex, int startingRowIndex) throws WriteException {
        Label label = new Label(startingColIndex, startingRowIndex,
                messageSource.getMessage(propertyName, appendedTextValues, Locale.US), cell);
        sheet.addCell(label);
    }

    public void createText(WritableSheet sheet, String textValue, WritableCellFormat cell, int startingColIndex, int startingRowIndex) throws WriteException {
        Label label = new Label(startingColIndex, startingRowIndex,
                textValue, cell);
        sheet.addCell(label);
    }

    public ExcelTemplate createPartsInExcel(ExcelTemplate excelTemplate) {



        try {
            excelTemplate.setCellFont(createCellFont(excelTemplate.getLabelFontSizePropertyName(), excelTemplate.getCellFontColor(), true));
            excelTemplate.setWritableCellFormat(createCellFormat(
                    excelTemplate.getCellFont(),
                    excelTemplate.getCellBackgroundColor(),
                    excelTemplate.getCellVerticalAlignment(),
                    excelTemplate.getCellHorizontalAlignment(),
                    excelTemplate.getCellBorderLineStyle(),
                    excelTemplate.isCellWrapped(),
                    excelTemplate.isHasCellTopBorder(),
                    excelTemplate.isHasCellLeftBorder(),
                    excelTemplate.isHasCellRightBorder(),
                    excelTemplate.isHasCellBottomBorder()));
            excelTemplate.getSheet().mergeCells(excelTemplate.getStartingColumnNumber(), excelTemplate.getStartingRowNumber(), excelTemplate.getEndingColumnNumber(), excelTemplate.getEndingRowNumber());

            if (!excelTemplate.isLabelReceivedFromProperties())
            {
                createText(excelTemplate.getSheet(), excelTemplate.getLabelNameOrValue(), null, excelTemplate.getWritableCellFormat(), excelTemplate.getStartingColumnNumber(), excelTemplate.getStartingRowNumber());
            }
            else
            {
                createText(excelTemplate.getSheet(), excelTemplate.getLabelNameOrValue(), excelTemplate.getValues(), excelTemplate.getWritableCellFormat(), excelTemplate.getStartingColumnNumber(), excelTemplate.getStartingRowNumber());

            }
            excelTemplate.setStartingColumnNumber(excelTemplate.getEndingColumnNumber() + 1);
            excelTemplate.setStartingRowNumber(excelTemplate.getEndingRowNumber() + 1);

        }
        catch (Exception ex)
        {
            getLog().error(" createPartsInExcel has errors " + ex.toString());
        }
        return excelTemplate;
    }


}
