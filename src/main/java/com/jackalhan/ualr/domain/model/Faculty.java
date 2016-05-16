package com.jackalhan.ualr.domain.model;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.service.batch.WorkloadReportService;
import jxl.write.DateTime;
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
@Table(name = "Faculty")
public class Faculty extends AbstractAuditingEntity implements Serializable{


    //@NotNull
    private String name;

    @Id
    private String code;

    @NotNull
    private String deanNameSurname;

    /*@OneToMany(mappedBy = "faculty")
    List<Department> departments;

    @OneToMany(mappedBy = "faculty")
    List<WorkloadReportTerm> workloadReportTerms;*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeanNameSurname() {
        return deanNameSurname;
    }

    public void setDeanNameSurname(String deanNameSurname) {
        this.deanNameSurname = deanNameSurname;
    }

   /* public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<WorkloadReportTerm> getWorkloadReportTerms() {
        return workloadReportTerms;
    }

    public void setWorkloadReportTerms(List<WorkloadReportTerm> workloadReportTerms) {
        this.workloadReportTerms = workloadReportTerms;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", deanNameAndSurname='" + deanNameAndSurname + '\'' +
                ", departments=" + departments +
                ", workloadReportTerms=" + workloadReportTerms +
                '}';
    }*/

    public Faculty(String name, String code, String deanNameSurname, List<Department> departments, List<WorkloadReportTerm> workloadReportTerms) {
        this.name = name;
        this.code = code;
        this.deanNameSurname = deanNameSurname;
       /* this.departments = departments;
        this.workloadReportTerms = workloadReportTerms;*/

    }

    public Faculty(String code) {
        this.code = code;

    }

    public Faculty() {

    }


}
