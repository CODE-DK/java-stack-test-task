package org.example.java.stack.test.task.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.java.stack.test.task.container.DataSourceConfig;
import org.example.java.stack.test.task.dto.CountryDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@Slf4j
@DirtiesContext
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DataSourceConfig.class)
public class CountryControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetCountries() {
        webTestClient.get()
                .uri("/countries")
                .header("Content-type", "application/json").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CountryDTO.class)
                .hasSize(8)
                .consumeWith(it -> {
                    List<CountryDTO> countryList = it.getResponseBody();
                    Assert.assertNotNull(countryList);
                    countryList.forEach(countryDTO -> {
                        Assert.assertNotNull(countryDTO.getCountryCode());
                        Assert.assertNotNull(countryDTO.getCounterValue());
                    });
                });
    }

    @Test
    public void testIncrementCountry_WhenWrongActionTag() {
        webTestClient.put()
                .uri("/countries/{countryCode}?action={action}", "AU", "justDoIt")
                .header("Content-type", "application/json").exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testIncrementCountry_WhenUnknownCountryCode() {
        webTestClient.put()
                .uri("/countries/{countryCode}?action={action}", "BIBA", "increment")
                .header("Content-type", "application/json").exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testIncrementCountry() {
        webTestClient.put()
                .uri("/countries/{countryCode}?action={action}", "AU", "increment")
                .header("Content-type", "application/json").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CountryDTO.class)
                .consumeWith(it -> {
                    CountryDTO countryDTO = it.getResponseBody();
                    Assert.assertNotNull(countryDTO);

                    Assert.assertEquals("AU", countryDTO.getCountryCode());
                    Assert.assertEquals("1", countryDTO.getCounterValue());
                });
    }
}