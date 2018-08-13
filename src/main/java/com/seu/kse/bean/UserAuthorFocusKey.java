package com.seu.kse.bean;

public class UserAuthorFocusKey {
    public UserAuthorFocusKey(){

    }

    public UserAuthorFocusKey(String uid, int aid){
        this.uid = uid;
        this.aid = aid;
    }

    private String uid;

    private Integer aid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }
}