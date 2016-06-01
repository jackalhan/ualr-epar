package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by txcakaloglu on 6/1/16.
 */
@Entity
@Table(name = "Author")
public class Author extends AbstractAuditingEntity implements Serializable {

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
    @JoinColumn(name="publication_id", referencedColumnName = "id")
    private Publication publication;

    public Author(String name, String surname, String middleName, Publication publication) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.publication = publication;
    }

    public Author() {
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

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", publication=" + publication +
                '}';
    }
}
