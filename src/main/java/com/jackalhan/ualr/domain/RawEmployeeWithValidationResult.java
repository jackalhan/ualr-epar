package com.jackalhan.ualr.domain;

import java.util.List;

/**
 * Created by txcakaloglu on 6/15/16.
 */
public class RawEmployeeWithValidationResult {
    public RawEmployeeWithValidationResult() {
    }

    private boolean hasInvalidatedData;
    private String caughtErrors;
    private List<RawEmployee> rawEmployeeList;


    public boolean isHasInvalidatedData() {
        return hasInvalidatedData;
    }

    public void setHasInvalidatedData(boolean hasInvalidatedData) {
        this.hasInvalidatedData = hasInvalidatedData;
    }

    public List<RawEmployee> getRawEmployeeList() {
        return rawEmployeeList;
    }

    public void setRawEmployeeList(List<RawEmployee> rawEmployeeList) {
        this.rawEmployeeList = rawEmployeeList;
    }

    public String getCaughtErrors() {
        return caughtErrors;
    }

    public void setCaughtErrors(String caughtErrors) {
        this.caughtErrors = caughtErrors;
    }

    @Override
    public String toString() {
        return "RawEmployeeWithValidationResult{" +
                "hasInvalidatedData=" + hasInvalidatedData +
                ", caughtErrors='" + caughtErrors + '\'' +
                ", rawEmployeeList=" + rawEmployeeList +
                '}';
    }
}
