package com.codingisthinking.gatewayeventhub.web.websocket.server;

public class ResponseMessage extends Message {
    private String author;
    private String messageText;


    public ResponseMessage() {
    }

    public ResponseMessage(String messageText) {
        this.author = "GatewayEventHub";
        this.messageText = messageText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
