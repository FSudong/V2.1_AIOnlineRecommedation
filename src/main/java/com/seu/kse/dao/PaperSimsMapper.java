package com.seu.kse.dao;

import com.seu.kse.bean.PaperSims;
import com.seu.kse.bean.PaperSimsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PaperSimsMapper {
    int countByExample(PaperSimsExample example);

    int deleteByExample(PaperSimsExample example);

    int insert(PaperSims record);

    int insertSelective(PaperSims record);


    List<PaperSims> selectByExample(PaperSimsExample example);

    int updateByExampleSelective(@Param("record") PaperSims record, @Param("example") PaperSimsExample example);

    int updateByExample(@Param("record") PaperSims record, @Param("example") PaperSimsExample example);
}