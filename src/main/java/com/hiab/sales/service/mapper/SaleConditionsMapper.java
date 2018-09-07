package com.hiab.sales.service.mapper;

import com.hiab.sales.domain.*;
import com.hiab.sales.service.dto.SaleConditionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SaleConditions and its DTO SaleConditionsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SaleConditionsMapper extends EntityMapper<SaleConditionsDTO, SaleConditions> {


    @Mapping(target = "sales", ignore = true)
    SaleConditions toEntity(SaleConditionsDTO saleConditionsDTO);

    default SaleConditions fromId(Long id) {
        if (id == null) {
            return null;
        }
        SaleConditions saleConditions = new SaleConditions();
        saleConditions.setId(id);
        return saleConditions;
    }
}
