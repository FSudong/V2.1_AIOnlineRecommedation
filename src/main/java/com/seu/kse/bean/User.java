package com.seu.kse.bean;


import com.seu.kse.util.MD5Utils;

public class User {


    public User(String uname,String upassword,String mailbox,int utype,int pushnum){

        this.uname=uname;
        this.upassword=upassword;
        this.mailbox=mailbox;
        this.utype=utype;
        this.pushnum = pushnum;
        // 生成id
        this.id= MD5Utils.MD5Encode(mailbox,"utf-8",false);
    }

    public User(){

    }

    private String id;

    private String uname;

    private String upassword;

    private String mailbox;

    private Integer utype;

    private String url;

    private Integer pushnum;

    public Integer getPushnum() {
        return pushnum;
    }

    public void setPushnum(Integer pushnum) {
        this.pushnum = pushnum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname == null ? null : uname.trim();
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword == null ? null : upassword.trim();
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox == null ? null : mailbox.trim();
    }

    public Integer getUtype() {
        return utype;
    }

    public void setUtype(Integer utype) {
        this.utype = utype;
    }
}