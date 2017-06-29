package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Yug;
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
 * Cassandra repository for the Yug entity.
 */
@Repository
public class YugRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Yug> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public YugRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Yug.class);
        this.findAllStmt = session.prepare("SELECT * FROM yug");
        this.truncateStmt = session.prepare("TRUNCATE yug");
    }

    public List<Yug> findAll() {
        List<Yug> yugsList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Yug yug = new Yug();
                yug.setId(row.getUUID("id"));
                yug.setName(row.getString("name"));
                yug.setSex(row.getString("sex"));
                yug.setEmail(row.getString("email"));
                yug.setAge(row.getInt("age"));
                return yug;
            }
        ).forEach(yugsList::add);
        return yugsList;
    }

    public Yug findOne(UUID id) {
        return mapper.get(id);
    }

    public Yug save(Yug yug) {
        if (yug.getId() == null) {
            yug.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Yug>> violations = validator.validate(yug);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(yug);
        return yug;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
