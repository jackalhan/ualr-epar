package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by txcakaloglu on 6/1/16.
 */
@Entity
@Table(name = "Publication")
public class Publication extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false)
    private long id;

    @NotNull
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", referencedColumnName = "id")
    private Employee employee;

    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Author> authorList;

    @NotNull
    private String articleTitle;

    @NotNull
    private String status;

    @NotNull
    private String title;

    private int year;

    private String volume;

    private String issue;

    private int pgs;

    private String publisher;

    private String dates;

    private int citedBy;

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

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public int getPgs() {
        return pgs;
    }

    public void setPgs(int pgs) {
        this.pgs = pgs;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getCitedBy() {
        return citedBy;
    }

    public void setCitedBy(int citedBy) {
        this.citedBy = citedBy;
    }

    public Publication(String type, Employee employee, List<Author> authorList, String articleTitle, String status, String title, int year, String volume, String issue, int pgs, String publisher, String dates, int citedBy) {
        this.type = type;
        this.employee = employee;
        this.authorList = authorList;
        this.articleTitle = articleTitle;
        this.status = status;
        this.title = title;
        this.year = year;
        this.volume = volume;
        this.issue = issue;
        this.pgs = pgs;
        this.publisher = publisher;
        this.dates = dates;
        this.citedBy = citedBy;
    }

    public Publication() {
    }

    @Override
    public String toString() {
        return "Publication{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", employee=" + employee +
                ", authorList=" + authorList +
                ", articleTitle='" + articleTitle + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", volume='" + volume + '\'' +
                ", issue='" + issue + '\'' +
                ", pgs=" + pgs +
                ", publisher='" + publisher + '\'' +
                ", dates='" + dates + '\'' +
                ", citedBy=" + citedBy +
                '}';
    }
}
