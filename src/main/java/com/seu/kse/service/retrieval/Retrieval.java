package com.seu.kse.service.retrieval;

import com.seu.kse.bean.Paper;
import com.seu.kse.quartz.RecommenderTask;
import com.seu.kse.util.Constant;
import com.seu.kse.util.LogUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 该类提供检索功能的接口
 */
public class Retrieval {

    public static List<Paper> retrieval(String tag){
        SearchHits hits = search(tag, 0.6f, 100);
        return getPapers(hits);
    }

    public static List<Paper> retrievalless(String tag){
        SearchHits hits = search(tag, 0.9f, 5);
        return getPapers(hits);
    }

    public static List<Paper> getPapers(SearchHits hits){
        List<Paper> papers = new ArrayList<Paper>();
        for(SearchHit hit :hits){
            Paper paper = new Paper();
            Map<String,Object> res = hit.getSourceAsMap();
            paper.setPaperAbstract((String)res.get(Constant.ES_FIELD_ABSTRACT));
            paper.setTitle((String)res.get(Constant.ES_FIELD_TITLE));
            paper.setId((String)res.get(Constant.ES_FIELD_ID));
            paper.setPublisher((String)res.get(Constant.ES_FIELD_PUBLISHER));
            paper.setUrl((String)res.get(Constant.ES_FIELD_URL));
            paper.setKeywords((String) res.get(Constant.ES_FIELD_KEYWORDS));
            try {
                paper.setTime(new SimpleDateFormat("yyyy-MM-dd").parse((String)res.get(Constant.ES_FIELD_TIME)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            paper.setType(Integer.parseInt((String) res.get(Constant.ES_FIELD_TYPE)));
            papers.add(paper);
        }
        return papers;
    }


    public static SearchHits search(String terms, float minScore, int limit){
        TransportClient client ;
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY).
                    addTransportAddress(new TransportAddress(InetAddress.getByName("120.78.165.80"), 9300));
            MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(terms, Constant.ES_SEARCH_FIELDS);
            LogUtils.info("开始查询ES......",RecommenderTask.class);
            SearchResponse search_response = client.prepareSearch(Constant.ES_INDEX)
                    .setTypes(Constant.ES_TYPE)
                    .setSearchType(SearchType.QUERY_THEN_FETCH)
                    .setQuery(multiMatchQueryBuilder)
                    .setExplain(true).setMinScore(minScore).setSize(limit).get();
            SearchHits searchHits = search_response.getHits();
            client.close();
            return searchHits;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }

    }
}
