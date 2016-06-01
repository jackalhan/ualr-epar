package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by txcakaloglu on 6/1/16.
 */
@Entity
@Table(name = "CitationTrend")
public class CitationTrend extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private long id;

    private int totalCitations;

    private int calculatedHIndex;

    private int calculatedI10Index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", referencedColumnName = "id")
    private Employee employee;

    public CitationTrend(int totalCitations, int calculatedHIndex, int calculatedI10Index, Employee employee) {
        this.totalCitations = totalCitations;
        this.calculatedHIndex = calculatedHIndex;
        this.calculatedI10Index = calculatedI10Index;
        this.employee = employee;
    }

    public CitationTrend() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTotalCitations() {
        return totalCitations;
    }

    public void setTotalCitations(int totalCitations) {
        this.totalCitations = totalCitations;
    }

    public int getCalculatedHIndex() {
        return calculatedHIndex;
    }

    public void setCalculatedHIndex(int calculatedHIndex) {
        this.calculatedHIndex = calculatedHIndex;
    }

    public int getCalculatedI10Index() {
        return calculatedI10Index;
    }

    public void setCalculatedI10Index(int calculatedI10Index) {
        this.calculatedI10Index = calculatedI10Index;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "CitationTrend{" +
                "id=" + id +
                ", totalCitations=" + totalCitations +
                ", calculatedHIndex=" + calculatedHIndex +
                ", calculatedI10Index=" + calculatedI10Index +
                ", employee=" + employee +
                '}';
    }
}
