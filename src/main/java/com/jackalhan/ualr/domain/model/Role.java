package com.jackalhan.ualr.domain.model;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.service.batch.WorkloadReportService;
import org.hibernate.annotations.GeneratorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jackalhan on 5/11/16.
 */
@Entity
@Table(name = "Role")
public class Role extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    public Role() {

    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
