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

import com.hiab.sales.domain.Sales;
import com.hiab.sales.domain.*; // for static metamodels
import com.hiab.sales.repository.SalesRepository;
import com.hiab.sales.service.dto.SalesCriteria;

import com.hiab.sales.service.dto.SalesDTO;
import com.hiab.sales.service.mapper.SalesMapper;

/**
 * Service for executing complex queries for Sales entities in the database.
 * The main input is a {@link SalesCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SalesDTO} or a {@link Page} of {@link SalesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SalesQueryService extends QueryService<Sales> {

    private final Logger log = LoggerFactory.getLogger(SalesQueryService.class);


    private final SalesRepository salesRepository;

    private final SalesMapper salesMapper;

    public SalesQueryService(SalesRepository salesRepository, SalesMapper salesMapper) {
        this.salesRepository = salesRepository;
        this.salesMapper = salesMapper;
    }

    /**
     * Return a {@link List} of {@link SalesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SalesDTO> findByCriteria(SalesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Sales> specification = createSpecification(criteria);
        return salesMapper.toDto(salesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SalesDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SalesDTO> findByCriteria(SalesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Sales> specification = createSpecification(criteria);
        final Page<Sales> result = salesRepository.findAll(specification, page);
        return result.map(salesMapper::toDto);
    }

    /**
     * Function to convert SalesCriteria to a {@link Specifications}
     */
    private Specifications<Sales> createSpecification(SalesCriteria criteria) {
        Specifications<Sales> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Sales_.id));
            }
            if (criteria.getFinalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFinalPrice(), Sales_.finalPrice));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Sales_.createDate));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActive(), Sales_.active));
            }
            if (criteria.getConditions() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConditions(), Sales_.conditions));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), Sales_.userId));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getClientId(), Sales_.client, Client_.id));
            }
            if (criteria.getContactId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getContactId(), Sales_.contact, Contact_.id));
            }
            if (criteria.getLocationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLocationId(), Sales_.location, Location_.id));
            }
            if (criteria.getSaleConditionId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSaleConditionId(), Sales_.saleConditions, SaleCondition_.id));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProductId(), Sales_.products, Product_.id));
            }
        }
        return specification;
    }

}
