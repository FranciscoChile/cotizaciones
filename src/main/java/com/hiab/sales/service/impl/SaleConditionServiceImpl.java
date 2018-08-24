package com.hiab.sales.service.impl;

import com.hiab.sales.service.SaleConditionService;
import com.hiab.sales.domain.SaleCondition;
import com.hiab.sales.repository.SaleConditionRepository;
import com.hiab.sales.service.dto.SaleConditionDTO;
import com.hiab.sales.service.mapper.SaleConditionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SaleCondition.
 */
@Service
@Transactional
public class SaleConditionServiceImpl implements SaleConditionService {

    private final Logger log = LoggerFactory.getLogger(SaleConditionServiceImpl.class);

    private final SaleConditionRepository saleConditionRepository;

    private final SaleConditionMapper saleConditionMapper;

    public SaleConditionServiceImpl(SaleConditionRepository saleConditionRepository, SaleConditionMapper saleConditionMapper) {
        this.saleConditionRepository = saleConditionRepository;
        this.saleConditionMapper = saleConditionMapper;
    }

    /**
     * Save a saleCondition.
     *
     * @param saleConditionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SaleConditionDTO save(SaleConditionDTO saleConditionDTO) {
        log.debug("Request to save SaleCondition : {}", saleConditionDTO);
        SaleCondition saleCondition = saleConditionMapper.toEntity(saleConditionDTO);
        saleCondition = saleConditionRepository.save(saleCondition);
        return saleConditionMapper.toDto(saleCondition);
    }

    /**
     * Get all the saleConditions.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SaleConditionDTO> findAll() {
        log.debug("Request to get all SaleConditions");
        return saleConditionRepository.findAll().stream()
            .map(saleConditionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one saleCondition by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SaleConditionDTO findOne(Long id) {
        log.debug("Request to get SaleCondition : {}", id);
        SaleCondition saleCondition = saleConditionRepository.findOne(id);
        return saleConditionMapper.toDto(saleCondition);
    }

    /**
     * Delete the saleCondition by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SaleCondition : {}", id);
        saleConditionRepository.delete(id);
    }
}
