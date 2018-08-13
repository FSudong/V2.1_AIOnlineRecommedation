package com.seu.kse.dao;

import com.seu.kse.bean.PaperTagKey;

import java.util.List;

public interface PaperTagMapper {
    int deleteByPrimaryKey(PaperTagKey key);

    int insert(PaperTagKey record);

    int insertSelective(PaperTagKey record);

    PaperTagKey selectByPrimaryKey(PaperTagKey key);

    List<PaperTagKey> selectByPID(String pid);
}