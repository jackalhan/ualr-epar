package com.jackalhan.ualr.domain;

/**
 * Created by jackalhan on 4/25/16.
 */
public class CourseDualState {

    private boolean hasDualCourse;
    private int dualCourseCode;
    private int numberOfTotalEnrollmentInDualCourse;

    public CourseDualState() {
    }

    public boolean hasDualCourse() {
        return hasDualCourse;
    }

    public void setHasDualCourse(boolean hasDualCourse) {
        this.hasDualCourse = hasDualCourse;
    }

    public int getDualCourseCode() {
        return dualCourseCode;
    }

    public void setDualCourseCode(int dualCourseCode) {
        this.dualCourseCode = dualCourseCode;
    }

    public int getNumberOfTotalEnrollmentInDualCourse() {
        return numberOfTotalEnrollmentInDualCourse;
    }

    public void setNumberOfTotalEnrollmentInDualCourse(int numberOfTotalEnrollmentInDualCourse) {
        this.numberOfTotalEnrollmentInDualCourse = numberOfTotalEnrollmentInDualCourse;
    }

    @Override
    public String toString() {
        return "CourseDualState{" +
                "hasDualCourse=" + hasDualCourse +
                ", dualCourseCode=" + dualCourseCode +
                ", numberOfTotalEnrollmentInDualCourse=" + numberOfTotalEnrollmentInDualCourse +
                '}';
    }
}
