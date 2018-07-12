package com.hiab.sales.service.mapper;

import com.hiab.sales.domain.*;
import com.hiab.sales.service.dto.SalesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Sales and its DTO SalesDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, ContactMapper.class, LocationMapper.class, ProductMapper.class})
public interface SalesMapper extends EntityMapper<SalesDTO, Sales> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "contact.id", target = "contactId")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "product.id", target = "productId")
    SalesDTO toDto(Sales sales);

    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "contactId", target = "contact")
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "productId", target = "product")
    Sales toEntity(SalesDTO salesDTO);

    default Sales fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sales sales = new Sales();
        sales.setId(id);
        return sales;
    }
}
