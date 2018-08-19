package com.seu.kse.buildESpaper;

import com.seu.kse.bean.Paper;
import org.quartz.utils.DBConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBservice {

    public  static  List<Paper> selectLimitPaper(DBConnection dbConnection, long start, long end){

        String sql = "select * from paper limit "+start+","+end;

        List<Paper> papers = new ArrayList<Paper>();
        try {
            Statement statement = (Statement) dbConnection.connection.createStatement();
            ResultSet resultSet = (ResultSet) statement.executeQuery(sql);
            while(resultSet.next()){
                try {
                    Paper paper = new Paper();
                    paper.setId(erosenull(resultSet.getString("id")));
                    paper.setTitle(erosenull(resultSet.getString("title")));
                    paper.setKeywords(erosenull(resultSet.getString("keywords")));
                    paper.setType(Integer.valueOf(resultSet.getString("type")));
                    paper.setPublisher(erosenull(resultSet.getString("publisher")));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse(resultSet.getString("time"));
                    paper.setTime(date);
//                    paper.setTime(new SimpleDateFormat("yyyy-MM-dd").parse((String)res.get(Constant.ES_FIELD_TIME)));
                    paper.setPaperAbstract(erosenull(resultSet.getString("paper_abstract")));
                    paper.setUrl(erosenull(resultSet.getString("url")));

                    paper.setContent(erosenull(resultSet.getString("content")));

                    papers.add(paper);
                    System.out.print("|");
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            System.out.println("");
            return papers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String erosenull(String s){
        if(s==null)
            s="";
        return s;
    }


    public static  int dataNum(DBConnection dbConnection,String tableName){
        int rowNum = 1;
        try {

            String sql = "select count(*) as result from " + tableName;
            Statement statement = (Statement) dbConnection.connection.createStatement();
            ResultSet resultSet = (ResultSet) statement.executeQuery(sql);
            while(resultSet.next()){
                rowNum = resultSet.getInt(1);
            }


        }catch (Exception e){
            System.out.println(e);
        }
        return rowNum;
    }

    public static List<Paper> selectNewPaper(DBConnection dbConnection,String time_string){

        String sql = "select * from paper where time>="+"'"+time_string+"'";
        System.out.println(sql);
        List<Paper> papers = new ArrayList<Paper>();
        try{
            Statement statement = (Statement) dbConnection.connection.createStatement();
            ResultSet resultSet = (ResultSet) statement.executeQuery(sql);
            while(resultSet.next()){
                try {
                    Paper paper = new Paper();
                    paper.setId(erosenull(resultSet.getString("id")));
                    paper.setTitle(erosenull(resultSet.getString("title")));
                    paper.setKeywords(erosenull(resultSet.getString("keywords")));
                    paper.setType(Integer.valueOf(resultSet.getString("type")));
                    paper.setPublisher(erosenull(resultSet.getString("publisher")));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse(resultSet.getString("time"));
                    paper.setTime(date);
//                    paper.setTime(new SimpleDateFormat("yyyy-MM-dd").parse((String)res.get(Constant.ES_FIELD_TIME)));
                    paper.setPaperAbstract(erosenull(resultSet.getString("paper_abstract")));
                    paper.setUrl(erosenull(resultSet.getString("url")));

                    paper.setContent(erosenull(resultSet.getString("content")));

                    papers.add(paper);
//                    System.out.print("|");
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            return papers;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }


}
