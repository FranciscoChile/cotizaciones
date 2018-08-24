package com.hiab.sales.repository;

import com.hiab.sales.domain.SaleCondition;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SaleCondition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleConditionRepository extends JpaRepository<SaleCondition, Long>, JpaSpecificationExecutor<SaleCondition> {

}
