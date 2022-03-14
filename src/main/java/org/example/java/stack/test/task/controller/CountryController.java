package org.example.java.stack.test.task.controller;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.java.stack.test.task.dto.CountryDTO;
import org.example.java.stack.test.task.exception.BadRequestException;
import org.example.java.stack.test.task.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static java.util.Locale.ROOT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/countries", produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CountryController {

    private static final Set<String> COUNTRY_CODES = new HashSet<>();
    private final CountryService countryService;

    @GetMapping
    @ApiOperation(
            value = "Get all countries.",
            response = CountryDTO.class,
            produces = APPLICATION_JSON_VALUE
    )
    public Flux<CountryDTO> getCountries() {
        return countryService.getAllCountries()
                .switchIfEmpty(Mono.error(new BadRequestException("Content not found.")));
    }

    @PostMapping("/{countryCode}")
    @ApiOperation(
            value = "Increment country counter by code.",
            response = CountryDTO.class,
            produces = APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<CountryDTO>> incrementCountry(@PathVariable String countryCode) {
        if (!COUNTRY_CODES.contains(countryCode.toUpperCase(ROOT))) {
            return Mono.error(new BadRequestException("Country code not available"));
        }
        return countryService.incrementByCountryCode(countryCode)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostConstruct
    private void initCountryCodes() {
        COUNTRY_CODES.addAll(Arrays.asList(Locale.getISOCountries()));
    }
}
