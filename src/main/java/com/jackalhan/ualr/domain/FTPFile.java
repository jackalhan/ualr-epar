package com.jackalhan.ualr.domain;



import java.util.Date;

/**
 * Created by jackalhan on 6/8/16.
 */
public class FTPFile {

    private String fileName;
    private String fileUploadedDate;
    private int fileUploadedDateAsInt;
    private String fileUploadedDateAsOriginal;

    public FTPFile() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUploadedDate() {
        return fileUploadedDate;
    }

    public void setFileUploadedDate(String fileUploadedDate) {
        this.fileUploadedDate = fileUploadedDate;
    }

    public int getFileUploadedDateAsInt() {
        return fileUploadedDateAsInt;
    }

    public void setFileUploadedDateAsInt(int fileUploadedDateAsInt) {
        this.fileUploadedDateAsInt = fileUploadedDateAsInt;
    }

    public String getFileUploadedDateAsOriginal() {
        return fileUploadedDateAsOriginal;
    }

    public void setFileUploadedDateAsOriginal(String fileUploadedDateAsOriginal) {
        this.fileUploadedDateAsOriginal = fileUploadedDateAsOriginal;
    }

    @Override
    public String toString() {
        return "FTPFile{" +
                "fileName='" + fileName + '\'' +
                ", fileUploadedDate='" + fileUploadedDate + '\'' +
                ", fileUploadedDateAsInt=" + fileUploadedDateAsInt +
                ", fileUploadedDateAsOriginal=" + fileUploadedDateAsOriginal +
                '}';
    }
}
