package org.example.java.stack.test.task.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.example.java.stack.test.task.dto.CountryDTO;
import org.example.java.stack.test.task.exception.BadRequestException;
import org.example.java.stack.test.task.service.CountryService;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/countries", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CountryController {

    private final CountryService countryService;

    public static final String INCREMENT = "increment";

    @GetMapping
    @ApiOperation(
            value = "Get all countries.",
            response = CountryDTO.class,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public Flux<CountryDTO> getCountries() {
        return countryService.getAllCountries()
                .switchIfEmpty(Mono.error(new BadRequestException("Content not found.")));
    }

    @PutMapping("/{countryCode}")
    @ApiOperation(
            value = "Update country counter by code.",
            response = CountryDTO.class,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<CountryDTO>> incrementCountry(
            @PathVariable String countryCode,
            @RequestParam String action
    ) {
        if (Objects.equals(INCREMENT, action)) {
            return countryService.incrementByCountryCode(countryCode)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.badRequest().build());
        }
        return Mono.just(ResponseEntity.badRequest().build());
    }
}
