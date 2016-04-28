package com.jackalhan.ualr.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jackalhan on 4/21/16.
 */
public class SimplifiedWorkload implements Serializable {

    private static final long serialVersionUID = 1L;

    private String instructorNameAndSurname;
    private String semesterTerm;
    private int semesterYear;
    private String departmentName;
    private String chairNameAndSurname;
    private String deanNameAndSurname;
    private int totalTaSupport;
    private int total11thDayCount;
    private int totalCreditHours;
    private int totalLectureHours;
    private double totalIUMultiplierForLectureHours;
    private int totalLabHours;
    private double totalTotalIUs;
    private int totalSsch;
    private List<RawWorkload> rawWorkloadDetails;

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

    public int getTotalTaSupport() {
        return totalTaSupport;
    }

    public void setTotalTaSupport(int totalTaSupport) {
        this.totalTaSupport = totalTaSupport;
    }

    public int getTotal11thDayCount() {
        return total11thDayCount;
    }

    public void setTotal11thDayCount(int total11thDayCount) {
        this.total11thDayCount = total11thDayCount;
    }

    public int getTotalCreditHours() {
        return totalCreditHours;
    }

    public void setTotalCreditHours(int totalCreditHours) {
        this.totalCreditHours = totalCreditHours;
    }

    public int getTotalLectureHours() {
        return totalLectureHours;
    }

    public void setTotalLectureHours(int totalLectureHours) {
        this.totalLectureHours = totalLectureHours;
    }

    public double getTotalIUMultiplierForLectureHours() {
        return totalIUMultiplierForLectureHours;
    }

    public void setTotalIUMultiplierForLectureHours(double totalIUMultiplierForLectureHours) {
        this.totalIUMultiplierForLectureHours = totalIUMultiplierForLectureHours;
    }

    public int getTotalLabHours() {
        return totalLabHours;
    }

    public void setTotalLabHours(int totalLabHours) {
        this.totalLabHours = totalLabHours;
    }

    public double getTotalTotalIUs() {
        return totalTotalIUs;
    }

    public void setTotalTotalIUs(double totalTotalIUs) {
        this.totalTotalIUs = totalTotalIUs;
    }

    public int getTotalSsch() {
        return totalSsch;
    }

    public void setTotalSsch(int totalSsch) {
        this.totalSsch = totalSsch;
    }

    public List<RawWorkload> getRawWorkloadDetails() {
        return rawWorkloadDetails;
    }

    public void setRawWorkloadDetails(List<RawWorkload> rawWorkloadDetails) {
        this.rawWorkloadDetails = rawWorkloadDetails;
    }

    @Override
    public String toString() {
        return "SimplifiedWorkload{" +
                ", instructorNameAndSurname='" + instructorNameAndSurname + '\'' +
                ", semesterTerm='" + semesterTerm + '\'' +
                ", semesterYear=" + semesterYear +
                ", departmentName='" + departmentName + '\'' +
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
                ", rawWorkloadDetails=" + rawWorkloadDetails +
                '}';
    }
}
