package com.jackalhan.ualr.domain;

import com.jackalhan.ualr.service.utils.FileUtilService;

import java.io.File;
import java.util.List;

/**
 * Created by jackalhan on 4/30/16.
 */
public class RawWorkloadWithValidationResult {

    public RawWorkloadWithValidationResult() {
    }

    private boolean hasInvalidatedData;
    private String caughtErrors;
    private List<RawWorkload> rawWorkloadList;


    public boolean isHasInvalidatedData() {
        return hasInvalidatedData;
    }

    public void setHasInvalidatedData(boolean hasInvalidatedData) {
        this.hasInvalidatedData = hasInvalidatedData;
    }

    public List<RawWorkload> getRawWorkloadList() {
        return rawWorkloadList;
    }

    public void setRawWorkloadList(List<RawWorkload> rawWorkloadList) {
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
        return "RawWorkloadWithValidationResult{" +
                "hasInvalidatedData=" + hasInvalidatedData +
                ", caughtErrors='" + caughtErrors + '\'' +
                ", rawWorkloadList=" + rawWorkloadList +
                '}';
    }
}
