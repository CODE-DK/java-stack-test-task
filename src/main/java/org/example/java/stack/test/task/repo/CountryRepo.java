package org.example.java.stack.test.task.repo;

import org.example.java.stack.test.task.entity.Country;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CountryRepo extends ReactiveCrudRepository<Country, Long> {

    Mono<Country> findByCountryCode(@NotNull String countryCode);
}
