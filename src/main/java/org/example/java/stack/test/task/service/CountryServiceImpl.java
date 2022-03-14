package org.example.java.stack.test.task.service;

import lombok.AllArgsConstructor;
import org.example.java.stack.test.task.dto.CountryDTO;
import org.example.java.stack.test.task.repo.CountryRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepo countryRepo;
    public static final Map<String, Long> BUFFER = new HashMap<>();

    @Override
    public Mono<CountryDTO> incrementByCountryCode(@NotNull String countryCode) {
        CountryDTO countryDTO = null;
        synchronized (BUFFER) {
            Long counter = BUFFER.getOrDefault(countryCode, 0L);
            counter += 1L;
            countryDTO = CountryDTO.builder()
                    .countryCode(countryCode)
                    .counterValue(counter)
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
                    .counterValue(entry.getValue())
                    .build()));
        }
        return flux;
    }

    @PostConstruct
    private void restore() {
        countryRepo.findAll().toStream()
                .forEach(country -> BUFFER.put(
                        country.getCountryCode(),
                        country.getCounter().longValue()
                ));
    }
}
