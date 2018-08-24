package com.hiab.sales.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.hiab.sales.domain.SaleCondition;
import com.hiab.sales.domain.*; // for static metamodels
import com.hiab.sales.repository.SaleConditionRepository;
import com.hiab.sales.service.dto.SaleConditionCriteria;

import com.hiab.sales.service.dto.SaleConditionDTO;
import com.hiab.sales.service.mapper.SaleConditionMapper;

/**
 * Service for executing complex queries for SaleCondition entities in the database.
 * The main input is a {@link SaleConditionCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SaleConditionDTO} or a {@link Page} of {@link SaleConditionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SaleConditionQueryService extends QueryService<SaleCondition> {

    private final Logger log = LoggerFactory.getLogger(SaleConditionQueryService.class);


    private final SaleConditionRepository saleConditionRepository;

    private final SaleConditionMapper saleConditionMapper;

    public SaleConditionQueryService(SaleConditionRepository saleConditionRepository, SaleConditionMapper saleConditionMapper) {
        this.saleConditionRepository = saleConditionRepository;
        this.saleConditionMapper = saleConditionMapper;
    }

    /**
     * Return a {@link List} of {@link SaleConditionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SaleConditionDTO> findByCriteria(SaleConditionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SaleCondition> specification = createSpecification(criteria);
        return saleConditionMapper.toDto(saleConditionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SaleConditionDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SaleConditionDTO> findByCriteria(SaleConditionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SaleCondition> specification = createSpecification(criteria);
        final Page<SaleCondition> result = saleConditionRepository.findAll(specification, page);
        return result.map(saleConditionMapper::toDto);
    }

    /**
     * Function to convert SaleConditionCriteria to a {@link Specifications}
     */
    private Specifications<SaleCondition> createSpecification(SaleConditionCriteria criteria) {
        Specifications<SaleCondition> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SaleCondition_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), SaleCondition_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), SaleCondition_.value));
            }
            if (criteria.getSalesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSalesId(), SaleCondition_.sales, Sales_.id));
            }
        }
        return specification;
    }

}
