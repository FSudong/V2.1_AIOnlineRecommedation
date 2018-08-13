package com.seu.kse.dao;

import com.seu.kse.bean.Author;

public interface AuthorMapper {
    int deleteByPrimaryKey(Integer aid);

    int insert(Author record);

    int insertSelective(Author record);

    Author selectByPrimaryKey(Integer aid);

    int updateByPrimaryKeySelective(Author record);

    int updateByPrimaryKey(Author record);

    Integer selectAidByAuthorName(String authorName);
}