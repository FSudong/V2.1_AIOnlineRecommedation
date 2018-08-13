package com.seu.kse.service.impl;

import com.seu.kse.bean.Paper;
import com.seu.kse.bean.PaperTagKey;
import com.seu.kse.bean.Tag;
import com.seu.kse.bean.UserTagKey;
import com.seu.kse.dao.PaperMapper;
import com.seu.kse.dao.PaperTagMapper;
import com.seu.kse.dao.TagMapper;
import com.seu.kse.dao.UserTagMapper;
import com.seu.kse.quartz.RecommederTask;
import com.seu.kse.service.retrieval.Retrieval;
import com.seu.kse.util.Constant;
import com.seu.kse.util.LogUtils;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 更新标签
 * 为论文打标签
 */
@Service
public class TaggingService {

    private final TagMapper tagDao;
    private final UserTagMapper userTagDao;
    private final PaperTagMapper paperTagDao;
    private final PaperMapper paperDao;

    @Autowired
    public TaggingService(TagMapper tagDao, UserTagMapper userTagDao, PaperTagMapper paperTagDao, PaperMapper paperDao){
        this.tagDao = tagDao;
        this.userTagDao = userTagDao;
        this.paperTagDao = paperTagDao;
        this.paperDao = paperDao;
        //init();
    }

    public void init(){
        //更新所有论文的标签
        List<Tag> tags = tagDao.selectAllTag();
        for(Tag tag : tags){
            LogUtils.info(tags.size()+" "+tag.getTagname(),RecommederTask.class);
            List<String> ids = getIDS(Retrieval.search(tag.getTagname(),0.8f,200));
            for(String id : ids){
                LogUtils.info(ids.size()+id,RecommederTask.class);
                PaperTagKey key = new PaperTagKey();
                key.setTagname(tag.getTagname());
                key.setPid(id);
                try{
                    LogUtils.info("存入paperTag......",RecommederTask.class);
                    paperTagDao.insert(key);
//                    System.out.println(tag.getTagname()+"::"+id+" saved");
                }catch (Exception e){
                    LogUtils.error(e.getMessage(),this.getClass());
                }

            }
        }
    }



    public void init2(){
        //更新所有论文的标签
        List<Tag> tags = tagDao.selectAllTag();
        int tagssize = tags.size();
        int i = 437;
        for(;i < tagssize;i++){
            Tag tag = tags.get(i);
            LogUtils.info(tags.size()+" "+i+" "+tag.getTagname(),RecommederTask.class);
            List<String> ids = getIDS(Retrieval.search(tag.getTagname(),0.9f,100));
            for(String id : ids){
                LogUtils.info(ids.size()+id,RecommederTask.class);
                PaperTagKey key = new PaperTagKey();
                key.setTagname(tag.getTagname());
                key.setPid(id);
                try{
                    LogUtils.info("存入paperTag......",RecommederTask.class);
                    paperTagDao.insert(key);
//                    System.out.println(tag.getTagname()+"::"+id+" saved");
                }catch (Exception e){
                    LogUtils.error(e.getMessage(),this.getClass());
                }

            }
        }
    }

    private List<String> getIDS(SearchHits hits){
        List<String> ids = new ArrayList<String>();
        if(hits == null) return ids;
        for(SearchHit hit : hits){
            ids.add((String) hit.getSourceAsMap().get(Constant.ES_FIELD_ID));
        }
        return  ids;
    }


    //为用户打标签
    public  void taggingPaper(String tag){

        List<String> ids = getIDS(Retrieval.search(tag,0.8f,200));

        for(String id : ids){
            // 更新论文-标签库
            PaperTagKey paperTagKey = new PaperTagKey();
            paperTagKey.setPid(id);
            paperTagKey.setTagname(tag);
            try{
                paperTagDao.insert(paperTagKey);
            }catch (Exception e){
                LogUtils.error(e.getMessage(),this.getClass());
            }

        }
    }

    // 为用户打标签
    public  void taggingUser(String uid, String tag){
        UserTagKey key = new UserTagKey();
        key.setUid(uid);
        key.setTagname(tag);
        userTagDao.insert(key);
    }

    private String getBeforeDate(int day){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,day*-1);
        Date yesterday = calendar.getTime();

        return  new SimpleDateFormat("yyyyMMdd").format(yesterday);
    }

    private boolean isUpdatedOfPaper(List<Paper> papers){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        Date yesterday = calendar.getTime();
        String yesterday_str = new SimpleDateFormat("yyyyMMdd").format(yesterday);
        papers = paperDao.selectTodayPaper(Integer.parseInt(yesterday_str));
        return papers.size()>0;

    }

    private boolean isTagUpdated(Map<String, String> userTags, List<String> terms){
        userTags = countKeyWordsOfLog();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        Date yesterday = calendar.getTime();
        String yesterday_str = new SimpleDateFormat("yyyyMMdd").format(yesterday);
//       搜索最新添加的标签,用于将最新的标签与论文连接
        List<Tag> tags = tagDao.selectTodayTag(Integer.parseInt(yesterday_str));
//        提取所有的标签，用于将所有标签与论文连接
//        List<Tag> tags = tagDao.selectAllTag();
        for(Tag tag : tags){
            terms.add(tag.getTagname());
        }
        return (userTags.size()>0 || terms.size()>0);


    }

    //更新标签
    public void update(){
        //1 . 检查OWL表，有无更新，将其更新到 tag库
        //2 . 检查当天的日志信息，统计日志中用户搜索的单词，加入标签库
        Map<String, String> newUserTags = new HashMap<String, String>();
        List<String> newOwls = new ArrayList<String>();
        boolean tagUpdateFlag = isTagUpdated(newUserTags, newOwls);


        //标签更新
        if(tagUpdateFlag) {
            LogUtils.info("开始更新标签",this.getClass());
            for(Map.Entry<String,String> entry : newUserTags.entrySet()){
                Tag tag = new Tag();
                tag.setTagname(entry.getValue());
                try{
                    LogUtils.info("更新标签集合",this.getClass());
                    //更新标签库
                    tagDao.insert(tag);
                }
                catch (Exception e){
                    LogUtils.error(e.getMessage(),this.getClass());
                }
                //更新用户标签
                LogUtils.info("更新用户标签",this.getClass());
                if(entry.getKey()!= Constant.VISIT_ID)taggingUser(entry.getKey(), tag.getTagname());
                //更新论文标签
                LogUtils.info("更新论文标签",this.getClass());
                taggingPaper(tag.getTagname());
            }

            for(String word : newOwls){
                //更新论文标签
                taggingPaper(word);
            }

        }

        List<Paper> newPapers = new ArrayList<Paper>();
        Boolean paperUpdateFlag = isUpdatedOfPaper(newPapers);

        //论文更新
        if(paperUpdateFlag){
            LogUtils.info("新论文打标签",this.getClass());
            //为新论文打标签
            //获取所有标签
            List<Tag> tags = tagDao.selectAllTag();
            for(Tag tag : tags){
                for(Paper newPaper : newPapers){
                    if(isContainTag(newPaper, tag.getTagname())){
                        PaperTagKey record = new PaperTagKey();
                        record.setTagname(tag.getTagname());
                        record.setPid(newPaper.getId());
                        paperTagDao.insert(record);
                    }
                }
            }
        }
    }



    //判断论文是否包含该标签
    private boolean isContainTag(Paper paper, String tag){
        boolean flag = true;
        String tittle = paper.getTitle();
        String paperAbstract = paper.getPaperAbstract();
        String content = tittle + "." + paperAbstract;
        String[] terms = tag.split(" |/t");
        for(String term : terms){
            boolean unitFlag = false;
            if(term.trim().length()>0&&content.contains(term.trim())){
                unitFlag = true;
            }
            flag = flag&unitFlag;
        }

        return flag;
    }


    //统计日志
    private Map<String,String> countKeyWordsOfLog(){
        Map<String,String> userKeyWords = new HashMap<String, String>();
        File file = new File(Constant.logPath);
        String yesterday = getBeforeDate(1);
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(file));
            String line = bReader.readLine();
            while(line!=null){
                if(line.contains(Constant.SEARCH_LOG_KEYWORD) && line.contains(yesterday)){
                    String[] searchUserTag = line.split(Constant.SEARCH_LOG_SPILT);
                    if(searchUserTag.length>=2){
                        String[] userTag = searchUserTag[1].split(Constant.SEARCH_TAG_SPILT);
                        String userID = userTag[0];
                        String terms = userTag[1];
                        userKeyWords.put(userID,terms);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            LogUtils.error(e.getMessage(),this.getClass());
        } catch (IOException e) {
            LogUtils.error(e.getMessage(),this.getClass());
        }
        return userKeyWords;
    }

    //

}
