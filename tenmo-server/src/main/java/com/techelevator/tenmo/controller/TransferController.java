package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transfers")
//@PreAuthorize("isAuthenticated()")  FIX THIS!
public class TransferController {

    @Autowired
    JdbcTransferDao transferDao;
    @Autowired
    private TransferDao dao;

    //If the authenticated user hits transfers/id with a GET, return a List of transfers
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public List<Transfer> transferList(@PathVariable long userId) {
        List<com.techelevator.tenmo.model.Transfer> tList = dao.transferList(userId);
        return tList;
    }

    //If the authenticated user hits transfers/id, with a POST, add transfer to list
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void createTransfer(@RequestBody Transfer transfer) {
       //IM HAVING ISSUES BECAUSE OPF USERID/ACCOUNTID CONFUSION.

        long fromUserId = transfer.getAccountFrom();
       long toUserId = transfer.getAccountTo();
       BigDecimal amount = transfer.getAmount();
        dao.makeTransfer(fromUserId, toUserId, amount);

    }

    @RequestMapping(value = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable long transferId) {
        return dao.getTransferByTransferId(transferId);
    }


}
