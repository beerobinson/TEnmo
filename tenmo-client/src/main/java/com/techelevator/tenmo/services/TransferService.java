package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/transfers/";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Transfer> getUserTransfers(AuthenticatedUser user){

        List transferList = new ArrayList<>();

        try {
            transferList = restTemplate.exchange(API_BASE_URL + user.getUser().getId(), HttpMethod.GET, authHttp(user), List.class).getBody();
        } catch (RestClientException e) {
            System.out.println("Exception " + e);
        }

        return transferList;

    }

    public Transfer getTransferByTransferId(AuthenticatedUser user, long transferId){
        Transfer transfer = new Transfer();

        try {
            transfer = restTemplate.exchange(API_BASE_URL + "transferid/" + transferId, HttpMethod.GET, authHttp(user), Transfer.class).getBody();
        } catch (RestClientException e) {
            System.out.println("Exception " + e);
        }

        return transfer;

    }

    public void makeTransfer(AuthenticatedUser user, long fromUserId, long toUserId, long amount) {
        //Transfer money between two accounts

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
