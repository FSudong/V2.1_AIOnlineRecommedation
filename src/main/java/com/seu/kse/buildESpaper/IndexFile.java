package com.seu.kse.buildESpaper;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public  class IndexFile {



    //判断当前索引 类型 是否存在
    public  static  boolean initTest(String indexName, String type) {
        try {
//            TransportClient client = GetClient.getTransportClient();
            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).
                    addTransportAddress(new TransportAddress(InetAddress.getByName("120.78.165.80"), 9300));
            //启动系统的时候判断索引是否存在
            IndicesExistsRequest inExistsRequest = new IndicesExistsRequest().indices(new String[]{indexName});
            //如果已经存在的话就更新数据；如果不存在就创建索引。
            IndicesExistsResponse indicesExistsResponse = client.admin().indices().exists(inExistsRequest).actionGet();
            System.out.println(indexName + " is existed or not: " + indicesExistsResponse.isExists());
            if (indicesExistsResponse.isExists()) {
                //索引index已经存在，则继续检查是不是存在该type
                TypesExistsResponse typesExistsResponse = client.admin().indices().prepareTypesExists(indexName).setTypes(type).get();
                System.out.println(type + " is existed or not: " + typesExistsResponse.isExists());
                //如果type已经存在，直接返回
                if(typesExistsResponse.isExists()){
                    return true;
                }
                else{ //type不存在，则定义索引结构
                    PutMappingRequest putMappingRequest = null;
                    if(type.equalsIgnoreCase(Configuration.ES_TYPE_ENTITY)){
                        putMappingRequest = Requests.putMappingRequest(indexName).type(type).source(_getModifiedMappingForTemplate(type));
                    }
                    else if(type.equalsIgnoreCase(Configuration.ES_TYPE_WDIC)){
                        putMappingRequest = Requests.putMappingRequest(indexName).type(type).source(_getModifiedMappingForWDIC(type));
                    }
                    else if(type.equalsIgnoreCase(Configuration.ES_TYPE_TYPE)){
                        putMappingRequest = Requests.putMappingRequest(indexName).type(type).source(_getModifiedMappingForTYPE(type));
                    }
                    else if(type.equalsIgnoreCase(Configuration.ES_TYPE_PAPER)){
                        putMappingRequest = Requests.putMappingRequest(indexName).type(type).source(_getModifiedMappingForPAPER(type));
                    }
                    PutMappingResponse putMappingResponse = client.admin().indices().putMapping(putMappingRequest).actionGet();

                    System.out.println("已定义index:"+indexName+"-"+type);
                    return false;
                }
            }
            else{
                //定义索引结构
                XContentBuilder mapping = null;
                if(type.equalsIgnoreCase(Configuration.ES_TYPE_ENTITY)){
                    mapping = _getDefinitionMappingForTemplate(indexName, type);
                }
                else if (type.equalsIgnoreCase(Configuration.ES_TYPE_WDIC)){
                    mapping = _getDefinitionMappingForWDIC(indexName, type);
                }
                else if (type.equalsIgnoreCase(Configuration.ES_TYPE_TYPE)){
                    mapping = _getDefinitionMappingForTYPE(indexName, type);
                }
                else if (type.equalsIgnoreCase(Configuration.ES_TYPE_PAPER)){
                    mapping = _getDefinitionMappingForPaper(indexName,type);
                }
//               这里应该有问题  都用了Configuration.ES_TYPE_PAPER作为了参数
                IndexResponse createIndexResponse = client.prepareIndex(indexName,Configuration.ES_TYPE_PAPER)
                        .setSource(mapping)
                        .get();
                System.out.println("已定义index:"+indexName+"-"+type);
                return false;


//                //定义索引结构
//                XContentBuilder mapping = null;
//                if(type.equalsIgnoreCase(Configuration.ES_TYPE_FAQ)){
//                    mapping = _getDefinitionMappingForTemplate(indexName, type);
//                }
//                System.out.println(mapping.toString());
//                //创建索引
//                CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices()
//                        .prepareCreate(indexName)
//                        .setSource(mapping);
////                System.out.println();
//                CreateIndexResponse createIndexResponse = createIndexRequestBuilder.execute().actionGet();
//                System.out.println("已定义index:"+indexName+"-"+type);
//                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    //已有索引，增加新的template type;
    private static XContentBuilder _getModifiedMappingForTemplate(String typeFaq) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties") //下面是设置文档属性
                    .startObject("name").field("type", "string").field("store", "yes")
                    .field("index","not_analyzed")
                    .endObject()
                    .startObject("entity").field("type", "text")
                    .endObject()
                    .startObject("rank").field("type", "text")
                    .endObject()
                .endObject()
                .endObject();
        return mapping;
    }
    //已有索引，增加新的dicts type;
    private static XContentBuilder _getModifiedMappingForWDIC(String typeFaq) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties") //下面是设置文档属性
                .startObject("termName").field("type", "string").field("store", "yes")
                .field("index","not_analyzed")
                .endObject()
                .startObject("vector").field("type", "text")
                .endObject()

                .endObject()
                .endObject();
        return mapping;
    }
    //已有索引，增加新的entity's TYPE type;
    private static XContentBuilder _getModifiedMappingForTYPE(String typeFaq) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties") //下面是设置文档属性
                .startObject("entityName").field("type", "string").field("store", "yes")
                .field("index","not_analyzed")
                .endObject()
                .startObject("typeName").field("type", "text")
                .endObject()

                .endObject()
                .endObject();
        return mapping;
    }
    //已有索引，增加新的PAPER type;
    private static XContentBuilder _getModifiedMappingForPAPER(String typeFaq) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties") //下面是设置文档属性

                .startObject("keywords").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("publisher").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("type").field("type", "text").field("store", true)
                .endObject()
                .startObject("url").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("id").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("paper_abstract").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("time").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("title").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("content").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()

                .endObject()
                .endObject();
        return mapping;
    }
    //没有索引，建立索引和增加新的template type; for 词条 实体 排名
    private static XContentBuilder _getDefinitionMappingForTemplate(String indexName, String typeFaq) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("settings")
                .field("number_of_shards", 1) //设置分片的数量
                .field("number_of_replicas", 1) //设置副本数量
                .endObject()
                .startObject("mappings")
                .startObject(typeFaq) // type名称
                .startObject("_all")
                .field("enabled", "false")
                .endObject()

                .startObject("properties") //下面是设置文档属性
                .startObject("name").field("type", "string").field("store", "yes")
                .field("index","not_analyzed")
                .endObject()
                .startObject("entity").field("type", "text").field("analyzer", "index_ansj")
                .field("search_analyzer", "query_ansj").field("store", "yes")
                .endObject()
                .startObject("rank").field("type", "text").field("analyzer", "index_ansj")
                .field("search_analyzer", "query_ansj").field("store", "yes")
                .endObject()
                .endObject()

                .endObject()
                .endObject()
                .endObject();

        return mapping;
    }

    //没有索引，建立索引和增加新的template type; for 词向量索引
    private static XContentBuilder _getDefinitionMappingForWDIC(String indexName, String typeFaq) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("settings")
                .field("number_of_shards", 1) //设置分片的数量
                .field("number_of_replicas", 1) //设置副本数量
                .endObject()
                .startObject("mappings")
                .startObject(typeFaq) // type名称
                .startObject("_all")
                .field("enabled", "false")
                .endObject()

                .startObject("properties") //下面是设置文档属性
                .startObject("termName").field("type", "string").field("store", "yes")
                .field("index","not_analyzed")
                .endObject()
                .startObject("vector").field("type", "text").field("analyzer", "index_ansj")
                .field("search_analyzer", "query_ansj").field("store", "yes")
                .endObject()
                .endObject()

                .endObject()
                .endObject()
                .endObject();

        return mapping;
    }

    //没有索引，建立索引和增加新的entity's TYPE type; for 寻找某类实体
    private static XContentBuilder _getDefinitionMappingForTYPE(String indexName, String typeFaq) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("settings")
                .field("number_of_shards", 1) //设置分片的数量
                .field("number_of_replicas", 1) //设置副本数量
                .endObject()
                .startObject("mappings")
                .startObject(typeFaq) // type名称
                .startObject("_all")
                .field("enabled", "false")
                .endObject()

                .startObject("properties") //下面是设置文档属性
                .startObject("entityName").field("type", "string").field("store", "yes")
                .field("index","not_analyzed")
                .endObject()
                .startObject("typeName").field("type", "text").field("analyzer", "index_ansj")
                .field("search_analyzer", "query_ansj").field("store", "yes")
                .endObject()
                .endObject()

                .endObject()
                .endObject()
                .endObject();

        return mapping;
    }


    //没有索引，建立索引和增加新的template type; for 词条 实体 排名
    private static XContentBuilder _getDefinitionMappingForPaper(String indexName, String typeFaq) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("settings")
                .field("number_of_shards", 1) //设置分片的数量
                .field("number_of_replicas", 1) //设置副本数量
                .endObject()
                .startObject("mappings")
                .startObject(typeFaq) // type名称
                .startObject("_all")
                .field("enabled", "false")
                .endObject()


                .startObject("properties") //下面是设置文档属性
                .startObject("keywords").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("publisher").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("type").field("type", "text").field("store", true)
                .endObject()
                .startObject("url").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("id").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("paper_abstract").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("time").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("title").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .startObject("content").field("type", "text").field("analyzer", "standard")
                .field("search_analyzer", "standard").field("store", true)
                .endObject()
                .endObject()

                .endObject()
                .endObject()
                .endObject();

        return mapping;
    }


    public static void indexFaqData() throws IOException {
        //默认使用ENTITY
        indexFaqData(DataSource.ENTITY);
    }

    //将数据索引至elasticsearch中
    public static void indexFaqData(DataSource... dataSources) throws IOException {
        for(DataSource dataSource: dataSources){

            //选择数据源
            String indexName;
            String filePath;
            String typeFaq;
            //选择索引的faq来源
            switch (dataSource) {

                case PAPER:
                    indexName = Configuration.ES_INDEX_PAPER;
                    typeFaq = Configuration.ES_TYPE_PAPER;

                    break;
                default:
                    indexName = Configuration.ES_INDEX_PAPER;
                    typeFaq = Configuration.ES_TYPE_PAPER;
            }

            //索引数据之前首先对es中的索引结构进行初始化
            if(!initTest(indexName, typeFaq)){
                if(typeFaq.equalsIgnoreCase(Configuration.ES_TYPE_PAPER)){
                    Mysql2ES_v1.all2es(indexName,typeFaq);
//                    List<Paper> papers;
//                    连接数据库将数据索引到ES中
//                    try {

//                        //要改成int 防止越界b
//                        int start = 0;
//                        int length = 100;
//                        int i = 0;
//                        TransportClient client = GetClient.getTransportClient();
//                        //读文本获取 待索引的数据集
//                        while(true) {
//                            BulkRequestBuilder bulkRequest = client.prepareBulk();
//                            papers = paperService.selectLimitPaper(start, length);
//                            if (length == -1) {
//                                return;
//                            }
//                            start = start + length;
//                            if (start >= 4400) length = -1;
//                            for (Paper paper : papers) {
//                                bulkRequest.add(client.prepareIndex(indexName, typeFaq)
//                                        .setSource(//这里可以直接使用json字符串
//                                                jsonBuilder()
//                                                        .startObject()
//                                                        .field("keywords", paper.getKeywords()).field("publisher", paper.getPublisher()).field("type", paper.getType())
//                                                        .field("url",paper.getUrl()).field("id",paper.getId()).field("paper_abstract",paper.getPaperAbstract())
//                                                        .field("time",paper.getTime()).field("title",paper.getTitle()).field("content",paper.getContent())
//                                                        .endObject()
//                                        )
//                                );
//                            }
//                            bulkRequest.execute().actionGet();
//                            bulkRequest = client.prepareBulk();
//                            System.out.println("提交了" + (start-length) + "条");
//                        }
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
                else {
                    System.out.println("非paper数据");
                }

            }
            else{
//                该索引结构已经存在 需要将最新的文章存进去

            }
        }
    }





}
