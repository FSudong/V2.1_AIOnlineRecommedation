package com.seu.kse.dao;

import com.seu.kse.bean.UserTagKey;

import java.util.List;

public interface UserTagMapper {
    int deleteByPrimaryKey(UserTagKey key);

    int insert(UserTagKey record);

    int insertSelective(UserTagKey record);

    List<UserTagKey> selectByUser(String uid);
}