package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Project;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Cassandra repository for the Project entity.
 */
@Repository
public class ProjectRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Project> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public ProjectRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Project.class);
        this.findAllStmt = session.prepare("SELECT * FROM project");
        this.truncateStmt = session.prepare("TRUNCATE project");
    }

    public List<Project> findAll() {
        List<Project> projectsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Project project = new Project();
                project.setId(row.getUUID("id"));
                project.setProject_name(row.getString("project_name"));
                project.setDuration(row.getInt("duration"));
                return project;
            }
        ).forEach(projectsList::add);
        return projectsList;
    }

    public Project findOne(UUID id) {
        return mapper.get(id);
    }

    public Project save(Project project) {
        if (project.getId() == null) {
            project.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(project);
        return project;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
