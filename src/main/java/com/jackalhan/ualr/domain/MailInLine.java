package com.jackalhan.ualr.domain;

/**
 * Created by jackalhan on 5/14/16.
 */
public class MailInLine {
    private String fileName;
    private String fileLocation;
    private String fileType;


    public MailInLine(String fileName, String fileLocation, String fileType) {
        this.fileName = fileName;
        this.fileLocation = fileLocation;
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return "MailInLine{" +
                "fileName='" + fileName + '\'' +
                ", fileLocation='" + fileLocation + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
