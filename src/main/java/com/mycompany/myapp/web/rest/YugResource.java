package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Yug;

import com.mycompany.myapp.repository.YugRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Yug.
 */
@RestController
@RequestMapping("/api")
public class YugResource {

    private final Logger log = LoggerFactory.getLogger(YugResource.class);

    private static final String ENTITY_NAME = "yug";

    private final YugRepository yugRepository;

    public YugResource(YugRepository yugRepository) {
        this.yugRepository = yugRepository;
    }

    /**
     * POST  /yugs : Create a new yug.
     *
     * @param yug the yug to create
     * @return the ResponseEntity with status 201 (Created) and with body the new yug, or with status 400 (Bad Request) if the yug has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/yugs")
    @Timed
    public ResponseEntity<Yug> createYug(@Valid @RequestBody Yug yug) throws URISyntaxException {
        log.debug("REST request to save Yug : {}", yug);
        if (yug.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new yug cannot already have an ID")).body(null);
        }
        Yug result = yugRepository.save(yug);
        return ResponseEntity.created(new URI("/api/yugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /yugs : Updates an existing yug.
     *
     * @param yug the yug to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated yug,
     * or with status 400 (Bad Request) if the yug is not valid,
     * or with status 500 (Internal Server Error) if the yug couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/yugs")
    @Timed
    public ResponseEntity<Yug> updateYug(@Valid @RequestBody Yug yug) throws URISyntaxException {
        log.debug("REST request to update Yug : {}", yug);
        if (yug.getId() == null) {
            return createYug(yug);
        }
        Yug result = yugRepository.save(yug);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, yug.getId().toString()))
            .body(result);
    }

    /**
     * GET  /yugs : get all the yugs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of yugs in body
     */
    @GetMapping("/yugs")
    @Timed
    public List<Yug> getAllYugs() {
        log.debug("REST request to get all Yugs");
        return yugRepository.findAll();
    }

    /**
     * GET  /yugs/:id : get the "id" yug.
     *
     * @param id the id of the yug to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the yug, or with status 404 (Not Found)
     */
    @GetMapping("/yugs/{id}")
    @Timed
    public ResponseEntity<Yug> getYug(@PathVariable String id) {
        log.debug("REST request to get Yug : {}", id);
        Yug yug = yugRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(yug));
    }

    /**
     * DELETE  /yugs/:id : delete the "id" yug.
     *
     * @param id the id of the yug to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/yugs/{id}")
    @Timed
    public ResponseEntity<Void> deleteYug(@PathVariable String id) {
        log.debug("REST request to delete Yug : {}", id);
        yugRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
