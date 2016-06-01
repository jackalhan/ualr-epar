package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by txcakaloglu on 6/1/16.
 */
@Entity
@Table(name = "PrincipalInvestigator")
public class PrincipalInvestigator extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String middleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="scholar_activity_id", referencedColumnName = "id")
    private ScholarlyActivity scholarlyActivity;

    public PrincipalInvestigator(String name, String surname, String middleName, ScholarlyActivity scholarlyActivity) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.scholarlyActivity = scholarlyActivity;
    }

    public PrincipalInvestigator() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public ScholarlyActivity getScholarlyActivity() {
        return scholarlyActivity;
    }

    public void setScholarlyActivity(ScholarlyActivity scholarlyActivity) {
        this.scholarlyActivity = scholarlyActivity;
    }

    @Override
    public String toString() {
        return "PrincipalInvestigator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", scholarlyActivity=" + scholarlyActivity +
                '}';
    }
}
