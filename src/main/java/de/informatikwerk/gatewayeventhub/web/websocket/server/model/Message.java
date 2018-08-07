package de.informatikwerk.gatewayeventhub.web.websocket.server.model;

import de.informatikwerk.gatewayeventhub.domain.Action;

public class Message {
    private String author;
    private Action action;

    public Message() {
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

    @Override
    public String toString() {
        return "Message{" +
            "from='" + author + '\'' +
            ", action=" + action +
            '}';
    }
}
