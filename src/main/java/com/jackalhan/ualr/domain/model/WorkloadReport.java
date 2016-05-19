package com.jackalhan.ualr.domain.model;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.service.batch.WorkloadReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by jackalhan on 5/10/16.
 */
@Entity
@Table(name = "WorkloadReport")
public class WorkloadReport extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(length=100000)
    private byte[] report;

    @NotNull
    private String reportName;

    @NotNull
    private String instructorNameSurname;

    @NotNull
    private String departmentName;

    @NotNull
    private String departmentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="workload_report_term_id", referencedColumnName = "id")
    private WorkloadReportTerm workloadReportTerm;

    public WorkloadReport(byte[] report, String reportName, String instructorNameSurname, String departmentName, String departmentCode, WorkloadReportTerm workloadReportTerm) {
        this.report = report;
        this.reportName = reportName;
        this.instructorNameSurname = instructorNameSurname;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.workloadReportTerm = workloadReportTerm;

    }

    public WorkloadReport() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getReport() {
        return report;
    }

    public void setReport(byte[] report) {
        this.report = report;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getInstructorNameSurname() {
        return instructorNameSurname;
    }

    public void setInstructorNameSurname(String instructorNameSurname) {
        this.instructorNameSurname = instructorNameSurname;
    }

    public WorkloadReportTerm getWorkloadReportTerm() {
        return workloadReportTerm;
    }

    public void setWorkloadReportTerm(WorkloadReportTerm workloadReportTermid) {
        this.workloadReportTerm = workloadReportTermid;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    @Override
    public String toString() {
        return "WorkloadReport{" +
                "id=" + id +
                ", report=" + Arrays.toString(report) +
                ", reportName='" + reportName + '\'' +
                ", instructorNameSurname='" + instructorNameSurname + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", workloadReportTerm=" + workloadReportTerm +
                '}';
    }
}
