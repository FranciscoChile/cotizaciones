package com.hiab.sales.service.impl;

import com.hiab.sales.service.SalesService;
import com.hiab.sales.domain.Sales;
import com.hiab.sales.repository.SalesRepository;
import com.hiab.sales.service.dto.SalesDTO;
import com.hiab.sales.service.mapper.SalesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Sales.
 */
@Service
@Transactional
public class SalesServiceImpl implements SalesService {

    private final Logger log = LoggerFactory.getLogger(SalesServiceImpl.class);

    private final SalesRepository salesRepository;

    private final SalesMapper salesMapper;

    public SalesServiceImpl(SalesRepository salesRepository, SalesMapper salesMapper) {
        this.salesRepository = salesRepository;
        this.salesMapper = salesMapper;
    }

    /**
     * Save a sales.
     *
     * @param salesDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public SalesDTO save(SalesDTO salesDTO) {
        log.debug("Request to save Sales : {}", salesDTO);
        Sales sales = salesMapper.toEntity(salesDTO);
        sales = salesRepository.save(sales);
        return salesMapper.toDto(sales);
    }

    /**
     * Get all the sales.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SalesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sales");
        return salesRepository.findAll(pageable)
            .map(salesMapper::toDto);
    }

    /**
     * Get one sales by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SalesDTO findOne(Long id) {
        log.debug("Request to get Sales : {}", id);
        Sales sales = salesRepository.findOne(id);
        return salesMapper.toDto(sales);
    }

    /**
     * Delete the sales by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sales : {}", id);
        salesRepository.delete(id);
    }
}
