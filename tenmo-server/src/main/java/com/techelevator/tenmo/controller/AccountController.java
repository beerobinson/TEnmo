package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class AccountController {


    private UserDao dao;
    private String isAuthenticated;
    JdbcAccountDao AccountDao;

    //Get User Balance


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable long userId){

        BigDecimal balance = AccountDao.getBalance(userId);
        return balance;

    }

}
