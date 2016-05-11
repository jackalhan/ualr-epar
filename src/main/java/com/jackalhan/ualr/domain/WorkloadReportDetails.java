package com.jackalhan.ualr.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by jackalhan on 5/10/16.
 */
@Entity
@Table(name = "Workload_Report_Details")
public class WorkloadReportDetails extends AbstractAuditingEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name="repdet_to_departmentStaff_fk", referencedColumnName = "id")
    private DepartmentStaff departmentStaff;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name="repdet_to_workloadReport_fk", referencedColumnName = "id")
    private WorkloadReport workloadReport;


    public WorkloadReportDetails(String filePath) {
        super();
        this.filePath = filePath;
    }

    public WorkloadReportDetails() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DepartmentStaff getDepartmentStaff() {
        return departmentStaff;
    }

    public void setDepartmentStaff(DepartmentStaff departmentStaff) {
        this.departmentStaff = departmentStaff;
    }

    public WorkloadReport getWorkloadReport() {
        return workloadReport;
    }

    public void setWorkloadReport(WorkloadReport workloadReport) {
        this.workloadReport = workloadReport;
    }

    @Override
    public String toString() {
        return "WorkloadReportDetails{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", departmentStaff=" + departmentStaff +
                ", workloadReport=" + workloadReport +
                '}';
    }
}
