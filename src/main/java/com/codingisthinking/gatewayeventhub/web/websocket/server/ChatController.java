package com.codingisthinking.gatewayeventhub.web.websocket.server;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(final Message message) throws Exception {
        System.out.println(message.getFrom() + ": "+message.getText());
        return new ResponseMessage(message.getText());
    }
}
