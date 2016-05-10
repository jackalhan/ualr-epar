package com.jackalhan.ualr.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by txcakaloglu on 5/10/16.
 */
@Entity
@Table(name = "Faculty")
public class Faculty extends AbstractAuditingEntity implements Serializable{


    /*@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;*/

    //@NotNull
    private String name;

    @Id
    private String code;

    @NotNull
    private String deanNameAndSurname;


    /*public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeanNameAndSurname() {
        return deanNameAndSurname;
    }

    public void setDeanNameAndSurname(String deanNameAndSurname) {
        this.deanNameAndSurname = deanNameAndSurname;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                //"id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", deanName='" + deanNameAndSurname + '\'' +
                '}';
    }

    public Faculty(String name, String code, String deanNameAndSurname) {
        this.name = name;
        this.code = code;
        this.deanNameAndSurname = deanNameAndSurname;
    }

    public Faculty() {
    }
}
