package org.example.java.stack.test.task.service;

import org.example.java.stack.test.task.dto.CountryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CountryService {

    Mono<CountryDTO> incrementByCountryCode(String countryCode);

    Flux<CountryDTO> getAllCountries();
}
