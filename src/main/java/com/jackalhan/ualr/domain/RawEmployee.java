package com.jackalhan.ualr.domain;

import com.jackalhan.ualr.constant.ValidateMessageConstant;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by txcakaloglu on 6/15/16.
 */
public class RawEmployee implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @NotEmpty
    private String tNumber;

    @NotNull
    @NotEmpty
    private String name;

    private String middleName;

    @NotNull
    @NotEmpty
    private String surname;

    @NotNull
    @NotEmpty
    private String positionCode;

    @NotNull
    @NotEmpty
    private String positionDescription;

    @NotNull
    @NotEmpty
    @NumberFormat
    private String organizationCode;

    @NotNull
    @NotEmpty
    private String organizationName;


    private String organizationShortname;

    @NotNull
    @NotEmpty
    private String role;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String netid;

    @NotNull
    @NotEmpty
    private String facultyCode;

    @NotNull
    @NotEmpty
    private String facultyName;

    public RawEmployee() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
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
        return "RawEmployee{" +
                "tNumber='" + tNumber + '\'' +
                ", name='" + name + '\'' +
                ", middleName='" + middleName + '\'' +
                ", surname='" + surname + '\'' +
                ", positionCode='" + positionCode + '\'' +
                ", positionDescription='" + positionDescription + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationShortname='" + organizationShortname + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", netid='" + netid + '\'' +
                ", facultyCode='" + facultyCode + '\'' +
                ", facultyName='" + facultyName + '\'' +
                '}';
    }

    public String toHTML() {
        return "RawEmployee{" +
                "tNumber='" + tNumber + "\n" +
                ", name='" + name + "\n" +
                ", middleName='" + middleName + "\n" +
                ", surname='" + surname + "\n" +
                ", positionCode='" + positionCode + "\n" +
                ", positionDescription='" + positionDescription + "\n" +
                ", organizationCode='" + organizationCode + "\n" +
                ", organizationName='" + organizationName + "\n" +
                ", organizationShortname='" + organizationShortname + "\n" +
                ", role='" + role + "\n" +
                ", email='" + email + "\n" +
                ", netid='" + netid + "\n" +
                ", facultyCode='" + facultyCode + "\n" +
                ", facultyName='" + facultyName + "\n" +
                '}';
    }
}
