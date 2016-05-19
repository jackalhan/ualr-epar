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
import java.util.Date;
import java.util.List;

/**
 * Created by txcakaloglu on 5/10/16.
 */
@Entity
@Table(name = "WorkloadReportTerm")
public class WorkloadReportTerm extends AbstractAuditingEntity implements Serializable {




    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private long id;

    @NotNull
    private String semesterTerm;

    @NotNull
    private int semesterTermCode;

    @NotNull
    private int semesterYear;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="facultyCode", referencedColumnName = "code")
    private Faculty faculty;

    @OneToMany(mappedBy = "workloadReportTerm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<WorkloadReport> workloadReports;


    public WorkloadReportTerm() {
    }

    public WorkloadReportTerm(String semesterTerm, int semesterTermCode, int semesterYear, Faculty faculty, List<WorkloadReport> workloadReports)  {
        this.semesterTerm = semesterTerm;
        this.semesterYear = semesterYear;
        this.semesterTermCode = semesterTermCode;
        this.faculty = faculty;
        this.workloadReports = workloadReports;

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public List<WorkloadReport> getWorkloadReports() {
        return workloadReports;
    }

    public void setWorkloadReports(List<WorkloadReport> workloadReports) {
        this.workloadReports = workloadReports;
    }

    public int getSemesterTermCode() {
        return semesterTermCode;
    }

    public void setSemesterTermCode(int semesterTermCode) {
        this.semesterTermCode = semesterTermCode;
    }

    @Override
    public String toString() {
        return "WorkloadReportTerm{" +
                "id=" + id +
                ", semesterTerm='" + semesterTerm + '\'' +
                ", semesterTermCode='" + semesterTermCode+ '\'' +
                ", semesterYear=" + semesterYear +
                ", faculty=" + faculty +
                ", workloadReports=" + workloadReports +
                '}';
    }
}
