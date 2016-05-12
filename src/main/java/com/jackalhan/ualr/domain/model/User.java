package com.jackalhan.ualr.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jackalhan on 5/11/16.
 */
@Entity
@Table(name = "User")
public class User extends AbstractAuditingEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String netId;

    private String username;

    @NotNull
    private String nameAndSurname;

    @NotNull
    private String emailAddr;

    @NotNull
    private boolean enabled = true;

    @NotNull
    private boolean locked = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinTable(name="UserRole" , //New generated middle table between two tables.
               joinColumns = @JoinColumn(   // first column details of newly generatedTable that is UserRole, referenced to user entity
                       name = "userId",
                       referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(  // second column details of newly generatedTable that is UserRole, referenced to role entity
                        name = "roleId",
                        referencedColumnName = "id")

    )//we do not want to refresh or update userRoles entity therefore this pattern used
    private List<Role> userRoles;

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNameAndSurname() {
        return nameAndSurname;
    }

    public void setNameAndSurname(String nameAndSurname) {
        this.nameAndSurname = nameAndSurname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", netId='" + netId + '\'' +
                ", username='" + username + '\'' +
                ", nameAndSurname='" + nameAndSurname + '\'' +
                ", emailAddr='" + emailAddr + '\'' +
                ", enabled=" + enabled +
                ", locked=" + locked +
                ", userRoles=" + userRoles +
                '}';
    }
}
