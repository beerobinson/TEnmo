package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/transfers/";
    private final RestTemplate restTemplate = new RestTemplate();

    public Transfer[] getUserTransfers(AuthenticatedUser user){
        //Return a list of all of the user's transfers
        Transfer[] transferList = null;

        try {
            transferList = restTemplate.exchange(API_BASE_URL + user.getUser().getId(), HttpMethod.GET, authHttp(user), Transfer[].class).getBody();
        } catch (RestClientException e) {
            System.out.println("Exception " + e);
        }

        return transferList;
    }

    public Transfer getTransferByTransferId(AuthenticatedUser user, long transferId){
        //Get Transfer by specified transfer ID
        Transfer transfer = new Transfer();
        try {
            transfer = restTemplate.exchange(API_BASE_URL + "transferid/" + transferId, HttpMethod.GET, authHttp(user), Transfer.class).getBody();
        } catch (RestClientException e) {
            System.out.println("Exception " + e);
        }
        return transfer;
    }

    public void makeTransfer(AuthenticatedUser user, long toAccountId, BigDecimal amount) {

        //pull users full account info
        AccountService accountService = new AccountService();
        Account userAccount = accountService.getAccount(user);
        long userId = userAccount.getUserId();
        long fromAccountId = userAccount.getAccountId();
        BigDecimal balance = userAccount.getBalance();

        //Create transfer to send
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(fromAccountId);
        transfer.setAccountTo(toAccountId);
        transfer.setAmount(amount);


        //specific token for mediatype
        String token = user.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(transfer, headers);


        //Check for zero/sufficient funds/separate accounts
        if (fromAccountId != toAccountId) {
            if (!balance.equals(0)) {
                if (amount.compareTo(BigDecimal.valueOf(0)) > 0) {
                    if (balance.subtract(amount).compareTo(BigDecimal.valueOf(0)) > 0) {

                        //Post Transaction if funds are sufficient
                        try {
                            restTemplate.exchange(API_BASE_URL + userId, HttpMethod.POST, entity, Transfer.class).getBody();
                            System.out.println("Transaction Successful!");
                        } catch (RestClientException e) {
                            System.err.println("That does not appear to be a valid account - TRANSACTION FAILED");
                        }

                    } else System.err.println("Insufficient funds! - TRANSACTION FAILED");
                } else System.err.println("Cannot send 0 or less than zero dollars! - TRANSACTION FAILED");
            } else System.err.println("Cannot Transfer with a balance of zero! - TRANSACTION FAILED");
        } else System.err.println("Cannot send money to your own account! - TRANSACTION FAILED");

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
