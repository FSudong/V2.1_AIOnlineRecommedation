package com.seu.kse.service;

import com.seu.kse.bean.User;

/**
 * Created by yaosheng on 2017/5/22.
 */
public interface IUserService {
     User getUserByID(String userID);
     int insertUser(User user);
     User verification(String mailBox, String psw);
     boolean isRegisterEmail(String email);
}
