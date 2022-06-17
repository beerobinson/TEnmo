package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.ListUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcListUserDao implements ListUserDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ListUser> listAllUsersByAcctId(){
        List<ListUser> listOfListUsers = new ArrayList<>();
        String sql = "select * from tenmo_user t JOIN account a on a.user_id = t.user_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

        while (results.next()) {
            ListUser listUser = mapRowToTransfer(results);
            listOfListUsers.add(listUser);
        }
        return listOfListUsers;
    };

    private ListUser mapRowToTransfer(SqlRowSet rowSet) {
        ListUser listUser = new ListUser();
        listUser.setAccountId(rowSet.getLong("account_id"));
        listUser.setUserName(rowSet.getString("username"));
        return listUser;
    }
}
