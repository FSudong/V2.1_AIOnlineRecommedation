package com.seu.kse.service.impl;

import com.seu.kse.bean.Tag;
import com.seu.kse.bean.UserTagKey;
import com.seu.kse.dao.TagMapper;
import com.seu.kse.dao.UserTagMapper;
import com.seu.kse.util.LogUtils;
import com.seu.kse.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yaosheng on 2017/6/2.
 */
@Service
public class UserTagService {
    @Resource
    private UserTagMapper userTagDao;

    @Resource
    private TagMapper tagDao;
    public int insertUserAndTag(UserTagKey userAndTag){
        String tagName = userAndTag.getTagname();
        Tag tag = tagDao.selectByTagName(tagName);
        int line = 0;
        if(tag == null){
            //插入
            tag = new Tag();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            tag.setTagname(userAndTag.getTagname());
            try {
                tag.setTime(format.parse(Utils.getDate(0)));
            } catch (ParseException e) {
                LogUtils.error(e.getMessage(),this.getClass());
            }
            line = tagDao.insert(tag);
        }
        //插入 user_tag表
        try{
            line = line +userTagDao.insert(userAndTag);
        }catch (Exception e){
            LogUtils.error(e.getMessage(),UserTagService.class);
        }
        return line;
    }
}
