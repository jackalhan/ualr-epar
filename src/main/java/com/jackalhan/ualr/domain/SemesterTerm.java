package com.jackalhan.ualr.domain;

/**
 * Created by jackalhan on 5/15/16.
 */
public class SemesterTerm {

    private int semesterTermCode;
    private String semesterTermName;
    private int year;

    public SemesterTerm() {
    }

    public SemesterTerm(int semesterTermCode, String semesterTermName, int year) {
        this.semesterTermCode = semesterTermCode;
        this.semesterTermName = semesterTermName;
        this.year = year;
    }

    public int getSemesterTermCode() {
        return semesterTermCode;
    }

    public void setSemesterTermCode(int semesterTermCode) {
        this.semesterTermCode = semesterTermCode;
    }

    public String getSemesterTermName() {
        return semesterTermName;
    }

    public void setSemesterTermName(String semesterTermName) {
        this.semesterTermName = semesterTermName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "SemesterTerm{" +
                "semesterTermCode=" + semesterTermCode +
                ", semesterTermName='" + semesterTermName + '\'' +
                ", year=" + year +
                '}';
    }
}
