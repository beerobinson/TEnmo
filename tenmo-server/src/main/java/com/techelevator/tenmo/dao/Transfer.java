package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

public interface Transfer {

    List<Transfer> listTransfersByUserId(long userId);

    public void makeTransfer(long toAccountId, long fromAccountId, BigDecimal amount);


}
