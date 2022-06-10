package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Transfer> transferList(Long userId){
        List<Transfer> listOfTransfers = new ArrayList<>();
        String sql = "select * from transfer where account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            Transfer transfer = mapRowToAccount(results);
            listOfTransfers.add(transfer);
        }
        return listOfTransfers;
    }

    @Override
    public void makeTransfer(Long fromUserId, long toUserId, BigDecimal amount){
        BigDecimal fromBalance ;
        BigDecimal toBalance;

        //get the balances for sender and user
       fromBalance = getAccountBalanceByUserId(fromUserId);
       toBalance = getAccountBalanceByUserId(toUserId);

        //Process and Adjust the sender's new balance
        fromBalance = fromBalance.subtract(amount);
        updateBalance(fromUserId, fromBalance);

        //process and adjust the receiver's new balance
        toBalance = toBalance.add(amount);
        updateBalance(toUserId,toBalance);

        //GET ACCOUNT IDS FOR FROMACCOUNTID TO ACCOUNTID

        //record the transfer
        recordTransfer(fromUserId,toUserId,amount);
    }

    private void recordTransfer(long accountFrom, long accountTo, BigDecimal amount){
        long defaultTransferStatus = 2;
        long defaultApprovalStatus = 2;
        //TRANSFER USES ACCOUNT ID, NOT USER ID!!!!
        //YOU NEED TO GET THEM BECAUSE USER IDS DON"T WORK HERE!
        String sql = "Insert into transfer" +
                "(transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "values (?,?,?,?,?);";
        jdbcTemplate.update(sql, defaultApprovalStatus, defaultTransferStatus,accountFrom,accountTo,amount);
    }


    private BigDecimal getAccountBalanceByUserId(long userId){
        BigDecimal balance;
        String sql = "select balance from account where account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            balance = results.getBigDecimal("amount");
        } else return null;
        return balance;
    }

    private void updateBalance(Long userId, BigDecimal balance){
        String sql = "Update account" +
                "SET balance = ?" +
                "WHERE" +
                "user_id = ?;";
        jdbcTemplate.update(sql, balance, userId);
    }



    private Transfer mapRowToAccount(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getLong("transfer_id"));
        transfer.setTransferTypeId(rowSet.getLong("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getLong("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getLong("account_from"));
        transfer.setAccountTo(rowSet.getLong("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;

    }


}
