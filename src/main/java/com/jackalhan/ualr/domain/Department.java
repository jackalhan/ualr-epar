package com.jackalhan.ualr.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by txcakaloglu on 5/10/16.
 */
@Entity
@Table(name = "Department")
public class Department extends AbstractAuditingEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    private String chairNameAndSurname;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="dept_to_faculty_fk", referencedColumnName = "code")
    private Faculty faculty;

    @ManyToMany(mappedBy = "departmentList")
    private List<DepartmentStaff> departmentStaffList;

    public Department() {
        super();
    }

    public Department(String name, String code, String chairNameAndSurname) {
        super();
        this.name = name;
        this.code = code;
        this.chairNameAndSurname = chairNameAndSurname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public List<DepartmentStaff> getDepartmentStaffList() {
        return departmentStaffList;
    }

    public void setDepartmentStaffList(List<DepartmentStaff> departmentStaffList) {
        this.departmentStaffList = departmentStaffList;
    }


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

    public String getChairNameAndSurname() {
        return chairNameAndSurname;
    }

    public void setChairNameAndSurname(String chairNameAndSurname) {
        this.chairNameAndSurname = chairNameAndSurname;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }


    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", chairNameAndSurname='" + chairNameAndSurname + '\'' +
                ", faculty=" + faculty +
                ", departmentStaffList=" + departmentStaffList +
                '}';
    }
}
