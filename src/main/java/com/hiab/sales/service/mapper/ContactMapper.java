package com.hiab.sales.service.mapper;

import com.hiab.sales.domain.*;
import com.hiab.sales.service.dto.ContactDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Contact and its DTO ContactDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {

    @Mapping(source = "client.id", target = "clientId")
    ContactDTO toDto(Contact contact);

    @Mapping(source = "clientId", target = "client")
    @Mapping(target = "sales", ignore = true)
    Contact toEntity(ContactDTO contactDTO);

    default Contact fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }
}
