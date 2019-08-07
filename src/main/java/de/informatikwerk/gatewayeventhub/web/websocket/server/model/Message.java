package de.informatikwerk.gatewayeventhub.web.websocket.server.model;

import de.informatikwerk.gatewayeventhub.domain.DelegateCommand;

import java.util.UUID;

public class Message {
    private String author;
    private DelegateCommand delegateCommand;
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

    public DelegateCommand getDelegateCommand() {
        return delegateCommand;
    }

    public void setDelegateCommand(DelegateCommand delegateCommand) {
        this.delegateCommand = delegateCommand;
    }

    public String getMessageId() {
        return messageId;
    }

    @Override
    public String toString() {
        return "Message{" +
            "from='" + author + '\'' +
            ", delegateCommand=" + delegateCommand +
            '}';
    }
}
