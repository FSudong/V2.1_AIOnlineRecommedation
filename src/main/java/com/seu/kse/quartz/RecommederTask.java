package com.seu.kse.quartz;


import com.seu.kse.bean.Paper;
import com.seu.kse.bean.User;
import com.seu.kse.bean.UserPaperBehavior;
import com.seu.kse.bean.UserPaperBehaviorKey;
import com.seu.kse.dao.PaperMapper;
import com.seu.kse.dao.UserMapper;
import com.seu.kse.dao.UserPaperBehaviorMapper;
import com.seu.kse.email.EmailSender;
import com.seu.kse.service.impl.RecommendationService;
import com.seu.kse.service.recommender.RecommenderCache;
import com.seu.kse.service.recommender.model.PaperSim;
import com.seu.kse.util.Constant;
import com.seu.kse.util.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RecommederTask {

    private EmailSender emailSender;
    private final PaperMapper paperDao;
    private final UserMapper userDao;
    private final UserPaperBehaviorMapper userPaperBehaviorDao;


    @Autowired
    public RecommederTask(PaperMapper paperDao, UserMapper userDao, UserPaperBehaviorMapper userPaperBehaviorDao){
        this.paperDao = paperDao;
        this.userDao = userDao;
        this.userPaperBehaviorDao = userPaperBehaviorDao;
        try {
            emailSender = new EmailSender(Constant.sender,Constant.emailhost);
            emailSender.init();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

    }
    public void execute(){
        LogUtils.info("开始推荐......",RecommederTask.class);


        recommend();

        LogUtils.info("结束推荐......",RecommederTask.class);

    }

    public boolean isRecommend(String uid, String pid){
        UserPaperBehaviorKey key = new UserPaperBehaviorKey();
        key.setPid(pid);
        key.setUid(uid);
        UserPaperBehavior ub = userPaperBehaviorDao.selectByPrimaryKey(key);
        return ub != null ;
    }

    public void recommend(){
        LogUtils.info("recommend start !",RecommendationService.class);
        Byte yes =1;
        Byte no = 0;
        for(Map.Entry<String,List<PaperSim>> e : RecommenderCache.userRecommend.entrySet()){
            String email = e.getKey();
            User user = userDao.selectByEmail(email);

            if(Constant.isTest){
                if(!(user.getUname().equals("yaosheng"))){
                    continue;
                }
            }
            List<PaperSim> val = e.getValue();
            List<String> paperURLs = new ArrayList<String>();
            List<String> paperTitles = new ArrayList<String>();
            int pushNum = user.getPushnum();
            for(int i=0;i<pushNum;i++){
                if(i>e.getValue().size()){
                    break;
                }
                String paperID = val.get(i).getPid();
                if(isRecommend(user.getId(),paperID)){
                    pushNum++;
                    continue;
                }
                Paper paper = paperDao.selectByPrimaryKey(paperID);
                String paperTitle = paper.getTitle();
                String paperURL = Constant.paperinfoURL + paperID;
                paperURLs.add(paperURL);
                paperTitles.add(paperTitle);
                //更新user_paper 表
                UserPaperBehavior upb = new UserPaperBehavior();
                upb.setUid(user.getId());
                upb.setPid(paperID);
                upb.setReaded(yes);
                upb.setInterest(0);
                upb.setAuthor(no);
                updateUserPaperB(upb);
            }
            recommendByEmail(email, paperURLs,paperTitles);
        }
        LogUtils.info("recommend end !",RecommendationService.class);
    }

    /**
     * 更新userPaper表
     * @param newRecord
     */
    public void updateUserPaperB(UserPaperBehavior newRecord){
        UserPaperBehaviorKey key = new UserPaperBehaviorKey(newRecord.getUid(),newRecord.getPid());
        UserPaperBehavior old = userPaperBehaviorDao.selectByPrimaryKey(key);
        if(old == null){
            userPaperBehaviorDao.insert(newRecord);
        }else{
            userPaperBehaviorDao.updateByPrimaryKey(newRecord);
        }
    }

    public void recommendByEmail(String email, List<String> paperURIs, List<String> paperTitls){
        String content = "下面是今日为您推荐的论文:"+"<br>";

        for(int i=0;i<paperURIs.size();i++){
            content = content + "<a href=\""+paperURIs.get(i)+"\">"+(i+1) +" : "+ paperTitls.get(i)+"</a>"+"<br>";
        }
        emailSender.send(email,content);
    }

}
