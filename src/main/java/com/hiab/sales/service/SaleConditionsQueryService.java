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

import com.hiab.sales.domain.SaleConditions;
import com.hiab.sales.domain.*; // for static metamodels
import com.hiab.sales.repository.SaleConditionsRepository;
import com.hiab.sales.service.dto.SaleConditionsCriteria;

import com.hiab.sales.service.dto.SaleConditionsDTO;
import com.hiab.sales.service.mapper.SaleConditionsMapper;

/**
 * Service for executing complex queries for SaleConditions entities in the database.
 * The main input is a {@link SaleConditionsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SaleConditionsDTO} or a {@link Page} of {@link SaleConditionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SaleConditionsQueryService extends QueryService<SaleConditions> {

    private final Logger log = LoggerFactory.getLogger(SaleConditionsQueryService.class);


    private final SaleConditionsRepository saleConditionsRepository;

    private final SaleConditionsMapper saleConditionsMapper;

    public SaleConditionsQueryService(SaleConditionsRepository saleConditionsRepository, SaleConditionsMapper saleConditionsMapper) {
        this.saleConditionsRepository = saleConditionsRepository;
        this.saleConditionsMapper = saleConditionsMapper;
    }

    /**
     * Return a {@link List} of {@link SaleConditionsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SaleConditionsDTO> findByCriteria(SaleConditionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<SaleConditions> specification = createSpecification(criteria);
        return saleConditionsMapper.toDto(saleConditionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SaleConditionsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SaleConditionsDTO> findByCriteria(SaleConditionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<SaleConditions> specification = createSpecification(criteria);
        final Page<SaleConditions> result = saleConditionsRepository.findAll(specification, page);
        return result.map(saleConditionsMapper::toDto);
    }

    /**
     * Function to convert SaleConditionsCriteria to a {@link Specifications}
     */
    private Specifications<SaleConditions> createSpecification(SaleConditionsCriteria criteria) {
        Specifications<SaleConditions> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), SaleConditions_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), SaleConditions_.key));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), SaleConditions_.value));
            }
            if (criteria.getSalesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSalesId(), SaleConditions_.sales, Sales_.id));
            }
        }
        return specification;
    }

}
