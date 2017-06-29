package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Yug.
 */
@Table(name = "yug")
public class Yug implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    @NotNull
    private String name;

    private String sex;

    private String email;

    @Min(value = 20)
    private Integer age;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Yug name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public Yug sex(String sex) {
        this.sex = sex;
        return this;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public Yug email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public Yug age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Yug yug = (Yug) o;
        if (yug.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), yug.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Yug{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sex='" + getSex() + "'" +
            ", email='" + getEmail() + "'" +
            ", age='" + getAge() + "'" +
            "}";
    }
}
