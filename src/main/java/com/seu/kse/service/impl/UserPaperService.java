package com.seu.kse.service.impl;

import com.seu.kse.bean.Paper;
import com.seu.kse.bean.User;
import com.seu.kse.bean.UserPaperBehavior;
import com.seu.kse.bean.UserPaperBehaviorKey;
import com.seu.kse.dao.UserPaperBehaviorMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaosheng on 2017/5/30.
 */
@Service
public class UserPaperService {
    @Resource
    UserPaperBehaviorMapper userPaperBehaviorDao;

    public int insertObject(UserPaperBehavior record){
        //判断是否已经存在记录
        UserPaperBehaviorKey key = new UserPaperBehaviorKey();
        key.setUid(record.getUid());
        key.setPid(record.getPid());
        UserPaperBehavior old = userPaperBehaviorDao.selectByPrimaryKey(key);
        if(old == null){
            //插入
            return userPaperBehaviorDao.insert(record);
        }else{
            //更新
            return userPaperBehaviorDao.updateByPrimaryKey(record);
        }

    }

    public List<UserPaperBehavior> getUserPaperByUID(String uid){
        List<UserPaperBehavior> userAndPaper = userPaperBehaviorDao.selectByUserID(uid);
        if(userAndPaper == null){
            userAndPaper = new ArrayList<UserPaperBehavior>();
        }
        return userAndPaper;
    }


    public List<String> getStartedPaperIDsByUID(String uid){
        List<UserPaperBehavior> userAndPaper = userPaperBehaviorDao.selectByUserID(uid);

        List<String> pids = new ArrayList<String>();
        if(userAndPaper!=null){
            for(UserPaperBehavior up : userAndPaper){
                int score = up.getInterest();
                if(score == 5){
                    pids.add(up.getPid());
                }
            }
        }
        return pids;
    }

    public Map<String,Boolean> getUserPaperStarted(List<Paper> papers , User user){
        Map<String,Boolean> res = new HashMap<String, Boolean>();
        List<String> startedPids = new ArrayList<String>();
        if(user!=null){
            startedPids = getStartedPaperIDsByUID(user.getId());
        }
        for(Paper paper : papers){
            String pid = paper.getId();
            if(user!=null){
                if(startedPids.contains(pid)){
                    res.put(pid,true);
                }else{
                    res.put(pid,false);
                }
            }else{
                res.put(pid,false);
            }
        }

        return res;
    }

    public UserPaperBehavior selectByKey(UserPaperBehaviorKey key){
        return userPaperBehaviorDao.selectByPrimaryKey(key);
    }

    public void updateByPrimaryKey(UserPaperBehavior ub){
        userPaperBehaviorDao.updateByPrimaryKey(ub);
    }
}
