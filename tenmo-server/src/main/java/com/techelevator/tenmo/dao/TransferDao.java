package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    public List<Transfer> transferList(Long userId);

    public void makeTransfer(Long fromUserId, long toUserId, BigDecimal amount);

}
