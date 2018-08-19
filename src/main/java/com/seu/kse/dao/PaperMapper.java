package com.seu.kse.dao;

import com.seu.kse.bean.Paper;
import com.seu.kse.bean.PaperExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaperMapper {
    long countByExample(PaperExample example);

    int deleteByExample(PaperExample example);

    int deleteByPrimaryKey(String id);

    int insert(Paper record);

    int insertSelective(Paper record);

    List<Paper> selectByExampleWithBLOBs(PaperExample example);

    List<Paper> selectByExample(PaperExample example);

    Paper selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Paper record, @Param("example") PaperExample example);

    int updateByExampleWithBLOBs(@Param("record") Paper record, @Param("example") PaperExample example);

    int updateByExample(@Param("record") Paper record, @Param("example") PaperExample example);

    int updateByPrimaryKeySelective(Paper record);

    int updateByPrimaryKeyWithBLOBs(Paper record);

    int updateByPrimaryKey(Paper record);

//    人工加
    List<Paper> selectPaperOrderByTime(int pageNum, int limit, int type);
    List<Paper> selectPaperOrderByTimeSource(int pageNum, int limit, int type);

    List<Paper> selectSomePaper(int start, int end);
    List<Paper> selectTodayArxiv(int pageNum, int limit, int time, int type);

    List<Paper> selectTodayPaper(int time);

    List<Paper> selectAllPaper();

    Paper selectByTitle(String title);

    Integer countPaperId(String id);

    List<Paper> selectAllArxivPaper();

    List<Paper> selectLimitPaper(int limit);

    List<Paper> selectLimitArxiv(int limit);

    List<Paper> selectPaperListByTitle(String title);
}