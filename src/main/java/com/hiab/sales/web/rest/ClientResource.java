package com.hiab.sales.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hiab.sales.service.ClientService;
import com.hiab.sales.web.rest.errors.BadRequestAlertException;
import com.hiab.sales.web.rest.util.HeaderUtil;
import com.hiab.sales.web.rest.util.PaginationUtil;
import com.hiab.sales.service.dto.ClientDTO;
import com.hiab.sales.service.dto.ClientCriteria;
import com.hiab.sales.service.ClientQueryService;
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
 * REST controller for managing Client.
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private static final String ENTITY_NAME = "client";

    private final ClientService clientService;

    private final ClientQueryService clientQueryService;

    public ClientResource(ClientService clientService, ClientQueryService clientQueryService) {
        this.clientService = clientService;
        this.clientQueryService = clientQueryService;
    }

    /**
     * POST  /clients : Create a new client.
     *
     * @param clientDTO the clientDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientDTO, or with status 400 (Bad Request) if the client has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/clients")
    @Timed
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to save Client : {}", clientDTO);
        if (clientDTO.getId() != null) {
            throw new BadRequestAlertException("A new client cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientDTO result = clientService.save(clientDTO);
        return ResponseEntity.created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clients : Updates an existing client.
     *
     * @param clientDTO the clientDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientDTO,
     * or with status 400 (Bad Request) if the clientDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/clients")
    @Timed
    public ResponseEntity<ClientDTO> updateClient(@Valid @RequestBody ClientDTO clientDTO) throws URISyntaxException {
        log.debug("REST request to update Client : {}", clientDTO);
        if (clientDTO.getId() == null) {
            return createClient(clientDTO);
        }
        ClientDTO result = clientService.save(clientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clientDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clients : get all the clients.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of clients in body
     */
    @GetMapping("/clients")
    @Timed
    public ResponseEntity<List<ClientDTO>> getAllClients(ClientCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Clients by criteria: {}", criteria);
        Page<ClientDTO> page = clientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clients");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clients/:id : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/clients/{id}")
    @Timed
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        ClientDTO clientDTO = clientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(clientDTO));
    }

    /**
     * DELETE  /clients/:id : delete the "id" client.
     *
     * @param id the id of the clientDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/clients/{id}")
    @Timed
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        clientService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
