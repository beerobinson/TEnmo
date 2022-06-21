package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.ListUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.ListUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/listusers")
@PreAuthorize("isAuthenticated()")
public class ListUserController {

    @Autowired
    JdbcTransferDao transferDao;
    @Autowired
    private ListUserDao dao;

    //Pull list of all usernames and account Id
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ListUser> listUserList() {
        List<ListUser> listUserList = dao.listAllUsersByAcctId();
        return listUserList;
    }



}
