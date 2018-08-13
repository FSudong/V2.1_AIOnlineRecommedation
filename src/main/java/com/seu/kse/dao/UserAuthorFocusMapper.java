package com.seu.kse.dao;

import com.seu.kse.bean.UserAuthorFocusKey;

public interface UserAuthorFocusMapper {
    int deleteByPrimaryKey(UserAuthorFocusKey key);

    int insert(UserAuthorFocusKey record);

    int insertSelective(UserAuthorFocusKey record);

    UserAuthorFocusKey selectByPrimaryKey(UserAuthorFocusKey key);

}