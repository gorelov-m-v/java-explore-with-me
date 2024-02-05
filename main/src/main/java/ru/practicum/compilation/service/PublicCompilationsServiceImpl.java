package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicCompilationsServiceImpl implements PublicCompilationsService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> searchCompilations(boolean pinned, PageRequest page) {
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, page);
        return CompilationMapper.toDtoList(compilations);
    }

    @Override
    public CompilationDto getCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка c id = " + compId + " не найдена или недоступна."));
        return CompilationMapper.toDto(compilation);
    }
}
