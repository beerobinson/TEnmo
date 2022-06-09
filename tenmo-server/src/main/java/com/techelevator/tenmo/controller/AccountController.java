package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
@PreAuthorize("permitAll")   //FIX THIS! You need to authenticate!
public class AccountController {


    private UserDao dao;
    private String isAuthenticated;
    @Autowired
    JdbcAccountDao AccountDao;

    //Get User Balance


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Account getBalance(@PathVariable long userId){
        Account account = AccountDao.getBalance(userId);
        return account;

    }

}
