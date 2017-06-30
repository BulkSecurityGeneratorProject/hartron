package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Cse.
 */
@Table(name = "cse")
public class Cse implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    private String dept;

    private String branch;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDept() {
        return dept;
    }

    public Cse dept(String dept) {
        this.dept = dept;
        return this;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getBranch() {
        return branch;
    }

    public Cse branch(String branch) {
        this.branch = branch;
        return this;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cse cse = (Cse) o;
        if (cse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cse{" +
            "id=" + getId() +
            ", dept='" + getDept() + "'" +
            ", branch='" + getBranch() + "'" +
            "}";
    }
}
