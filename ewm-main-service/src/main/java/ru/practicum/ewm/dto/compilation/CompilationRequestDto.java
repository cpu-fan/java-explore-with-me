package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationRequestDto {

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    private Set<Long> events;

    @Value("${pinned:false}")
    private boolean pinned;
}
