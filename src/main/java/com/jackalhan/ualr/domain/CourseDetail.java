package com.jackalhan.ualr.domain;

import com.jackalhan.ualr.service.utils.StringUtilService;

/**
 * Created by jackalhan on 4/26/16.
 */
public class CourseDetail implements Cloneable {

    public CourseDetail() {
    }

    private int codeNumber;
    private String instructionType;
    private String titleName;
    private String section;
    private String taName;
    private String subjectCode;
    private String courseTypeName;
    private String courseTypeCode;
    private double lectureHours;
    private boolean hasDualCourse;
    private int dualCourseCode;
    private double numberOfTotalEnrollmentInDualCourse;
    private double iuMultiplierLectureHours;
    private double iuMultiplicationResultOfLectureHours;
    private double numberOfTotaltotalSsch;

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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
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

    public double getNumberOfTotalEnrollmentInDualCourse() {
        return numberOfTotalEnrollmentInDualCourse;
    }

    public void setNumberOfTotalEnrollmentInDualCourse(double numberOfTotalEnrollmentInDualCourse) {
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

    public double getNumberOfTotaltotalSsch() {
        return numberOfTotaltotalSsch;
    }

    public void setNumberOfTotaltotalSsch(double numberOfTotaltotalSsch) {
        this.numberOfTotaltotalSsch = numberOfTotaltotalSsch;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public double getLectureHours() {
        return lectureHours;
    }

    public void setLectureHours(double lectureHours) {
        this.lectureHours = lectureHours;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    @Override
    public String toString() {
        return "CourseDetail{" +
                "codeNumber=" + codeNumber +
                ", instructionType='" + instructionType + '\'' +
                ", titleName='" + titleName + '\'' +
                ", section='" + section + '\'' +
                ", taName='" + taName + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", courseTypeName='" + courseTypeName + '\'' +
                ", courseTypeCode='" + courseTypeCode + '\'' +
                ", lectureHours=" + lectureHours +
                ", hasDualCourse=" + hasDualCourse +
                ", dualCourseCode=" + dualCourseCode +
                ", numberOfTotalEnrollmentInDualCourse=" + numberOfTotalEnrollmentInDualCourse +
                ", iuMultiplierLectureHours=" + iuMultiplierLectureHours +
                ", iuMultiplicationResultOfLectureHours=" + iuMultiplicationResultOfLectureHours +
                ", numberOfTotaltotalSsch=" + numberOfTotaltotalSsch +
                '}';
    }
}
