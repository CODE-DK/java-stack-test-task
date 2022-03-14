package org.example.java.stack.test.task.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.java.stack.test.task.repo.CountryRepo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.example.java.stack.test.task.service.CountryServiceImpl.BUFFER;

@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor
public class CountrySchedulerImpl implements CountryScheduler {

    private final CountryRepo countryRepo;

    @Override
    @Scheduled(fixedDelay = 1000L)
    public void execute() {
        synchronized (BUFFER) {
            BUFFER.forEach((k, v) -> {
                Mono<Integer> integerMono = countryRepo.insertOrUpdateCountryCounter(k, v);
                integerMono.block();
            });
        }
    }
}
