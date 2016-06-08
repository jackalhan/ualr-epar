package com.jackalhan.ualr.domain.model;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.service.batch.WorkloadReportService;
import jxl.write.DateTime;
import org.omg.CORBA.PRIVATE_MEMBER;
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


    @NotNull
    private String name;

    @Id
    private String code;

    @NotNull
    private String shortName;

    @NotNull
    private String deanNameSurname;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Department> departmentList;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public String getDeanNameSurname() {
        return deanNameSurname;
    }

    public void setDeanNameSurname(String deanNameSurname) {
        this.deanNameSurname = deanNameSurname;
    }

    public Faculty(String name, String code, String shortName, List<Department> departmentList, String deanNameSurname) {
        this.name = name;
        this.code = code;
        this.shortName = shortName;
        this.departmentList = departmentList;
        this.deanNameSurname = deanNameSurname;

    }

    public Faculty(String code) {
        this.code = code;

    }

    public Faculty() {

    }

    @Override
    public String toString() {
        return "Faculty{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", shortName='" + shortName + '\'' +
                ", departmentList=" + departmentList +
                '}';
    }
}
