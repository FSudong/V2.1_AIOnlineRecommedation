package com.seu.kse.buildESpaper;

import java.io.IOException;
import java.net.UnknownHostException;

public class test {


    public static void main(String[] args) {

        try {
              //删除标签
              DeleteIndex.deleteIndex(Configuration.ES_INDEX_PAPER);
              //索引paper
              IndexFile.indexFaqData(DataSource.PAPER);
              //数据库信息全部传到es
              Mysql2ES_v1.all2es(Configuration.ES_INDEX_PAPER,Configuration.ES_TYPE_PAPER);
//            将最新的数据传入es
//              Mysql2ES_v1.new2es(Configuration.ES_INDEX_PAPER, Configuration.ES_TYPE_PAPER, -1);
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
