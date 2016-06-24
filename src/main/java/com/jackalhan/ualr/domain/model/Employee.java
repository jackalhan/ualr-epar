package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by txcakaloglu on 6/1/16.
 */
@Entity
@Table(name = "Employee")
public class Employee extends AbstractAuditingEntity implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String middleName;

    @NotNull
    private String tNumber;

    @NotNull
    private String email;

    @Id
    @NotNull
    private String netid;

    @NotNull
    private String positionCode;

    @NotNull
    private String positionDescription;

    @NotNull
    private String role;

    @NotNull
    private String sourceDataName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="department_id", referencedColumnName = "id")
    private Department department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Publication> publicationList;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ScholarlyActivity> scholarlyActivityList;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CitationTrend> citationTrendList;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<YearlyCitation> yearlyCitationList;

    @NotNull
    private boolean isActive;

    public Employee() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String gettNumber() {
        return tNumber;
    }

    public void settNumber(String tNumber) {
        this.tNumber = tNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNetid() {
        return netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Publication> getPublicationList() {
        return publicationList;
    }

    public void setPublicationList(List<Publication> publicationList) {
        this.publicationList = publicationList;
    }

    public List<ScholarlyActivity> getScholarlyActivityList() {
        return scholarlyActivityList;
    }

    public List<YearlyCitation> getYearlyCitationList() {
        return yearlyCitationList;
    }

    public void setYearlyCitationList(List<YearlyCitation> yearlyCitationList) {
        this.yearlyCitationList = yearlyCitationList;
    }

    public void setScholarlyActivityList(List<ScholarlyActivity> scholarlyActivityList) {
        this.scholarlyActivityList = scholarlyActivityList;
    }

    public List<CitationTrend> getCitationTrendList() {
        return citationTrendList;
    }

    public void setCitationTrendList(List<CitationTrend> citationTrendList) {
        this.citationTrendList = citationTrendList;
    }

    public String getSourceDataName() {
        return sourceDataName;
    }

    public void setSourceDataName(String sourceDataName) {
        this.sourceDataName = sourceDataName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public Employee(String name, String surname, String middleName, String tNumber, String email, String netid, String positionCode, String positionDescription, String role, String sourceDataName, Department department, List<Publication> publicationList, List<ScholarlyActivity> scholarlyActivityList, List<CitationTrend> citationTrendList, List<YearlyCitation> yearlyCitationList, boolean isActive) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.tNumber = tNumber;
        this.email = email;
        this.netid = netid;
        this.positionCode = positionCode;
        this.positionDescription = positionDescription;
        this.role = role;
        this.sourceDataName = sourceDataName;
        this.department = department;
        this.publicationList = publicationList;
        this.scholarlyActivityList = scholarlyActivityList;
        this.citationTrendList = citationTrendList;
        this.yearlyCitationList = yearlyCitationList;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", tNumber='" + tNumber + '\'' +
                ", email='" + email + '\'' +
                ", netid='" + netid + '\'' +
                ", positionCode='" + positionCode + '\'' +
                ", positionDescription='" + positionDescription + '\'' +
                ", role='" + role + '\'' +
                ", sourceDataName='" + sourceDataName + '\'' +
                ", department=" + department +
                ", publicationList=" + publicationList +
                ", scholarlyActivityList=" + scholarlyActivityList +
                ", citationTrendList=" + citationTrendList +
                ", yearlyCitationList=" + yearlyCitationList +
                ", isActive=" + isActive +
                '}';
    }
}
