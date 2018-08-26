package com.seu.kse.service.impl;

import com.seu.kse.bean.*;
import com.seu.kse.dao.*;
import com.seu.kse.service.recommender.model.CB.CBKNNModel;
import com.seu.kse.util.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaosheng on 2017/5/31.
 */

@Service
public class RecommendationService {

    private static CBKNNModel cmodel;
    private final PaperMapper paperDao;
    private final UserMapper userDao;
    private final UserPaperBehaviorMapper userPaperBehaviorDao;
    private final UserTagMapper userTagDao;
    private final TagMapper tagDao;
    private final CBKNNModel cbknnModel;

//    private final TaggingService taggingService;

    @Autowired
    public RecommendationService(PaperMapper paperDao, UserMapper userDao, UserPaperBehaviorMapper userPaperBehaviorDao, UserTagMapper userTagDao, TagMapper tagDao, CBKNNModel cbknnModel) {
        this.paperDao = paperDao;
        this.userDao = userDao;
        this.userPaperBehaviorDao = userPaperBehaviorDao;
        this.userTagDao = userTagDao;
        this.tagDao = tagDao;
        this.cbknnModel = cbknnModel;
        init();
    }

    public void init(){

            LogUtils.info("init start",RecommendationService.class);
            LogUtils.info("read all paper"   ,RecommendationService.class);
//            原版本使用了Arixv中的前100篇文章
//            List<Paper> papers = paperDao.selectLimitArxiv(100);
//            新系统使用了zh pw arxiv 最新的文章
            List<Paper> papers = paperDao.selectPaperOrderByTimeSource(0,100,10);
            LogUtils.info("read new paper",RecommendationService.class);
            List<Paper> newPapers = paperDao.selectPaperOrderByTime(0,5,10);
            LogUtils.info("read user",RecommendationService.class);
            List<User> users = userDao.getAllUser();
            LogUtils.info("user actions",RecommendationService.class);
//            LogUtils.info("开始打标签......",RecommenderTask.class);
////        System.out.println("为论文打标签");
//            taggingService.init();
////        System.out.println("标签完成");
//            LogUtils.info("打标签完成......",RecommenderTask.class);
            Map<String,List<UserPaperBehavior>> userPaperBehaviors = new HashMap<String, List<UserPaperBehavior>>();
            Map<String, List<UserTagKey>> usersTag = new HashMap<String, List<UserTagKey>>();
            Map<String, List<UserTagKey>> usersHeadTag = new HashMap<String, List<UserTagKey>>();
            setUserInformation(userPaperBehaviors, usersTag, usersHeadTag, users);
            //List<Paper> allPapers = paperDao.selectAllPaper();
            //训练模型
            cbknnModel.init(true,papers,papers,1);
            cbknnModel.model(papers,userPaperBehaviors,users,newPapers,usersTag,usersHeadTag);
            LogUtils.info("init complete",RecommendationService.class);

    }

    public void updateModel(){
        //判断什么时候需要更新模型
        //若需要更新模型，重新计算论文向量
        //生产文件
        LogUtils.info("model update init!",RecommendationService.class);
        //20180826 backup writted by yaosheng
//        List<Paper> papers = paperDao.selectLimitArxiv(8000);
        List<Paper> papers = paperDao.selectPaperOrderByTimeSource(0,500,10);
        System.out.println(papers.size());
        List<Paper> newPapers = paperDao.selectPaperOrderByTime(0,5,10);
        List<User> users = userDao.getAllUser();
        Map<String,List<UserPaperBehavior>> userPaperBehaviors = new HashMap<String, List<UserPaperBehavior>>();
        Map<String, List<UserTagKey>> usersTag = new HashMap<String, List<UserTagKey>>();
        Map<String, List<UserTagKey>> usersHeadTag = new HashMap<String, List<UserTagKey>>();
        setUserInformation(userPaperBehaviors, usersTag, usersHeadTag,users);

        //List<Paper> allPapers = paperDao.selectAllPaper();
        //训练模型
        cbknnModel.init(true,papers,papers,1);
        cbknnModel.model(papers,userPaperBehaviors,users,newPapers,usersTag,usersHeadTag);
        LogUtils.info("model update complete !",RecommendationService.class);
    }

    private void setUserInformation(Map<String,List<UserPaperBehavior>> userPaperBehaviors,
                                    Map<String, List<UserTagKey>> usersTag, Map<String, List<UserTagKey>> usersHeadTag, List<User> users){
        for(User user : users){
            List<UserPaperBehavior> userPaperBehavior = userPaperBehaviorDao.selectByUserID(user.getId());
            userPaperBehaviors.put(user.getId(),userPaperBehavior);
            List<UserTagKey> userTagKeys = userTagDao.selectByUser(user.getId());
            List<String> tagNameStrings = new ArrayList<String>();
            List<String> headTagNames = new ArrayList<String>();
            for(UserTagKey userTagKey:userTagKeys){
//               获取路径上所有的标签
                List<String> strings = new ArrayList<String>();
                strings = getAllFatherTag(userTagKey.getTagname());
                for(String string:strings){
                    if(tagNameStrings.contains(string))
                        continue;
                    tagNameStrings.add(string);
                }
//                获取顶级标签
                String headTag = getHeadFatherTag(userTagKey.getTagname());
                if(!headTagNames.contains(headTag)){
                    headTagNames.add(headTag);
                }

            }
            List<UserTagKey> newUserTagKeys = new ArrayList<UserTagKey>();
//            新建立包含路径上所有标签的usertagkey
            for(String tagname:tagNameStrings){
                UserTagKey userTagKey = new UserTagKey(user.getId(),tagname);
                newUserTagKeys.add(userTagKey);
            }
//            新建包含顶级标签的usertagkey
            List<UserTagKey> UserHeadTagKey = new ArrayList<UserTagKey>();
            for(String tagname:headTagNames){
                UserTagKey userTagKey = new UserTagKey(user.getId(),tagname);
                UserHeadTagKey.add(userTagKey);
            }
            usersTag.put(user.getId(),newUserTagKeys);
            usersHeadTag.put(user.getId(),UserHeadTagKey);
        }
    }

    public List<String> getAllFatherTag(String tagName){
        List<String> strings = new ArrayList<String>();
        Tag tag = tagDao.selectByTagName(tagName);
        if(tag == null){
            return strings;
        }
        strings.add(tagName);
        strings.addAll(getAllFatherTag(tag.getFathername()));
        return strings;
    }

    public String getHeadFatherTag(String tagName){
        Tag tag = tagDao.selectByTagName(tagName);
        if(tag.getFathername().equals("head")){
            return tag.getTagname();
        }
        return getHeadFatherTag(tag.getFathername());
    }


//    @Test
//    public void run(){
//        RecommendationService rs =new RecommendationService();
//        rs.updateModel();
//        rs.recommend(5);
//    }
}
