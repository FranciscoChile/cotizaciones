package com.hiab.sales.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hiab.sales.service.SalesService;
import com.hiab.sales.web.rest.errors.BadRequestAlertException;
import com.hiab.sales.web.rest.util.HeaderUtil;
import com.hiab.sales.web.rest.util.PaginationUtil;
import com.hiab.sales.service.dto.SalesDTO;
import com.hiab.sales.service.dto.SalesCriteria;
import com.hiab.sales.service.SalesQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Sales.
 */
@RestController
@RequestMapping("/api")
public class SalesResource {

    private final Logger log = LoggerFactory.getLogger(SalesResource.class);

    private static final String ENTITY_NAME = "sales";

    private final SalesService salesService;

    private final SalesQueryService salesQueryService;

    public SalesResource(SalesService salesService, SalesQueryService salesQueryService) {
        this.salesService = salesService;
        this.salesQueryService = salesQueryService;
    }

    /**
     * POST  /sales : Create a new sales.
     *
     * @param salesDTO the salesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salesDTO, or with status 400 (Bad Request) if the sales has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales")
    @Timed
    public ResponseEntity<SalesDTO> createSales(@Valid @RequestBody SalesDTO salesDTO) throws URISyntaxException {
        log.debug("REST request to save Sales : {}", salesDTO);
        if (salesDTO.getId() != null) {
            throw new BadRequestAlertException("A new sales cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalesDTO result = salesService.save(salesDTO);
        return ResponseEntity.created(new URI("/api/sales/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sales : Updates an existing sales.
     *
     * @param salesDTO the salesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salesDTO,
     * or with status 400 (Bad Request) if the salesDTO is not valid,
     * or with status 500 (Internal Server Error) if the salesDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sales")
    @Timed
    public ResponseEntity<SalesDTO> updateSales(@Valid @RequestBody SalesDTO salesDTO) throws URISyntaxException {
        log.debug("REST request to update Sales : {}", salesDTO);
        if (salesDTO.getId() == null) {
            return createSales(salesDTO);
        }
        SalesDTO result = salesService.save(salesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sales : get all the sales.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of sales in body
     */
    @GetMapping("/sales")
    @Timed
    public ResponseEntity<List<SalesDTO>> getAllSales(SalesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sales by criteria: {}", criteria);
        Page<SalesDTO> page = salesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sales");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sales/:id : get the "id" sales.
     *
     * @param id the id of the salesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salesDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sales/{id}")
    @Timed
    public ResponseEntity<SalesDTO> getSales(@PathVariable Long id) {
        log.debug("REST request to get Sales : {}", id);
        SalesDTO salesDTO = salesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salesDTO));
    }

    /**
     * DELETE  /sales/:id : delete the "id" sales.
     *
     * @param id the id of the salesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sales/{id}")
    @Timed
    public ResponseEntity<Void> deleteSales(@PathVariable Long id) {
        log.debug("REST request to delete Sales : {}", id);
        salesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
