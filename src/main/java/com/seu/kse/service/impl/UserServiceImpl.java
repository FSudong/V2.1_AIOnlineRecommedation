package com.seu.kse.service.impl;
import javax.annotation.Resource;

import com.seu.kse.dao.UserMapper;
import com.seu.kse.service.IUserService;
import com.seu.kse.bean.User;
import org.springframework.stereotype.Service;
/**
 * Created by yaosheng on 2017/5/22.
 */

@Service("userService")
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userDao;

    public User getUserByID(String userId) {
        // TODO Auto-generated method stub
        return this.userDao.selectByPrimaryKey(userId);
    }

    public int insertUser(User user){
        return this.userDao.insert(user);
    }

    public User verification(String mailbox, String psw){
        return userDao.verificationUser(mailbox,psw);
    }

    public boolean isRegisterEmail(String email){
        User user=userDao.selectByEmail(email);
        if(user==null){
            return false;
        }
        return true;
    }

    public int updateUser(User user){


        return userDao.updateByPrimaryKey(user);


    }

}
