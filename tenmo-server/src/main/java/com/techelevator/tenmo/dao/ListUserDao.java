package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.ListUser;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface ListUserDao {

    public List<ListUser> listAllUsersByAcctId();
}
