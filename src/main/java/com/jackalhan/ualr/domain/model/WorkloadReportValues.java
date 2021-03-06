package com.jackalhan.ualr.domain.model;

import com.jackalhan.ualr.constant.ValidateMessageConstant;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by txcakaloglu on 6/1/16.
 */
@Entity
@Table(name = "WorkloadReportValues")
public class WorkloadReportValues extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String instructionType;

    @NotNull
    private String subjectCode;

    @NotNull
    private String courseTypeName; // NEEDS TO BE CALCULATED : Undergraduate Course =========> ?

    @NotNull
    private String courseTypeCode; // NEEDS TO BE CALCULATED : DL  =========> ?

    @NotNull
    private int courseNumber;

    @NotNull
    private String section;

    @NotNull
    private String courseTitle;

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="workload_report_id", referencedColumnName = "id")
    private WorkloadReport workloadReport;

    public WorkloadReportValues(String instructionType, String subjectCode, String courseTypeName, String courseTypeCode, int courseNumber, String section, String courseTitle, String taStudent, int taSupport, int taEleventhDayCount, int taCeditHours, int taLectureHours, double iuMultipliertaLectureHours, int taLabHours, double iuMultipliertaLabHours, double totalIus, String otherInstructorsInTeam, int totalSsch, WorkloadReport workloadReport) {
        this.instructionType = instructionType;
        this.subjectCode = subjectCode;
        this.courseTypeName = courseTypeName;
        this.courseTypeCode = courseTypeCode;
        this.courseNumber = courseNumber;
        this.section = section;
        this.courseTitle = courseTitle;
        this.taStudent = taStudent;
        this.taSupport = taSupport;
        this.taEleventhDayCount = taEleventhDayCount;
        this.taCeditHours = taCeditHours;
        this.taLectureHours = taLectureHours;
        this.iuMultipliertaLectureHours = iuMultipliertaLectureHours;
        this.taLabHours = taLabHours;
        this.iuMultipliertaLabHours = iuMultipliertaLabHours;
        this.totalIus = totalIus;
        this.otherInstructorsInTeam = otherInstructorsInTeam;
        this.totalSsch = totalSsch;
        this.workloadReport = workloadReport;
    }

    public WorkloadReportValues() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    public String getCourseTypeCode() {
        return courseTypeCode;
    }

    public void setCourseTypeCode(String courseTypeCode) {
        this.courseTypeCode = courseTypeCode;
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
        this.section = section;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getTaStudent() {
        return taStudent;
    }

    public void setTaStudent(String taStudent) {
        this.taStudent = taStudent;
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

    public double getTotalIus() {
        return totalIus;
    }

    public void setTotalIus(double totalIus) {
        this.totalIus = totalIus;
    }

    public String getOtherInstructorsInTeam() {
        return otherInstructorsInTeam;
    }

    public void setOtherInstructorsInTeam(String otherInstructorsInTeam) {
        this.otherInstructorsInTeam = otherInstructorsInTeam;
    }

    public double getTotalSsch() {
        return totalSsch;
    }

    public void setTotalSsch(double totalSsch) {
        this.totalSsch = totalSsch;
    }

    public WorkloadReport getWorkloadReport() {
        return workloadReport;
    }

    public void setWorkloadReport(WorkloadReport workloadReport) {
        this.workloadReport = workloadReport;
    }

    @Override
    public String toString() {
        return "WorkloadReportValues{" +
                "id=" + id +
                ", instructionType='" + instructionType + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", courseTypeName='" + courseTypeName + '\'' +
                ", courseTypeCode='" + courseTypeCode + '\'' +
                ", courseNumber=" + courseNumber +
                ", section='" + section + '\'' +
                ", courseTitle='" + courseTitle + '\'' +
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
                ", workloadReport=" + workloadReport +
                '}';
    }
}
