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

import com.hiab.sales.domain.Location;
import com.hiab.sales.domain.*; // for static metamodels
import com.hiab.sales.repository.LocationRepository;
import com.hiab.sales.service.dto.LocationCriteria;

import com.hiab.sales.service.dto.LocationDTO;
import com.hiab.sales.service.mapper.LocationMapper;

/**
 * Service for executing complex queries for Location entities in the database.
 * The main input is a {@link LocationCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LocationDTO} or a {@link Page} of {@link LocationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocationQueryService extends QueryService<Location> {

    private final Logger log = LoggerFactory.getLogger(LocationQueryService.class);


    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    public LocationQueryService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    /**
     * Return a {@link List} of {@link LocationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LocationDTO> findByCriteria(LocationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Location> specification = createSpecification(criteria);
        return locationMapper.toDto(locationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LocationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LocationDTO> findByCriteria(LocationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Location> specification = createSpecification(criteria);
        final Page<Location> result = locationRepository.findAll(specification, page);
        return result.map(locationMapper::toDto);
    }

    /**
     * Function to convert LocationCriteria to a {@link Specifications}
     */
    private Specifications<Location> createSpecification(LocationCriteria criteria) {
        Specifications<Location> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Location_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Location_.description));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Location_.createDate));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActive(), Location_.active));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getClientId(), Location_.client, Client_.id));
            }
            if (criteria.getSalesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSalesId(), Location_.sales, Sales_.id));
            }
        }
        return specification;
    }

}
