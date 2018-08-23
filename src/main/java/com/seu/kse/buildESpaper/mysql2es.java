package com.seu.kse.buildESpaper;

import java.io.IOException;
import java.net.UnknownHostException;

public class mysql2es {

//    private final mysql2es m2s;

//    @Autowired
//    public mysql2es(mysql2es m2s){
//        this.m2s = m2s;
//    }
//
//    public void execute(){
//        try {
//
//            IndexFile.indexFaqData(DataSource.PAPER);
////            将最新的数据传入es
//            Mysql2ES_v1.new2es(Configuration.ES_INDEX_PAPER, Configuration.ES_TYPE_PAPER, -3);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {

        try {
              //删除标签
//              DeleteIndex.deleteIndex(Configuration.ES_INDEX_PAPER);
              //索引paper
              IndexFile.indexFaqData(DataSource.PAPER);
              //数据库信息全部传到es
//              Mysql2ES_v1.all2es(Configuration.ES_INDEX_PAPER,Configuration.ES_TYPE_PAPER);
//            将最新的数据传入es
             Mysql2ES_v1.new2es(Configuration.ES_INDEX_PAPER, Configuration.ES_TYPE_PAPER, -3);
              //为论文打标签

//            IndexFile indexFile = new IndexFile();
//            IndexFile.initTest(Configuration.ES_INDEX_PAPER,Configuration.ES_TYPE_PAPER);

//            System.out.println("end");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
