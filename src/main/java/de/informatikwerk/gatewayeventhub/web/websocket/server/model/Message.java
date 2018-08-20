package de.informatikwerk.gatewayeventhub.web.websocket.server.model;

import de.informatikwerk.gatewayeventhub.domain.Action;

import java.util.UUID;

public class Message {
    private String author;
    private Action action;
    private String messageId;

    public Message() {
        this.messageId = UUID.randomUUID().toString();
    }

    public Message(final String from) {
        this.author = from;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String from) {
        this.author = from;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return "Message{" +
            "from='" + author + '\'' +
            ", action=" + action +
            '}';
    }
}
