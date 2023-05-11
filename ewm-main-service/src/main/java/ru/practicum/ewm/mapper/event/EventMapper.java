package ru.practicum.ewm.mapper.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.dto.event.EventRequestDto;
import ru.practicum.ewm.dto.event.EventResponseDto;
import ru.practicum.ewm.mapper.category.CategoryMapper;
import ru.practicum.ewm.mapper.user.UserMapper;
import ru.practicum.ewm.model.event.Event;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {LocationMapper.class, CategoryMapper.class, UserMapper.class})
public interface EventMapper {

    @Mapping(source = "category", target = "category.id")
    Event toEntity(EventRequestDto eventRequestDto);

    EventResponseDto toResponse(Event event);

//    @Mapping(target = "views", source = "views")
//    EventResponseDto toResponseWithViews(Event event, long views);
}
