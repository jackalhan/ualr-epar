package com.jackalhan.ualr.domain;

/**
 * Created by jackalhan on 4/23/16.
 */
public class CourseTypeIUValues {

    private double iuMultiplier;
    private double iuMultiplicationResult;

    public CourseTypeIUValues() {
    }

    public CourseTypeIUValues(double iuMultiplier, double iuMultiplicationResult) {
        this.iuMultiplier = iuMultiplier;
        this.iuMultiplicationResult = iuMultiplicationResult;
    }

    public double getIuMultiplier() {
        return iuMultiplier;
    }

    public void setIuMultiplier(double iuMultiplier) {
        this.iuMultiplier = iuMultiplier;
    }

    public double getIuMultiplicationResult() {
        return iuMultiplicationResult;
    }

    public void setIuMultiplicationResult(double iuMultiplicationResult) {
        this.iuMultiplicationResult = iuMultiplicationResult;
    }

    @Override
    public String toString() {
        return "CourseTypeIUValues{" +
                "iuMultiplier=" + iuMultiplier +
                ", iuMultiplicationResult=" + iuMultiplicationResult +
                '}';
    }
}
