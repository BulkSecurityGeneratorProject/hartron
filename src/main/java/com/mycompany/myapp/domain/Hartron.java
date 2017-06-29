package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Hartron.
 */
@Table(name = "hartron")
public class Hartron implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    private String employee_name;

    private String designation;

    private Integer phone_no;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public Hartron employee_name(String employee_name) {
        this.employee_name = employee_name;
        return this;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getDesignation() {
        return designation;
    }

    public Hartron designation(String designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getPhone_no() {
        return phone_no;
    }

    public Hartron phone_no(Integer phone_no) {
        this.phone_no = phone_no;
        return this;
    }

    public void setPhone_no(Integer phone_no) {
        this.phone_no = phone_no;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hartron hartron = (Hartron) o;
        if (hartron.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), hartron.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Hartron{" +
            "id=" + getId() +
            ", employee_name='" + getEmployee_name() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", phone_no='" + getPhone_no() + "'" +
            "}";
    }
}
