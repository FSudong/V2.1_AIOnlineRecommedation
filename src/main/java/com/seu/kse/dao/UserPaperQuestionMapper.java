package com.seu.kse.dao;

import com.seu.kse.bean.UserPaperQuestion;
import com.seu.kse.bean.UserPaperQuestionKey;

public interface UserPaperQuestionMapper {
    int deleteByPrimaryKey(UserPaperQuestionKey key);

    int insert(UserPaperQuestion record);

    int insertSelective(UserPaperQuestion record);

    UserPaperQuestion selectByPrimaryKey(UserPaperQuestionKey key);

    int updateByPrimaryKeySelective(UserPaperQuestion record);

    int updateByPrimaryKeyWithBLOBs(UserPaperQuestion record);
}