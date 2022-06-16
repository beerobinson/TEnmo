package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import io.cucumber.java.bs.A;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/users/";
    private final RestTemplate restTemplate = new RestTemplate();



    public BigDecimal getBalance(AuthenticatedUser user) {
        //Pull user data object and return the balance of the current user
        Account account = new Account();

        try {
           //account = restTemplate.getForObject(API_BASE_URL + user.getUser().getId(), Account.class, HttpMethod.GET, authHttp(user));
            account = restTemplate.exchange(API_BASE_URL + user.getUser().getId(),HttpMethod.GET, authHttp(user), Account.class).getBody();
        } catch (RestClientException e) {
            System.out.println("Exception " + e);
        }

        return account.getBalance();
    }


    private HttpEntity authHttp(AuthenticatedUser user) {
        //Generate an entity with the User's Token
        String token = user.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }


    }






