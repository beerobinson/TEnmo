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


    //Query database for a list of transfers based on incoming User ID
    @Override
    public List<Transfer> transferList(Long userId){
        //Pull the user's account ID:
        long accountId = getAccountIdFromUserId(userId);

        //Query for transfers of the User's account ID
        List<Transfer> listOfTransfers = new ArrayList<>();
        String sql = "select * from transfer where account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);

        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfer.setFromUserName(getUserNameFromAccountId(transfer.getAccountFrom()));
            transfer.setToUserName(getUserNameFromAccountId(transfer.getAccountTo()));
            listOfTransfers.add(transfer);
        }
        return listOfTransfers;
    }

    @Override
    public void makeTransfer(Transfer transfer, long userId){

        //Make the actual transfer
        BigDecimal fromBalance;
        BigDecimal toBalance;

        long accountFrom = transfer.getAccountFrom();
        long accountTo = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();

        //Pull the actual User IDs from Account IDs
        long fromUserId = getUserIdFromAccountId(accountFrom);
        long toUserId = getUserIdFromAccountId(accountTo);

        //get the balances for sender and user userIds
       fromBalance = getAccountBalanceByUserId(fromUserId);
       toBalance = getAccountBalanceByUserId(toUserId);

        //Process and Adjust the sender's new balance
        fromBalance = fromBalance.subtract(amount);
        updateBalance(fromUserId, fromBalance);

        //process and adjust the receiver's new balance
        toBalance = toBalance.add(amount);
        updateBalance(toUserId,toBalance);

        //recordTransfer(accountFrom,accountTo,amount);
        recordTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    @Override
    public void recordTransfer(long accountFrom, long accountTo, BigDecimal amount){
        long transferTypeId = 2;
        long transferStatusId = 2;

        String sql = "Insert into transfer" +
                "(transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "values (?,?,?,?,?);";

        jdbcTemplate.update(sql, transferTypeId, transferStatusId,accountFrom,accountTo,amount);
    }


    private long getAccountIdFromUserId(long userId) {
        long accountId;
        String sql = "select account_id from account where user_id=?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            accountId = results.getLong("account_id");
        } else return 0;

        return accountId;
    }

    private long getUserIdFromAccountId(long accountId) {
        long userId;
        String sql = "select user_id from account where account_id=?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            userId = results.getLong("user_id");
        } else return 0;

        return userId;
    }

    private String getUserNameFromAccountId(long accountId) {
        long userId = getUserIdFromAccountId(accountId);

        String userName;
        String sql = "select username from tenmo_user where user_id=?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            userName = results.getString("username");
        } else return null;

        return userName;
    }

    @Override
    public Transfer getTransferByTransferId(long transferId){
        Transfer transfer = new Transfer();
        String sql = "select * from transfer where transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()){
            transfer = mapRowToTransfer(results);
            transfer.setFromUserName(getUserNameFromAccountId(transfer.getAccountFrom()));
            transfer.setToUserName(getUserNameFromAccountId(transfer.getAccountTo()));
        } else return null;
        return transfer;
    }

    private BigDecimal getAccountBalanceByUserId(long userId){
        BigDecimal balance;
        String sql = "select balance from account where user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            balance = results.getBigDecimal("balance");
        } else return null;
        return balance;
    }

    private void updateBalance(long userId, BigDecimal balance){
        String sql = "Update account " +
                "SET balance = ? " +
                "WHERE " +
                "user_id = ?;";
        jdbcTemplate.update(sql, balance, userId);
    }


    private Transfer mapRowToTransfer(SqlRowSet rowSet){
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
