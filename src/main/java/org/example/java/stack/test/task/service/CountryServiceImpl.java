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

    @Override
    public Mono<CountryDTO> incrementByCountryCode(@NotNull String countryCode) {
        CountryDTO countryDTO = null;
        synchronized (BUFFER) {
            AtomicLong counter = BUFFER.getOrDefault(countryCode, new AtomicLong(0L));
            countryDTO = CountryDTO.builder()
                    .countryCode(countryCode)
                    .counterValue(counter.incrementAndGet())
                    .build();
            BUFFER.put(countryCode, counter);
        }
        return Mono.just(countryDTO);
    }

    @Override
    public Flux<CountryDTO> getAllCountries() {
        Flux<CountryDTO> flux = null;
        synchronized (BUFFER) {
            flux = Flux.fromStream(BUFFER.entrySet().stream().map(entry -> CountryDTO.builder()
                    .countryCode(entry.getKey())
                    .counterValue(entry.getValue().longValue())
                    .build()));
        }
        return flux;
    }
}
