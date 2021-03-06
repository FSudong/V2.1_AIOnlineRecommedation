package com.seu.kse.buildESpaper;

import com.seu.kse.bean.Paper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class Mysql2ES_v1 {



    public static void all2es(String indexName, String typeFaq){

        List<Paper> papers = new ArrayList<Paper>();
        //要改成int 防止越界
        int start = 0;
        int length = 1000;
//        int i = 0;
        try {
            TransportClient client = GetClient.getTransportClient();
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            DBConnection dbConnection = new DBConnection();
            int rowNum = DBservice.dataNum(dbConnection,"paper");
            int rowFloor = (int) Math.floor(rowNum/1000)*1000;
            //读文本获取 待索引的数据集
            while (true) {
//                mysql> SELECT * FROM table LIMIT 5,10; //检索记录行6-15
                System.out.println("\n");
                if(length == -1){
                    System.out.println("从mysql读取"+start+"--"+"最后...");
                }else{
                    System.out.println("从mysql读取"+start+"--"+(start+length)+"...");
                }
                papers = DBservice.selectLimitPaper(dbConnection,start,length);
                System.out.println("从mysql读取完成!!!");
//                papers = paperService.selectLimitPaper(start, length);
                start = start + length;
//                if (start >= 1000) length = -1;
                for (Paper paper : papers) {
                    bulkRequest.add(client.prepareIndex(indexName, typeFaq, paper.getId())
                            .setSource(//这里可以直接使用json字符串
                                    jsonBuilder()
                                            .startObject()
                                            .field("keywords", paper.getKeywords()).field("publisher", paper.getPublisher())
                                            .field("type", paper.getType().toString())
                                            .field("url", paper.getUrl()).field("id", paper.getId()).field("paper_abstract", paper.getPaperAbstract())
                                            .field("time", new SimpleDateFormat("yyyy-MM-dd").format(paper.getTime()))
                                            .field("title", paper.getTitle()).field("content", paper.getContent())
                                            .endObject()
                            )
                    );
                }
                System.out.println("start save to ES...");
                bulkRequest.execute().actionGet();
                System.out.println(start + "save to ES success!!");
                bulkRequest = client.prepareBulk();
                if(start >= rowNum){
                    break;
                }
                if(start >= rowFloor) {
                    length = rowNum - rowFloor;
                }
            }
            client.close();
            dbConnection.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }





    public static void new2es(String indexName, String typeFaq, Integer days){
        List<Paper> papers = new ArrayList<Paper>();
//       取当天的数据
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,days);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        try {
            TransportClient client = GetClient.getTransportClient();
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            DBConnection dbConnection = new DBConnection();
            //读文本获取 待索引的数据集
            int num = 1;
            while(num <=1){
                System.out.println("\n");
                papers = DBservice.selectNewPaper(dbConnection,dateString);
                System.out.println("从mysql读取完成!!!"+papers.size());
//                if (start >= 1000) length = -1;
                for (Paper paper : papers) {
                    bulkRequest.add(client.prepareIndex(indexName, typeFaq, paper.getId())
                            .setSource(//这里可以直接使用json字符串
                                    jsonBuilder()
                                            .startObject()
                                            .field("keywords", paper.getKeywords()).field("publisher", paper.getPublisher())
                                            .field("type", paper.getType().toString())
                                            .field("url", paper.getUrl()).field("id", paper.getId()).field("paper_abstract", paper.getPaperAbstract())
                                            .field("time", new SimpleDateFormat("yyyy-MM-dd").format(paper.getTime()))
                                            .field("title", paper.getTitle()).field("content", paper.getContent())
                                            .endObject()
                            )
                    );
                }
                System.out.println("start save to ES...");
                bulkRequest.execute().actionGet();
                System.out.println("save to ES success!!");
                bulkRequest = client.prepareBulk();
                num = num +1;
            }
            client.close();
            dbConnection.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
