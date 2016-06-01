package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by txcakaloglu on 6/1/16.
 */
@Entity
@Table(name = "ScholarlyActivity")
public class ScholarlyActivity extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private long id;

    @NotNull
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", referencedColumnName = "id")
    private Employee employee;

    @OneToMany(mappedBy = "scholarlyActivity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PrincipalInvestigator> principalInvestigatorList;

    @NotNull
    private String status;

    @NotNull
    private String title;

    private String fundingAgency;

    @NotNull
    private BigDecimal totalAmount;

    @NotNull
    private BigDecimal sharedAmount;

    private String dates;

    public ScholarlyActivity(String type, Employee employee, List<PrincipalInvestigator> principalInvestigatorList, String status, String title, String fundingAgency, BigDecimal totalAmount, BigDecimal sharedAmount, String dates) {
        this.type = type;
        this.employee = employee;
        this.principalInvestigatorList = principalInvestigatorList;
        this.status = status;
        this.title = title;
        this.fundingAgency = fundingAgency;
        this.totalAmount = totalAmount;
        this.sharedAmount = sharedAmount;
        this.dates = dates;
    }

    public ScholarlyActivity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<PrincipalInvestigator> getPrincipalInvestigatorList() {
        return principalInvestigatorList;
    }

    public void setPrincipalInvestigatorList(List<PrincipalInvestigator> principalInvestigatorList) {
        this.principalInvestigatorList = principalInvestigatorList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFundingAgency() {
        return fundingAgency;
    }

    public void setFundingAgency(String fundingAgency) {
        this.fundingAgency = fundingAgency;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getSharedAmount() {
        return sharedAmount;
    }

    public void setSharedAmount(BigDecimal sharedAmount) {
        this.sharedAmount = sharedAmount;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    @Override
    public String toString() {
        return "ScholarlyActivity{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", employee=" + employee +
                ", principalInvestigatorList=" + principalInvestigatorList +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", fundingAgency='" + fundingAgency + '\'' +
                ", totalAmount=" + totalAmount +
                ", sharedAmount=" + sharedAmount +
                ", dates='" + dates + '\'' +
                '}';
    }
}
