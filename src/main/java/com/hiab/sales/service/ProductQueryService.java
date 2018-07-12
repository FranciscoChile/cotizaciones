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

import com.hiab.sales.domain.Product;
import com.hiab.sales.domain.*; // for static metamodels
import com.hiab.sales.repository.ProductRepository;
import com.hiab.sales.service.dto.ProductCriteria;

import com.hiab.sales.service.dto.ProductDTO;
import com.hiab.sales.service.mapper.ProductMapper;

/**
 * Service for executing complex queries for Product entities in the database.
 * The main input is a {@link ProductCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);


    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Product> specification = createSpecification(criteria);
        final Page<Product> result = productRepository.findAll(specification, page);
        return result.map(productMapper::toDto);
    }

    /**
     * Function to convert ProductCriteria to a {@link Specifications}
     */
    private Specifications<Product> createSpecification(ProductCriteria criteria) {
        Specifications<Product> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModel(), Product_.model));
            }
            if (criteria.getPriceList() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriceList(), Product_.priceList));
            }
            if (criteria.getStock() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStock(), Product_.stock));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Product_.description));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Product_.createDate));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActive(), Product_.active));
            }
            if (criteria.getSalesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSalesId(), Product_.sales, Sales_.id));
            }
        }
        return specification;
    }

}
