package com.jackalhan.ualr.domain;

import jxl.format.*;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

import java.io.Serializable;

/**
 * Created by jackalhan on 6/17/16.
 */
public class ExcelTemplate implements Serializable, Cloneable{

    private WritableFont cellFont;
    private WritableCellFormat writableCellFormat;
    private WritableSheet sheet;
    private int startingColumnNumber;
    private int endingColumnNumber;
    private int startingRowNumber;
    private int endingRowNumber;

    private String labelFontSizePropertyName;
    private String labelNameOrValue;
    private boolean isLabelReceivedFromProperties;
    private Object[] values;

    private Colour cellFontColor;
    private Colour cellBackgroundColor;
    private BorderLineStyle cellBorderLineStyle;
    private VerticalAlignment cellVerticalAlignment;
    private Alignment cellHorizontalAlignment;
    private boolean isCellWrapped;
    private boolean hasCellTopBorder;
    private boolean hasCellLeftBorder;
    private boolean hasCellRightBorder;
    private boolean hasCellBottomBorder;


    public ExcelTemplate() {
    }

    public boolean isLabelReceivedFromProperties() {
        return isLabelReceivedFromProperties;
    }

    public void setLabelReceivedFromProperties(boolean labelReceivedFromProperties) {
        isLabelReceivedFromProperties = labelReceivedFromProperties;
    }

    public Colour getCellFontColor() {
        return cellFontColor;
    }

    public void setCellFontColor(Colour cellFontColor) {
        this.cellFontColor = cellFontColor;
    }

    public Colour getCellBackgroundColor() {
        return cellBackgroundColor;
    }

    public void setCellBackgroundColor(Colour cellBackgroundColor) {
        this.cellBackgroundColor = cellBackgroundColor;
    }

    public BorderLineStyle getCellBorderLineStyle() {
        return cellBorderLineStyle;
    }

    public void setCellBorderLineStyle(BorderLineStyle cellBorderLineStyle) {
        this.cellBorderLineStyle = cellBorderLineStyle;
    }

    public VerticalAlignment getCellVerticalAlignment() {
        return cellVerticalAlignment;
    }

    public void setCellVerticalAlignment(VerticalAlignment cellVerticalAlignment) {
        this.cellVerticalAlignment = cellVerticalAlignment;
    }

    public Alignment getCellHorizontalAlignment() {
        return cellHorizontalAlignment;
    }

    public void setCellHorizontalAlignment(Alignment cellHorizontalAlignment) {
        this.cellHorizontalAlignment = cellHorizontalAlignment;
    }

    public boolean isCellWrapped() {
        return isCellWrapped;
    }

    public void setCellWrapped(boolean cellWrapped) {
        isCellWrapped = cellWrapped;
    }

    public boolean isHasCellTopBorder() {
        return hasCellTopBorder;
    }

    public void setHasCellTopBorder(boolean hasCellTopBorder) {
        this.hasCellTopBorder = hasCellTopBorder;
    }

    public boolean isHasCellLeftBorder() {
        return hasCellLeftBorder;
    }

    public void setHasCellLeftBorder(boolean hasCellLeftBorder) {
        this.hasCellLeftBorder = hasCellLeftBorder;
    }

    public boolean isHasCellRightBorder() {
        return hasCellRightBorder;
    }

    public void setHasCellRightBorder(boolean hasCellRightBorder) {
        this.hasCellRightBorder = hasCellRightBorder;
    }

    public boolean isHasCellBottomBorder() {
        return hasCellBottomBorder;
    }

    public void setHasCellBottomBorder(boolean hasCellBottomBorder) {
        this.hasCellBottomBorder = hasCellBottomBorder;
    }

    public String getLabelFontSizePropertyName() {
        return labelFontSizePropertyName;
    }

    public void setLabelFontSizePropertyName(String labelFontSizePropertyName) {
        this.labelFontSizePropertyName = labelFontSizePropertyName;
    }

    public String getLabelNameOrValue() {
        return labelNameOrValue;
    }

    public void setLabelNameOrValue(String labelNameOrValue) {
        this.labelNameOrValue = labelNameOrValue;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public WritableFont getCellFont() {
        return cellFont;
    }

    public void setCellFont(WritableFont cellFont) {
        this.cellFont = cellFont;
    }

    public WritableCellFormat getWritableCellFormat() {
        return writableCellFormat;
    }

    public void setWritableCellFormat(WritableCellFormat writableCellFormat) {
        this.writableCellFormat = writableCellFormat;
    }

   public WritableSheet getSheet() {
        return sheet;
    }

    public void setSheet(WritableSheet sheet) {
        this.sheet = sheet;
    }

    public int getStartingColumnNumber() {
        return startingColumnNumber;
    }

    public void setStartingColumnNumber(int startingColumnNumber) {
        this.startingColumnNumber = startingColumnNumber;
    }

    public int getEndingColumnNumber() {
        return endingColumnNumber;
    }

    public void setEndingColumnNumber(int endingColumnNumber) {
        this.endingColumnNumber = endingColumnNumber;
    }

    public int getStartingRowNumber() {
        return startingRowNumber;
    }

    public void setStartingRowNumber(int startingRowNumber) {
        this.startingRowNumber = startingRowNumber;
    }

    public int getEndingRowNumber() {
        return endingRowNumber;
    }

    public void setEndingRowNumber(int endingRowNumber) {
        this.endingRowNumber = endingRowNumber;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
