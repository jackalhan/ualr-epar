package com.jackalhan.ualr.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jackalhan.ualr.constant.GenericConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jackalhan on 4/18/16.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements Serializable {

    //private final Logger log = LoggerFactory.getLogger(AbstractAuditingEntity.class);
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 100, updatable = false)
    @JsonIgnore
    private String createdBy = GenericConstant.APPLICATION_NAME;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    @JsonIgnore
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", length = 100)
    @JsonIgnore
    private String lastModifiedBy = GenericConstant.APPLICATION_NAME;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    private Date lastModifiedDate;

    @PrePersist
    public void prePersist() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GenericConstant.SIMPLE_DATE_TIME_FORMAT_WITH_MILISECOND, Locale.US);

        this.lastModifiedDate = simpleDateFormat.parse(String.valueOf(new Date()));
        this.createdDate = simpleDateFormat.parse(String.valueOf(new Date()));

    }

    @PreUpdate
    public void preUpdate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GenericConstant.SIMPLE_DATE_TIME_FORMAT_WITH_MILISECOND, Locale.US);

        this.lastModifiedDate = simpleDateFormat.parse(String.valueOf(new Date()));

    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy.trim();
    }


    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy.trim();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
