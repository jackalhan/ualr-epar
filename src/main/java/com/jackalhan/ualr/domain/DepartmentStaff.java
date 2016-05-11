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
@Table(name = "Department_Staff")
public class DepartmentStaff extends AbstractAuditingEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String nameAndSurname;


    @NotNull
    private String title;


 /*   @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JoinColumn(name="staff_to_department_fk", referencedColumnName = "id")
    private Department department;*/



    @ManyToMany
    @JoinTable
    private List<Department> departmentList;


    public DepartmentStaff() {
        super();
    }

    public DepartmentStaff(String nameAndSurname, String title /*, Set departmentSet */) {
        this.nameAndSurname = nameAndSurname;
        this.title = title;
        //this.departmentSet = departmentSet;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
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

    @Override
    public String toString() {
        return "DepartmentStaff{" +
                "id=" + id +
                ", nameAndSurname='" + nameAndSurname + '\'' +
                ", title='" + title + '\'' +
                ", departmentList=" + departmentList +
                '}';
    }
}
