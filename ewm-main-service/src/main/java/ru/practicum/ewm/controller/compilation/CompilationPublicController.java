package ru.practicum.ewm.controller.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.compilation.CompilationResponseDto;
import ru.practicum.ewm.service.compilation.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping
    public Collection<CompilationResponseDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationResponseDto getCompilation(@PathVariable long compId) {
        return compilationService.getCompilation(compId);
    }
}
