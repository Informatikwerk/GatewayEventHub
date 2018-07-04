package com.codingisthinking.gatewayeventhub.web.websocket.server;

import com.codingisthinking.gatewayeventhub.web.websocket.server.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate template;


    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(final Message message) throws Exception {
        System.out.println(message.getAuthor() + ": "+message.getAction());
        return new Message(message.getAction().toString());
    }
}
