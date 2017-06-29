package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Hartron;
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
 * Cassandra repository for the Hartron entity.
 */
@Repository
public class HartronRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Hartron> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public HartronRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Hartron.class);
        this.findAllStmt = session.prepare("SELECT * FROM hartron");
        this.truncateStmt = session.prepare("TRUNCATE hartron");
    }

    public List<Hartron> findAll() {
        List<Hartron> hartronsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Hartron hartron = new Hartron();
                hartron.setId(row.getUUID("id"));
                hartron.setEmployee_name(row.getString("employee_name"));
                hartron.setDesignation(row.getString("designation"));
                hartron.setPhone_no(row.getInt("phone_no"));
                return hartron;
            }
        ).forEach(hartronsList::add);
        return hartronsList;
    }

    public Hartron findOne(UUID id) {
        return mapper.get(id);
    }

    public Hartron save(Hartron hartron) {
        if (hartron.getId() == null) {
            hartron.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Hartron>> violations = validator.validate(hartron);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(hartron);
        return hartron;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
