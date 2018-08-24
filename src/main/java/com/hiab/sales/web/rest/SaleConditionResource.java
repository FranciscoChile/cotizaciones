package com.hiab.sales.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hiab.sales.service.SaleConditionService;
import com.hiab.sales.web.rest.errors.BadRequestAlertException;
import com.hiab.sales.web.rest.util.HeaderUtil;
import com.hiab.sales.service.dto.SaleConditionDTO;
import com.hiab.sales.service.dto.SaleConditionCriteria;
import com.hiab.sales.service.SaleConditionQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing SaleCondition.
 */
@RestController
@RequestMapping("/api")
public class SaleConditionResource {

    private final Logger log = LoggerFactory.getLogger(SaleConditionResource.class);

    private static final String ENTITY_NAME = "saleCondition";

    private final SaleConditionService saleConditionService;

    private final SaleConditionQueryService saleConditionQueryService;

    public SaleConditionResource(SaleConditionService saleConditionService, SaleConditionQueryService saleConditionQueryService) {
        this.saleConditionService = saleConditionService;
        this.saleConditionQueryService = saleConditionQueryService;
    }

    /**
     * POST  /sale-conditions : Create a new saleCondition.
     *
     * @param saleConditionDTO the saleConditionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleConditionDTO, or with status 400 (Bad Request) if the saleCondition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sale-conditions")
    @Timed
    public ResponseEntity<SaleConditionDTO> createSaleCondition(@RequestBody SaleConditionDTO saleConditionDTO) throws URISyntaxException {
        log.debug("REST request to save SaleCondition : {}", saleConditionDTO);
        if (saleConditionDTO.getId() != null) {
            throw new BadRequestAlertException("A new saleCondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaleConditionDTO result = saleConditionService.save(saleConditionDTO);
        return ResponseEntity.created(new URI("/api/sale-conditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sale-conditions : Updates an existing saleCondition.
     *
     * @param saleConditionDTO the saleConditionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated saleConditionDTO,
     * or with status 400 (Bad Request) if the saleConditionDTO is not valid,
     * or with status 500 (Internal Server Error) if the saleConditionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sale-conditions")
    @Timed
    public ResponseEntity<SaleConditionDTO> updateSaleCondition(@RequestBody SaleConditionDTO saleConditionDTO) throws URISyntaxException {
        log.debug("REST request to update SaleCondition : {}", saleConditionDTO);
        if (saleConditionDTO.getId() == null) {
            return createSaleCondition(saleConditionDTO);
        }
        SaleConditionDTO result = saleConditionService.save(saleConditionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, saleConditionDTO.getId().toString()))
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
    public ResponseEntity<List<SaleConditionDTO>> getAllSaleConditions(SaleConditionCriteria criteria) {
        log.debug("REST request to get SaleConditions by criteria: {}", criteria);
        List<SaleConditionDTO> entityList = saleConditionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /sale-conditions/:id : get the "id" saleCondition.
     *
     * @param id the id of the saleConditionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the saleConditionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sale-conditions/{id}")
    @Timed
    public ResponseEntity<SaleConditionDTO> getSaleCondition(@PathVariable Long id) {
        log.debug("REST request to get SaleCondition : {}", id);
        SaleConditionDTO saleConditionDTO = saleConditionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(saleConditionDTO));
    }

    /**
     * DELETE  /sale-conditions/:id : delete the "id" saleCondition.
     *
     * @param id the id of the saleConditionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sale-conditions/{id}")
    @Timed
    public ResponseEntity<Void> deleteSaleCondition(@PathVariable Long id) {
        log.debug("REST request to delete SaleCondition : {}", id);
        saleConditionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
