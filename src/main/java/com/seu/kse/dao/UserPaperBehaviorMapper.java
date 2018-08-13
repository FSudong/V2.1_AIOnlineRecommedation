package com.seu.kse.dao;

import com.seu.kse.bean.UserPaperBehavior;
import com.seu.kse.bean.UserPaperBehaviorKey;

import java.util.List;

public interface UserPaperBehaviorMapper {
    int deleteByPrimaryKey(UserPaperBehaviorKey key);

    int insert(UserPaperBehavior record);

    int insertSelective(UserPaperBehavior record);

    UserPaperBehavior selectByPrimaryKey(UserPaperBehaviorKey key);

    int updateByPrimaryKeySelective(UserPaperBehavior record);

    int updateByPrimaryKey(UserPaperBehavior record);

    List<UserPaperBehavior> selectByUserID(String userID);


}