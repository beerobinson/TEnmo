package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/users/";
    private final RestTemplate restTemplate = new RestTemplate();



    public BigDecimal getBalance(Long userId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BigDecimal> entity = new HttpEntity<>(headers);

        BigDecimal balance = restTemplate.postForObject(API_BASE_URL + userId, entity, BigDecimal.class);
        return balance;
    }



    }






