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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/countries", produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CountryController {

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

    @PatchMapping("/{countryCode}")
    @ApiOperation(
            value = "Increment country counter by code.",
            response = CountryDTO.class,
            produces = APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<CountryDTO>> incrementCountry(@PathVariable String countryCode) {
        return countryService.incrementByCountryCode(countryCode)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
