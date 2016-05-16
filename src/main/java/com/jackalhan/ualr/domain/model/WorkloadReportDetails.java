package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by jackalhan on 5/10/16.
 */
/*@Entity
@Table(name = "WorkloadReportDetails")*/
public class WorkloadReportDetails extends AbstractAuditingEntity implements Serializable {


    @NotNull
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "WorkloadReportDetails{" +
                "filePath='" + filePath + '\'' +
                '}';
    }
}
