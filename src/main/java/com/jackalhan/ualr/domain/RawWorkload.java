package com.jackalhan.ualr.domain;

import com.jackalhan.ualr.constant.ValidateMessageConstant;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by jackalhan on 4/18/16.
 */

public class RawWorkload implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty
    private String instructionType; // needs to be filtered for these records PEDAGOGICAL or INDIVIDUALIZED

    private String istructionPidm;

    private String instructorTNumber;

    @NotNull
    @NotEmpty
    private String instructorNameSurname; // NEEDS TO BE PARSED

    @Pattern(regexp="[\\d]{6}", message = ValidateMessageConstant.VALIDATE_FIELD_FORMAT)
    private String semesterTermCode; // 201610  {SPRING, 2016}
    private String crn;

    @NotNull
    @NotEmpty
    @Size(min=4, max = 4, message = ValidateMessageConstant.VALIDATE_FIELD_FORMAT)
    private String subjectCode;

    @Pattern(regexp="[\\d]{4}", message = ValidateMessageConstant.VALIDATE_FIELD_FORMAT)
    private String courseNumber; // USE TO CALCULATE CourseType, CourseCode, IU MUltipliers

    @NotNull
    @NotEmpty
    private String section;

    private String pct_response;

    @NotNull
    @NotEmpty
    private String courseTitle;
    private String collCode; // Just for info : What is that SS =========> ?

    @NotNull
    @NotEmpty
    @Size(min=4, max = 4, message = ValidateMessageConstant.VALIDATE_FIELD_FORMAT)
    private String instructorDepartmentCode;

    @NotNull
    @NotEmpty
    private String instructorDepartmentDescription;
    private String taStudent;

    @NotNull
    @NotEmpty
    private String deptChair;

    @NotNull
    @NotEmpty
    private String dean;

    @Range(min = 0, max= 1000, message = ValidateMessageConstant.VALIDATE_FIELD_FORMAT)
    private String taEleventhDayCount; // Just for info : What is that 17 =========> ?
    @Range(min = 0, max= 1000, message = ValidateMessageConstant.VALIDATE_FIELD_FORMAT)
    private String taCeditHours;

    private String taLectureHours;
    private String taLabHours;

    @Range(min = 0, max= 1000, message = ValidateMessageConstant.VALIDATE_FIELD_FORMAT)
    private String totalSsch; // Just for info : What is that SS =========> 51 ?


    public RawWorkload() {
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType.trim();
    }

    public String getIstructionPidm() {
        return istructionPidm;
    }

    public void setIstructionPidm(String istructionPidm) {
        this.istructionPidm = istructionPidm.trim();
    }

    public String getInstructorTNumber() {
        return instructorTNumber;
    }

    public void setInstructorTNumber(String instructorTNumber) {
        this.instructorTNumber = instructorTNumber.trim();
    }

    public String getInstructorNameSurname() {
        return instructorNameSurname;
    }

    public void setInstructorNameSurname(String instructorNameSurname) {
        this.instructorNameSurname = instructorNameSurname.trim();
    }

    public String getSemesterTermCode() {
        return semesterTermCode;
    }

    public void setSemesterTermCode(String semesterTermCode) {
        this.semesterTermCode = semesterTermCode.trim();
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn.trim();
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode.trim();
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber.trim();
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section.trim();
    }

    public String getPct_response() {
        return pct_response;
    }

    public void setPct_response(String pct_response) {
        this.pct_response = pct_response.trim();
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

    public String getInstructorDepartmentCode() {
        return instructorDepartmentCode;
    }

    public void setInstructorDepartmentCode(String instructorDepartmentCode) {
        this.instructorDepartmentCode = instructorDepartmentCode.trim();
    }

    public String getInstructorDepartmentDescription() {
        return instructorDepartmentDescription;
    }

    public void setInstructorDepartmentDescription(String instructorDepartmentDescription) {
        this.instructorDepartmentDescription = instructorDepartmentDescription.trim();
    }

    public String getTaStudent() {
        return taStudent;
    }

    public void setTaStudent(String taStudent) {
        this.taStudent = taStudent.trim();
    }

    public String getDeptChair() {
        return deptChair;
    }

    public void setDeptChair(String deptChair) {
        this.deptChair = deptChair.trim();
    }

    public String getDean() {
        return dean;
    }

    public void setDean(String dean) {
        this.dean = dean.trim();
    }

    public String getTaEleventhDayCount() {
        return taEleventhDayCount;
    }

    public void setTaEleventhDayCount(String taEleventhDayCount) {
        this.taEleventhDayCount = taEleventhDayCount.trim();
    }

    public String getTaCeditHours() {
        return taCeditHours;
    }

    public void setTaCeditHours(String taCeditHours) {
        this.taCeditHours = taCeditHours.trim();
    }

    public String getTaLectureHours() {
        return taLectureHours;
    }

    public void setTaLectureHours(String taLectureHours) {
        this.taLectureHours = taLectureHours.trim();
    }

    public String getTaLabHours() {
        return taLabHours;
    }

    public void setTaLabHours(String taLabHours) {
        this.taLabHours = taLabHours.trim();
    }

    public String getTotalSsch() {
        return totalSsch;
    }

    public void setTotalSsch(String totalSsch) {
        this.totalSsch = totalSsch.trim();
    }

    @Override
    public String toString() {
        return "RawWorkload{" +
                "instructionType='" + instructionType + '\'' +
                ", istructionPidm='" + istructionPidm + '\'' +
                ", instructorTNumber='" + instructorTNumber + '\'' +
                ", instructorNameSurname='" + instructorNameSurname + '\'' +
                ", semesterTermCode='" + semesterTermCode + '\'' +
                ", crn='" + crn + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", courseNumber='" + courseNumber + '\'' +
                ", section='" + section + '\'' +
                ", pct_response='" + pct_response + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
                ", collCode='" + collCode + '\'' +
                ", instructorDepartmentCode='" + instructorDepartmentCode + '\'' +
                ", instructorDepartmentDescription='" + instructorDepartmentDescription + '\'' +
                ", taStudent='" + taStudent + '\'' +
                ", deptChair='" + deptChair + '\'' +
                ", dean='" + dean + '\'' +
                ", taEleventhDayCount='" + taEleventhDayCount + '\'' +
                ", taCeditHours='" + taCeditHours + '\'' +
                ", taLectureHours='" + taLectureHours + '\'' +
                ", taLabHours='" + taLabHours + '\'' +
                ", totalSsch=" + totalSsch +
                '}';
    }

    public String toHTML() {
        return "RawWorkload{" + "<br>" +
                "instructionType=" + instructionType + "<br>" +
                ", istructionPidm=" + istructionPidm + "<br>" +
                ", instructorTNumber=" + instructorTNumber + "<br>" +
                ", instructorNameSurname=" + instructorNameSurname + "<br>" +
                ", semesterTermCode=" + semesterTermCode + "<br>" +
                ", crn=" + crn + "<br>" +
                ", subjectCode=" + subjectCode + "<br>" +
                ", courseNumber=" + courseNumber + "<br>" +
                ", section=" + section + "<br>" +
                ", pct_response=" + pct_response + "<br>" +
                ", courseTitle=" + courseTitle + "<br>" +
                ", collCode=" + collCode + "<br>" +
                ", instructorDepartmentCode=" + instructorDepartmentCode + "<br>" +
                ", instructorDepartmentDescription=" + instructorDepartmentDescription + "<br>" +
                ", taStudent=" + taStudent + "<br>" +
                        ", deptChair=" + deptChair + "<br>" +
                ", dean=" + dean + "<br>" +
                ", taEleventhDayCount=" + taEleventhDayCount + "<br>" +
                ", taCeditHours=" + taCeditHours + "<br>" +
                ", taLectureHours=" + taLectureHours + "<br>" +
                ", taLabHours=" + taLabHours + "<br>" +
                ", totalSsch=" + totalSsch + "<br>" +
                '}';
    }

}
