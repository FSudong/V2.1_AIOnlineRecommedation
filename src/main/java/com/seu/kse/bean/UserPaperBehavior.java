package com.seu.kse.bean;

public class UserPaperBehavior extends UserPaperBehaviorKey {
    private Byte author;

    private Byte readed;

    private int interest;

    public Byte getAuthor() {
        return author;
    }

    public void setAuthor(Byte author) {
        this.author = author;
    }

    public Byte getReaded() {
        return readed;
    }

    public void setReaded(Byte readed) {
        this.readed = readed;
    }

    public int getInterest() {
        return interest;
    }

    public void setInterest(int interest) {
        this.interest = interest;
    }
}