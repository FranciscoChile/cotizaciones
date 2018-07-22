package com.hiab.sales.repository;

import com.hiab.sales.domain.Sales;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Sales entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesRepository extends JpaRepository<Sales, Long>, JpaSpecificationExecutor<Sales> {
    @Query("select distinct sales from Sales sales left join fetch sales.products")
    List<Sales> findAllWithEagerRelationships();

    @Query("select sales from Sales sales left join fetch sales.products where sales.id =:id")
    Sales findOneWithEagerRelationships(@Param("id") Long id);

}
