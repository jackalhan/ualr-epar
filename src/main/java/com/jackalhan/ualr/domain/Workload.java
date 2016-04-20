package com.jackalhan.ualr.domain;

import com.jackalhan.ualr.service.utils.StringUtilService;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by jackalhan on 4/18/16.
 */
@Entity
@Table(name="workload")
public class Workload extends AbstractAuditingEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String instructionType;
    // PEDAGOGICAL or INDIVIDUALIZED

    private String instructorTNumber;

    private String instructorDepartment;
    // computer science

    private int semesterTermCode;
    // 201610  {SPRING, 2016}

    private int crn;
    // 10480

    private String subjectCode;
    // SYEN  =========> ?

    private String courseType;
    // Undergraduate Course =========> ?

    private String courseCode;
    // DL  =========> ?

    private int courseNumber;
    // 4373

    private int section;
    // 45  =========> ?

    private int tst;
    // 0 =========> ?

    private String courseTitle;
    // Fundamentals of Software Engr

    private String collCode;
    // SS =========> ?

    private String taStudent;
    // Mohammad Alimohammadi

    private int taSupport;
    // 10

    private int taEleventhDayCount;
    // 17

    private int taCeditHours;
    // 3

    private int taLectureHours;
    // 3

    private double iuMultipliertaLectureHours;
    // 0.666

    private int taLabHours;
    // 0

    private double iuMultipliertaLabHours;
    // 1.3333

    private String otherInstructorsInTeam;


    private int totalSsch;
    // 51 =========> ?

    private String chair;
    private String dean;

    public Workload() {
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

    private String getCourseType() {
        return courseType;
    }

    private void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    private String getCourseCode() {
        return courseCode;
    }

    private void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
/*        if (!StringUtilService.getInstance().isEmpty(String.valueOf(courseNumber)))
        {
            if (courseNumber >= 1000 && courseNumber < 5000)
            {
                setCourseType("UNDERGRADUATE COURSE");
            }
        }*/
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
