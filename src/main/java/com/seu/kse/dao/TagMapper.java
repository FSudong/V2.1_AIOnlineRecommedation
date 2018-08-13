package com.seu.kse.dao;

import com.seu.kse.bean.Tag;
import com.seu.kse.bean.TagExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagMapper {
    long countByExample(TagExample example);

    int deleteByExample(TagExample example);

    int deleteByPrimaryKey(String tagname);

    int insert(Tag record);

    int insertSelective(Tag record);

    List<Tag> selectByExample(TagExample example);

    Tag selectByPrimaryKey(String tagname);

    int updateByExampleSelective(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByExample(@Param("record") Tag record, @Param("example") TagExample example);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);
//人工添加
    Tag selectByTagName(String tagname);

    List<Tag> selectAllTag();

    List<Tag> selectTodayTag(int time);
}