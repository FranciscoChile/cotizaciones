package com.hiab.sales.service;

import com.hiab.sales.service.dto.SaleConditionsDTO;
import java.util.List;

/**
 * Service Interface for managing SaleConditions.
 */
public interface SaleConditionsService {

    /**
     * Save a saleConditions.
     *
     * @param saleConditionsDTO the entity to save
     * @return the persisted entity
     */
    SaleConditionsDTO save(SaleConditionsDTO saleConditionsDTO);

    /**
     * Get all the saleConditions.
     *
     * @return the list of entities
     */
    List<SaleConditionsDTO> findAll();

    /**
     * Get the "id" saleConditions.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SaleConditionsDTO findOne(Long id);

    /**
     * Delete the "id" saleConditions.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
