package com.hiab.sales.service.mapper;

import com.hiab.sales.domain.*;
import com.hiab.sales.service.dto.LocationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Location and its DTO LocationDTO.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {

    @Mapping(source = "client.id", target = "clientId")
    LocationDTO toDto(Location location);

    @Mapping(source = "clientId", target = "client")
    @Mapping(target = "sales", ignore = true)
    Location toEntity(LocationDTO locationDTO);

    default Location fromId(Long id) {
        if (id == null) {
            return null;
        }
        Location location = new Location();
        location.setId(id);
        return location;
    }
}
