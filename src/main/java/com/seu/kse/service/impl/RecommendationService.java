package com.seu.kse.service.impl;

import com.seu.kse.bean.*;
import com.seu.kse.dao.PaperMapper;
import com.seu.kse.dao.UserMapper;
import com.seu.kse.dao.UserPaperBehaviorMapper;
import com.seu.kse.dao.UserTagMapper;
import com.seu.kse.email.EmailSender;
import com.seu.kse.quartz.RecommederTask;
import com.seu.kse.service.recommender.model.CB.CBKNNModel;
import com.seu.kse.service.recommender.RecommenderCache;
import com.seu.kse.service.recommender.model.PaperSim;
import com.seu.kse.util.Constant;
import com.seu.kse.util.LogUtils;
import com.seu.kse.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.GeneralSecurityException;
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
    private final CBKNNModel cbknnModel;

//    private final TaggingService taggingService;

    @Autowired
    public RecommendationService(PaperMapper paperDao, UserMapper userDao, UserPaperBehaviorMapper userPaperBehaviorDao, UserTagMapper userTagDao,CBKNNModel cbknnModel) {
        this.paperDao = paperDao;
        this.userDao = userDao;
        this.userPaperBehaviorDao = userPaperBehaviorDao;
        this.userTagDao = userTagDao;
        this.cbknnModel = cbknnModel;
        init();
    }

    public void init(){

            LogUtils.info("init start",RecommendationService.class);
            LogUtils.info("read all paper"   ,RecommendationService.class);
            List<Paper> papers = paperDao.selectLimitArxiv(100);
            LogUtils.info("read new paper",RecommendationService.class);
            List<Paper> newPapers = paperDao.selectPaperOrderByTime(0,5,10);
            LogUtils.info("read user",RecommendationService.class);
            List<User> users = userDao.getAllUser();
            LogUtils.info("user actions",RecommendationService.class);
//            LogUtils.info("开始打标签......",RecommederTask.class);
////        System.out.println("为论文打标签");
//            taggingService.init();
////        System.out.println("标签完成");
//            LogUtils.info("打标签完成......",RecommederTask.class);
            Map<String,List<UserPaperBehavior>> userPaperBehaviors = new HashMap<String, List<UserPaperBehavior>>();
            Map<String, List<UserTagKey>> usersTag = new HashMap<String, List<UserTagKey>>();
            setUserInformation(userPaperBehaviors, usersTag, users);
            //List<Paper> allPapers = paperDao.selectAllPaper();
            //训练模型
            cbknnModel.init(true,papers,papers,1);
            cbknnModel.model(papers,userPaperBehaviors,users,newPapers,usersTag);
            LogUtils.info("init complete",RecommendationService.class);

    }

    public void updateModel(){
        //判断什么时候需要更新模型
        //若需要更新模型，重新计算论文向量
        //生产文件
        LogUtils.info("model update init!",RecommendationService.class);

        List<Paper> papers = paperDao.selectLimitArxiv(8000);
        System.out.println(papers.size());
        List<Paper> newPapers = paperDao.selectPaperOrderByTime(0,5,10);
        List<User> users = userDao.getAllUser();
        Map<String,List<UserPaperBehavior>> userPaperBehaviors = new HashMap<String, List<UserPaperBehavior>>();
        Map<String, List<UserTagKey>> usersTag = new HashMap<String, List<UserTagKey>>();

        setUserInformation(userPaperBehaviors, usersTag, users);

        //List<Paper> allPapers = paperDao.selectAllPaper();
        //训练模型
        cbknnModel.init(true,papers,papers,1);
        cbknnModel.model(papers,userPaperBehaviors,users,newPapers,usersTag);
        LogUtils.info("model update complete !",RecommendationService.class);
    }

    private void setUserInformation(Map<String,List<UserPaperBehavior>> userPaperBehaviors,
                                   Map<String, List<UserTagKey>> usersTag, List<User> users){
        for(User user : users){
            List<UserPaperBehavior> userPaperBehavior = userPaperBehaviorDao.selectByUserID(user.getId());
            userPaperBehaviors.put(user.getId(),userPaperBehavior);
            List<UserTagKey> userTagKeys = userTagDao.selectByUser(user.getId());
            usersTag.put(user.getId(),userTagKeys);
        }
    }




//    @Test
//    public void run(){
//        RecommendationService rs =new RecommendationService();
//        rs.updateModel();
//        rs.recommend(5);
//    }
}
