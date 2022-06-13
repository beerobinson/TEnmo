package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    public List<Transfer> transferList(Long userId);

    public void makeTransfer(long fromUserId, long toUserId, BigDecimal amount);

    public void recordTransfer(long accountFrom, long accountTo, BigDecimal amount);

    public Transfer getTransferByTransferId(long transferId);

}
