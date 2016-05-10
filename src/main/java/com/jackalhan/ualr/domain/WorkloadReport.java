package com.jackalhan.ualr.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by txcakaloglu on 5/10/16.
 */
@Entity
@Table(name = "Workload_Report")
public class WorkloadReport extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String semesterTerm;

    @NotNull
    private int semesterYear;

    @ManyToOne (cascade=CascadeType.ALL)
    private Faculty faculty;


    public WorkloadReport() {
        super();
    }

    public WorkloadReport(String semesterTerm, int semesterYear) {
        super();
        this.semesterTerm = semesterTerm;
        this.semesterYear = semesterYear;
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

    @Override
    public String toString() {
        return "WorkloadReport{" +
                "id=" + id +
                ", semesterTerm='" + semesterTerm + '\'' +
                ", semesterYear=" + semesterYear +
                ", faculty=" + faculty +
                '}';
    }
}
