package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Hartron;

import com.mycompany.myapp.repository.HartronRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing Hartron.
 */
@RestController
@RequestMapping("/api")
public class HartronResource {

    private final Logger log = LoggerFactory.getLogger(HartronResource.class);

    private static final String ENTITY_NAME = "hartron";

    private final HartronRepository hartronRepository;

    public HartronResource(HartronRepository hartronRepository) {
        this.hartronRepository = hartronRepository;
    }

    /**
     * POST  /hartrons : Create a new hartron.
     *
     * @param hartron the hartron to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hartron, or with status 400 (Bad Request) if the hartron has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hartrons")
    @Timed
    public ResponseEntity<Hartron> createHartron(@RequestBody Hartron hartron) throws URISyntaxException {
        log.debug("REST request to save Hartron : {}", hartron);
        if (hartron.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new hartron cannot already have an ID")).body(null);
        }
        Hartron result = hartronRepository.save(hartron);
        return ResponseEntity.created(new URI("/api/hartrons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hartrons : Updates an existing hartron.
     *
     * @param hartron the hartron to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hartron,
     * or with status 400 (Bad Request) if the hartron is not valid,
     * or with status 500 (Internal Server Error) if the hartron couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hartrons")
    @Timed
    public ResponseEntity<Hartron> updateHartron(@RequestBody Hartron hartron) throws URISyntaxException {
        log.debug("REST request to update Hartron : {}", hartron);
        if (hartron.getId() == null) {
            return createHartron(hartron);
        }
        Hartron result = hartronRepository.save(hartron);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hartron.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hartrons : get all the hartrons.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of hartrons in body
     */
    @GetMapping("/hartrons")
    @Timed
    public List<Hartron> getAllHartrons() {
        log.debug("REST request to get all Hartrons");
        return hartronRepository.findAll();
    }

    /**
     * GET  /hartrons/:id : get the "id" hartron.
     *
     * @param id the id of the hartron to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hartron, or with status 404 (Not Found)
     */
    @GetMapping("/hartrons/{id}")
    @Timed
    public ResponseEntity<Hartron> getHartron(@PathVariable String id) {
        log.debug("REST request to get Hartron : {}", id);
        Hartron hartron = hartronRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hartron));
    }

    /**
     * DELETE  /hartrons/:id : delete the "id" hartron.
     *
     * @param id the id of the hartron to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hartrons/{id}")
    @Timed
    public ResponseEntity<Void> deleteHartron(@PathVariable String id) {
        log.debug("REST request to delete Hartron : {}", id);
        hartronRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
