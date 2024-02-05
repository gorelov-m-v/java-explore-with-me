package ru.practicum.ewm.main_service.compilation.service;

import ru.practicum.ewm.main_service.compilation.dto.CompilationDto;
import ru.practicum.ewm.main_service.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.main_service.compilation.dto.UpdateCompilationRequest;

public interface AdminCompilationsService {
    CompilationDto saveCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(int compId);

    CompilationDto updateCompilation(int compId, UpdateCompilationRequest request);
}
