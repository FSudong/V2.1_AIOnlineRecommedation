package com.seu.kse.dao;

import com.seu.kse.bean.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User verificationUser(String mailbox, String upassword);

    User selectByEmail(String mailbox);

    List<User> getAllUser();

    User testConnect();
};