package com.jackalhan.ualr.domain;

/**
 * Created by jackalhan on 4/26/16.
 */
public class CourseDetail implements Cloneable {

    public CourseDetail() {
    }

    private int codeNumber;
    private String instructionType;
    private String titleName;
    private int section;
    private String taName;
    private String courseTypeName;
    private String courseTypeCode;
    private boolean hasDualCourse;
    private int dualCourseCode;
    private int numberOfTotalEnrollmentInDualCourse;
    private double iuMultiplierLectureHours;
    private double iuMultiplicationResultOfLectureHours;
    private int totalSsch;

    public int getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(int codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName.trim();
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getTaName() {
        return taName;
    }

    public void setTaName(String taName) {
        this.taName = taName.trim();
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName.trim();
    }

    public String getCourseTypeCode() {
        return courseTypeCode;
    }

    public void setCourseTypeCode(String courseTypeCode) {
        this.courseTypeCode = courseTypeCode.trim();
    }

    public boolean isHasDualCourse() {
        return hasDualCourse;
    }

    public void setHasDualCourse(boolean hasDualCourse) {
        this.hasDualCourse = hasDualCourse;
    }

    public int getDualCourseCode() {
        return dualCourseCode;
    }

    public void setDualCourseCode(int dualCourseCode) {
        this.dualCourseCode = dualCourseCode;
    }

    public int getNumberOfTotalEnrollmentInDualCourse() {
        return numberOfTotalEnrollmentInDualCourse;
    }

    public void setNumberOfTotalEnrollmentInDualCourse(int numberOfTotalEnrollmentInDualCourse) {
        this.numberOfTotalEnrollmentInDualCourse = numberOfTotalEnrollmentInDualCourse;
    }

    public double getIuMultiplierLectureHours() {
        return iuMultiplierLectureHours;
    }

    public void setIuMultiplierLectureHours(double iuMultiplierLectureHours) {
        this.iuMultiplierLectureHours = iuMultiplierLectureHours;
    }

    public double getIuMultiplicationResultOfLectureHours() {
        return iuMultiplicationResultOfLectureHours;
    }

    public void setIuMultiplicationResultOfLectureHours(double iuMultiplicationResultOfLectureHours) {
        this.iuMultiplicationResultOfLectureHours = iuMultiplicationResultOfLectureHours;
    }

    public int getTotalSsch() {
        return totalSsch;
    }

    public void setTotalSsch(int totalSsch) {
        this.totalSsch = totalSsch;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    @Override
    public String toString() {
        return "CourseDetail{" +
                "codeNumber=" + codeNumber +
                ", instructionType='" + instructionType + '\'' +
                ", titleName='" + titleName + '\'' +
                ", section=" + section +
                ", taName='" + taName + '\'' +
                ", courseTypeName='" + courseTypeName + '\'' +
                ", courseTypeCode='" + courseTypeCode + '\'' +
                ", hasDualCourse=" + hasDualCourse +
                ", dualCourseCode=" + dualCourseCode +
                ", numberOfTotalEnrollmentInDualCourse=" + numberOfTotalEnrollmentInDualCourse +
                ", iuMultiplierLectureHours=" + iuMultiplierLectureHours +
                ", iuMultiplicationResultOfLectureHours=" + iuMultiplicationResultOfLectureHours +
                ", totalSsch=" + totalSsch +
                '}';
    }
}
