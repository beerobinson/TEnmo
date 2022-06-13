package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class AccountController {


    @Autowired
    JdbcAccountDao AccountDao;
    @Autowired
    private UserDao dao;



    //If the authenticated user hits user/id, return an account object of the user ID.
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public Account getBalance(@PathVariable long userId){
        Account account = AccountDao.getBalance(userId);
        return account;
    }




}
