package ru.practicum.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.Stat;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsClientImpl implements StatsClient {
    private final RestTemplate rest;

    @Autowired
    public StatsClientImpl(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void saveEndpoint(HitDto hitDto) {
        try {
            rest.exchange("/hit", HttpMethod.POST, new HttpEntity<>(hitDto, defaultHeaders()), Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Произошла ошибка при обращении к сервису статистики.", e.getCause());
        }
    }

    public List<Stat> findStatistic(ViewStatsRequest request) {
        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();
        if (start == null || end == null) {
            throw new IllegalArgumentException("Не заданы даты поиска.");
        }

        Map<String, Object> parameters = new LinkedHashMap<>();

        parameters.put("start", encodeTime(start));
        parameters.put("end", encodeTime(end));
        parameters.put("uris", request.getUris());
        if (request.getUnique() != null) {
            parameters.put("unique", request.getUnique());
        }
        ResponseEntity<Object> statsServerResponse;
        try {
            statsServerResponse = rest.exchange("/stats", HttpMethod.GET,
                    new HttpEntity<>(null, defaultHeaders()), Object.class, parameters);
        } catch (Exception e) {
            throw new RuntimeException("Произошла ошибка при обращении к сервису статистики.", e.getCause());
        }
        return (List<Stat>) statsServerResponse.getBody();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private String encodeTime(LocalDateTime time) {
        return URLEncoder.encode(time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                StandardCharsets.UTF_8);
    }
}