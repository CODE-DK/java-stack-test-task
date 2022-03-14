package org.example.java.stack.test.task.service;

import lombok.AllArgsConstructor;
import org.example.java.stack.test.task.dto.CountryDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    public static final Map<String, AtomicLong> BUFFER = new ConcurrentHashMap<>();

    static {
        BUFFER.put("DE", new AtomicLong(0));
        BUFFER.put("RU", new AtomicLong(0));
        BUFFER.put("EU", new AtomicLong(0));
        BUFFER.put("CH", new AtomicLong(0));
    }

    @Override
    public Mono<CountryDTO> incrementByCountryCode(@NotNull String countryCode) {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryCode(countryCode)
                .counterValue(BUFFER.get(countryCode).incrementAndGet())
                .build();
        return Mono.just(countryDTO);
    }

    @Override
    public Flux<CountryDTO> getAllCountries() {
        return Flux.fromStream(BUFFER.entrySet().stream().map(entry -> CountryDTO.builder()
                .countryCode(entry.getKey())
                .counterValue(entry.getValue().longValue())
                .build()));
    }
}
