package com.hiab.sales.repository;

import com.hiab.sales.domain.SaleConditions;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SaleConditions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaleConditionsRepository extends JpaRepository<SaleConditions, Long>, JpaSpecificationExecutor<SaleConditions> {

}
