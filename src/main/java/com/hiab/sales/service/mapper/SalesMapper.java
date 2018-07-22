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
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "contact.name", target = "contactName")
    @Mapping(source = "contact.surname", target = "contactSurname")
    @Mapping(source = "location.description", target = "locationDescription")
    @Mapping(source = "client.address", target = "clientAddress")
    @Mapping(source = "client.numDocument", target = "clientNumDocument")
    @Mapping(source = "contact.cellphone", target = "contactCellPhone")
    @Mapping(source = "contact.email", target = "contactEmail")
    SalesDTO toDto(Sales sales);

    @Mapping(source = "clientId", target = "client")
    @Mapping(source = "contactId", target = "contact")
    @Mapping(source = "locationId", target = "location")
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
