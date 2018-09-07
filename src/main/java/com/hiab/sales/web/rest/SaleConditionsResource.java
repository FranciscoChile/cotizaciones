package com.hiab.sales.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hiab.sales.service.SaleConditionsService;
import com.hiab.sales.web.rest.errors.BadRequestAlertException;
import com.hiab.sales.web.rest.util.HeaderUtil;
import com.hiab.sales.service.dto.SaleConditionsDTO;
import com.hiab.sales.service.dto.SaleConditionsCriteria;
import com.hiab.sales.service.SaleConditionsQueryService;
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

/**
 * REST controller for managing SaleConditions.
 */
@RestController
@RequestMapping("/api")
public class SaleConditionsResource {

    private final Logger log = LoggerFactory.getLogger(SaleConditionsResource.class);

    private static final String ENTITY_NAME = "saleConditions";

    private final SaleConditionsService saleConditionsService;

    private final SaleConditionsQueryService saleConditionsQueryService;

    public SaleConditionsResource(SaleConditionsService saleConditionsService, SaleConditionsQueryService saleConditionsQueryService) {
        this.saleConditionsService = saleConditionsService;
        this.saleConditionsQueryService = saleConditionsQueryService;
    }

    /**
     * POST  /sale-conditions : Create a new saleConditions.
     *
     * @param saleConditionsDTO the saleConditionsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleConditionsDTO, or with status 400 (Bad Request) if the saleConditions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-conditions")
    @Timed
    public ResponseEntity<SaleConditionsDTO> createSaleConditions(@Valid @RequestBody SaleConditionsDTO saleConditionsDTO) throws URISyntaxException {
        log.debug("REST request to save SaleConditions : {}", saleConditionsDTO);
        if (saleConditionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new saleConditions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaleConditionsDTO result = saleConditionsService.save(saleConditionsDTO);
        return ResponseEntity.created(new URI("/api/sale-conditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-conditions : Updates an existing saleConditions.
     *
     * @param saleConditionsDTO the saleConditionsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saleConditionsDTO,
     * or with status 400 (Bad Request) if the saleConditionsDTO is not valid,
     * or with status 500 (Internal Server Error) if the saleConditionsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-conditions")
    @Timed
    public ResponseEntity<SaleConditionsDTO> updateSaleConditions(@Valid @RequestBody SaleConditionsDTO saleConditionsDTO) throws URISyntaxException {
        log.debug("REST request to update SaleConditions : {}", saleConditionsDTO);
        if (saleConditionsDTO.getId() == null) {
            return createSaleConditions(saleConditionsDTO);
        }
        SaleConditionsDTO result = saleConditionsService.save(saleConditionsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saleConditionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sale-conditions : get all the saleConditions.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of saleConditions in body
     */
    @GetMapping("/sale-conditions")
    @Timed
    public ResponseEntity<List<SaleConditionsDTO>> getAllSaleConditions(SaleConditionsCriteria criteria) {
        log.debug("REST request to get SaleConditions by criteria: {}", criteria);
        List<SaleConditionsDTO> entityList = saleConditionsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /sale-conditions/:id : get the "id" saleConditions.
     *
     * @param id the id of the saleConditionsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saleConditionsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sale-conditions/{id}")
    @Timed
    public ResponseEntity<SaleConditionsDTO> getSaleConditions(@PathVariable Long id) {
        log.debug("REST request to get SaleConditions : {}", id);
        SaleConditionsDTO saleConditionsDTO = saleConditionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(saleConditionsDTO));
    }

    /**
     * DELETE  /sale-conditions/:id : delete the "id" saleConditions.
     *
     * @param id the id of the saleConditionsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-conditions/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaleConditions(@PathVariable Long id) {
        log.debug("REST request to delete SaleConditions : {}", id);
        saleConditionsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
