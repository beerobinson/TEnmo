package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.ListUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ListUserService {

    private static final String API_BASE_URL = "http://localhost:8080/listusers/";
    private final RestTemplate restTemplate = new RestTemplate();

    public ListUser[] getListUsers(AuthenticatedUser user){

        ListUser[] listUsers = null;

        try {
            listUsers = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, authHttp(user), ListUser[].class).getBody();
        } catch (RestClientException e) {
            System.out.println("Exception " + e);
        }

        return listUsers;
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
