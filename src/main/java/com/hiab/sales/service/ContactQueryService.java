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

import com.hiab.sales.domain.Contact;
import com.hiab.sales.domain.*; // for static metamodels
import com.hiab.sales.repository.ContactRepository;
import com.hiab.sales.service.dto.ContactCriteria;

import com.hiab.sales.service.dto.ContactDTO;
import com.hiab.sales.service.mapper.ContactMapper;

/**
 * Service for executing complex queries for Contact entities in the database.
 * The main input is a {@link ContactCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContactDTO} or a {@link Page} of {@link ContactDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContactQueryService extends QueryService<Contact> {

    private final Logger log = LoggerFactory.getLogger(ContactQueryService.class);


    private final ContactRepository contactRepository;

    private final ContactMapper contactMapper;

    public ContactQueryService(ContactRepository contactRepository, ContactMapper contactMapper) {
        this.contactRepository = contactRepository;
        this.contactMapper = contactMapper;
    }

    /**
     * Return a {@link List} of {@link ContactDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContactDTO> findByCriteria(ContactCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Contact> specification = createSpecification(criteria);
        return contactMapper.toDto(contactRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContactDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContactDTO> findByCriteria(ContactCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Contact> specification = createSpecification(criteria);
        final Page<Contact> result = contactRepository.findAll(specification, page);
        return result.map(contactMapper::toDto);
    }

    /**
     * Function to convert ContactCriteria to a {@link Specifications}
     */
    private Specifications<Contact> createSpecification(ContactCriteria criteria) {
        Specifications<Contact> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Contact_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Contact_.name));
            }
            if (criteria.getSurname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSurname(), Contact_.surname));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), Contact_.position));
            }
            if (criteria.getArea() != null) {
                specification = specification.and(buildStringSpecification(criteria.getArea(), Contact_.area));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Contact_.address));
            }
            if (criteria.getCellphone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCellphone(), Contact_.cellphone));
            }
            if (criteria.getLinePhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinePhone(), Contact_.linePhone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Contact_.email));
            }
            if (criteria.getComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComments(), Contact_.comments));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Contact_.createDate));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActive(), Contact_.active));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getClientId(), Contact_.client, Client_.id));
            }
            if (criteria.getSalesId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getSalesId(), Contact_.sales, Sales_.id));
            }
        }
        return specification;
    }

}
