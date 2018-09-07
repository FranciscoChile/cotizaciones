package com.hiab.sales.service.impl;

import com.hiab.sales.service.SaleConditionsService;
import com.hiab.sales.domain.SaleConditions;
import com.hiab.sales.repository.SaleConditionsRepository;
import com.hiab.sales.service.dto.SaleConditionsDTO;
import com.hiab.sales.service.mapper.SaleConditionsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SaleConditions.
 */
@Service
@Transactional
public class SaleConditionsServiceImpl implements SaleConditionsService {

    private final Logger log = LoggerFactory.getLogger(SaleConditionsServiceImpl.class);

    private final SaleConditionsRepository saleConditionsRepository;

    private final SaleConditionsMapper saleConditionsMapper;

    public SaleConditionsServiceImpl(SaleConditionsRepository saleConditionsRepository, SaleConditionsMapper saleConditionsMapper) {
        this.saleConditionsRepository = saleConditionsRepository;
        this.saleConditionsMapper = saleConditionsMapper;
    }

    /**
     * Save a saleConditions.
     *
     * @param saleConditionsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SaleConditionsDTO save(SaleConditionsDTO saleConditionsDTO) {
        log.debug("Request to save SaleConditions : {}", saleConditionsDTO);
        SaleConditions saleConditions = saleConditionsMapper.toEntity(saleConditionsDTO);
        saleConditions = saleConditionsRepository.save(saleConditions);
        return saleConditionsMapper.toDto(saleConditions);
    }

    /**
     * Get all the saleConditions.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SaleConditionsDTO> findAll() {
        log.debug("Request to get all SaleConditions");
        return saleConditionsRepository.findAll().stream()
            .map(saleConditionsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one saleConditions by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SaleConditionsDTO findOne(Long id) {
        log.debug("Request to get SaleConditions : {}", id);
        SaleConditions saleConditions = saleConditionsRepository.findOne(id);
        return saleConditionsMapper.toDto(saleConditions);
    }

    /**
     * Delete the saleConditions by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SaleConditions : {}", id);
        saleConditionsRepository.delete(id);
    }
}
