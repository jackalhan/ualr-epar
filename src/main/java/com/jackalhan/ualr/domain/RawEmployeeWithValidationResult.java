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
    private List<RawEmployee> rawWorkloadList;


    public boolean isHasInvalidatedData() {
        return hasInvalidatedData;
    }

    public void setHasInvalidatedData(boolean hasInvalidatedData) {
        this.hasInvalidatedData = hasInvalidatedData;
    }

    public List<RawEmployee> getRawWorkloadList() {
        return rawWorkloadList;
    }

    public void setRawWorkloadList(List<RawEmployee> rawWorkloadList) {
        this.rawWorkloadList = rawWorkloadList;
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
                ", rawWorkloadList=" + rawWorkloadList +
                '}';
    }
}
