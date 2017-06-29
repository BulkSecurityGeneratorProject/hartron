package com.mycompany.myapp.domain;

import com.datastax.driver.mapping.annotations.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Project.
 */
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    @PartitionKey
    private UUID id;

    private String project_name;

    private Integer duration;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProject_name() {
        return project_name;
    }

    public Project project_name(String project_name) {
        this.project_name = project_name;
        return this;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public Integer getDuration() {
        return duration;
    }

    public Project duration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", project_name='" + getProject_name() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
