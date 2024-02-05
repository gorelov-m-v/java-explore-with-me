package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto compilationDto) {
        List<Event> events = null;
        if (compilationDto.getEvents() != null) {
            events = eventRepository.findByIdIn(compilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.toEntity(compilationDto, new HashSet<>(events));
        compilation = compilationRepository.save(compilation);
        return CompilationMapper.toDto(compilation);
    }

    @Override
    public void deleteCompilation(int compId) {
        Compilation compilation = checkExist(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest request) {
        Compilation compilation = checkExist(compId);
        compilation.setPinned(request.isPinned());
        compilation.setTitle(request.getTitle());

        if (request.getEvents() == null) {
            compilation.setEvents(null);
        } else {
            List<Event> newEvents = eventRepository.findByIdIn(request.getEvents());
            compilation.setEvents(new HashSet<>(newEvents));
        }
        return CompilationMapper.toDto(compilation);
    }

    private Compilation checkExist(int compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id = " + compId + " не найдена."));
    }
}
