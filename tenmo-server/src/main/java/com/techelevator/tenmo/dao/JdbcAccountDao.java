package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{


    @Autowired
    private JdbcTemplate jdbcTemplate;


 @Override
    public Account getBalance(Long userId){
     String sql = "select balance from account where user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);




     if (results.next()) {
         Account account = mapRowToAccount(results);
         return account;
     } return null;

     }


    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getLong("account_id"));
        account.setUserId(rowSet.getLong("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }


    }




