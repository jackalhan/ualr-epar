package com.jackalhan.ualr.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by jackalhan on 4/18/16.
 */
@Entity
@Table(name="workload")
public class Workload extends AbstractAuditingEntity implements Serializable{

    private static final long serialVersionUID = 1L;


    private String instructionType;
    // PEDAGOGICAL or INDIVIDUALIZED

    private String instructorTNumber;

    private String instructorDepartment;
    // computer science

    private int semesterTermCode;
    // 201610  {SPRING, 2016}

    private int crn;
    // 10480

    private String courseCode;
    // CPSC  {Computer Science}

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

    private int taLabHours;
    // 0

    private int totalSsch;
    // 51 =========> ?



/*
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
