package com.jackalhan.ualr.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jackalhan on 4/21/16.
 */
public class SimplifiedWorkload implements Serializable {

    private static final long serialVersionUID = 1L;

    private String collegeCode;
    private String instructorNameAndSurname;
    private String semesterTerm;
    private int semesterYear;
    private String departmentName;
    private String departmentCode;
    private String chairNameAndSurname;
    private String deanNameAndSurname;
    private double totalTaSupport;
    private double total11thDayCount;
    private double totalCreditHours;
    private double totalLectureHours;
    private double totalIUMultiplierForLectureHours;
    private double totalLabHours;
    private double totalTotalIUs;
    private double totalSsch;
    private List<TypeSafeRawWorkload> typeSafeRawWorkloads;
    private String withoutSwitchingInstructorNameAndSurname;
    private String withoutSwitchingChairNameAndSurname;
    private String withoutSwitchingdeanNameAndSurname;

    public SimplifiedWorkload() {
    }

    public String getInstructorNameAndSurname() {
        return instructorNameAndSurname;
    }

    public void setInstructorNameAndSurname(String instructorNameAndSurname) {
        this.instructorNameAndSurname = instructorNameAndSurname.trim();
    }

    public String getSemesterTerm() {
        return semesterTerm;
    }

    public void setSemesterTerm(String semesterTerm) {
        this.semesterTerm = semesterTerm;
    }

    public int getSemesterYear() {
        return semesterYear;
    }

    public void setSemesterYear(int semesterYear) {
        this.semesterYear = semesterYear;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName.trim();
    }

    public String getChairNameAndSurname() {
        return chairNameAndSurname;
    }

    public void setChairNameAndSurname(String chairNameAndSurname) {
        this.chairNameAndSurname = chairNameAndSurname.trim();
    }

    public String getDeanNameAndSurname() {
        return deanNameAndSurname;
    }

    public void setDeanNameAndSurname(String deanNameAndSurname) {
        this.deanNameAndSurname = deanNameAndSurname.trim();
    }

    public double getTotalTaSupport() {
        return totalTaSupport;
    }

    public void setTotalTaSupport(double totalTaSupport) {
        this.totalTaSupport = totalTaSupport;
    }

    public double getTotal11thDayCount() {
        return total11thDayCount;
    }

    public void setTotal11thDayCount(double total11thDayCount) {
        this.total11thDayCount = total11thDayCount;
    }

    public double getTotalCreditHours() {
        return totalCreditHours;
    }

    public void setTotalCreditHours(double totalCreditHours) {
        this.totalCreditHours = totalCreditHours;
    }

    public double getTotalLectureHours() {
        return totalLectureHours;
    }

    public void setTotalLectureHours(double totalLectureHours) {
        this.totalLectureHours = totalLectureHours;
    }

    public double getTotalIUMultiplierForLectureHours() {
        return totalIUMultiplierForLectureHours;
    }

    public void setTotalIUMultiplierForLectureHours(double totalIUMultiplierForLectureHours) {
        this.totalIUMultiplierForLectureHours = totalIUMultiplierForLectureHours;
    }

    public double getTotalLabHours() {
        return totalLabHours;
    }

    public void setTotalLabHours(double totalLabHours) {
        this.totalLabHours = totalLabHours;
    }

    public double getTotalTotalIUs() {
        return totalTotalIUs;
    }

    public void setTotalTotalIUs(double totalTotalIUs) {
        this.totalTotalIUs = totalTotalIUs;
    }

    public double getTotalSsch() {
        return totalSsch;
    }

    public void setTotalSsch(double totalSsch) {
        this.totalSsch = totalSsch;
    }

    public List<TypeSafeRawWorkload> getTypeSafeRawWorkloads() {
        return typeSafeRawWorkloads;
    }

    public void setTypeSafeRawWorkloads(List<TypeSafeRawWorkload> typeSafeRawWorkloads) {
        this.typeSafeRawWorkloads = typeSafeRawWorkloads;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public String getWithoutSwitchingInstructorNameAndSurname() {
        return withoutSwitchingInstructorNameAndSurname;
    }

    public void setWithoutSwitchingInstructorNameAndSurname(String withoutSwitchingInstructorNameAndSurname) {
        this.withoutSwitchingInstructorNameAndSurname = withoutSwitchingInstructorNameAndSurname;
    }

    public String getWithoutSwitchingChairNameAndSurname() {
        return withoutSwitchingChairNameAndSurname;
    }

    public void setWithoutSwitchingChairNameAndSurname(String withoutSwitchingChairNameAndSurname) {
        this.withoutSwitchingChairNameAndSurname = withoutSwitchingChairNameAndSurname;
    }

    public String getWithoutSwitchingdeanNameAndSurname() {
        return withoutSwitchingdeanNameAndSurname;
    }

    public void setWithoutSwitchingdeanNameAndSurname(String withoutSwitchingdeanNameAndSurname) {
        this.withoutSwitchingdeanNameAndSurname = withoutSwitchingdeanNameAndSurname;
    }

    @Override
    public String toString() {
        return "SimplifiedWorkload{" +
                "collegeCode='" + collegeCode + '\'' +
                ", instructorNameAndSurname='" + instructorNameAndSurname + '\'' +
                ", semesterTerm='" + semesterTerm + '\'' +
                ", semesterYear=" + semesterYear +
                ", departmentName='" + departmentName + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", chairNameAndSurname='" + chairNameAndSurname + '\'' +
                ", deanNameAndSurname='" + deanNameAndSurname + '\'' +
                ", totalTaSupport=" + totalTaSupport +
                ", total11thDayCount=" + total11thDayCount +
                ", totalCreditHours=" + totalCreditHours +
                ", totalLectureHours=" + totalLectureHours +
                ", totalIUMultiplierForLectureHours=" + totalIUMultiplierForLectureHours +
                ", totalLabHours=" + totalLabHours +
                ", totalTotalIUs=" + totalTotalIUs +
                ", totalSsch=" + totalSsch +
                ", typeSafeRawWorkloads=" + typeSafeRawWorkloads +
                ", withoutSwitchingInstructorNameAndSurname='" + withoutSwitchingInstructorNameAndSurname + '\'' +
                ", withoutSwitchingChairNameAndSurname='" + withoutSwitchingChairNameAndSurname + '\'' +
                ", withoutSwitchingdeanNameAndSurname='" + withoutSwitchingdeanNameAndSurname + '\'' +
                '}';
    }
}
