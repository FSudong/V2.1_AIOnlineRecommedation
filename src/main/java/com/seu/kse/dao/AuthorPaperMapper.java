package com.seu.kse.dao;


import com.seu.kse.bean.AuthorPaperKey;

import java.util.List;

public interface AuthorPaperMapper {
    int deleteByPrimaryKey(AuthorPaperKey key);

    int insert(AuthorPaperKey record);

    int insertSelective(AuthorPaperKey record);



    List<AuthorPaperKey> selectAuthorsByPaper(String pid);

    Integer countPid(String pid);
}