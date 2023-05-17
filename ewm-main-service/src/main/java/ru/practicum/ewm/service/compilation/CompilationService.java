package ru.practicum.ewm.service.compilation;

import ru.practicum.ewm.dto.compilation.CompilationRequestDto;
import ru.practicum.ewm.dto.compilation.CompilationResponseDto;
import ru.practicum.ewm.dto.compilation.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {

    CompilationResponseDto addCompilation(CompilationRequestDto compilationDto);

    CompilationResponseDto getCompilation(long compilationId);

    List<CompilationResponseDto> getCompilations(Boolean pinned, int from, int size);

    CompilationResponseDto updateCompilation(long id, CompilationUpdateDto compilationDto);

    void deleteCompilation(long id);
}
