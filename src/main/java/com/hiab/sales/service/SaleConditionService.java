package com.hiab.sales.service;

import com.hiab.sales.service.dto.SaleConditionDTO;
import java.util.List;

/**
 * Service Interface for managing SaleCondition.
 */
public interface SaleConditionService {

    /**
     * Save a saleCondition.
     *
     * @param saleConditionDTO the entity to save
     * @return the persisted entity
     */
    SaleConditionDTO save(SaleConditionDTO saleConditionDTO);

    /**
     * Get all the saleConditions.
     *
     * @return the list of entities
     */
    List<SaleConditionDTO> findAll();

    /**
     * Get the "id" saleCondition.
     *
     * @param id the id of the entity
     * @return the entity
     */
    SaleConditionDTO findOne(Long id);

    /**
     * Delete the "id" saleCondition.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
