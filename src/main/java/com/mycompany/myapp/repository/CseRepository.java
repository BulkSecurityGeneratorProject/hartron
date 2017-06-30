package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cse;
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
 * Cassandra repository for the Cse entity.
 */
@Repository
public class CseRepository {

    private final Session session;

    private final Validator validator;

    private Mapper<Cse> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    public CseRepository(Session session, Validator validator) {
        this.session = session;
        this.validator = validator;
        this.mapper = new MappingManager(session).mapper(Cse.class);
        this.findAllStmt = session.prepare("SELECT * FROM cse");
        this.truncateStmt = session.prepare("TRUNCATE cse");
    }

    public List<Cse> findAll() {
        List<Cse> csesList = new ArrayList<>();
        BoundStatement stmt = findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Cse cse = new Cse();
                cse.setId(row.getUUID("id"));
                cse.setDept(row.getString("dept"));
                cse.setBranch(row.getString("branch"));
                return cse;
            }
        ).forEach(csesList::add);
        return csesList;
    }

    public Cse findOne(UUID id) {
        return mapper.get(id);
    }

    public Cse save(Cse cse) {
        if (cse.getId() == null) {
            cse.setId(UUID.randomUUID());
        }
        Set<ConstraintViolation<Cse>> violations = validator.validate(cse);
        if (violations != null && !violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        mapper.save(cse);
        return cse;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt = truncateStmt.bind();
        session.execute(stmt);
    }
}
