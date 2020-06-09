package com.library.repository.models;

public class AuthQuestionModel {


    /**
     * qId : 1
     * question : 1+1=？
     * answer : 2
     */

    private String qId;
    private String question;
    private String answer;

    public String getQId() {
        return qId;
    }

    public void setQId(String qId) {
        this.qId = qId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
