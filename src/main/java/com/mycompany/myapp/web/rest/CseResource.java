package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Cse;

import com.mycompany.myapp.repository.CseRepository;
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
 * REST controller for managing Cse.
 */
@RestController
@RequestMapping("/api")
public class CseResource {

    private final Logger log = LoggerFactory.getLogger(CseResource.class);

    private static final String ENTITY_NAME = "cse";

    private final CseRepository cseRepository;

    public CseResource(CseRepository cseRepository) {
        this.cseRepository = cseRepository;
    }

    /**
     * POST  /cses : Create a new cse.
     *
     * @param cse the cse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cse, or with status 400 (Bad Request) if the cse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cses")
    @Timed
    public ResponseEntity<Cse> createCse(@RequestBody Cse cse) throws URISyntaxException {
        log.debug("REST request to save Cse : {}", cse);
        if (cse.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cse cannot already have an ID")).body(null);
        }
        Cse result = cseRepository.save(cse);
        return ResponseEntity.created(new URI("/api/cses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cses : Updates an existing cse.
     *
     * @param cse the cse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cse,
     * or with status 400 (Bad Request) if the cse is not valid,
     * or with status 500 (Internal Server Error) if the cse couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cses")
    @Timed
    public ResponseEntity<Cse> updateCse(@RequestBody Cse cse) throws URISyntaxException {
        log.debug("REST request to update Cse : {}", cse);
        if (cse.getId() == null) {
            return createCse(cse);
        }
        Cse result = cseRepository.save(cse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cses : get all the cses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cses in body
     */
    @GetMapping("/cses")
    @Timed
    public List<Cse> getAllCses() {
        log.debug("REST request to get all Cses");
        return cseRepository.findAll();
    }

    /**
     * GET  /cses/:id : get the "id" cse.
     *
     * @param id the id of the cse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cse, or with status 404 (Not Found)
     */
    @GetMapping("/cses/{id}")
    @Timed
    public ResponseEntity<Cse> getCse(@PathVariable String id) {
        log.debug("REST request to get Cse : {}", id);
        Cse cse = cseRepository.findOne(UUID.fromString(id));
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cse));
    }

    /**
     * DELETE  /cses/:id : delete the "id" cse.
     *
     * @param id the id of the cse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCse(@PathVariable String id) {
        log.debug("REST request to delete Cse : {}", id);
        cseRepository.delete(UUID.fromString(id));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
