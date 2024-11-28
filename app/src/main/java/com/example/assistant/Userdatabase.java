package com.example.assistant;

/**
 * Userdatabase 類表示用戶的聊天紀錄
 */
public class Userdatabase {
    private String messageId;
    private String senderId;
    private String messageText;
    private String question;
    private String answer;
    private String dateTime;

    /**
     * 默認構造函數
     */
    public Userdatabase() {
        // 默認構造函數可以為空，或者你可以在這裡進行一些初始化操作
    }

    /**
     * 構造函數，接受兩個參數
     * @param question 用戶問題
     * @param answer 回答
     */
    public Userdatabase(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    /**
     * 構造函數，接受三個參數
     * @param messageId 訊息ID
     * @param messageText 訊息文本
     * @param dateTime 日期時間
     */
    public Userdatabase(String messageId, String messageText, String dateTime) {
        this(messageId, null, messageText, null, null, dateTime);
    }

    /**
     * 完整的構造函數，接受所有參數
     * @param messageId 訊息ID
     * @param senderId 發送者ID
     * @param messageText 訊息文本
     * @param question 問題
     * @param answer 回答
     * @param dateTime 日期時間
     */
    public Userdatabase(String messageId, String senderId, String messageText, String question, String answer, String dateTime) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.messageText = messageText;
        this.question = question;
        this.answer = answer;
        this.dateTime = dateTime;
    }

    // Getter 和 Setter 方法
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
