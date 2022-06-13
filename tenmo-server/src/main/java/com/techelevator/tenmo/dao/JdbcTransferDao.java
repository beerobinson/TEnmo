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


    //Query database for a list of transfers based on Account ID.
    @Override
    public List<Transfer> transferList(Long userId){
        //Pull the user's account ID:
        long accountId = getAccountIdFromUserId(userId);

        //Query for transfers of the User's account ID
        List<Transfer> listOfTransfers = new ArrayList<>();
        String sql = "select * from transfer where account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);

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

        //pull the account_ID of fromUserid
        long fromAccountId = getAccountIdFromUserId(fromUserId);

        //pull the account_ID ot toUserId
        long toAccountId = getAccountIdFromUserId(toUserId)

        //record the transfer
        recordTransfer(fromAccountId,toAccountId,amount);
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


    private long getAccountIdFromUserId(long userId){
        long accountId;
        String sql = "select account_id from account where user_id=?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            accountId=results.getLong("account_id");
        } else return 0;

        return accountId;
    }

    private BigDecimal getAccountBalanceByUserId(long userId){
        BigDecimal balance;
        String sql = "select balance from account where user_id = ?;";
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
