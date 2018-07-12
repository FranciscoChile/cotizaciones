package com.hiab.sales.service;

import com.hiab.sales.service.dto.SalesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Sales.
 */
public interface SalesService {

    /**
     * Save a sales.
     *
     * @param salesDTO the entity to save
     * @return the persisted entity
     */
    SalesDTO save(SalesDTO salesDTO);

    /**
     * Get all the sales.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<SalesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sales.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SalesDTO findOne(Long id);

    /**
     * Delete the "id" sales.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
