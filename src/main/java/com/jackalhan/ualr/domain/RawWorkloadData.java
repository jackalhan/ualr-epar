package com.jackalhan.ualr.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jackalhan on 4/18/16.
 */
@Entity
@Table(name = "raw_workload_data")
public class RawWorkloadData extends AbstractAuditingEntity implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String instructionType; // needs to be filtered for these records PEDAGOGICAL or INDIVIDUALIZED

    private String instructorTNumber;
    private String instructorNameSurname; // NEEDS TO BE PARSED
    /*private String instructorName; // NEEDS TO BE SET FROM instructorNameSurname BY PARSING DATA
    private String instructorSurname; //NEEDS TO BE SET FROM instructorNameSurname BY PARSING DATA*/
    private String instructorDepartment;
    private int semesterTermCode; // 201610  {SPRING, 2016}
    /*    private String semesterTerm; //SPRING
        private String semesterTermYear; //2016*/
    private int crn;
    private String subjectCode;
    private String courseTypeName; // NEEDS TO BE CALCULATED : Undergraduate Course =========> ?
    private String courseTypeCode; // NEEDS TO BE CALCULATED : DL  =========> ?
    private int courseNumber; // USE TO CALCULATE CourseType, CourseCode, IU MUltipliers
    private int section;
    private int tst; // Just for info : What is that 0 =========> ?
    private String courseTitle;
    private String collCode; // Just for info : What is that SS =========> ?
    private String taStudent;
    private int taSupport; // HOW IS IT CALCULATED? NO DATA FROM BANNER 10
    private int taEleventhDayCount; // Just for info : What is that 17 =========> ?
    private int taCeditHours;
    private int taLectureHours;
    private double iuMultipliertaLectureHours; //NEEDS TO BE CALCULATED : ========>  0.666
    private int taLabHours;
    private double iuMultipliertaLabHours; //NEEDS TO BE CALCULATED : ========>  1.3333
    private double totalIus; //NEEDS TO BE CALCULATED : ========>  1.3333
    private String otherInstructorsInTeam;
    private int totalSsch; // Just for info : What is that SS =========> 51 ?
    private String chair;
    private String dean;

    public RawWorkloadData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstructionType() {
        return instructionType;
    }

    public void setInstructionType(String instructionType) {
        this.instructionType = instructionType;
    }

    public String getInstructorTNumber() {
        return instructorTNumber;
    }

    public void setInstructorTNumber(String instructorTNumber) {
        this.instructorTNumber = instructorTNumber;
    }

    public String getInstructorDepartment() {
        return instructorDepartment;
    }

    public void setInstructorDepartment(String instructorDepartment) {
        this.instructorDepartment = instructorDepartment;
    }

    public int getSemesterTermCode() {
        return semesterTermCode;
    }

    public void setSemesterTermCode(int semesterTermCode) {
        this.semesterTermCode = semesterTermCode;
    }

    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    private String getCourseTypeName() {
        return courseTypeName;
    }

    private void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
    }

    private String getCourseTypeCode() {
        return courseTypeCode;
    }

    private void setCourseTypeCode(String courseTypeCode) {
        this.courseTypeCode = courseTypeCode;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getTst() {
        return tst;
    }

    public void setTst(int tst) {
        this.tst = tst;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCollCode() {
        return collCode;
    }

    public void setCollCode(String collCode) {
        this.collCode = collCode;
    }

    public String getTaStudent() {
        return taStudent;
    }

    public void setTaStudent(String taStudent) {
        this.taStudent = taStudent;
    }

    public int getTaSupport() {
        return taSupport;
    }

    public void setTaSupport(int taSupport) {
        this.taSupport = taSupport;
    }

    public int getTaEleventhDayCount() {
        return taEleventhDayCount;
    }

    public void setTaEleventhDayCount(int taEleventhDayCount) {
        this.taEleventhDayCount = taEleventhDayCount;
    }

    public int getTaCeditHours() {
        return taCeditHours;
    }

    public void setTaCeditHours(int taCeditHours) {
        this.taCeditHours = taCeditHours;
    }

    public int getTaLectureHours() {
        return taLectureHours;
    }

    public void setTaLectureHours(int taLectureHours) {
        this.taLectureHours = taLectureHours;
    }

    public double getIuMultipliertaLectureHours() {
        return iuMultipliertaLectureHours;
    }

    public void setIuMultipliertaLectureHours(double iuMultipliertaLectureHours) {
        this.iuMultipliertaLectureHours = iuMultipliertaLectureHours;
    }

    public int getTaLabHours() {
        return taLabHours;
    }

    public void setTaLabHours(int taLabHours) {
        this.taLabHours = taLabHours;
    }

    public double getIuMultipliertaLabHours() {
        return iuMultipliertaLabHours;
    }

    public void setIuMultipliertaLabHours(double iuMultipliertaLabHours) {
        this.iuMultipliertaLabHours = iuMultipliertaLabHours;
    }

    public String getOtherInstructorsInTeam() {
        return otherInstructorsInTeam;
    }

    public void setOtherInstructorsInTeam(String otherInstructorsInTeam) {
        this.otherInstructorsInTeam = otherInstructorsInTeam;
    }

    public int getTotalSsch() {
        return totalSsch;
    }

    public void setTotalSsch(int totalSsch) {
        this.totalSsch = totalSsch;
    }

    public String getChair() {
        return chair;
    }

    public void setChair(String chair) {
        this.chair = chair;
    }

    public String getDean() {
        return dean;
    }

    public void setDean(String dean) {
        this.dean = dean;
    }


    public String getInstructorNameSurname() {
        return instructorNameSurname;
    }

    public void setInstructorNameSurname(String instructorNameSurname) {
        this.instructorNameSurname = instructorNameSurname;
    }


    public double getTotalIus() {
        return totalIus;
    }

    public void setTotalIus(double totalIus) {
        this.totalIus = totalIus;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "RawWorkloadData{" +
                "id=" + id +
                ", instructionType='" + instructionType + '\'' +
                ", instructorTNumber='" + instructorTNumber + '\'' +
                ", instructorNameSurname='" + instructorNameSurname + '\'' +
                ", instructorDepartment='" + instructorDepartment + '\'' +
                ", semesterTermCode=" + semesterTermCode +
                ", crn=" + crn +
                ", subjectCode='" + subjectCode + '\'' +
                ", courseTypeName='" + courseTypeName + '\'' +
                ", courseTypeCode='" + courseTypeCode + '\'' +
                ", courseNumber=" + courseNumber +
                ", section=" + section +
                ", tst=" + tst +
                ", courseTitle='" + courseTitle + '\'' +
                ", collCode='" + collCode + '\'' +
                ", taStudent='" + taStudent + '\'' +
                ", taSupport=" + taSupport +
                ", taEleventhDayCount=" + taEleventhDayCount +
                ", taCeditHours=" + taCeditHours +
                ", taLectureHours=" + taLectureHours +
                ", iuMultipliertaLectureHours=" + iuMultipliertaLectureHours +
                ", taLabHours=" + taLabHours +
                ", iuMultipliertaLabHours=" + iuMultipliertaLabHours +
                ", totalIus=" + totalIus +
                ", otherInstructorsInTeam='" + otherInstructorsInTeam + '\'' +
                ", totalSsch=" + totalSsch +
                ", chair='" + chair + '\'' +
                ", dean='" + dean + '\'' +
                '}';
    }

/*


    @NotNull
    @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash",length = 60)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;*/

}
