package com.seu.kse.dao;

import com.seu.kse.bean.userFieldsKey;

public interface userFieldsMapper {
    int deleteByPrimaryKey(userFieldsKey key);

    int insert(userFieldsKey record);

    int insertSelective(userFieldsKey record);
}