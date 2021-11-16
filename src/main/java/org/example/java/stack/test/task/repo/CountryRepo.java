package org.example.java.stack.test.task.repo;

import org.example.java.stack.test.task.entity.Country;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CountryRepo extends ReactiveCrudRepository<Country, Long> {

    @Query(value = "update countries set counter = counter + :incrementValue where country_code = :countryCode returning *")
    Mono<Country> findByCountryCodeAndIncrement(@NotNull String countryCode, @NotNull Integer incrementValue);
}
