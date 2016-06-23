package com.jackalhan.ualr.domain;

/**
 * Created by jackalhan on 6/23/16.
 */
public class TypeSafeRawEmployee {

    private static final long serialVersionUID = 1L;

    private String tNumber;

    private String name;

    private String middleName;

    private String surname;

    private String positionCode;

    private String positionDescription;

    private int organizationCode;

    private String organizationName;


    private String organizationShortname;

    private String role;

    private String email;

    private String netid;

    private String facultyCode;

    private String facultyName;

    public TypeSafeRawEmployee() {
    }

    public String gettNumber() {
        return tNumber;
    }

    public void settNumber(String tNumber) {
        this.tNumber = tNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public int getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(int organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationShortname() {
        return organizationShortname;
    }

    public void setOrganizationShortname(String organizationShortname) {
        this.organizationShortname = organizationShortname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    @Override
    public String toString() {
        return "TypeSafeRawEmployee{" +
                "tNumber='" + tNumber + '\'' +
                ", name='" + name + '\'' +
                ", middleName='" + middleName + '\'' +
                ", surname='" + surname + '\'' +
                ", positionCode='" + positionCode + '\'' +
                ", positionDescription='" + positionDescription + '\'' +
                ", organizationCode=" + organizationCode +
                ", organizationName='" + organizationName + '\'' +
                ", organizationShortname='" + organizationShortname + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", netid='" + netid + '\'' +
                ", facultyCode='" + facultyCode + '\'' +
                ", facultyName='" + facultyName + '\'' +
                '}';
    }
}
