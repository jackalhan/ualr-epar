package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by txcakaloglu on 6/1/16.
 */
@Entity
@Table(name = "Department")
public class Department extends AbstractAuditingEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private long id;

    @NotNull
    private String code;

    @NotNull
    private String description;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employeeList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="faculty_code", referencedColumnName = "code")
    private Faculty faculty;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Department(String code, String description, List<Employee> employeeList, Faculty faculty) {
        this.code = code;
        this.description = description;
        this.employeeList = employeeList;
        this.faculty = faculty;
    }

    public Department() {
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", employeeList=" + employeeList +
                ", faculty=" + faculty +
                '}';
    }
}
