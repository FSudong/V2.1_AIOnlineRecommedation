package com.seu.kse.service.impl;

import com.seu.kse.bean.userFieldsKey;
import com.seu.kse.dao.userFieldsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by yaosheng on 2017/5/23.
 */
@Service
public class UserFieldService {
    @Resource
    private userFieldsMapper userFieldDao;
    public int insertRecord(userFieldsKey uftable){
        return userFieldDao.insert(uftable);
    }


}
