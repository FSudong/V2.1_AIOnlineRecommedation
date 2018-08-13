package com.seu.kse.bean;

import com.seu.kse.service.impl.UserTagService;

public class UserTagKey {

    public UserTagKey(){

    }
    public UserTagKey(String uid, String tagname){
        this.uid = uid;
        this.tagname = tagname;
    }
    private String uid;

    private String tagname;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname == null ? null : tagname.trim();
    }
}