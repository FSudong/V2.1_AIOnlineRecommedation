package com.seu.kse.bean;

public class UserPaperNoteWithBLOBs extends UserPaperNote {
    private String context;

    private String paperDirection;

    private String paperModel;

    private String paperProblem;

    private String paperRelatedwork;

    private String paperReview;

    public UserPaperNoteWithBLOBs() {
    }

    public UserPaperNoteWithBLOBs(String uid, String pid, String contexturi, String author, String paperSource, String keywords,
                                  String tags, String authorPlace, String context, String paperDirection, String paperModel,
                                  String paperProblem, String paperRelatedwork, String paperReview) {
        super(uid, pid, contexturi, author, paperSource, keywords, tags, authorPlace);
        this.context = context;
        this.paperDirection = paperDirection;
        this.paperModel = paperModel;
        this.paperProblem = paperProblem;
        this.paperRelatedwork = paperRelatedwork;
        this.paperReview = paperReview;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getPaperDirection() {
        return paperDirection;
    }

    public void setPaperDirection(String paperDirection) {
        this.paperDirection = paperDirection == null ? null : paperDirection.trim();
    }

    public String getPaperModel() {
        return paperModel;
    }

    public void setPaperModel(String paperModel) {
        this.paperModel = paperModel == null ? null : paperModel.trim();
    }

    public String getPaperProblem() {
        return paperProblem;
    }

    public void setPaperProblem(String paperProblem) {
        this.paperProblem = paperProblem == null ? null : paperProblem.trim();
    }

    public String getPaperRelatedwork() {
        return paperRelatedwork;
    }

    public void setPaperRelatedwork(String paperRelatedwork) {
        this.paperRelatedwork = paperRelatedwork == null ? null : paperRelatedwork.trim();
    }

    public String getPaperReview() {
        return paperReview;
    }

    public void setPaperReview(String paperReview) {
        this.paperReview = paperReview == null ? null : paperReview.trim();
    }
}