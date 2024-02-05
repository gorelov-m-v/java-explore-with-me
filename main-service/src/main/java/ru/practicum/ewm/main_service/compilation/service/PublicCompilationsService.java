package ru.practicum.ewm.main_service.compilation.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.main_service.compilation.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {
    List<CompilationDto> searchCompilations(boolean pinned, PageRequest page);

    CompilationDto getCompilation(int compId);
}
