package com.jackalhan.ualr.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by txcakaloglu on 5/10/16.
 */
@Entity
@Table(name = "Department_Staff")
public class DepartmentStaff extends AbstractAuditingEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String nameAndSurname;


    @NotNull
    private String title;


    @ManyToOne(cascade=CascadeType.ALL)
    private Department department;

/*
    @ManyToMany(targetEntity = Department.class)
    private Set departmentSet;
*/

    public DepartmentStaff() {
        super();
    }

    public DepartmentStaff(String nameAndSurname, String title /*, Set departmentSet */) {
        this.nameAndSurname = nameAndSurname;
        this.title = title;
        //this.departmentSet = departmentSet;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameAndSurname() {
        return nameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

 /*public Set getDepartmentSet() {
        return departmentSet;
    }

    public void setDepartmentSet(Set departmentSet) {
        this.departmentSet = departmentSet;
    }*/

    @Override
    public String toString() {
        return "DepartmentStaff{" +
                "id=" + id +
                ", nameAndSurname='" + nameAndSurname + '\'' +
                ", title='" + title + '\'' +
                ", department=" + department +
                '}';
    }
}
