package com.jackalhan.ualr.domain;

import java.io.Serializable;

/**
 * Created by jackalhan on 4/18/16.
 */
public class TypeSafeRawWorkload implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private String instructionType;
    private String instructorTNumber;
    private String instructorNameSurname;
    private String instructorDepartment;
    private String instructorDepartmentCode;
    private int semesterTermCode;
    private int crn;
    private String subjectCode;
    private String courseTypeName; // NEEDS TO BE CALCULATED : Undergraduate Course =========> ?
    private String courseTypeCode; // NEEDS TO BE CALCULATED : DL  =========> ?
    private int courseNumber;
    private String section;
    private int tst; // ?
    private String courseTitle;
    private String collCode;
    private String taStudent;
    private double taSupport;
    private double taEleventhDayCount;
    private double taCeditHours;
    private double taLectureHours;
    private double iuMultipliertaLectureHours;
    private double taLabHours;
    private double iuMultipliertaLabHours;
    private double totalIus;
    private String otherInstructorsInTeam;
    private double totalSsch;
    private String chair;
    private String dean;

    public TypeSafeRawWorkload() {
    }


    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType.trim();
    }

    public String getInstructorTNumber() {
        return instructorTNumber;
    }

    public void setInstructorTNumber(String instructorTNumber) {
        this.instructorTNumber = instructorTNumber.trim();
    }

    public String getInstructorDepartment() {
        return instructorDepartment;
    }

    public void setInstructorDepartment(String instructorDepartment) {
        this.instructorDepartment = instructorDepartment.trim();
    }

    public int getSemesterTermCode() {
        return semesterTermCode;
    }

    public void setSemesterTermCode(int semesterTermCode) {
        this.semesterTermCode = semesterTermCode;
    }

    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode.trim();
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

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section.trim();
    }

    public int getTst() {
        return tst;
    }

    public void setTst(int tst) {
        this.tst = tst;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle.trim();
    }

    public String getCollCode() {
        return collCode;
    }

    public void setCollCode(String collCode) {
        this.collCode = collCode.trim();
    }

    public String getTaStudent() {
        return taStudent;
    }

    public void setTaStudent(String taStudent) {
        this.taStudent = taStudent.trim();
    }

    public double getTaSupport() {
        return taSupport;
    }

    public void setTaSupport(double taSupport) {
        this.taSupport = taSupport;
    }

    public double getTaEleventhDayCount() {
        return taEleventhDayCount;
    }

    public void setTaEleventhDayCount(double taEleventhDayCount) {
        this.taEleventhDayCount = taEleventhDayCount;
    }

    public double getTaCeditHours() {
        return taCeditHours;
    }

    public void setTaCeditHours(double taCeditHours) {
        this.taCeditHours = taCeditHours;
    }

    public double getTaLectureHours() {
        return taLectureHours;
    }

    public void setTaLectureHours(double taLectureHours) {
        this.taLectureHours = taLectureHours;
    }

    public double getIuMultipliertaLectureHours() {
        return iuMultipliertaLectureHours;
    }

    public void setIuMultipliertaLectureHours(double iuMultipliertaLectureHours) {
        this.iuMultipliertaLectureHours = iuMultipliertaLectureHours;
    }

    public double getTaLabHours() {
        return taLabHours;
    }

    public void setTaLabHours(double taLabHours) {
        this.taLabHours = taLabHours;
    }

    public double getIuMultipliertaLabHours() {
        return iuMultipliertaLabHours;
    }

    public void setIuMultipliertaLabHours(double iuMultipliertaLabHours) {
        this.iuMultipliertaLabHours = iuMultipliertaLabHours;
    }

    public String getOtherInstructorsInTeam() {
        return otherInstructorsInTeam;
    }

    public void setOtherInstructorsInTeam(String otherInstructorsInTeam) {
        this.otherInstructorsInTeam = otherInstructorsInTeam.trim();
    }

    public double getTotalSsch() {
        return totalSsch;
    }

    public void setTotalSsch(double totalSsch) {
        this.totalSsch = totalSsch;
    }

    public String getChair() {
        return chair;
    }

    public void setChair(String chair) {
        this.chair = chair.trim();
    }

    public String getDean() {
        return dean;
    }

    public void setDean(String dean) {
        this.dean = dean.trim();
    }


    public String getInstructorNameSurname() {
        return instructorNameSurname;
    }

    public void setInstructorNameSurname(String instructorNameSurname) {
        this.instructorNameSurname = instructorNameSurname.trim();
    }


    public double getTotalIus() {
        return totalIus;
    }

    public void setTotalIus(double totalIus) {
        this.totalIus = totalIus;
    }

    public String getInstructorDepartmentCode() {
        return instructorDepartmentCode;
    }

    public void setInstructorDepartmentCode(String instructorDepartmentCode) {
        this.instructorDepartmentCode = instructorDepartmentCode;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "TypeSafeRawWorkload{" +
                "instructionType='" + instructionType + '\'' +
                ", instructorTNumber='" + instructorTNumber + '\'' +
                ", instructorNameSurname='" + instructorNameSurname + '\'' +
                ", instructorDepartment='" + instructorDepartment + '\'' +
                ", instructorDepartmentCode='" + instructorDepartmentCode + '\'' +
                ", semesterTermCode=" + semesterTermCode +
                ", crn=" + crn +
                ", subjectCode='" + subjectCode + '\'' +
                ", courseTypeName='" + courseTypeName + '\'' +
                ", courseTypeCode='" + courseTypeCode + '\'' +
                ", courseNumber=" + courseNumber +
                ", section='" + section + '\'' +
                ", tst=" + tst +
                ", courseTitle='" + courseTitle + '\'' +
                ", collCode='" + collCode + '\'' +
                ", taStudent='" + taStudent + '\'' +
                ", taSupport=" + taSupport +
                ", taEleventhDayCount=" + taEleventhDayCount +
                ", taCeditHours=" + taCeditHours +
                ", taLectureHours=" + taLectureHours +
                ", iuMultipliertaLectureHours=" + iuMultipliertaLectureHours +
                ", taLabHours=" + taLabHours +
                ", iuMultipliertaLabHours=" + iuMultipliertaLabHours +
                ", totalIus=" + totalIus +
                ", otherInstructorsInTeam='" + otherInstructorsInTeam + '\'' +
                ", totalSsch=" + totalSsch +
                ", chair='" + chair + '\'' +
                ", dean='" + dean + '\'' +
                '}';
    }
}
