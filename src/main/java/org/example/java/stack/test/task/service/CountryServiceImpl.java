package org.example.java.stack.test.task.service;

import lombok.AllArgsConstructor;
import org.example.java.stack.test.task.dto.CountryDTO;
import org.example.java.stack.test.task.entity.Country;
import org.example.java.stack.test.task.repo.CountryRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepo countryRepo;

    @Override
    @Transactional
    public Mono<CountryDTO> incrementByCountryCode(@NotNull String countryCode) {
        return countryRepo.findByCountryCodeAndIncrement(countryCode, 1)
                .map(mapCountryToDTO);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Flux<CountryDTO> getAllCountries() {
        return countryRepo.findAll()
                .map(mapCountryToDTO);
    }

    private final Function<Country, CountryDTO> mapCountryToDTO = (country) -> CountryDTO.builder()
            .countryCode(country.getCountryCode())
            .counterValue(country.getCounter().toString())
            .build();
}
