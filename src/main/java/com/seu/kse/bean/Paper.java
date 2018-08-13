package com.seu.kse.bean;

import java.util.Date;

public class Paper {
    public Paper(){

    }

    public Paper(String id, String title, String keywords, Integer type, String publisher, Date time, String paperAbstract, String url) {
        this.id = id;
        this.title = title;
        this.keywords = keywords;
        this.type = type;
        this.publisher = publisher;
        this.time = time;
        this.paperAbstract = paperAbstract;
        this.url = url;
    }
    private String id;

    private String keywords;

    private String publisher;

    private Integer type;

    private String url;

    private String paperAbstract;

    private Date time;

    private String title;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher == null ? null : publisher.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getPaperAbstract() {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract == null ? null : paperAbstract.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}