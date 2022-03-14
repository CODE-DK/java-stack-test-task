package org.example.java.stack.test.task.repo;

import org.example.java.stack.test.task.entity.Country;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CountryRepo extends ReactiveCrudRepository<Country, String> {

    String INSERT_OR_UPDATE_COUNTRY_COUNTER = "insert into countries (country_code, counter)" +
            "values (:country_code, :counter) " +
            "   on conflict (country_code) " +
            "   do update set counter = :counter returning *";

    @Modifying
    @Query(value = INSERT_OR_UPDATE_COUNTRY_COUNTER)
    Mono<Integer> insertOrUpdateCountryCounter(@Param("country_code") String countryCode,
                                               @Param("counter") Long counter);
}
