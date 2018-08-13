package com.seu.kse.datainjection;


import com.seu.kse.dao.AuthorMapper;

import javax.annotation.Resource;

public class AminerDataInjector {
    @Resource
    private final AuthorMapper authorDao;
    public AminerDataInjector(AuthorMapper authorDao){
        this.authorDao = authorDao;
    }


    public void insertAminerData(){
/*       //从文件中读入
        aid: 2841
        authorname: Yongli Hu
        atype: 0
        organization: Beijing Key Laboratory of Multimedia and Intelligent Software Technology, College of Computer Science and Technology, Beijing University of Technology, Beijing, People's Republic of China 100124
        url: https://arxiv.org/find/cs/1/au:+Hu_Y/0/1/0/all/0/1
        publishNum: 2
        citationNUm: 3
        Hindex: 1
        Pindex: 0.6429
        researchInterests: face recognition;face reconstruction;face recognition algorithm;face deformable model;face division scheme;face reconstruction method;face region;face sample;human face;skull3D face reconstruction
        index: 868518*/
        String fileName = "";





    }


}
