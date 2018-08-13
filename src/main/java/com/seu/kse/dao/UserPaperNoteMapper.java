package com.seu.kse.dao;

import com.seu.kse.bean.UserPaperNote;
import com.seu.kse.bean.UserPaperNoteKey;
import com.seu.kse.bean.UserPaperNoteWithBLOBs;

import java.util.List;

public interface UserPaperNoteMapper {
    int deleteByPrimaryKey(UserPaperNoteKey key);

    int insert(UserPaperNoteWithBLOBs record);

    int insertSelective(UserPaperNoteWithBLOBs record);

    UserPaperNoteWithBLOBs selectByPrimaryKey(UserPaperNoteKey key);

    int updateByPrimaryKeySelective(UserPaperNoteWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(UserPaperNoteWithBLOBs record);

    int updateByPrimaryKey(UserPaperNote record);

    List<UserPaperNoteWithBLOBs> selectByPrimaryPaperIDKey(String paperID);
}