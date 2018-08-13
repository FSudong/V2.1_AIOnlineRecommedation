package com.seu.kse.bean;

public class UserPaperNote extends UserPaperNoteKey {
    private String contexturi;

    private String author;

    private String paperSource;

    private String keywords;

    private String tags;

    private String authorPlace;

    public UserPaperNote() {
    }

    public UserPaperNote(String uid, String pid, String contexturi, String author, String paperSource, String keywords, String tags, String authorPlace) {
        super(uid, pid);
        this.contexturi = contexturi;
        this.author = author;
        this.paperSource = paperSource;
        this.keywords = keywords;
        this.tags = tags;
        this.authorPlace = authorPlace;
    }

    public String getAuthorPlace() {
        return authorPlace;
    }

    public void setAuthorPlace(String authorPlace) {
        this.authorPlace = authorPlace;
    }

    public String getContexturi() {
        return contexturi;
    }

    public void setContexturi(String contexturi) {
        this.contexturi = contexturi == null ? null : contexturi.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getPaperSource() {
        return paperSource;
    }

    public void setPaperSource(String paperSource) {
        this.paperSource = paperSource == null ? null : paperSource.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }
}