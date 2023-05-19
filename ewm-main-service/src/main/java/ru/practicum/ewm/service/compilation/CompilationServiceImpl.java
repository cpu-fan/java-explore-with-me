package ru.practicum.ewm.service.compilation;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilation.CompilationRequestDto;
import ru.practicum.ewm.dto.compilation.CompilationResponseDto;
import ru.practicum.ewm.dto.compilation.CompilationUpdateDto;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.compilation.CompilationMapper;
import ru.practicum.ewm.model.compilation.Compilation;
import ru.practicum.ewm.model.compilation.QCompilation;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.repository.compilation.CompilationRepository;
import ru.practicum.ewm.service.event.EventService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final EventService eventService;

    private final CompilationMapper mapper;

    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public CompilationResponseDto addCompilation(CompilationRequestDto compilationDto) {
        Set<Event> events = eventService.getEventEntities(compilationDto.getEvents());
        Compilation compilation = mapper.toEntity(compilationDto, events);
        compilation = compilationRepository.save(compilation);
        log.info("Добавлена новая подборка id = {}, title = {}", compilation.getId(), compilation.getTitle());
        return mapper.toDto(compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationResponseDto getCompilation(long compilationId) {
        Compilation compilation = getCompilationEntity(compilationId);
        log.info("Запрошена подборка событий id = {}", compilationId);
        return mapper.toDto(compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationResponseDto> getCompilations(Boolean pinned, int from, int size) {
        List<CompilationResponseDto> compilations;

        BooleanBuilder builder = new BooleanBuilder();
        if (pinned != null) {
            builder.and(QCompilation.compilation.pinned.eq(pinned));
        }

        compilations = compilationRepository.findAll(builder, PageRequest.of(from / size, size))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        log.info("Запрошен список подборок событий");
        return compilations;
    }

    @Override
    @Transactional
    public CompilationResponseDto updateCompilation(long compilationId, CompilationUpdateDto compilationDto) {
        Compilation compilation = getCompilationEntity(compilationId);

        compilation = mapper.partialUpdate(compilationDto, compilation);
        if (compilationDto.getEvents() != null) {
            Set<Event> events = eventService.getEventEntities(compilationDto.getEvents());
            compilation.setEvents(events);
        }

        compilation = compilationRepository.save(compilation);
        log.info("Обновлена категория id = {}", compilationId);
        return mapper.toDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(long compilationId) {
        if (compilationRepository.existsById(compilationId)) {
            compilationRepository.deleteById(compilationId);
        } else {
            String message = "Подборка id = " + compilationId + " не найдена";
            log.error(message);
            throw new NotFoundException(message);
        }
    }

    private Compilation getCompilationEntity(long compilationId) {
        return compilationRepository.findById(compilationId).orElseThrow(() -> {
            String message = "Подборка id = " + compilationId + " не найдена";
            log.error(message);
            throw new NotFoundException(message);
        });
    }
}
