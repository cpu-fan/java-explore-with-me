package ru.practicum.ewm.mapper.event;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.dto.event.LocationDto;
import ru.practicum.ewm.model.event.Location;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LocationMapper {

    Location toEntity(LocationDto locationDto);

    LocationDto toDto(Location location);
}
