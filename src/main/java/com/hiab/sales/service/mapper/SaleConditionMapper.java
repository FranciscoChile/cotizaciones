package com.hiab.sales.service.mapper;

import com.hiab.sales.domain.*;
import com.hiab.sales.service.dto.SaleConditionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SaleCondition and its DTO SaleConditionDTO.
 */
@Mapper(componentModel = "spring", uses = {SalesMapper.class})
public interface SaleConditionMapper extends EntityMapper<SaleConditionDTO, SaleCondition> {

    @Mapping(source = "sales.id", target = "salesId")
    SaleConditionDTO toDto(SaleCondition saleCondition);

    @Mapping(source = "salesId", target = "sales")
    SaleCondition toEntity(SaleConditionDTO saleConditionDTO);

    default SaleCondition fromId(Long id) {
        if (id == null) {
            return null;
        }
        SaleCondition saleCondition = new SaleCondition();
        saleCondition.setId(id);
        return saleCondition;
    }
}
