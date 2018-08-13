package com.seu.kse.bean;

public class UserPaperQuestion extends UserPaperQuestionKey {
    private String question;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }
}