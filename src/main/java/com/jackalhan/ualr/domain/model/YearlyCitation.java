package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by txcakaloglu on 6/1/16.
 */
@Entity
@Table(name = "YearlyCitation")
public class YearlyCitation extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int year;

    private int citedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", referencedColumnName = "id")
    private Employee employee;

    public YearlyCitation(int year, int citedBy, Employee employee) {
        this.year = year;
        this.citedBy = citedBy;
        this.employee = employee;
    }

    public YearlyCitation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCitedBy() {
        return citedBy;
    }

    public void setCitedBy(int citedBy) {
        this.citedBy = citedBy;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "YearlyCitation{" +
                "id=" + id +
                ", year=" + year +
                ", citedBy=" + citedBy +
                ", employee=" + employee +
                '}';
    }
}
